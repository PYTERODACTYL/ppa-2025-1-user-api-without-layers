package br.edu.ifrs.tads.ppa.demo.packages.user.newUser;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.edu.ifrs.tads.ppa.demo.packages.config.RabbitMQConfig;

@Component
public class NewUserEventListener {

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void newUser(NewUserEvent event) {
        
    }
}