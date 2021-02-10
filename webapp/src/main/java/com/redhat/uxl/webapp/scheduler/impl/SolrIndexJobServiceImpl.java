package com.redhat.uxl.webapp.scheduler.impl;

import com.redhat.uxl.dataobjects.domain.IndexData;
import com.redhat.uxl.dataobjects.domain.types.IndexStatusType;
import com.redhat.uxl.services.service.SolrService;
import com.redhat.uxl.webapp.scheduler.SolrIndexJobService;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.logging.Level;

/**
 * The type Solr index job service.
 */
@Log
@ConditionalOnProperty("app.solr.indexJob.enabled")
@Service
public class SolrIndexJobServiceImpl implements SolrIndexJobService {

    @Inject
    private SolrService solrService;

    @Override
    @Scheduled(cron = "${app.solr.indexJob.cronSchedule}")
    public void reindexData() {
        log.log(Level.INFO, "Starting job to reindex solr database");
        solrService.saveIndexStart(0l);
        solrService.reindexSearchDatabase();

    }

    @Override
    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void reindexManualData() {
        IndexData indexData = solrService.finLastQueuedData();
        if (indexData != null) {
            indexData.setStatus(IndexStatusType.STARTED);
            solrService.updateIndex(indexData);
            solrService.reindexSearchDatabase();
        }

    }

}
