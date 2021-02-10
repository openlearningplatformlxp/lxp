package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.services.service.solr.strategies.BaseSolrPagerServiceImpl;
import org.slf4j.Logger;

/**
 * The type Solr paged index thread bo.
 */
public class SolrPagedIndexThreadBO implements Runnable {

    private final Logger log;
    private final BaseSolrPagerServiceImpl strategy;
    private final Integer maxSize;
    private final Integer order;
    private final Integer page;

    /**
     * Instantiates a new Solr paged index thread bo.
     *
     * @param log      the log
     * @param strategy the strategy
     * @param maxSize  the max size
     * @param order    the order
     * @param page     the page
     */
    public SolrPagedIndexThreadBO(Logger log, BaseSolrPagerServiceImpl strategy, Integer maxSize, Integer order,
            Integer page) {
        this.log = log;
        this.strategy = strategy;
        this.maxSize = maxSize;
        this.order = order;
        this.page = page;
    }

    @Override
    public void run() {
        log.warn("Running thread for " + strategy.logName() + " page: " + page);
        strategy.index(maxSize, order, page);
    }
}
