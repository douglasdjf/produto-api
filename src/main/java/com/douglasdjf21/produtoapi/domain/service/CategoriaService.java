package com.douglasdjf21.produtoapi.domain.service;

import com.douglasdjf21.produtoapi.domain.entity.Categoria;
import com.douglasdjf21.produtoapi.domain.entity.Produto;
import com.douglasdjf21.produtoapi.domain.respository.CategoriaRepository;
import com.douglasdjf21.produtoapi.dto.CategoriaDTO;
import com.douglasdjf21.produtoapi.exception.ValidacaoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private ModelMapper modelMapper;


    public CategoriaDTO salvar(CategoriaDTO categoriaDTO){
        Categoria categoria = modelMapper.map(categoriaDTO,Categoria.class);
        return  modelMapper.map(repository.save(categoria),CategoriaDTO.class);
    }

    public List<CategoriaDTO> buscar(){
         return   repository.findAll().stream().map(c -> modelMapper.map(c,CategoriaDTO.class)).collect(Collectors.toList());
    }

    public CategoriaDTO atualizar(Long id, CategoriaDTO categoriaDTO){

       Optional<Categoria> optionalCategoria = repository.findById(id);
        if(!optionalCategoria.isPresent())
                throw new ValidacaoException("Id inválido");

        Categoria categoriaNovo = modelMapper.map(categoriaDTO,Categoria.class);
        Categoria categoriaAntes = optionalCategoria.get();

        BeanUtils.copyProperties(categoriaAntes,categoriaNovo,"descricao");
        Categoria categoriaDepois = repository.save(categoriaNovo);

        return modelMapper.map(categoriaDepois,CategoriaDTO.class);
    }

    public void delete(Long id) {
        Optional<Categoria> categoriaOptional = repository.findById(id);
        if(!categoriaOptional.isPresent())
            throw new ValidacaoException("Id inválido");

        repository.deleteById(id);
    }
}
