
INSERT INTO tb_categoria (id, descricao) VALUES (1001, 'Comic Books');
INSERT INTO tb_categoria (id, descricao) VALUES  (1002, 'Movies') ;
INSERT INTO tb_categoria (id, descricao) VALUES  (1003, 'Books');


INSERT INTO tb_fornecedor (id, nome) VALUES (1001, 'Panini Comics');
INSERT INTO tb_fornecedor (id, nome) VALUES  (1002, 'Amazon');

INSERT INTO tb_produto (id, nome, fornecedor_id, categoria_id, quantidade, data_criacao) VALUES (1001, 'Crise nas Infinitas Terras', 1001, 1001, 10, CURRENT_TIMESTAMP);
INSERT INTO tb_produto (id, nome, fornecedor_id, categoria_id, quantidade, data_criacao) VALUES  (1002, 'Interestelar', 1002, 1002, 5, CURRENT_TIMESTAMP);
INSERT INTO tb_produto (id, nome, fornecedor_id, categoria_id, quantidade, data_criacao) VALUES (1003, 'Harry Potter E A Pedra Filosofal', 1002, 1003, 3, CURRENT_TIMESTAMP);