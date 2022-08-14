package com.douglasdjf21.produtoapi.listener.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoQuantidadeDTO {

    private Long produtoId;
    private Integer quantidade;
}
