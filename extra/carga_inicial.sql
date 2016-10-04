Insert into USUARIO (ID, NOME, VER, BLOQ, CPF, DT_CRIACAO, EXP_SENHA, DT_NASCIMENTO, EMAIL, RG, SENHA, SEXO, TENTATIVAS, USER_ID, ACESSO_GERAL, FL_EXCLUSAO)
    values ( 1, 'Administrador', 0, false, null, date('2014-02-28'), date('2030-07-03'), null, 'admin@tcc.org.br', null, 'e86202d43fa8d5b23da7d3292fc293c4a997fab2', null, 0, 'admin', true, 0);


Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (1,'Authenticated',0,'Acesso ao Menu.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (2,'AdminUsuarios',0,'Acesso completo ao CRUD de usuários.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (3,'ConsultarUsuarios',0,'Consulta de usuários.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (4,'AdminGrupos',0,'Acesso completo ao CRUD de grupos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (5,'ConsultarGrupos',0,'Consulta de grupos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (6,'AdminInstituicoes',0,'Acesso completo ao CRUD de instituições.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (7,'ConsultarInstituicoes',0,'Consulta de instituições.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (8,'AdminBeneficiarios',0,'Acesso completo ao CRUD de beneficiários.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (9,'ConsultarBeneficiarios',0,'Consultas de beneficiários.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (10,'AdminEmpenhos',0,'Acesso completo ao CRUD de empenhos',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (11,'ConsultarEmpenhos',0,'Consultas de empenhos.',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (12,'AdminAtestados',0,'Acesso completo ao CRUD de atestados de vida',null,null);
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (13,'ConsultaAtestados',0,'Consultas de atestados de vida.',null,null);

--Query de segurança, utilizada no jaas(standalone.xml), para login.
--select f.nome Role, 'Roles' RoleGroup from funcionalidade f, usuario u where  u.user_id = ? and (u.acesso_geral = true or f.id in (select gf.funcionalidades_id from grupo_funcionalidade gf, usuario uu, grupo g, usuario_grupo ug where uu.id = u.id and g.id = gf.grupos_id and g.id = ug.grupos_id and uu.id = ug.usuarios_id)) group by f.nome order by f.nome;
