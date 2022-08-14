package com.douglasdjf21.produtoapi.resource;

import com.douglasdjf21.produtoapi.domain.service.FornecedorService;
import com.douglasdjf21.produtoapi.dto.FornecedorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedor")
public class FornecedorResource {

    @Autowired
    private FornecedorService fornecedorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<FornecedorDTO> salvar(@Valid @RequestBody FornecedorDTO fornecedorDTO){
        return ResponseEntity.ok(fornecedorService.salvar(fornecedorDTO));
    }

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> buscar(){
        List<FornecedorDTO> fornecedores = fornecedorService.buscar();
        if(CollectionUtils.isEmpty(fornecedores))
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(fornecedores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable("id") Long id , @RequestBody FornecedorDTO fornecedorDTO){
        return  ResponseEntity.ok(fornecedorService.atualizar(id,fornecedorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id ){
        fornecedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
