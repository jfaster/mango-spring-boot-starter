package org.jfaster.mango.plugin.spring.starter;

import com.zaxxer.hikari.HikariDataSource;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.datasource.DataSourceFactory;
import org.jfaster.mango.datasource.MasterSlaveDataSourceFactory;
import org.jfaster.mango.datasource.SimpleDataSourceFactory;
import org.jfaster.mango.interceptor.Interceptor;
import org.jfaster.mango.operator.Mango;
import org.jfaster.mango.operator.cache.CacheHandler;
import org.jfaster.mango.plugin.spring.DefaultMangoFactoryBean;
import org.jfaster.mango.plugin.spring.config.MangoHikaricpConfig;
import org.jfaster.mango.util.Strings;
import org.jfaster.mango.util.logging.InternalLogger;
import org.jfaster.mango.util.logging.InternalLoggerFactory;
import org.jfaster.mango.util.reflect.Reflection;
import org.jfaster.mango.plugin.spring.config.MangoConfig;
import org.jfaster.mango.plugin.spring.config.MangoConfigFactory;
import org.jfaster.mango.plugin.spring.config.NamedDataSource;
import org.jfaster.mango.plugin.spring.exception.MangoAutoConfigException;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author  fangyanpeng.
 */
public class MangoDaoAutoCreator implements BeanFactoryPostProcessor,BeanPostProcessor,ApplicationContextAware {

    private final static InternalLogger logger = InternalLoggerFactory.getInstance(MangoDaoAutoCreator.class);


    private static final String PREFIX = "mango";

    private static final List<String> DAO_ENDS = Arrays.asList("Dao", "DAO");

    Class<?> factoryBeanClass = DefaultMangoFactoryBean.class;

    private ApplicationContext context;

    private MangoConfig config;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
        config = MangoConfigFactory.getMangoConfig(beanFactory,PREFIX);
        if(config == null){
            throw new MangoAutoConfigException("Mango config file does not exist!");
        }
        String mangoDaoPackage = config.getScanPackage();
        if(Strings.isEmpty(mangoDaoPackage)){
            throw new MangoAutoConfigException("mango.scan-package is not configured");
        }
        if(!Strings.isEmpty(config.getFactoryClass())){
            try {
                factoryBeanClass = ClassUtils.forName(config.getFactoryClass(), MangoAutoConfiguration.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new MangoAutoConfigException(e);
            }
        }
        if(factoryBeanClass.equals(DefaultMangoFactoryBean.class)){
            Map<String,Map<String,MangoHikaricpConfig>> datasources = config.getDatasources();
            if(datasources == null || datasources.size() == 0){
                throw new MangoAutoConfigException("mango.datasources is not configured");
            }
            registryMangoInstance(beanFactory);
        }
        registryMangoDao(beanFactory);

    }

    /**
     * 向spring中注入mango实例
     * @param beanFactory
     */
    private void registryMangoInstance(DefaultListableBeanFactory beanFactory){
        BeanDefinitionBuilder mangoBuilder = BeanDefinitionBuilder.rootBeanDefinition(Mango.class);

        mangoBuilder.setFactoryMethod("newInstance");
        mangoBuilder.addPropertyValue("checkColumn",config.isCheckColumn());
        mangoBuilder.addPropertyValue("compatibleWithEmptyList",config.isCompatibleWithEmptyList());
        mangoBuilder.addPropertyValue("lazyInit",config.isLazyInit());
        mangoBuilder.addPropertyValue("useActualParamName",config.isUseActualParamName());
        mangoBuilder.addPropertyValue("useTransactionForBatchUpdate",config.isUseTransactionForBatchUpdate());

        configCacheHandler(mangoBuilder);

        beanFactory.registerBeanDefinition(Mango.class.getName(),mangoBuilder.getBeanDefinition());
    }

    /**
     * 设置缓存处理器
     * @param mangoBuilder
     */

    private void configCacheHandler(BeanDefinitionBuilder mangoBuilder){
        String cacheHandlerClassPath = config.getCacheHandler();
        if(!Strings.isEmpty(cacheHandlerClassPath)) {
            try {
                Class<? extends CacheHandler> cachHandlerClz = (Class<? extends CacheHandler>) Class.forName(cacheHandlerClassPath);
                CacheHandler cacheHandler = Reflection.instantiateClass(cachHandlerClz);
                mangoBuilder.addPropertyValue("cacheHandler", cacheHandler);
            } catch (Throwable e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    /**
     * 设置datasource
     * @param mango
     */
    private void configMangoDatasourceFactory(Mango mango){
        for(Map.Entry<String,Map<String,MangoHikaricpConfig>> datasourceEntry : config.getDatasources().entrySet()){
            String dataSourceKey = datasourceEntry.getKey();
            Map<String,MangoHikaricpConfig> datasourceGroup = datasourceEntry.getValue();
            boolean hasMaster = false;
            DataSource masterDataSource = null;
            List<DataSource> slaveDataSources = null;
            DataSourceFactory dataSourceFactory;
            for (Map.Entry<String,MangoHikaricpConfig> datasource : datasourceGroup.entrySet()){
                String key = datasource.getKey();
                MangoHikaricpConfig dataSourceConfig = datasource.getValue();
                String dataSourceRef = dataSourceConfig.getRef();
                if(NamedDataSource.isMaster(key)){
                    if(hasMaster){
                        throw new MangoAutoConfigException("Exists more than one master datasource");
                    }
                    hasMaster = true;
                    masterDataSource = getDataSource(dataSourceRef,dataSourceConfig);
                }else if(NamedDataSource.isSlave(key)){
                    if(slaveDataSources == null){
                        slaveDataSources = new ArrayList<>();
                    }
                    slaveDataSources.add(getDataSource(dataSourceRef,dataSourceConfig));

                }else {
                    logger.info("'{}' is a illegal datasource key",key);
                }
            }
            if(!hasMaster){
                throw new MangoAutoConfigException("Does not exist master datasource");
            }

            if(slaveDataSources == null){ //SimpleDataSourceFactory
                dataSourceFactory = new SimpleDataSourceFactory(dataSourceKey,masterDataSource);
            }else { //MasterSlaveDataSourceFactory
                dataSourceFactory = new MasterSlaveDataSourceFactory(dataSourceKey,masterDataSource,slaveDataSources);
            }
            mango.addDataSourceFactory(dataSourceFactory);
        }
    }

    private DataSource getDataSource(String dataSourceRef,MangoHikaricpConfig dataSourceConfig){
        if(!Strings.isEmpty(dataSourceRef)) {
            DataSource dataSource = context.getBean(dataSourceRef, DataSource.class);
            if (dataSource == null) {
                throw new MangoAutoConfigException("'%s' not exist in spring context", dataSourceRef);
            }
            return dataSource;
        }
        return new HikariDataSource(dataSourceConfig);
    }

    /**
     * 向spring中注入dao代理
     * @param beanFactory
     */
    private void registryMangoDao(DefaultListableBeanFactory beanFactory){
        for (Class<?> daoClass : findMangoDaoClasses(config.getScanPackage())) {
            GenericBeanDefinition bf = new GenericBeanDefinition();
            bf.setBeanClassName(daoClass.getName());
            MutablePropertyValues pvs = bf.getPropertyValues();
            pvs.addPropertyValue("daoClass", daoClass);
            bf.setBeanClass(factoryBeanClass);
            bf.setPropertyValues(pvs);
            bf.setLazyInit(false);
            beanFactory.registerBeanDefinition(daoClass.getName(), bf);
        }
    }


    private List<Class<?>> findMangoDaoClasses(String packages) {
        try {
            List<Class<?>> daos = new ArrayList<Class<?>>();
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (String locationPattern : getLocationPattern(packages)) {
                Resource[] rs = resourcePatternResolver.getResources(locationPattern);
                for (Resource r : rs) {
                    MetadataReader reader = metadataReaderFactory.getMetadataReader(r);
                    AnnotationMetadata annotationMD = reader.getAnnotationMetadata();
                    if (annotationMD.hasAnnotation(DB.class.getName())) {
                        ClassMetadata clazzMD = reader.getClassMetadata();
                        daos.add(Class.forName(clazzMD.getClassName()));
                    }
                }
            }
            return daos;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private List<String> getLocationPattern(String packages) {
        List<String> locationPatterns = new ArrayList<>();
        String[] locationPackages = packages.split("\\s*[,;]+\\s*");
        for (String p : locationPackages) {
            for (String daoEnd : DAO_ENDS) {
                String locationPattern = "classpath*:" + p.replaceAll("\\.", "/") + "/**/*" + daoEnd + ".class";
                logger.info("trnas package[" + p + "] to locationPattern[" + locationPattern + "]");
                locationPatterns.add(locationPattern);
            }
        }
        return locationPatterns;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String s) throws BeansException {
        return bean;
    }

    /**
     * 由于Mango没有提供setInterceptor方法，所以通过beanPostProcessor的方式添加interceptor。同时数据源支持内置和引用混合所以也需要这种方式注入
     * @param bean
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String s) throws BeansException {
        if(bean instanceof Mango){
            /*设置拦截器*/
            Mango mango = (Mango) bean;
            List<String> interceptorClassPaths = config.getInterceptors();
            if(interceptorClassPaths != null && interceptorClassPaths.size() != 0){
                for (String interceptorClassPath : interceptorClassPaths){
                    try {
                        Class< ? extends Interceptor> interceptorClz = (Class<? extends Interceptor>) Class.forName(interceptorClassPath);
                        Interceptor interceptor = Reflection.instantiateClass(interceptorClz);
                        mango.addInterceptor(interceptor);
                    } catch (Throwable e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
            /*设置datasource*/
            if(factoryBeanClass.equals(DefaultMangoFactoryBean.class)){
                configMangoDatasourceFactory(mango);
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
