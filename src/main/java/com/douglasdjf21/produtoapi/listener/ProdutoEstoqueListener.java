package com.douglasdjf21.produtoapi.listener;

import com.douglasdjf21.produtoapi.domain.service.ProdutoService;
import com.douglasdjf21.produtoapi.listener.dto.ProdutoEstoqueDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProdutoEstoqueListener {

    @Autowired
    private ProdutoService produtoService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void recieveProductStockMessage(@Payload ProdutoEstoqueDTO produtoEstoqueDTO) throws JsonProcessingException {
        log.info("Recebendo a mensagem : {} and TransactionID: {}",
                new ObjectMapper().writeValueAsString(produtoEstoqueDTO),
                produtoEstoqueDTO.getTransactionId());
        produtoService.atualizaProdutoEstoque(produtoEstoqueDTO);
    }
}
