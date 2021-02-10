package com.redhat.uxl.webapp.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * The interface Solr index job service.
 */
public interface SolrIndexJobService {
    /**
     * Reindex data.
     */
    @Scheduled(cron = "${app.solr.indexJob.cronSchedule}")
    void reindexData();

    /**
     * Reindex manual data.
     */
    @Scheduled(fixedRate = 1000)
    void reindexManualData();
}
