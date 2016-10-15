Insert into USUARIO (ID, NOME, VER, BLOQ, CPF, DT_CRIACAO, EXP_SENHA, DT_NASCIMENTO, EMAIL, RG, SENHA, SEXO, TENTATIVAS, USER_ID, ACESSO_GERAL, FL_EXCLUSAO)
    values ( 1, 'Administrador', 0, false, null, date('2014-02-28'), date('2030-07-03'), null, 'admin@tcc.org.br', null, 'e86202d43fa8d5b23da7d3292fc293c4a997fab2', null, 0, 'admin', true, 0);


Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (1,'Authenticated',0,'Acesso ao Menu.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (2,'AdminUsuarios',0,'Acesso completo ao CRUD de usu�rios.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (3,'ConsultarUsuarios',0,'Consulta de usu�rios.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (4,'AdminGrupos',0,'Acesso completo ao CRUD de grupos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (5,'ConsultarGrupos',0,'Consulta de grupos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (6,'AdminInstituicoes',0,'Acesso completo ao CRUD de institui��es.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (7,'ConsultarInstituicoes',0,'Consulta de institui��es.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (8,'AdminBeneficiarios',0,'Acesso completo ao CRUD de benefici�rios.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (9,'ConsultarBeneficiarios',0,'Consultas de benefici�rios.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (10,'AdminEmpenhos',0,'Acesso completo ao CRUD de empenhos',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (11,'ConsultarEmpenhos',0,'Consultas de empenhos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (12,'AdminAtestados',0,'Acesso completo ao CRUD de atestados de vida',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (13,'ConsultaAtestados',0,'Consultas de atestados de vida.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (14,'ConsultarRelatorios',0,'Consulta completa a todos os relatorios.',null,null);

Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (1,'Administrador Sistema',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (2,'Acessar Menu',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (3,'Manter Usu�rio',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (4,'Manter Grupo',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (5,'Manter Institui��o',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (6,'Manter Benefici�rio',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (7,'Manter Pagamento',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (8,'Manter Atestado',0,null,0);

Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,4);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,5);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,2);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,3);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,8);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,9);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,10);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,11);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,12);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,13);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,6);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (1,7);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (2,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (3,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (3,2);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (3,3);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (4,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (4,4);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (4,5);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (5,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (5,6);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (5,7);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (6,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (6,8);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (6,9);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (7,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (7,10);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (7,11);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (8,1);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (8,12);
Insert into GRUPO_FUNCIONALIDADE (GRUPOS_ID,FUNCIONALIDADES_ID) values (8,13);


--Query de seguran�a, utilizada no jaas(standalone.xml), para login.
--select f.nome Role, 'Roles' RoleGroup from funcionalidade f, usuario u where  u.user_id = ? and (u.acesso_geral = true or f.id in (select gf.funcionalidades_id from grupo_funcionalidade gf, usuario uu, grupo g, usuario_grupo ug where uu.id = u.id and g.id = gf.grupos_id and g.id = ug.grupos_id and uu.id = ug.usuarios_id)) group by f.nome order by f.nome;




create table "TCC".TIPO_PRODUTO
(
	ID BIGINT not null primary key,
	DESCRICAO VARCHAR(500)
);

create table "TCC".PRODUTO
(
	ID BIGINT not null primary key,
	DATA_REFERENCIA DATE,
	DESCRICAO VARCHAR(500),
	ID_INSTITUICAO BIGINT,
	ID_USUARIO BIGINT,
	QUANTIDADE INTEGER,
	VALOR_CUSTO NUMERIC(19,2),
	VALOR_UNITARIO NUMERIC(19,2),
	ID_TIPO_PRODUTO BIGINT not null,
	VER INTEGER
);




INSERT INTO TCC.TIPO_PRODUTO (ID, DESCRICAO) VALUES (1, 'Pratos');
INSERT INTO TCC.TIPO_PRODUTO (ID, DESCRICAO) VALUES (2, 'Bebidas');
INSERT INTO TCC.TIPO_PRODUTO (ID, DESCRICAO) VALUES (3, 'Sobremesas');




INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (1, CURRENT_DATE, 'Macarr�o', 1, 1, 12, 5.6, 12, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (2, CURRENT_DATE, 'Executivo File', 1, 1, 12, 5.6, 12, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (3, CURRENT_DATE, 'Executivo Frango', 1, 1, 12, 5.6, 12, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (4, CURRENT_DATE, 'Executivo Picanha', 1, 1, 12, 5.6, 12, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (5, CURRENT_DATE, 'Executivo Peixe', 1, 1, 12, 5.6, 12, 1, 0);

INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (6, CURRENT_DATE, 'Coca Cola', 1, 1, 12, 5.6, 12, 2, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) 
	VALUES (7, CURRENT_DATE, 'Suco de Laranja', 1, 1, 12, 5.6, 12, 2, 0);

INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (8, date('2016-10-15'), 'Macarr�o', 1, 1, 3, 7.5, 18, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (9, date('2016-10-15'), 'Executivo File', 1, 1, 5, 16, 29.9, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (10, date('2016-10-15'), 'Executivo Frango', 1, 1, 15, 17, 32, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (11, date('2016-10-15'), 'Executivo Picanha', 1, 1, 5, 25, 50, 1, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (12, date('2016-10-15'), 'Executivo Peixe', 1, 1, 10, 18, 43, 1, 0);

INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (13, date('2016-10-15'), 'Coca Cola', 1, 1, 14, 1.5, 5, 2, 0);
INSERT INTO TCC.PRODUTO (ID, DATA_REFERENCIA, DESCRICAO, ID_INSTITUICAO, ID_USUARIO, QUANTIDADE, VALOR_CUSTO, VALOR_UNITARIO, ID_TIPO_PRODUTO, VER) VALUES (14, date('2016-10-15'), 'Suco de Laranja', 1, 1, 50, 2.5, 7.5, 2, 0);

