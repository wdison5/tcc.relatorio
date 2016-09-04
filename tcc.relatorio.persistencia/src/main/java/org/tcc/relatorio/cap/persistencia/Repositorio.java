/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 140200
 */
public class Repositorio extends org.tcc.relatorio.hammer.persistencia.Repositorio {

    private static final Logger log = LoggerFactory.getLogger(Repositorio.class);
    @PersistenceContext(unitName = "prodesp.cap.persistenciaPU") // <<--- persistence.xml
    protected EntityManager eManager;

    /**
     * @return EntityManager EntityManager
     */
    @Override
    public EntityManager entityManager() {
        //log.debug("entityManager({})", eManager);
        return eManager;
    }
}
