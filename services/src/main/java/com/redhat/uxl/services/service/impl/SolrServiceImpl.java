package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.dao.TotaraSolrDAO;
import com.redhat.uxl.datalayer.repository.IndexDataRepository;
import com.redhat.uxl.datalayer.solr.repository.CourseDocumentSolrRepository;
import com.redhat.uxl.dataobjects.domain.IndexData;
import com.redhat.uxl.dataobjects.domain.types.IndexStatusType;
import com.redhat.uxl.services.service.SolrService;
import com.redhat.uxl.services.service.bo.SolrPagedIndexThreadBO;
import com.redhat.uxl.services.service.solr.strategies.AllegoCourseSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.AllegoRecipientsSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.BaseSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.KalturaCourseSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.LinkedinCourseSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.TotaraClassesSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.TotaraCourseSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.TotaraProgramSolrPagerServiceImpl;
import com.redhat.uxl.services.service.solr.strategies.WikipagesSolrPagerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The type Solr service.
 */
@Service
@Slf4j
public class SolrServiceImpl implements SolrService {

    /**
     * The constant DEFAULT_PROGRAM_MAX_SIZE.
     */
    public static final int DEFAULT_PROGRAM_MAX_SIZE = 20;
    /**
     * The constant DEFAULT_MAX_CLASSES_SIZE.
     */
    public static final int DEFAULT_MAX_CLASSES_SIZE = 20;
    /**
     * The constant DEFAULT_MAX_KALTURA_SIZE.
     */
    public static final int DEFAULT_MAX_KALTURA_SIZE = 500;
    /**
     * The constant DEFAULT_MAX_LYNDA_SIZE.
     */
    public static final int DEFAULT_MAX_LYNDA_SIZE = 100;
    /**
     * The constant DEFAULT_MAX_ALLEGO_RECIPIENTS_SIZE.
     */
    public static final int DEFAULT_MAX_ALLEGO_RECIPIENTS_SIZE = 500;
    /**
     * The constant DEFAULT_MAX_ALLEGO_COURSES_SIZE.
     */
    public static final int DEFAULT_MAX_ALLEGO_COURSES_SIZE = 200;
    /**
     * The constant MAX_SIZE_TO_ADD.
     */
    public static final int MAX_SIZE_TO_ADD = 100;

    /**
     * The Index data repository.
     */
    @Inject
    IndexDataRepository indexDataRepository;

    /**
     * The Wikipages solr pager service.
     */
    @Inject
    WikipagesSolrPagerServiceImpl wikipagesSolrPagerService;

    /**
     * The Course document solr repository.
     */
    @Inject
    CourseDocumentSolrRepository courseDocumentSolrRepository;

    /**
     * The Classes solr pager service.
     */
    @Inject
    TotaraClassesSolrPagerServiceImpl classesSolrPagerService;

    /**
     * The Course solr pager service.
     */
    @Inject
    TotaraCourseSolrPagerServiceImpl courseSolrPagerService;

    /**
     * The Program solr pager service.
     */
    @Inject
    TotaraProgramSolrPagerServiceImpl programSolrPagerService;

    /**
     * The Kaltura course solr pager service.
     */
    @Inject
    KalturaCourseSolrPagerServiceImpl kalturaCourseSolrPagerService;

    /**
     * The Allego recipients solr pager service.
     */
    @Inject
    AllegoRecipientsSolrPagerServiceImpl allegoRecipientsSolrPagerService;

    /**
     * The Allego course solr pager service.
     */
    @Inject
    AllegoCourseSolrPagerServiceImpl allegoCourseSolrPagerService;

    /**
     * The Linkedin course solr pager service.
     */
    @Inject
    LinkedinCourseSolrPagerServiceImpl linkedinCourseSolrPagerService;

    /**
     * The Totara solr dao.
     */
    @Inject
    protected TotaraSolrDAO totaraSolrDAO;

    /**
     * The Kaltura enabled.
     */
    @Value("${app.kaltura.api.enabled:false}")
    protected boolean kalturaEnabled;

    /**
     * The Allego enabled.
     */
    @Value("${app.allego.api.enabled:false}")
    protected boolean allegoEnabled;

    /**
     * The Linkedin enabled.
     */
    @Value("${app.linkedin.api.enabled:false}")
    protected boolean linkedinEnabled;

    @Override
    public void saveIndexStart(Long userId) {
        IndexData indexData = new IndexData();
        indexData.setRunBy(userId);
        indexData.setStartedOn(new LocalDateTime());
        indexData.setCreatedBy("system");
        indexData.setCreatedDate(new DateTime());
        indexDataRepository.save(indexData);
    }

    @Override
    public void queueIndex(Long userId) {
        IndexData indexData = new IndexData();
        indexData.setRunBy(userId);
        indexData.setStatus(IndexStatusType.WAITING);
        indexData.setStartedOn(new LocalDateTime());
        indexData.setCreatedBy("system");
        indexData.setCreatedDate(new DateTime());
        indexDataRepository.save(indexData);
    }

    @Override
    @Async
    public Future<Integer> reindexSearchDatabase() {
        log.warn("Starting reindex database process");

        try {
            courseDocumentSolrRepository.deleteAll();
            // Reset the reporting table
            totaraSolrDAO.clearTable();

            ExecutorService taskExecutor = Executors.newFixedThreadPool(5);

            int total = 0;

            taskExecutor.execute(() -> {
                wikipagesSolrPagerService.indexAll(DEFAULT_PROGRAM_MAX_SIZE, 5);
            });

            taskExecutor.execute(() -> {
                programSolrPagerService.indexAll(DEFAULT_PROGRAM_MAX_SIZE, 10);
            });
            taskExecutor.execute(() -> {
                courseSolrPagerService.indexAll(DEFAULT_MAX_CLASSES_SIZE, 20);
            });
            taskExecutor.execute(() -> {
                classesSolrPagerService.indexAll(DEFAULT_MAX_CLASSES_SIZE, 30);
            });

            if (linkedinEnabled) {
                parallelReindex(taskExecutor, linkedinCourseSolrPagerService, DEFAULT_MAX_LYNDA_SIZE, 40);
            }

            if (allegoEnabled) {
                taskExecutor.execute(() -> {
                    // Allego requires a call before the index
                    allegoRecipientsSolrPagerService.indexAll(DEFAULT_MAX_ALLEGO_RECIPIENTS_SIZE, 50);
                    allegoCourseSolrPagerService.indexAll(DEFAULT_MAX_ALLEGO_COURSES_SIZE, 50);
                });
            }


            if (kalturaEnabled) {
                parallelReindex(taskExecutor, kalturaCourseSolrPagerService, DEFAULT_MAX_KALTURA_SIZE, 60);
            }


            taskExecutor.shutdown();

            log.warn("Reindexed: " + total);
            return new AsyncResult<>(total);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public IndexData findLastIndexData() {
        IndexData indexData = indexDataRepository.findRecentIndex();
        return indexData;
    }

    @Override
    public IndexData finLastQueuedData() {
        IndexData indexData = indexDataRepository.findLastWaitingIndex();
        return indexData;
    }

    @Override
    public void updateIndex(IndexData indexData) {
        indexDataRepository.save(indexData);
    }

    /**
     * Parallel reindex.
     *
     * @param taskExecutor the task executor
     * @param strategy     the strategy
     * @param size         the size
     * @param order        the order
     */
    protected void parallelReindex(ExecutorService taskExecutor, BaseSolrPagerServiceImpl strategy, int size,
            int order) {
        final int pages = strategy.getTotalPages(size);
        for (int i = strategy.startPage(); i <= pages; i++) {
            taskExecutor.execute(new SolrPagedIndexThreadBO(log, strategy, size, order, i));
        }
    }

}
