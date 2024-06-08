package org.superdata.medismart.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.superdata.medismart.common.constant.Constants;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue queue() {
        return new Queue(Constants.STOCK_QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(Constants.STOCK_EX);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with(Constants.STOCK_ROUT);
    }
}
