package com.redhat.uxl.services.service.impl;

import com.redhat.uxl.datalayer.repository.AnnouncementRepository;
import com.redhat.uxl.datalayer.repository.PersonAnnouncementRepository;
import com.redhat.uxl.dataobjects.domain.Announcement;
import com.redhat.uxl.dataobjects.domain.PersonAnnouncement;
import com.redhat.uxl.services.service.AnnouncementService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * The type Announcement service.
 */
@Service
@Transactional
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

  @Inject
  private AnnouncementRepository announcementRepository;

  @Inject
  private PersonAnnouncementRepository personAnnouncementRepository;

  @Override
  public Announcement pullAnnouncement(Long personTotaraId) {
    Validate.notNull(personTotaraId);
    return announcementRepository.findRecentUnread(personTotaraId);
  }

  @Override
  public void markAsRead(Long personTotaraId, Long announcementId) {
    Validate.notNull(personTotaraId);
    Validate.notNull(announcementId);
    PersonAnnouncement personAnnouncement = new PersonAnnouncement();
    personAnnouncement.setPersonTotaraId(personTotaraId);
    personAnnouncement.setAnnouncementId(announcementId);
    personAnnouncement.setCreatedBy("system");
    personAnnouncement.setCreatedDate(new DateTime());
    personAnnouncementRepository.save(personAnnouncement);
  }

  @Override
  public Announcement findPageById(Long id) {
    Validate.notNull(id);
    return announcementRepository.findOne(id);
  }

  @Override
  public Page<Announcement> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<Announcement> searchBuilder = new SearchBuilder<>(announcementRepository);

    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("message", searchOperation, searchValue),
            SearchSpec.valueOf("linkUrl", searchOperation, searchValue),
            SearchSpec.valueOf("linkText", searchOperation, searchValue));

    Page<Announcement> page = searchBuilder.findForPagedSearch(pageable);

    return page;
  }

  @Override
  public Announcement createAnnouncement(Announcement announcement) {
    announcement.validate();
    announcement.setCreatedBy("system");
    announcement.setCreatedDate(new DateTime());
    announcement = announcementRepository.save(announcement);
    return announcement;
  }

  @Override
  public Announcement updateAnnouncement(Announcement announcement) {
    announcement.validate();
    announcement.setLastModifiedBy("system");
    announcement.setLastModifiedDate(new DateTime());
    announcement = announcementRepository.save(announcement);
    return announcement;
  }

  @Override
  public void deleteAnnouncement(Long id) {
    Validate.notNull(id);
    announcementRepository.deleteById(id);
  }

}
