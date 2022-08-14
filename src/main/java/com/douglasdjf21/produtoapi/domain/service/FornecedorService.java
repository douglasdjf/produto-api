package com.douglasdjf21.produtoapi.domain.service;

import com.douglasdjf21.produtoapi.domain.entity.Categoria;
import com.douglasdjf21.produtoapi.domain.entity.Fornecedor;
import com.douglasdjf21.produtoapi.domain.respository.FornecedorRepository;
import com.douglasdjf21.produtoapi.dto.FornecedorDTO;
import com.douglasdjf21.produtoapi.exception.ValidacaoException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public FornecedorDTO salvar(FornecedorDTO fornecedorDTO){
        Fornecedor fornecedor = modelMapper.map(fornecedorDTO,Fornecedor.class);
        return  modelMapper.map(repository.save(fornecedor), FornecedorDTO.class);
    }

    public List<FornecedorDTO> buscar(){
        return   repository.findAll().stream().map(c -> modelMapper.map(c,FornecedorDTO.class)).collect(Collectors.toList());
    }

    public FornecedorDTO atualizar(Long id, FornecedorDTO fornecedorDTO){

        Optional<Fornecedor> optionalFornecedor = repository.findById(id);
        if(!optionalFornecedor.isPresent())
            throw new ValidacaoException("Id inválido");

        Fornecedor fornecedorNovo = modelMapper.map(fornecedorDTO,Fornecedor.class);
        Fornecedor fornecedorAntes = optionalFornecedor.get();

        BeanUtils.copyProperties(fornecedorAntes,fornecedorNovo,"nome");
        Fornecedor fornecedorDepois = repository.save(fornecedorNovo);

        return modelMapper.map(fornecedorDepois,FornecedorDTO.class);
    }

    public void delete(Long id) {
        Optional<Fornecedor> fornecedorOptional = repository.findById(id);
        if(!fornecedorOptional.isPresent())
            throw new ValidacaoException("Id inválido");

        repository.deleteById(id);
    }


}
