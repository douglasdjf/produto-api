package com.douglasdjf21.produtoapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false)
    private String nome;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fornecedor_id",nullable = false)
    private Fornecedor fornecedor;


    @PrePersist
    public void prePersist(){
        dataCriacao = LocalDateTime.now();
    }

    public void atualizaQuantidade(Integer qtd) {
        quantidade = quantidade - qtd;
    }
}
