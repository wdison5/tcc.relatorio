package org.tcc.relatorio.hammer.persistencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdi_s
 */
@Deprecated
public class HibernateDao {
    private static final Logger log = LoggerFactory.getLogger(HibernateDao.class);
    private int count = 0;
    protected static final int FLUSH_COUNT = 20;
    protected static final int CLEAN_COUNT = 2000;

    public HibernateDao() {
        log.debug("__¢ HibernateDao");
    }
}
