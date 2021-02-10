package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.SolrUtils;
import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.datalayer.dao.TotaraCourseDAO;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.datalayer.repository.DiscoveryProgramRepository;
import com.redhat.uxl.datalayer.repository.PersonSearchRepository;
import com.redhat.uxl.datalayer.repository.PersonalPlanShareRepository;
import com.redhat.uxl.datalayer.solr.repository.CourseDocumentCustomSolrRepository;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.DiscoveryProgram;
import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import com.redhat.uxl.dataobjects.domain.PersonSearch;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseStatusDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraHtmlBlockDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import com.redhat.uxl.dataobjects.domain.types.AppointmentItemType;
import com.redhat.uxl.dataobjects.domain.types.CalendarType;
import com.redhat.uxl.dataobjects.domain.types.DiscoveryProgramType;
import com.redhat.uxl.dataobjects.domain.types.PersonalPlanShareType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import com.redhat.uxl.services.service.CalendarService;
import com.redhat.uxl.services.service.FeaturedSearchService;
import com.redhat.uxl.services.service.PersonAccessService;
import com.redhat.uxl.services.service.PersonalPlanService;
import com.redhat.uxl.services.service.PersonalPlanShareService;
import com.redhat.uxl.services.service.ProgramItemService;
import com.redhat.uxl.services.service.TotaraActivityService;
import com.redhat.uxl.services.service.TotaraCourseService;
import com.redhat.uxl.services.service.TotaraProgramService;
import com.redhat.uxl.services.service.dto.AppointmentItemDTO;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.HtmlBlockDTO;
import com.redhat.uxl.services.service.dto.ProgramItemDTO;
import com.redhat.uxl.services.service.dto.ProgramItemWrapperDTO;
import com.redhat.uxl.services.service.dto.SearchDTO;
import com.redhat.uxl.services.service.dto.SearchInputFiltersDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.redhat.uxl.commonjava.utils.SolrUtils.focusHighlight;

/**
 * The type Program item service.
 */
@Slf4j
@Transactional
@Service
public class ProgramItemServiceImpl implements ProgramItemService {

    /**
     * The constant DEFAULT_MAX_SIZE.
     */
    public static final int DEFAULT_MAX_SIZE = 100;
    /**
     * The constant MAX_SIZE_TO_ADD.
     */
    public static final int MAX_SIZE_TO_ADD = 100;
    /**
     * The constant MAX_INTERESTS.
     */
    public static final int MAX_INTERESTS = 6;

    /**
     * The Totara course dao.
     */
    @Inject
    TotaraCourseDAO totaraCourseDAO;
    /**
     * The Totara tag dao.
     */
    @Inject
    TotaraTagDAO totaraTagDAO;
    /**
     * The Person search repository.
     */
    @Inject
    PersonSearchRepository personSearchRepository;
    /**
     * The Person access service.
     */
    @Inject
    PersonAccessService personAccessService;
    /**
     * The Course document custom solr repository.
     */
    @Inject
    CourseDocumentCustomSolrRepository courseDocumentCustomSolrRepository;
    /**
     * The Totara course service.
     */
    @Inject
    TotaraCourseService totaraCourseService;
    /**
     * The Discovery program repository.
     */
    @Inject
    DiscoveryProgramRepository discoveryProgramRepository;
    /**
     * The Totara program service.
     */
    @Inject
    TotaraProgramService totaraProgramService;
    /**
     * The Totara activity service.
     */
    @Inject
    TotaraActivityService totaraActivityService;
    /**
     * The Totara profile dao.
     */
    @Inject
    TotaraProfileDAO totaraProfileDAO;
    @Inject
    private FeaturedSearchService featuredSearchService;
    @Inject
    private PersonalPlanService personalPlanService;
    @Inject
    private PersonalPlanShareService personalPlanShareService;
    @Inject
    private PersonalPlanShareRepository personalPlanShareRepository;
    @Inject
    private CalendarService calendarService;

    @Value("${totara.classroom.baseurl}")
    private String totaraClassroomLaunchUrl;

    @Override
    @Timed
    @Transactional(readOnly = true)
    public Page<ProgramItemDTO> getInProgressProgramItems(ProgramType type, int page, int maxSize, Long userId) {
        PageRequest pageRequest = PageRequest.of(page, maxSize);
        Page pageResult = new PageImpl(new ArrayList());
        List<ProgramItemDTO> programItemDTOS = new ArrayList<>();

        if (ProgramType.LEARNING_PATH.equals(type)) { // TODO Need to fix tag management like we did with courses
            log.debug("Looking for learning path in progress");
            Page<TotaraProgramDTO> programs = totaraCourseDAO.findInProgressProgramsByUserId(userId, pageRequest);
            programs.getContent().parallelStream().forEach(tp -> {
                log.debug("Enrolled Program: " + tp.getCourseFullName());
                TotaraTagDTO tag = totaraTagDAO.findLearningPathFirstTopic(tp.getProgramId());
                ProgramItemDTO itemDTO = new ProgramItemDTO(tp);
                if (tag != null) {
                    itemDTO.setFirstTopic(tag.getName());
                }
                programItemDTOS.add(itemDTO);
            });
        } else if (ProgramType.COURSE.equals(type)) {
            log.debug("Looking for courses in progress");
            Page<TotaraCourseDTO> courses = totaraCourseDAO.findInProgressCoursesByUserId(userId, pageRequest);
            pageResult = courses;
            // Get Tags
            List<Long> courseIdList = new ArrayList<>();
            for (TotaraCourseDTO tc : courses) {
                courseIdList.add(tc.getCourseId());
            }
            List<TotaraTagDTO> tags = new ArrayList<>();
            if (!courseIdList.isEmpty()) {
                tags = totaraTagDAO.findCourseFirstTopic(courseIdList);
            }
            Map<Long, TotaraTagDTO> tagMap = new HashMap<>();
            for (TotaraTagDTO tt : tags) {
                tagMap.put(tt.getItemId(), tt); // TODO: we need to somehow weight this to pick a winner
            }
            for (TotaraCourseDTO tc : courses) {
                ProgramItemDTO itemDTO = new ProgramItemDTO(tc);
                if (tagMap.containsKey(tc.getCourseId())) {
                    itemDTO.setFirstTopic(tagMap.get(tc.getCourseId()).getName());
                }
                programItemDTOS.add(itemDTO);
            }

        } else if (ProgramType.CLASSROOM.equals(type)) {
            log.debug("Looking for classes");
            Page<TotaraEventDTO> events = calendarService.findCalendarEventsByPage(userId, pageRequest);
            pageResult = events;
            TotaraUserDTO totaraUserDTO = totaraProfileDAO.getUserProfile(userId);
            for (TotaraEventDTO te : events) {
                ProgramItemDTO itemDTO = new ProgramItemDTO(te, totaraClassroomLaunchUrl + te.getSessionId());
                itemDTO.setEventTimezone(totaraUserDTO.getTimezone());
                programItemDTOS.add(itemDTO);
            }
        }

        return new PageImpl<>(programItemDTOS, pageRequest, pageResult.getTotalElements());
    }

    @Override
    public List<ProgramItemDTO> getDiscoverProgramItems(ProgramType type, int page, int maxSize) {
        List<ProgramItemDTO> programItemDTOS = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, maxSize);
        //limit 4 for active discover programs
        List<DiscoveryProgram> discoveryProgramList = discoveryProgramRepository.findByActive(maxSize);
        List<Long> programIds = new ArrayList<>();
        for (DiscoveryProgram dp : discoveryProgramList) {
            if (dp.getType().equals(DiscoveryProgramType.TEXT)) {
                programItemDTOS.add(new ProgramItemDTO(dp));
            } else {
                programIds.add(dp.getProgramId());
            }
        }
        Page<TotaraProgramDTO> programs = totaraCourseDAO.findActiveProgramsById(programIds, pageRequest);
        for (TotaraProgramDTO tp : programs) {
            TotaraTagDTO tag = totaraTagDAO.findLearningPathFirstTopic(tp.getProgramId());
            ProgramItemDTO itemDTO = new ProgramItemDTO(tp);
            itemDTO.setDiscoveryProgramType(DiscoveryProgramType.PROGRAM);
            if (tag != null) {
                itemDTO.setFirstTopic(tag.getName());
            }
            programItemDTOS.add(itemDTO);
        }
        return programItemDTOS;
    }

    @Override
    public ProgramItemWrapperDTO getInProgressProgramItemsWrapper(ProgramType type, int page, Long userId,
            int maxSize) {
        Page<ProgramItemDTO> items = getInProgressProgramItems(type, page, maxSize, userId);
        ProgramItemWrapperDTO dto = new ProgramItemWrapperDTO();
        dto.setMaxNumber(maxSize);
        dto.setTotalCount(items.getTotalElements());
        dto.setProgramItems(items.getContent());

        if (dto.getProgramItems() != null) {
            dto.getProgramItems().forEach(program -> {
                if (program.getDescription() != null) {
                    program.setDescription(Jsoup.parse(program.getDescription()).text());
                }
                program.setStatus(0);
            });
        }
        return dto;
    }

    @Override
    public ProgramItemWrapperDTO getPersonalPrograms(Long userId, TotaraUserDTO manager) {
        ProgramItemWrapperDTO wrapperDTO = new ProgramItemWrapperDTO();
        wrapperDTO.setMaxNumber(5);

        List<PersonalProgramDTO> sharedWithMePlans = personalPlanShareService.findSharedWithMePlans(userId);
        // Get the current list of personal programs
        List<PersonalProgramDTO> plans = personalPlanService.getPersonalPlansForUser(userId);
        List<PersonalPlanShare> planShares = personalPlanShareService
                .findSharesWithDirectReports(plans.stream().map(p -> p.getProgramId()).collect(Collectors.toList()));
        Set<Long> planShareIds = planShares.stream()
                .map(personalPlanShare -> personalPlanShare.getPk().getPersonalPlanId()).collect(Collectors.toSet());
        List<PersonalProgramDTO> archivedPlans = personalPlanService.getArchivedPersonalPlansForUser(userId);

        wrapperDTO.setSharedWithMeItems(
                sharedWithMePlans.stream().map(ProgramItemDTO::valueOf).collect(Collectors.toList()));
        wrapperDTO.setProgramItems(plans.stream().map(ProgramItemDTO::valueOf).peek(p -> {
            if (manager != null) {
                p.setManager(manager.getDisplayName());
            }
            PersonalPlanShare share = personalPlanShareRepository.findByPkPersonalPlanIdAndType(p.getId(),
                    PersonalPlanShareType.MANAGER);
            if (share != null) {
                p.setSharedWithManager(true);
                p.setSharedWithManagerOn(share.getCreatedDate());
            }
            p.setShares(planShares.stream().filter(ps -> ps.getPk().getPersonalPlanId().equals(p.getId()))
                    .collect(Collectors.toList()));
            p.setShared(p.isSharedWithManager() || p.getShares().size() > 0);
        }).collect(Collectors.toList()));
        wrapperDTO.setArchivedProgramItems(
                archivedPlans.stream().map(ProgramItemDTO::valueOf).collect(Collectors.toList()));
        wrapperDTO.setTotalCount(wrapperDTO.getProgramItems().size() - planShareIds.size());

        return wrapperDTO;

    }
    @Transactional(readOnly = false)
    @Override
    public SearchDTO searchItems(StringSolrBuffer searchTerm, SearchInputFiltersDTO inputFilters, Integer page,
            int maxSize, Long userId) {
        List<ProgramItemDTO> programItemDTOS = new ArrayList<>();
        if (page == null) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, maxSize);
        TotaraUserDTO totaraUserDTO = totaraProfileDAO.getUserProfile(userId);
        Set<FeaturedSearchInstance> instances = featuredSearchService.findFeaturedSearchByQuery(searchTerm.toString());

        HighlightPage<CourseDocument> courseDocuments = courseDocumentCustomSolrRepository.findByCriteria(userId,
                instances,
                searchTerm, inputFilters.getType(), inputFilters.getCms(), inputFilters.getTopic(),
                inputFilters.getDelivery(),
                inputFilters.getSkillLevels(), inputFilters.getLanguages(), inputFilters.getCountry(),
                inputFilters.getCity(), inputFilters.isAdminSearch(), pageable);

        if (page == 0) {
            // Only save search for first page
            PersonSearch personSearch = new PersonSearch();
            personSearch.setPersonTotaraId(userId);
            personSearch.setSearchTerm(searchTerm.toString());
            personSearch.setFilters(inputFilters.toString());
            personSearch.setResults(courseDocuments.getTotalElements());
            personSearch.setCreatedBy("system");
            personSearch.setCreatedDate(DateTime.now());
            personSearch.setLastModifiedBy("system");
            personSearch.setLastModifiedDate(DateTime.now());
            personSearchRepository.save(personSearch);
        }
        Set<ProgramItemDTO> featuredItems = new TreeSet<>();

        courseDocuments.getContent().stream().filter(CourseDocument::isClassroom)
                .filter(CourseDocument::requireUserTimezone).forEach(c -> calendarService.fixUserTimezone(userId, c));

        for (CourseDocument courseDocument : courseDocuments.getContent()) {
            List<HighlightEntry.Highlight> highlights = courseDocuments.getHighlights(courseDocument);
            for (HighlightEntry.Highlight highlight : highlights) {
                if (highlight.getField().getName().equals("description")) {
                    String descriptionString = SolrUtils.highlightOriginalInput(highlight.getSnipplets(),
                            courseDocument.getDescriptionString());
                    courseDocument.setDescriptionString(focusHighlight(descriptionString, 280));
                }
            }

            ProgramItemDTO programItem = new ProgramItemDTO();

            switch (courseDocument.getContentSource()) {
            case LMS:
                programItem.setId(courseDocument.getProgramId());
                programItem.setExternalUrl(courseDocument.getExternalUrl());
                break;
            case ALLEGO:
            case KALTURA:
            case LYNDA:
                programItem.setExternalId(courseDocument.getId());
                programItem.setExternalUrl(courseDocument.getExternalUrl());
                break;
            }
            programItem.setCms(courseDocument.getContentSource());
            programItem.setTitle(courseDocument.getFullNameString());
            programItem.setDescription(courseDocument.getDescriptionString());
            programItem.setType(courseDocument.getType());
            programItem.setFirstTopic(courseDocument.getFirstTopic());
            programItem.setDuration(NumberUtils.toFloat(courseDocument.getDuration()));
            programItem.setCity(courseDocument.getCity());
            programItem.setCountry(courseDocument.getCountry());
            programItem.setEventTime(new DateTime(courseDocument.getEventTime()));
            programItem.setEventTimezone(totaraUserDTO.getTimezone());

            programItem.setCeCredits(courseDocument.getCecredits());
            programItem.setClassType(CalendarType.buildFrom(courseDocument.getClassType()));

            programItem.setMobile(false);
            programItem.setPersonal(false);

            long count = instances.stream().filter(i -> i.getInstanceId().equals(programItem.getId()))
                    .filter(i -> i.getInstanceType().equals(programItem.getType())).count();

            if (count > 0) {
                featuredItems.add(programItem);
            } else {
                programItemDTOS.add(programItem);
            }
        }

        return new SearchDTO(featuredItems,
                new PageImpl(programItemDTOS, pageable, (courseDocuments.getTotalElements())));
    }

    @Override
    public ProgramItemDTO findByProgramId(Long totaraId, Long programId) {
        return findByProgramId(totaraId, programId, true);
    }

    @Override
    public ProgramItemDTO findByProgramId(Long totaraId, Long programId, boolean registerAccess) {
        TotaraProgramDTO tp = totaraCourseDAO.findProgramByProgramId(programId);
        if (tp != null) {
            if (registerAccess) {
                personAccessService.registerAccessToProgram(totaraId, programId);
            }
            TotaraTagDTO tag = totaraTagDAO.findLearningPathFirstTopic(tp.getProgramId());
            ProgramItemDTO itemDTO = new ProgramItemDTO(tp);
            if (tag != null) {
                itemDTO.setFirstTopic(tag.getName());
            }
            return itemDTO;
        } else {
            return null;
        }
    }

    @Override
    public HtmlBlockDTO findHtmlBlockByProgramId(Long programId) {
        TotaraHtmlBlockDTO htmlBlock = totaraCourseDAO.findProgramHtmlBlockByProgramId(programId);
        return (htmlBlock != null) ? new HtmlBlockDTO(htmlBlock) : null;
    }

    @Override
    public Page<ProgramItemDTO> findCoursesByTag(Long totaraId, Long tagId) {
        return findCoursesByTag(totaraId, tagId, 0, MAX_INTERESTS);
    }

    @Override
    public Page<ProgramItemDTO> findCoursesByTag(Long totaraId, Long tagId, int pageNumber, int maxSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, maxSize);
        Page<TotaraCourseDTO> courses = totaraCourseDAO.findActiveCoursesByTag(tagId, pageRequest);
        Page<ProgramItemDTO> page = buildProgramItemFromCourses(pageRequest, courses);
        page.getContent().forEach(program -> {
            program.setDescription(Jsoup.parse(program.getDescription()).text());
        });
        return page;
    }

    @Override
    public Page<ProgramItemDTO> findProgramsByTag(Long totaraId, Long tagId) {
        Pageable pageRequest = PageRequest.of(0, MAX_INTERESTS);
        Page<TotaraProgramDTO> programs = totaraCourseDAO.findActiveProgramsByTag(tagId, pageRequest);
        Page<ProgramItemDTO> page = buildProgramItemFromProgram(pageRequest, programs);
        return page;
    }

    @Override
    public List<ProgramItemDTO> findActivePrograms() {
        return totaraCourseDAO.findActivePrograms().stream().map((program) -> new ProgramItemDTO(program))
                .collect(Collectors.toList());
    }

    private Page<ProgramItemDTO> buildProgramItemFromCourses(Pageable pageRequest, Page<TotaraCourseDTO> courses) {
        return new PageImpl<>(courses.getContent().stream().map((totaraCourse) -> {
            TotaraTagDTO tag = totaraTagDAO.findCourseFirstTopic(totaraCourse.getId());
            ProgramItemDTO itemDTO = new ProgramItemDTO(totaraCourse);
            if (tag != null) {
                itemDTO.setFirstTopic(tag.getName());
            }
            return itemDTO;
        }).collect(Collectors.toList()), pageRequest, courses.getTotalElements());
    }

    private Page<ProgramItemDTO> buildProgramItemFromProgram(Pageable pageRequest, Page<TotaraProgramDTO> programs) {
        return new PageImpl<>(programs.getContent().stream().map((totaraProgram) -> {
            TotaraTagDTO tag = totaraTagDAO.findLearningPathFirstTopic(totaraProgram.getProgramId());
            ProgramItemDTO itemDTO = new ProgramItemDTO(totaraProgram);
            if (tag != null) {
                itemDTO.setFirstTopic(tag.getName());
            }
            return itemDTO;
        }).collect(Collectors.toList()), pageRequest, programs.getTotalElements());
    }

    @Override
    public List<AppointmentItemDTO> determineAppointments(Long personTotaraId, Long programId) {
        return determineAppointments(totaraCourseService.getProgramCourseSets(personTotaraId, programId));
    }

    @Override
    public List<AppointmentItemDTO> determineAppointments(List<ProgramCourseSetDTO> programCourseSets) {
        List<AppointmentItemDTO> appointments = new ArrayList<>();

        for (ProgramCourseSetDTO courseSet : programCourseSets) {
            for (int i = 0, len = courseSet.getCourses().size(); i < len && appointments.size() < 3; i++) {
                ProgramCourseDTO course = courseSet.getCourses().get(i);
                ProgramCourseStatusDTO status = course.getStatus();
                if (!status.isLocked() && !status.isCompleted()) {
                    appointments.add(new AppointmentItemDTO(course.getId(),
                        course.getFullName(),
                        0,
                        AppointmentItemType.COURSE,
                        // TODO: (WJK) Replace this with non-hardcoded when available
                        status.isInProgress(),
                        status.isCompleted(),
                        status.isEnrolled()));
                }
            }
        }

        return appointments;
    }

    @Override
    public List<DiscoveryProgram> getDiscoveryPrograms() {
        return discoveryProgramRepository.findAll();
    }

    @Override
    public DiscoveryProgram upsertDiscoveryProgram(Long id, Long programId, Boolean active, DiscoveryProgramType type,
            String discoveryProgramText) {
        DiscoveryProgram dp = new DiscoveryProgram();
        if (id != null) {
            dp = discoveryProgramRepository.findOne(id);
        }
        dp.setType(type);
        dp.setProgramId(programId);
        dp.setActive(active);
        if (discoveryProgramText != null && !discoveryProgramText.isEmpty()) {
            String newText = discoveryProgramText.replaceAll("<a href", "<a target=\"_blank\" href");
            dp.setDiscoveryProgramText(newText);
        }
        return discoveryProgramRepository.save(dp);
    }

    @Override
    public void deleteDiscoveryProgram(Long discoveryProgramId) {
        if (discoveryProgramId == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
                    "Id for DiscoveryProgram record to delete cannot be null.");
        }

        discoveryProgramRepository.deleteById(discoveryProgramId);
    }

    @Override
    public List<CourseSetProgressionOverviewDTO> getProgramProgressionOverview(Long programId, Long personTotaraId) {
        // TODO: (WJK) Replace with actual implementation
        // Get courses in a program

        List<CourseSetProgressionOverviewDTO> courseSetProgressionOverviews = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");

        ProgramItemDTO prog = findByProgramId(personTotaraId, programId, false);

        if (prog.getHidesets() == null || !prog.getHidesets().equalsIgnoreCase("1")) {

            List<ProgramCourseSetDTO> courseSets = totaraCourseService.getProgramCourseSets(personTotaraId, programId);
            if (courseSets != null) {

                final Map<Long, CompletableFuture<List<CourseProgressionOverviewDTO>>> futureCourseProgressions = new TreeMap<>();
                long i = 1;
                for (ProgramCourseSetDTO courseSet : courseSets) {
                    courseSet.setId(i++);
                    futureCourseProgressions.put(courseSet.getId(), CompletableFuture.supplyAsync(() -> {
                        List<ProgramCourseDTO> courses = courseSet.getCourses();
                        List<CourseProgressionOverviewDTO> courseProgressions = new ArrayList<>();
                        for (ProgramCourseDTO course : courses) {
                            CourseProgressionOverviewDTO progressDTO1 = new CourseProgressionOverviewDTO();
                            progressDTO1.setCourseName(course.getFullName());
                            progressDTO1.setType(ProgramType.valueOf(course.getType()));

                            // Adding logic to check to see if course is complete first
                            Long statusCode = Long.valueOf(0);
                            try {
                                statusCode = totaraActivityService.getCourseStatus(course.getId(), personTotaraId);
                            } catch (Exception e) {
                                log.error("Could not find entry for course in course completions: " + course.getId());
                            }


                            progressDTO1.setActivityCount(totaraActivityService.getActivityCount(course.getId()));
                            if (statusCode.compareTo(Long.valueOf(49)) > 0) {
                                // Course is complete...no need to calculate
                                progressDTO1.setActivityCompleteCount(progressDTO1.getActivityCount());
                            } else {
                                progressDTO1.setActivityCompleteCount(totaraActivityService
                                        .getCompletedActivityCount(course.getId(), personTotaraId));
                            }

                            int percentComplete = (int) Math.ceil(((float) progressDTO1.getActivityCompleteCount()
                                    / (float) progressDTO1.getActivityCount()) * 100);
                            progressDTO1.setPercentComplete(percentComplete);
                            log.error("Percent complete:  " + progressDTO1.getPercentComplete());

                            progressDTO1.setHideProgressEvents(true);
                            courseProgressions.add(progressDTO1);
                        }
                        return courseProgressions;
                    }));
                }
                ;
                courseSets.forEach(courseSet -> {
                    try {
                        courseSetProgressionOverviews.add(CourseSetProgressionOverviewDTO.builder()
                                .courseSetName(courseSet.getName())
                                .progressionOverviews(futureCourseProgressions.get(courseSet.getId()).get()).build());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }

        return courseSetProgressionOverviews;
    }

    @Override
    public Long findAllowedUserAudienceByUserId(Long personTotaraId, Long programId) {
        return totaraCourseDAO.findAllowedUserAudienceByUserId(personTotaraId, programId);
    }

    @Override
    public PersonalLearningPathDTO createPersonalProgram(Long personTotaraId, PersonalLearningPathDTO path) {
        Long planId = personalPlanService.insertPersonalPlan(personTotaraId, path);
        return personalPlanService.getPersonalPlanForUser(personTotaraId, planId);
    }

    @Override
    public PersonalLearningPathDTO updatePersonalProgram(Long personTotaraId,
            PersonalLearningPathDTO personalLearningPath) {
        personalPlanService.updatePersonalPlan(personTotaraId, personalLearningPath);

        return personalPlanService.getPersonalPlanForUser(personTotaraId, personalLearningPath.getId());
    }

    @Override
    public ProgramItemWrapperDTO getInProgressPersonalProgramItemsWrapper(Long userId, TotaraUserDTO manager,
            int maxSize) {
        ProgramItemWrapperDTO dto = getPersonalPrograms(userId, manager);
        return dto;
    }

    @Override
    public void updatePersonalPlanManualCompletion(Long itemId, Long status, Long personTotaraId) {
        personalPlanService.setPersonalPlanManualCompletion(itemId, status, personTotaraId);
    }

}
