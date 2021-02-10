package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.PersonSearch;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.PersonSearchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The type Csv audit searches strategy bo.
 */
public class CsvAuditSearchesStrategyBO extends BaseCsvStrategyBO {

    /**
     * The Person search service.
     */
    protected final PersonSearchService personSearchService;

    /**
     * Instantiates a new Csv audit searches strategy bo.
     *
     * @param currentPerson       the current person
     * @param personSearchService the person search service
     */
    public CsvAuditSearchesStrategyBO(TotaraUserDTO currentPerson, PersonSearchService personSearchService) {
        super(currentPerson);
        this.personSearchService = personSearchService;
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("ID", "Created Date", "User ID", "Search Term", "Results", "Filters");
    }

    @Override
    protected List<List<String>> getRowData() {
        List<List<String>> rowData = new ArrayList<>();
        List<PersonSearch> searches = personSearchService.getAllAuditSearches();
        DateTimeFormatter createdDateString = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        for (PersonSearch search : searches) {
            List<String> row = new ArrayList<>();
            row.add(search.getId().toString());
            row.add(search.getCreatedDate().toString(createdDateString));
            row.add(search.getPersonTotaraId().toString());
            row.add(search.getSearchTerm());
            row.add(search.getResults().toString());
            row.add(search.getFilters());
            rowData.add(row);
        }
        return rowData;
    }

    @Override
    protected String getFileName() {
        return "audit_searches_report_" + DateTime.now().toString(fileNameDateFormatter);
    }

    @Override
    protected String getHeaderName() {
        return "Audit Searches Report";
    }
}
