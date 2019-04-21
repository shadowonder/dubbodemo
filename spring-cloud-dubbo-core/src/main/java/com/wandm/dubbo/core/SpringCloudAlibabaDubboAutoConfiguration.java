package com.wandm.dubbo.core;

import com.alibaba.dubbo.config.AbstractConfig;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.DubboFeignBuilder;
import com.alibaba.dubbo.config.spring.beans.factory.annotation.FeignClientToDubboProviderBeanPostProcessor;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Set;

import static com.alibaba.boot.dubbo.util.DubboUtils.BASE_PACKAGES_PROPERTY_NAME;
import static com.alibaba.boot.dubbo.util.DubboUtils.DUBBO_PREFIX;
import static java.util.Collections.emptySet;

/**
 * 自己注入。把我们的FeignBuilder注入到feign.builder中
 * 根据两个condition，只有有dubbo以来的时候，配置才会生效，也就是有dubbo的时候注入到dubbo中，没有的话使用默认http注入
 * 此时feign注解既可以：
 *      http客户端调用
 *      可以是@service注解
 *      能当@reference注解
 */
@Configuration
//当配置文件有dubbo的时候，配置才生效
@ConditionalOnProperty(prefix = DUBBO_PREFIX, name = "enabled", matchIfMissing = true, havingValue = "true")
//当classpath有AbstractConfig的时候配置才生效
@ConditionalOnClass(AbstractConfig.class)
public class SpringCloudAlibabaDubboAutoConfiguration {
    @ConditionalOnProperty(name = BASE_PACKAGES_PROPERTY_NAME)
    @ConditionalOnClass(ConfigurationPropertySources.class)
    @Bean//用我们自己的不用dubbo的
    public FeignClientToDubboProviderBeanPostProcessor feignClientToDubboProviderBeanPostProcessor(Environment environment) {
        Set<String> packagesToScan = environment.getProperty(BASE_PACKAGES_PROPERTY_NAME, Set.class, emptySet());
        return new FeignClientToDubboProviderBeanPostProcessor(packagesToScan);
    }

    @Bean
    public Feign.Builder feignDubboBuilder() {
        return new DubboFeignBuilder();
    }
}