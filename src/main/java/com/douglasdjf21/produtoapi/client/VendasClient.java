package com.douglasdjf21.produtoapi.client;

import com.douglasdjf21.produtoapi.client.dto.VendaProdutoDTO;
import com.douglasdjf21.produtoapi.client.interceptor.FeignClientAuthInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "salesClient",
        contextId = "salesClient",
        url = "${app-config.services.sales}",
        configuration = FeignClientAuthInterceptor.class
)
public interface VendasClient {

    @GetMapping("/api/orders/produto/{produtoId}")
    Optional<VendaProdutoDTO> obterVendasProdutoId(@PathVariable Long produtoId);
}
