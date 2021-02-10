package com.redhat.uxl.services.service.bo;

import com.redhat.uxl.datalayer.repository.PersonRepository;
import com.redhat.uxl.dataobjects.domain.Authority;
import com.redhat.uxl.dataobjects.domain.Person;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Csv all users strategy bo.
 */
public class CsvAllUsersStrategyBO extends BaseCsvStrategyBO {

    /**
     * The Person repository.
     */
    protected final PersonRepository personRepository;

    /**
     * Instantiates a new Csv all users strategy bo.
     *
     * @param currentPerson    the current person
     * @param personRepository the person repository
     */
    public CsvAllUsersStrategyBO(TotaraUserDTO currentPerson, PersonRepository personRepository) {
        super(currentPerson);
        this.personRepository = personRepository;
    }

    @Override
    protected List<String> getColumnTitles() {
        return Arrays.asList("ID", "Created Date", "Email", "First Name", "Last Name", "Login", "Status",
                "Authorities");
    }


    @Override
    protected List<List<String>> getRowData() {
        List<List<String>> rowData = new ArrayList<>();
        List<Person> users = personRepository.findAllUsers();
        DateTimeFormatter createdDateString = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        for (Person user : users) {
            List<String> row = new ArrayList<>();
            row.add(user.getId().toString());
            row.add(user.getCreatedDate().toString(createdDateString));
            row.add(user.getEmail());
            row.add(user.getFirstName());
            row.add(user.getLastName());
            row.add(user.getLogin());
            row.add(user.isActivated() ? "ACTIVATED" : "DEACTIVATED");
            if (user.getAuthorities() != null) {
                row.add(StringUtils.join(
                        user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()), " - "));
            } else {
                row.add(" ");
            }
            rowData.add(row);
        }
        return rowData;
    }

    @Override
    protected String getFileName() {
        return "users_report_" + DateTime.now().toString(fileNameDateFormatter);
    }

    @Override
    protected String getHeaderName() {
        return "Users Report";
    }
}
