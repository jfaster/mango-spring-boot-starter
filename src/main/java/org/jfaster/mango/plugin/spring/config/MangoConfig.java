package org.jfaster.mango.plugin.spring.config;


import java.util.List;
import java.util.Map;

/**
 * @author fangyanpeng.
 */

public class MangoConfig {

    private String scanPackage;

    private Map<String,Map<String,MangoHikaricpConfig>> datasources;

    private String factoryClass;

    private boolean compatibleWithEmptyList = true;

    private boolean checkColumn;

    private boolean useActualParamName;

    private boolean useTransactionForBatchUpdate;

    private String cacheHandler;

    private boolean lazyInit;

    private List<String> interceptors;

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public Map<String, Map<String, MangoHikaricpConfig>> getDatasources() {
        return datasources;
    }

    public void setDatasources(Map<String, Map<String, MangoHikaricpConfig>> datasources) {
        this.datasources = datasources;
    }

    public String getFactoryClass() {
        return factoryClass;
    }

    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    public boolean isCompatibleWithEmptyList() {
        return compatibleWithEmptyList;
    }

    public void setCompatibleWithEmptyList(boolean compatibleWithEmptyList) {
        this.compatibleWithEmptyList = compatibleWithEmptyList;
    }

    public boolean isCheckColumn() {
        return checkColumn;
    }

    public void setCheckColumn(boolean checkColumn) {
        this.checkColumn = checkColumn;
    }

    public boolean isUseActualParamName() {
        return useActualParamName;
    }

    public void setUseActualParamName(boolean useActualParamName) {
        this.useActualParamName = useActualParamName;
    }

    public boolean isUseTransactionForBatchUpdate() {
        return useTransactionForBatchUpdate;
    }

    public void setUseTransactionForBatchUpdate(boolean useTransactionForBatchUpdate) {
        this.useTransactionForBatchUpdate = useTransactionForBatchUpdate;
    }

    public String getCacheHandler() {
        return cacheHandler;
    }

    public void setCacheHandler(String cacheHandler) {
        this.cacheHandler = cacheHandler;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public List<String> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<String> interceptors) {
        this.interceptors = interceptors;
    }
}
