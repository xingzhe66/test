package com.dcits.comet.batch.reader;

/*
 *    Copyright 2010-2013 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

        import static org.springframework.util.Assert.notNull;
        import static org.springframework.util.ClassUtils.getShortName;

        import java.util.Map;

        import javax.annotation.Resource;

        import com.dcits.comet.dao.DaoSupport;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.batch.item.database.AbstractPagingItemReader;


/**
 * {@code org.springframework.batch.item.ItemReader} for reading database
 * records using MyBatis in a paging fashion.
 * <p>
 * Provided to facilitate the migration from Spring-Batch iBATIS 2 page item
 * reader to MyBatis 3.
 *
 * @since 1.1.0
 * @version $Id$
 */

public abstract class AbstractPagingReader<T> extends AbstractPagingItemReader<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractPagingReader.class);


//    public AgentProfitTranInReader() {
//        setName(getShortName(AbstractPagingReader.class));
//    }


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
//        ApplicationContext applicationContext = BatchConfig.getApplicationContext();
//        AbstractBatch abstractBatch= (AbstractBatch) applicationContext.getBean("");
//        if (results == null) {
//            results = new CopyOnWriteArrayList<T>();
//        } else {
//            results.clear();
//        }
//        results.addAll(abstractBatch.getChunk());
//        Map<String, Object> parameters = new HashMap<String, Object>();
//        if (parameterValues != null) {
//            parameters.putAll(parameterValues);
//        }
//        parameters.put("_page", getPage());
//        parameters.put("_pagesize", 5);
//        parameters.put("_skiprows", getPage() * getPageSize());
//        parameters.put("_srctype", "2");
//      //  parameters.put("_predate", dateUtil.getPreDate());
//
//        if (results == null) {
//            results = new CopyOnWriteArrayList<AgentPrifitSumMap>();
//        } else {
//            results.clear();
//        }
//        results.addAll(daoSupport.selectList(queryId, parameters));
//        log.debug("++++++++++++++");

        this.doReadPage(getPage() * getPageSize(),getPageSize());
    }

    protected void doReadPage(int offset, int pageSize) { }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }


}
