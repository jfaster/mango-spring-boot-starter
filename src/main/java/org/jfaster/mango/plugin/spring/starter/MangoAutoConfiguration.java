package org.jfaster.mango.plugin.spring.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  fangyanpeng.
 */
@Configuration
@ConditionalOnClass({MangoDaoAutoCreator.class})
public class MangoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MangoDaoAutoCreator.class)
    public MangoDaoAutoCreator autoCreator(){
        return new MangoDaoAutoCreator();
    }
}
