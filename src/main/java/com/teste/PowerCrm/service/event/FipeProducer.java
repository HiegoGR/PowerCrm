package com.teste.PowerCrm.service.event;

import com.teste.PowerCrm.config.RabbitMQConfig;
import com.teste.PowerCrm.dto.FipeRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class FipeProducer {

    private final RabbitTemplate rabbitTemplate;

    public FipeProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(FipeRequestDTO message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FIPE_QUEUE, message);
    }
}
