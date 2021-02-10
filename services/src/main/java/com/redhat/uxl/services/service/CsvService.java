package com.redhat.uxl.services.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Csv service.
 */
public interface CsvService {
    /**
     * Gets team progress csv by program.
     *
     * @param currentUserId the current user id
     * @param request       the request
     * @param response      the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getTeamProgressCSVByProgram(Long currentUserId, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Gets team progress csv by shared programs.
     *
     * @param currentUserId the current user id
     * @param response      the response
     * @param search        the search
     * @param startDate     the start date
     * @param endDate       the end date
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getTeamProgressCSVBySharedPrograms(Long currentUserId, HttpServletResponse response, String search,
            DateTime startDate, DateTime endDate) throws ServletException, IOException;

    /**
     * Gets team progress csv by team member.
     *
     * @param currentUserId the current user id
     * @param request       the request
     * @param response      the response
     * @param search        the search
     * @param startDate     the start date
     * @param endDate       the end date
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getTeamProgressCSVByTeamMember(Long currentUserId, HttpServletRequest request, HttpServletResponse response,
            String search, DateTime startDate, DateTime endDate) throws ServletException, IOException;

    /**
     * Gets team progress csv by course programs.
     *
     * @param currentUserId the current user id
     * @param search        the search
     * @param startDate     the start date
     * @param endDate       the end date
     * @param response      the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getTeamProgressCSVByCoursePrograms(Long currentUserId, String search, DateTime startDate, DateTime endDate,
            HttpServletResponse response) throws ServletException, IOException;

    /**
     * Gets team progress csv by learning path programs.
     *
     * @param currentUserId the current user id
     * @param search        the search
     * @param startDate     the start date
     * @param endDate       the end date
     * @param response      the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getTeamProgressCSVByLearningPathPrograms(Long currentUserId, String search, DateTime startDate,
            DateTime endDate, HttpServletResponse response) throws ServletException, IOException;

    /**
     * Gets all feed back.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getAllFeedBack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * Gets all users.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    /**
     * Gets all audit searches.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    @Transactional(readOnly = true)
    void getAllAuditSearches(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
