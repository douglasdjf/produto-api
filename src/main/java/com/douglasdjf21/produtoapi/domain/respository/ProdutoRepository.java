package com.douglasdjf21.produtoapi.domain.respository;

import com.douglasdjf21.produtoapi.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Long> {
}
