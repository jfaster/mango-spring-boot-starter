/*
 * Copyright 2014 mango.jfaster.org
 *
 * The Mango Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.jfaster.mango.plugin.spring.config;

import java.util.Map;
import java.util.function.Consumer;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;

/**
 * @author yangnan.
 */
public class MangoConfigFactory {

    /**
     * 获取配置
     *
     * @param beanFactory beanFactory
     * @param prefix 前缀
     * @return
     */
    public static MangoConfig getMangoConfig(DefaultListableBeanFactory beanFactory, String prefix) {
        MangoConfig config = new MangoConfig();
        Bindable<?> target = Bindable.ofInstance(config);
        PropertySources propertySources = getPropertySources(beanFactory);
        BindHandler bindHandler = getBindHandler();
        BindResult configBindResult = getBinder(propertySources, beanFactory).bind(prefix, target, bindHandler);
        return (MangoConfig) configBindResult.get();
    }


    private static Binder getBinder(PropertySources propertySources, DefaultListableBeanFactory beanFactory) {
        return new Binder(getConfigurationPropertySources(propertySources),
                          getPropertySourcesPlaceholdersResolver(propertySources), getConversionService(),
                          getPropertyEditorInitializer(beanFactory));
    }

    private static Consumer<PropertyEditorRegistry> getPropertyEditorInitializer(DefaultListableBeanFactory beanFactory) {
        return beanFactory::copyRegisteredEditorsTo;
    }

    private static ConversionService getConversionService() {
        return new DefaultConversionService();
    }

    private static Iterable<ConfigurationPropertySource> getConfigurationPropertySources(PropertySources propertySources) {
        return ConfigurationPropertySources.from(propertySources);
    }

    private static PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver(PropertySources propertySources) {
        return new PropertySourcesPlaceholdersResolver(propertySources);
    }

    private static BindHandler getBindHandler() {
        BindHandler handler = new ThrowErrorBindHandler();
        return handler;
    }

    public static PropertySources getPropertySources(DefaultListableBeanFactory beanFactory) {
        PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer(beanFactory);
        if (configurer != null) {
            return configurer.getAppliedPropertySources();
        }
        MutablePropertySources sources = extractEnvironmentPropertySources(beanFactory);
        if (sources != null) {
            return sources;
        }
        throw new IllegalStateException("Unable to obtain PropertySources from "
                                                + "PropertySourcesPlaceholderConfigurer or Environment");
    }

    private static MutablePropertySources extractEnvironmentPropertySources(DefaultListableBeanFactory beanFactory) {
        Environment environment = beanFactory.getBean(Environment.class);
        if (environment instanceof ConfigurableEnvironment) {
            return ((ConfigurableEnvironment) environment).getPropertySources();
        }
        return null;
    }

    private static PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer(DefaultListableBeanFactory beanFactory) {
        // Take care not to cause early instantiation of all FactoryBeans
        Map<String, PropertySourcesPlaceholderConfigurer> beans = beanFactory.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
        if (beans.size() == 1) {
            return beans.values().iterator().next();
        }
        return null;
    }

}
