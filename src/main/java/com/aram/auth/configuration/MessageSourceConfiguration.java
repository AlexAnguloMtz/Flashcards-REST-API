package com.aram.auth.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
class MessageSourceConfiguration {

    private static final String MESSAGES_FILE = "classpath:messages";
    private static final String ENCODING = "UTF-8";

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        var bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    private MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGES_FILE);
        messageSource.setDefaultEncoding(ENCODING);
        return messageSource;
    }

}