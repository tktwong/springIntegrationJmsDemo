package com.appswalker.springIntegrationJmsDemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.QueueConnectionFactory;
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
public class JMSConfigurer  {

    private String url = "t3://localhost:7001";
    private String connectionFactoryName = "jms/DpmsRequestQcf";
    private String requestQueue = "jms/DpmsRequestQue";

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
    public Destination requestDestination() {
        return lookupByJndiTemplate(requestQueue, Destination.class);
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

