package com.douglasdjf21.produtoapi.resource;


import com.douglasdjf21.produtoapi.domain.service.ProdutoService;
import com.douglasdjf21.produtoapi.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/produto")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<ProdutoDTO> salvar(@Valid @RequestBody ProdutoDTO produtoDTO){
        log.info("salvar");
        log.info("body: " + produtoDTO.toString());
        return ResponseEntity.ok(produtoService.salvar(produtoDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> buscar(){
        List<ProdutoDTO> produtos = produtoService.buscar();
        if(CollectionUtils.isEmpty(produtos))
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable("id") Long id , @RequestBody ProdutoUpdateDTO produtoDTO){
        log.info("atualizar");
        log.info("body: " + produtoDTO.toString());
        return  ResponseEntity.ok(produtoService.atualizar(id,produtoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id ){
        log.info("delete");
        log.info("body: " + id);
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("check-estoque")
    public ResponseEntity<RetornoDTO> checkProdutoEstoque(@RequestBody  ProdutoValidaEstoqueDTO  produtoValidaEstoqueDTO){
            log.info("check-estoque");
            log.info("body: " + produtoValidaEstoqueDTO.toString());
            return ResponseEntity.ok(produtoService.checkProdutoEstoque(produtoValidaEstoqueDTO));
    }

    @GetMapping("{id}/vendas")
    public ProdutoVendaDTO obterProdutosVendas(@PathVariable Long id) {
        log.info("{id}/vendas");
        log.info("body: " + id);
        return produtoService.obterProdutoVendas(id);
    }


}
