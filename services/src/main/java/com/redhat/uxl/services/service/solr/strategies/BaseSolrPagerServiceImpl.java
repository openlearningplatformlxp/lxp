package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.datalayer.dao.TotaraSolrDAO;
import com.redhat.uxl.datalayer.repository.WikipageRepository;
import com.redhat.uxl.datalayer.solr.repository.CourseDocumentSolrRepository;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.Searchable;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.WikipageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Base solr pager service.
 *
 * @param <T> the type parameter
 * @param <D> the type parameter
 */
@Slf4j
public abstract class BaseSolrPagerServiceImpl<T extends Searchable, D> {

    /**
     * The Totara course service.
     */
    @Inject
    protected TotaraCourseService totaraCourseService;
    /**
     * The Course document solr repository.
     */
    @Inject
    protected CourseDocumentSolrRepository courseDocumentSolrRepository;
    /**
     * The Wikipage repository.
     */
    @Inject
    protected WikipageRepository wikipageRepository;
    /**
     * The Wikipage service.
     */
    @Inject
    protected WikipageService wikipageService;

    /**
     * The Totara solr dao.
     */
    @Inject
    protected TotaraSolrDAO totaraSolrDAO;

    /**
     * Index.
     *
     * @param maxSize   the max size
     * @param sortOrder the sort order
     * @param page      the page
     */
    @Transactional
    public void index(int maxSize, int sortOrder, int page) {
        D data = prepare();
        index(data, maxSize, sortOrder, page);
    }

    /**
     * Index all.
     *
     * @param maxSize   the max size
     * @param sortOrder the sort order
     */
    public void indexAll(int maxSize, int sortOrder) {
        try {
            int page = startPage();
            Page<T> items;
            D data = prepare();
            do {
                items = index(data, maxSize, sortOrder, page);
                page++;
            } while ((page - startPage()) < items.getTotalPages());
        } catch (RuntimeException e) {
            log.error("Failed to run index for: " + logName(), e);
        }
    }

    /**
     * Index page.
     *
     * @param data      the data
     * @param maxSize   the max size
     * @param sortOrder the sort order
     * @param page      the page
     * @return the page
     */
    protected Page<T> index(D data, int maxSize, int sortOrder, int page) {
        int total = 0;
        data = reprepare(data, page, maxSize);
        Page<T> items = findActiveItems(data, page, maxSize);
        page++;
        List<CourseDocument> documents = buildDocuments(data, items.getContent());

        documents.stream().forEach((document) -> {
            document.sanitizeStrings();
            if (document.getTags() != null && document.getTags().isEmpty()) {
                try {
                    document.setTags(new ArrayList<>());
                    document.getTags().add("$");// default tag fix
                } catch (Exception e) {
                    log.error("Could not default tags for reindex of: " + document.getFullName());
                }
            }
            document.setSortOrder(sortOrder);
        });
        documents = documents.stream().filter(document -> StringUtils.isNotEmpty(document.getFullName()))
                .collect(Collectors.toList());

        log.warn("Build " + documents.size() + " " + logName() + " documents.");
        if (documents.size() > 0) {
            courseDocumentSolrRepository.save(documents);
            total += documents.size();
        }
        return items;
    }

    /**
     * Gets total pages.
     *
     * @param size the size
     * @return the total pages
     */
    public int getTotalPages(int size) {
        int total = findTotalElements();
        int increaseRemaining = total / size > 0 ? 1 : 0;
        return (total / size) + increaseRemaining;
    }

    /**
     * Prepare d.
     *
     * @return the d
     */
    protected D prepare() {
        return null;
    }

    /**
     * Reprepare d.
     *
     * @param data    the data
     * @param page    the page
     * @param maxSize the max size
     * @return the d
     */
    protected D reprepare(D data, int page, int maxSize) {
        return data;
    }

    /**
     * Find total elements int.
     *
     * @return the int
     */
    protected int findTotalElements() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    /**
     * Find active items page.
     *
     * @param data    the data
     * @param page    the page
     * @param maxSize the max size
     * @return the page
     */
    protected abstract Page<T> findActiveItems(D data, int page, int maxSize);

    /**
     * Build documents list.
     *
     * @param data    the data
     * @param content the content
     * @return the list
     */
    protected abstract List<CourseDocument> buildDocuments(D data, List<T> content);

    /**
     * Log name string.
     *
     * @return the string
     */
    public abstract String logName();

    /**
     * Start page int.
     *
     * @return the int
     */
    public int startPage() {
        return 0;
    }

}
