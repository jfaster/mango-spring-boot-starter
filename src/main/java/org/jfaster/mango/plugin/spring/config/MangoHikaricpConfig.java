package org.jfaster.mango.plugin.spring.config;

import com.zaxxer.hikari.HikariConfig;

/**
 * @author fangyanpeng.
 */
public class MangoHikaricpConfig extends HikariConfig {
    /*如果ref有值,则引用其他数据源*/
    private String ref;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
