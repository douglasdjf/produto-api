package com.douglasdjf21.produtoapi.resource;

import com.douglasdjf21.produtoapi.domain.service.CategoriaService;
import com.douglasdjf21.produtoapi.dto.CategoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CategoriaDTO> salvar(@Valid @RequestBody CategoriaDTO categoriaDTO){
        return ResponseEntity.ok(categoriaService.salvar(categoriaDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> buscar(){
        List<CategoriaDTO> categorias = categoriaService.buscar();
       if(CollectionUtils.isEmpty(categorias))
           return ResponseEntity.noContent().build();

       return ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizar(@PathVariable("id") Long id , @RequestBody CategoriaDTO categoriaDTO){
       return  ResponseEntity.ok(categoriaService.atualizar(id,categoriaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id ){
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
