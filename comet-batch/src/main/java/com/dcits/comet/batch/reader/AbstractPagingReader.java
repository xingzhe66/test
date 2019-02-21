package com.dcits.comet.batch.reader;


import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ClassUtils.getShortName;

import java.util.Map;

import javax.annotation.Resource;

import com.dcits.comet.dao.DaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * {@code org.springframework.batch.item.ItemReader} for reading database
 * records using MyBatis in a paging fashion.
 * <p>
 * Provided to facilitate the migration from Spring-Batch iBATIS 2 page item
 * reader to MyBatis 3.
 *
 * @version $Id$
 * @since 1.1.0
 */

public abstract class AbstractPagingReader<T> extends AbstractPagingItemReader<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractPagingReader.class);

    /**
     * Check mandatory properties.
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

    }

    @Override
    protected void doReadPage() {
        this.doReadPage(getPage() * getPageSize(), getPageSize());
    }

    protected void doReadPage(int offset, int pageSize) {
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }


}
