package com.redhat.uxl.services.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Message constants.
 */
@Component
public class MessageConstants {

    /**
     * The constant S_MESSAGE_TITLE_ENROLLMENT_REQUEST.
     */
    public static String S_MESSAGE_TITLE_ENROLLMENT_REQUEST;
    /**
     * The constant S_MESSAGE_APPROVE.
     */
    public static String S_MESSAGE_APPROVE;
    /**
     * The constant S_MESSAGE_APPROVE_MESSAGE.
     */
    public static String S_MESSAGE_APPROVE_MESSAGE;
    /**
     * The constant S_MESSAGE_REJECT.
     */
    public static String S_MESSAGE_REJECT;
    /**
     * The constant S_MESSAGE_REJECT_MESSAGE.
     */
    public static String S_MESSAGE_REJECT_MESSAGE;
    /**
     * The constant S_MESSAGE_PLP_SHARED_TITLE.
     */
    public static String S_MESSAGE_PLP_SHARED_TITLE;
    /**
     * The constant S_MESSAGE_PLP_UNSHARED_TITLE.
     */
    public static String S_MESSAGE_PLP_UNSHARED_TITLE;
    /**
     * The constant S_MESSAGE_PLP_SHARED_WITH_MANAGER.
     */
    public static String S_MESSAGE_PLP_SHARED_WITH_MANAGER;
    /**
     * The constant S_MESSAGE_PLP_SHARED_WITH_DIRECT_REPORT.
     */
    public static String S_MESSAGE_PLP_SHARED_WITH_DIRECT_REPORT;
    /**
     * The constant S_MESSAGE_PLP_UNSHARED_WITH_DIRECT_REPORT.
     */
    public static String S_MESSAGE_PLP_UNSHARED_WITH_DIRECT_REPORT;
    /**
     * The constant S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED_TITLE.
     */
    public static String S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED_TITLE;
    /**
     * The constant S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED_TITLE.
     */
    public static String S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED_TITLE;
    /**
     * The constant S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED.
     */
    public static String S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED;
    /**
     * The constant S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED.
     */
    public static String S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED;

    /**
     * Sets message title enrollment request.
     *
     * @param value the value
     */
    @Value("${messages.constants.courseEnrollmentRequest}")
    public void setMessageTitleEnrollmentRequest(String value) {
        S_MESSAGE_TITLE_ENROLLMENT_REQUEST = value;
    }

    /**
     * Sets message approve.
     *
     * @param value the value
     */
    @Value("${messages.constants.approve}")
    public void setMessageApprove(String value) {
        S_MESSAGE_APPROVE = value;
    }

    /**
     * Sets message approve message.
     *
     * @param value the value
     */
    @Value("${messages.constants.approveMessage}")
    public void setMessageApproveMessage(String value) {
        S_MESSAGE_APPROVE_MESSAGE = value;
    }

    /**
     * Sets message reject.
     *
     * @param value the value
     */
    @Value("${messages.constants.reject}")
    public void setMessageReject(String value) {
        S_MESSAGE_REJECT = value;
    }

    /**
     * Sets message reject message.
     *
     * @param value the value
     */
    @Value("${messages.constants.rejectMessage}")
    public void setMessageRejectMessage(String value) {
        S_MESSAGE_REJECT_MESSAGE = value;
    }

    /**
     * Sets message plp shared title.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.title.shared}")
    public void setsMessagePlpSharedTitle(String value) {
        S_MESSAGE_PLP_SHARED_TITLE = value;
    }

    /**
     * Sets message plp unshared title.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.title.unshared}")
    public void setsMessagePlpUnsharedTitle(String value) {
        S_MESSAGE_PLP_UNSHARED_TITLE = value;
    }

    /**
     * Sets message plp shared with manager.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.sharedWithManager}")
    public void setsMessagePlpSharedWithManager(String value) {
        S_MESSAGE_PLP_SHARED_WITH_MANAGER = value;
    }

    /**
     * Sets message plp shared with direct report.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.sharedWithDirectReport}")
    public void setsMessagePlpSharedWithDirectReport(String value) {
        S_MESSAGE_PLP_SHARED_WITH_DIRECT_REPORT = value;
    }

    /**
     * Sets message plp unshared with direct report.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.unsharedWithDirectReport}")
    public void setsMessagePlpUnsharedWithDirectReport(String value) {
        S_MESSAGE_PLP_UNSHARED_WITH_DIRECT_REPORT = value;
    }

    /**
     * Sets message plp due date items has changed title.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.title.dueDateItemsHasChanged}")
    public void setsMessagePlpDueDateItemsHasChangedTitle(String value) {
        S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED_TITLE = value;
    }

    /**
     * Sets message plp due date items has passed title.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.title.dueDateItemsHasPassed}")
    public void setsMessagePlpDueDateItemsHasPassedTitle(String value) {
        S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED_TITLE = value;
    }

    /**
     * Sets message plp due date items has changed.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.dueDateItemsHasChanged}")
    public void setsMessagePlpDueDateItemsHasChanged(String value) {
        S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_CHANGED = value;
    }

    /**
     * Sets message plp due date items has passed.
     *
     * @param value the value
     */
    @Value("${messages.constants.plp.dueDateItemsHasPassed}")
    public void setsMessagePlpDueDateItemsHasPassed(String value) {
        S_MESSAGE_PLP_DUE_DATE_ITEMS_HAS_PASSED = value;
    }

}
