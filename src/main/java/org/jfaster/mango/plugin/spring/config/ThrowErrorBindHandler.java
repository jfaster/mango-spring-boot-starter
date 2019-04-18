package org.jfaster.mango.plugin.spring.config;

import org.jfaster.mango.plugin.spring.exception.MangoAutoConfigException;
import org.springframework.boot.context.properties.bind.BindContext;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;

/**
 *
 * @author yanpengfang
 * create 2019-04-18 5:00 PM
 */
public class ThrowErrorBindHandler extends IgnoreErrorsBindHandler {
    public ThrowErrorBindHandler() {
    }

    public ThrowErrorBindHandler(BindHandler parent) {
        super(parent);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target,
            BindContext context, Exception error) throws Exception {
        throw new  MangoAutoConfigException(error);
    }
}
