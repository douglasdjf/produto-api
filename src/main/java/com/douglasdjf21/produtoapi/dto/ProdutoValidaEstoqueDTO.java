package com.douglasdjf21.produtoapi.dto;


import com.douglasdjf21.produtoapi.listener.dto.ProdutoQuantidadeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoValidaEstoqueDTO {
    private List<ProdutoQuantidadeDTO> produtos;
}
