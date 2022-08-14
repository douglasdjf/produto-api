package com.douglasdjf21.produtoapi.publisher;

import com.douglasdjf21.produtoapi.listener.dto.VendaConfirmadaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class VendaConfirmacaoPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationKey;

    public void sendSalesConfirmationMessage(@Payload VendaConfirmadaDTO message) {
        try {
            log.info("Enviando mensagem : {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationKey, message);
            log.info("Mensagem enviada com sucesso!");
        } catch (Exception ex) {
            log.info("Erro ao enviar mensagem: ", ex);
        }
    }
}
