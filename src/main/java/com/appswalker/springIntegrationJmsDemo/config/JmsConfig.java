package com.appswalker.springIntegrationJmsDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import java.util.Properties;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jndi.JndiTemplate;

@Configuration
@EnableJms
public class JmsConfig {

    private String url = "t3://localhost:7001";
    private String connectionFactoryName = "jms/DpmsRequestQcf";
    private String dpmsRequestQueJndiName = "jms/DpmsRequestQue";
    private String dpmsServiceQcfJndiName = "jms/DpmsServiceQcf";
    private String dpmsServiceQueJndiName = "jms/DpmsServiceQue";
    private String dpmsServiceTcfJndiName = "jms/DpmsServiceTcf";
    private String dpmsServiceTopicJndiName = "jms/DpmsServiceTopic";

    private Properties getJNDiProperties() {
        final Properties jndiProps = new Properties();
        jndiProps.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        jndiProps.setProperty(Context.PROVIDER_URL, url);
        return jndiProps;
    }

    /**
     * Create connection factory.
     *
     * @return
     */
    @Bean
    public ConnectionFactory queueConnectionFactory() {
        // JNDI connection factory name stored in weblogic.
        return new CachingConnectionFactory(lookupByJndiTemplate(connectionFactoryName, QueueConnectionFactory.class));
    }

    @Bean
    public ConnectionFactory dpmsServiceQcf() {
        return new CachingConnectionFactory(lookupByJndiTemplate(dpmsServiceQcfJndiName, QueueConnectionFactory.class));
    }

    @Bean
    public ConnectionFactory dpmsServiceTcf() {
        return new CachingConnectionFactory(lookupByJndiTemplate(dpmsServiceTcfJndiName, TopicConnectionFactory.class));
    }

    /**
     * Create InitialContext.
     *
     * @return
     */
    @Bean
    public JndiTemplate jndiTemplate() {
        JndiTemplate jndiTemplate = new JndiTemplate();
        jndiTemplate.setEnvironment(getJNDiProperties());
        return jndiTemplate;
    }

    @Bean
    public Destination dpmsRequestQue() {
        return lookupByJndiTemplate(dpmsRequestQueJndiName, Destination.class);
    }

    @Bean
    public Destination dpmsServiceQue() {
        return lookupByJndiTemplate(dpmsServiceQueJndiName, Destination.class);
    }

    @Bean
    public Destination dpmsServiceTopic() {
        return lookupByJndiTemplate(dpmsServiceTopicJndiName, Topic.class);
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /**
     *
     * @param jndiName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T lookupByJndiTemplate(String jndiName, Class<T> requiredType) {
        try {
            Object located = jndiTemplate().lookup(jndiName);
            if (located == null) {
                throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
            }
            return (T) located;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param jndiName
     * @param requiredType
     * @return
     */
    @SuppressWarnings("unchecked")
    protected final <T> T lookup(String jndiName, Class<T> requiredType) {

        try {
            InitialContext initialContext = new InitialContext(getJNDiProperties());
            Object located = initialContext.lookup(jndiName);
            if (located == null) {
                throw new NameNotFoundException("JNDI object with [" + jndiName + "] not found");
            }
            return (T) located;
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

