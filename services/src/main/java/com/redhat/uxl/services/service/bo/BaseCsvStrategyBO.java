package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * The type Base csv strategy bo.
 */
@Slf4j
public abstract class BaseCsvStrategyBO {

    /**
     * The File name date formatter.
     */
    protected DateTimeFormatter fileNameDateFormatter = DateTimeFormat.forPattern("yyyyMMddhhmma");
    /**
     * The Date string formatter.
     */
    protected DateTimeFormatter dateStringFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    /**
     * The Current person.
     */
    protected TotaraUserDTO currentPerson;
    /**
     * The Search.
     */
    @Setter
    protected String search;
    /**
     * The Start date.
     */
    @Setter
    protected DateTime startDate;
    /**
     * The End date.
     */
    @Setter
    protected DateTime endDate;

    /**
     * Instantiates a new Base csv strategy bo.
     *
     * @param currentPerson the current person
     */
    public BaseCsvStrategyBO(TotaraUserDTO currentPerson) {
        this.currentPerson = currentPerson;
    }

    /**
     * Build csv.
     *
     * @param response the response
     */
    public void buildCsv(HttpServletResponse response) {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + getFileName() + "_"
                + DateTime.now().toString(fileNameDateFormatter) + ".csv\"");
        try {
            OutputStream outputStream = response.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String header = "," + getHeaderName() + "\n" + ",," + DateTime.now().toString(dateStringFormatter) + "\n";
            outputStream.write(header.getBytes());

            StringBuilder sb = new StringBuilder();

            sb.append(StringUtils.join(getColumnTitles(), ","));
            sb.append("\n");

            List<List<String>> rowData = getRowData();
            rowData.stream().forEach(row -> {
                sb.append(StringUtils.join(row, ","));
                sb.append("\n");
            });

            pw.write(sb.toString());
            pw.close();
        } catch (Exception e) {
            log.error("Failed to produce CSV", e);
        }
    }

    /**
     * Gets column titles.
     *
     * @return the column titles
     */
    protected abstract List<String> getColumnTitles();

    /**
     * Gets row data.
     *
     * @return the row data
     */
    protected abstract List<List<String>> getRowData();

    /**
     * Gets file name.
     *
     * @return the file name
     */
    protected abstract String getFileName();

    /**
     * Gets header name.
     *
     * @return the header name
     */
    protected abstract String getHeaderName();

    /**
     * Check field and set value string.
     *
     * @param checkString the check string
     * @return the string
     */
    protected String checkFieldAndSetValue(String checkString) {
        if (checkString != null && !checkString.isEmpty()) {
            return "\"" + checkString + "\"";
        }
        return ("");
    }
}
