package com.douglasdjf21.produtoapi.domain.service;


import com.douglasdjf21.produtoapi.client.VendasClient;
import com.douglasdjf21.produtoapi.client.dto.VendaProdutoDTO;
import com.douglasdjf21.produtoapi.domain.entity.Produto;
import com.douglasdjf21.produtoapi.domain.respository.ProdutoRepository;
import com.douglasdjf21.produtoapi.dto.*;
import com.douglasdjf21.produtoapi.exception.ValidacaoException;
import com.douglasdjf21.produtoapi.listener.dto.ProdutoEstoqueDTO;
import com.douglasdjf21.produtoapi.listener.dto.ProdutoQuantidadeDTO;
import com.douglasdjf21.produtoapi.listener.dto.VendaConfirmadaDTO;
import com.douglasdjf21.produtoapi.listener.enums.VendasStatus;
import com.douglasdjf21.produtoapi.publisher.VendaConfirmacaoPublisher;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;


@Slf4j
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaConfirmacaoPublisher vendaConfirmacaoPublisher;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VendasClient vendasClient;

    public ProdutoDTO salvar(ProdutoDTO produtoDTO){
        Produto produto = modelMapper.map(produtoDTO,Produto.class);
        return  modelMapper.map(produtoRepository.save(produto), ProdutoDTO.class);
    }

    public List<ProdutoDTO> buscar(){
        return   produtoRepository.findAll().stream().map(c -> modelMapper.map(c,ProdutoDTO.class)).collect(Collectors.toList());
    }

    public ProdutoDTO atualizar(Long id, ProdutoUpdateDTO produtoDTO){

        Optional<Produto> optionalProduto = produtoRepository.findById(id);
        if(!optionalProduto.isPresent())
            throw new ValidacaoException("Id inválido");

        Produto produtoNovo = modelMapper.map(produtoDTO,Produto.class);
        Produto produtoAntes = optionalProduto.get();
        Produto produtoAlterado =  Produto.builder().build();

        BeanUtils.copyProperties(produtoAntes,produtoAlterado);
        BeanUtils.copyProperties(produtoNovo,produtoAlterado,"id","dataCriacao","fornecedor","categoria");
        Produto fornecedorDepois = produtoRepository.save(produtoAlterado);

        return modelMapper.map(fornecedorDepois,ProdutoDTO.class);
    }

    public void delete(Long id) {
        Optional<Produto> optionalProduto = produtoRepository.findById(id);
        if(!optionalProduto.isPresent())
            throw new ValidacaoException("Id inválido");

        produtoRepository.deleteById(id);
    }

    public Produto findById(Long id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ValidacaoException("Id do Produto Inválido"));
    }


    /**
     * Atualiza e valida o estoque
     * @param produtoEstoqueDTO
     */
    public void atualizaProdutoEstoque(ProdutoEstoqueDTO produtoEstoqueDTO) {
        try {
            log.info("atualizaProdutoEstoque");
            validaEstoqueAtualizaDados(produtoEstoqueDTO);
            atualizaEstoque(produtoEstoqueDTO);
        } catch (Exception ex) {
            log.error("Erro ao tentar atualizar o estoque: {}", ex.getMessage(), ex);
            var rejectedMessage = VendaConfirmadaDTO.builder()
                                      .vendaId(produtoEstoqueDTO.getVendaId())
                                      .transactionId(produtoEstoqueDTO.getTransactionId())
                                      .status(VendasStatus.REJEITADO)
                                      .timestamp(LocalDateTime.now())
                                      .build();
            vendaConfirmacaoPublisher.sendSalesConfirmationMessage(rejectedMessage);
        }
    }

    /**
     *
     * Valida o estoque
     * @param produtoEstoqueDTO
     */
    private void validaEstoqueAtualizaDados(ProdutoEstoqueDTO produtoEstoqueDTO) {
        log.info("validaEstoqueAtualizaDados");
        if (isEmpty(produtoEstoqueDTO)
                || isEmpty(produtoEstoqueDTO.getVendaId())) {
            throw new ValidacaoException("As informações do produto e vendas id deve ser informados .");
        }
        if (isEmpty(produtoEstoqueDTO.getProdutos())) {
            throw new ValidacaoException("Os produtos da venda devem ser informados.");
        }
        log.info("produtoEstoqueDTO: " + produtoEstoqueDTO);
        produtoEstoqueDTO
                .getProdutos()
                .forEach(vendasProduto -> {
                    if (isEmpty(vendasProduto.getQuantidade())
                            || isEmpty(vendasProduto.getProdutoId())) {
                        throw new ValidacaoException("O produto id e a quantidade devem ser informadas");
                    }
                });
    }

    /**
     * Atualiza estoque
     * @param produtoEstoqueDTO
     */
    @Transactional
    private void atualizaEstoque(ProdutoEstoqueDTO produtoEstoqueDTO) {
        log.info("atualizaEstoque");
        log.info("produtoEstoqueDTO: " + produtoEstoqueDTO);

        var produtosParaAtualizar = new ArrayList<Produto>();

        produtoEstoqueDTO
                .getProdutos()
                .forEach(vendasProduto -> {
                    var produtoExistente = findById(vendasProduto.getProdutoId());
                    validaQuantidadeEmEstoque(vendasProduto, produtoExistente);
                    produtoExistente.atualizaQuantidade(vendasProduto.getQuantidade());
                    produtosParaAtualizar.add(produtoExistente);
                });
        if (!isEmpty(produtosParaAtualizar)) {
            produtoRepository.saveAll(produtosParaAtualizar);

            var approvedMessage = VendaConfirmadaDTO.builder()
                    .vendaId(produtoEstoqueDTO.getVendaId())
                    .transactionId(produtoEstoqueDTO.getTransactionId())
                    .status(VendasStatus.APROVADO)
                    .timestamp(LocalDateTime.now())
                    .build();
            vendaConfirmacaoPublisher.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    /**
     * Valida quantidade no estoque
     *
     * @param produtoQuantidadeDTO
     * @param existingProduct
     */
    private void validaQuantidadeEmEstoque(ProdutoQuantidadeDTO produtoQuantidadeDTO,
                                           Produto existingProduct) {

        log.info("validaQuantidadeEmEstoque");
        if (produtoQuantidadeDTO.getQuantidade() > existingProduct.getQuantidade()) {
            throw new ValidacaoException(
                    String.format("The product %s is out of stock.", existingProduct.getId()));
        }
    }

    public ProdutoVendaDTO obterProdutoVendas(Long id) {
        log.info("obterProdutoVendas");
        log.info("id: " + id);
        Produto produto = findById(id);
        try{
            VendaProdutoDTO vendasProdutoDto = vendasClient
                                                    .obterVendasProdutoId(produto.getId())
                                                    .orElseThrow(()->  new ValidacaoException("vendas não encontrada para o produto informado"));

          ProdutoVendaDTO produtoVendas =  modelMapper.map(produto,ProdutoVendaDTO.class);
          produtoVendas.inserirVendasIds(vendasProdutoDto.getVendas());
          return produtoVendas;
        }catch (Exception ex){
            throw  new ValidacaoException("Erro ao tentar obter as vendas do produto");
        }


    }

    public RetornoDTO checkProdutoEstoque(ProdutoValidaEstoqueDTO  produtoValidaEstoqueDTO){
        log.info("checkProdutoEstoque");
        log.info("produtoValidaEstoqueDTO: " + produtoValidaEstoqueDTO);
        if(ObjectUtils.isEmpty(produtoValidaEstoqueDTO) || ObjectUtils.isEmpty(produtoValidaEstoqueDTO.getProdutos()) )
            throw new ValidacaoException("Produto de validação de estoque inválido");

        produtoValidaEstoqueDTO
                    .getProdutos()
                    .forEach(this::validaEstoque);
        return RetornoDTO.builder()
                         .status(HttpStatus.OK.value())
                         .message("Estoque está ok!")
                         .build();

    }

    private void validaEstoque(ProdutoQuantidadeDTO produtoQuantidadeDTO){
            if(ObjectUtils.isEmpty(produtoQuantidadeDTO.getQuantidade()) || ObjectUtils.isEmpty(produtoQuantidadeDTO.getProdutoId())){
                throw new ValidacaoException("Dados do produto inválidos");
            }
            Produto produto = findById(produtoQuantidadeDTO.getProdutoId());
            if(produtoQuantidadeDTO.getQuantidade()> produto.getQuantidade()){
                throw  new ValidacaoException(String.format("Produto %s fora de estoque ",produto.getId()));
            }
    }


}
