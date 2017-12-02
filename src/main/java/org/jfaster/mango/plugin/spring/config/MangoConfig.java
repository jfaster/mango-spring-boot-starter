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


import java.util.List;

/**
 * @author fangyanpeng.
 */

public class MangoConfig {

    private String scanPackage;

    private List<MangoDataSourceConfig> datasources;

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

    public List<MangoDataSourceConfig> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<MangoDataSourceConfig> datasources) {
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
