package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.Feedback;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.services.service.FeedbackService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * The type Csv feedback strategy bo.
 */
public class CsvFeedbackStrategyBO extends BaseCsvStrategyBO {

    /**
     * The Feedback service.
     */
    protected final FeedbackService feedbackService;

    /**
     * Instantiates a new Csv feedback strategy bo.
     *
     * @param currentPerson   the current person
     * @param feedbackService the feedback service
     */
    public CsvFeedbackStrategyBO(TotaraUserDTO currentPerson, FeedbackService feedbackService) {
        super(currentPerson);
        this.feedbackService = feedbackService;
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("Created Date", "Type", "Url", "Email", "Region", "Job Title", "Business Line", "Message");
    }

    @Override
    protected List<List<String>> getRowData() {
        List<List<String>> rowData = new ArrayList<>();
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        DateTimeFormatter createdDateString = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        for (Feedback feedback : feedbacks) {
            List<String> row = new ArrayList<>();
            row.add(feedback.getCreatedDate().toString(createdDateString));
            row.add(feedback.getType().name());
            row.add(feedback.getUrl());
            row.add(checkFieldAndSetValue(feedback.getEmail()));
            row.add(checkFieldAndSetValue(feedback.getRegion()));
            row.add(checkFieldAndSetValue(feedback.getJobTitle()));
            row.add(checkFieldAndSetValue(feedback.getBusinessLine()));
            row.add(checkFieldAndSetValue(feedback.getMessage()));
            rowData.add(row);
        }
        return rowData;
    }

    @Override
    protected String getFileName() {
        return "feedback_report_" + DateTime.now().toString(fileNameDateFormatter);
    }

    @Override
    protected String getHeaderName() {
        return "Feedback Report";
    }

}
