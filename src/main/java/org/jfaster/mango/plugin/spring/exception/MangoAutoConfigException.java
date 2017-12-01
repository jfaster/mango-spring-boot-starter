package org.jfaster.mango.plugin.spring.exception;

/**
 * @author fangyanpeng.
 */
public class MangoAutoConfigException extends RuntimeException {

    public MangoAutoConfigException(Throwable e){
        super(e);
    }

    public MangoAutoConfigException(Exception e){
        super(e);
    }

    public MangoAutoConfigException(String message){
        super(message);
    }

    public MangoAutoConfigException(String format,Object... args){
        super(String.format(format,args));
    }
}
