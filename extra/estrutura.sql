CREATE TABLE produto
  (
    id_produto      bigint NOT NULL ,
    descricao       varchar (500) ,
    id_tipo_produto bigint NOT NULL ,
    data_referencia DATE ,
    quantidade      int ,
    valor_unitario  numeric (20,2) ,
    valor_custo     numeric (20,2) ,
    id_usuario      bigint ,
    id_instituicao  bigint
  ) ;
ALTER TABLE produto ADD CONSTRAINT produto_PK PRIMARY KEY ( id_produto ) ;
CREATE TABLE tipo_produto
  (
    id_tipo_produto bigint NOT NULL ,
    descricao       varchar (500)
  );
ALTER TABLE tipo_produto ADD CONSTRAINT tipo_produto_PK PRIMARY KEY ( id_tipo_produto ) ;
ALTER TABLE produto ADD CONSTRAINT produto_tipo_produto_FK FOREIGN KEY ( id_tipo_produto ) REFERENCES tipo_produto ( id_tipo_produto ) ;
