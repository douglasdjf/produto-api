package com.douglasdjf21.produtoapi.dto;

import com.douglasdjf21.produtoapi.domain.entity.Produto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProdutoVendaDTO {

    private Long id;
    private String nome;
    private Integer quantidade;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;
    private CategoriaDTO categoria;
    private FornecedorDTO fornecedor;
    private List<String> vendas;

    public void inserirVendasIds(List<String> vendasIds){
        this.vendas = vendasIds;
    }

}
