package org.tcc.relatorio.cap.web.login;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.service.AutenticadorSC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 * An abstract subclass of AbstractServerLoginModule that imposes an identity == String username, credentials == String
 * password view on the login process.
 *
 * Subclasses override the
 * <code>getUsersPassword()</code> and
 * <code>getRoleSets()</code> methods to return the expected password and roles for the user.
 *
 * @see #getUsername()
 * @see #getUsersPassword()
 * @see #getRoleSets()
 * @see #createIdentity(String)
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision: 85135 $
 *
 */
public class JCapLoginModule extends UsernamePasswordLoginModule {

    private AutenticadorSC service;
    private UsuarioEntity usuario;
    private static final Logger logger = LoggerFactory.getLogger(JCapLoginModule.class);

    /**
     * Construtor.
     */
    public JCapLoginModule() {
        Context ctx = null;
        try {
            ctx = new InitialContext();
            Object obj = ctx.lookup("java:app/jcap.negocio-1.0.99/AutenticadorBC");
            service = (AutenticadorSC) obj;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException exx) {
                    logger.error(exx.getMessage());
                }
            }
        }
        logger.warn("JCapLoginModule(): {}", service);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map sharedState,
            Map options) {
        super.initialize(subject, callbackHandler, sharedState, options);
    }

    /**
     * (required) The UsernamePasswordLoginModule modules compares the result of this method with the actual password.
     */
    @Override
    protected String getUsersPassword() throws LoginException {

        Object obj = getCredentials();
        String userId = this.getUsername();
        String passwd = new String((char[]) obj);
        usuario = new UsuarioEntity();
        try {
            usuario.setUserId(userId);
            usuario.setSenha(passwd);
            usuario = service.autenticar(usuario);
            if (usuario.isOk()) {
                return usuario.getSenha();
            } else {
                for (String msg : usuario.getMsg()) {
                    logger.info(msg);
                }
            }
        } catch (BCException ex) {
            logger.warn("Usuario {} inválido: {}", userId, ex.getMessage());
        }
        return null;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        List<String> funcionalidades = new ArrayList<String>();
        Group grupo = new SimpleGroup("Roles");

        try {
            grupo.addMember(new SimplePrincipal("Authenticated"));
            for (GrupoEntity g : usuario.getGrupos()) {
                for (FuncionalidadeEntity func : g.getFuncionalidades()) {
                    if (!funcionalidades.contains(func.getNome())) {
                        funcionalidades.add(func.getNome());
                        grupo.addMember(new SimplePrincipal(func.getNome()));
                        logger.info("Role added: " + func.getNome());
                    }
                }
            }
        } catch (Exception e) {
            throw new LoginException("Failed to create group member");
        }

        return new Group[]{grupo};
    }
}
