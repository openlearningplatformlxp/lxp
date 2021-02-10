package com.redhat.uxl.dataobjects.domain;

import static org.joda.time.DateTimeZone.*;

import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * The type Learning locker job execution.
 */
@Data
@Entity
@Table(name = "job_execution_learning_locker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LearningLockerJobExecution extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "type")
    private LearningLockerJobExecutionType type;

    @Column(nullable = false, name = "activities_found")
    private int activitiesFound = 0;

    @Column(nullable = false, name = "activities_course_notfound")
    private int activitiesCourseNotFound = 0;

    @Column(nullable = false, name = "activities_completed")
    private int activitiesCompleted = 0;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "execution_date", nullable = false)
    private DateTime executionDate = DateTime.now().withTimeAtStartOfDay().toDateTime(DateTimeZone.getDefault())
            .toDateTime(UTC);

    /**
     * Increase activities course not found.
     */
    public synchronized void increaseActivitiesCourseNotFound() {
        activitiesCourseNotFound++;
    }

    /**
     * Increase activities completed.
     */
    public synchronized void increaseActivitiesCompleted() {
        activitiesCompleted++;
    }

    /**
     * Is complete failure boolean.
     *
     * @return the boolean
     */
    public boolean isCompleteFailure() {
        return activitiesFound > 0 && (activitiesCompleted == 0 && activitiesCourseNotFound > 0);
    }

    /**
     * Increase activities found.
     *
     * @param found the found
     */
    public void increaseActivitiesFound(int found) {
        activitiesFound += found;
    }
}
