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
Insert into FUNCIONALIDADE (ID,NOME,VER,DESCRICAO,TP_OPER,URL) values (14,'ConsultarRelatorios',0,'Consulta completa a todos os relatorios.',null,null);

Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (1,'Administrador Sistema',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (2,'Acessar Menu',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (3,'Manter Usuário',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (4,'Manter Grupo',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (5,'Manter Instituição',0,null,0);
Insert into GRUPO (ID,NOME,VER,DESCRICAO,FL_EXCLUSAO) values (6,'Manter Beneficiário',0,null,0);
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


--Query de segurança, utilizada no jaas(standalone.xml), para login.
--select f.nome Role, 'Roles' RoleGroup from funcionalidade f, usuario u where  u.user_id = ? and (u.acesso_geral = true or f.id in (select gf.funcionalidades_id from grupo_funcionalidade gf, usuario uu, grupo g, usuario_grupo ug where uu.id = u.id and g.id = gf.grupos_id and g.id = ug.grupos_id and uu.id = ug.usuarios_id)) group by f.nome order by f.nome;
