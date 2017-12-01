package org.jfaster.mango.plugin.spring.config;

import org.jfaster.mango.util.Strings;

/**
 * @author fangyanpeng.
 */
public enum NamedDataSource {
    
    MASTER("master"),SLAVE("slave");

    private String name;
    
    NamedDataSource(String name){
        this.name = name;
    }

    public static boolean isMaster(String name){
        return MASTER.name.equalsIgnoreCase(name);
    }

    public static boolean isSlave(String name){
        if(Strings.isEmpty(name)){
            return false;
        }
        return name.startsWith(SLAVE.name);
    }
    
}
