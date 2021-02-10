package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.datalayer.repository.CmsBlockRepository;
import com.redhat.uxl.dataobjects.domain.CmsBlock;
import com.redhat.uxl.services.service.CmsBlockService;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

/**
 * The type Cms block service.
 */
@Service
@Slf4j
@Transactional
public class CmsBlockServiceImpl implements CmsBlockService {
  @Inject
  private CmsBlockRepository cmsBlockRepository;

  @Override
  @Timed
  @Transactional
  public CmsBlock addBlock(CmsBlock cmsBlock) {
    if (cmsBlock == null || cmsBlock.getId() != null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Passed CMS Block is invalid.");
    }

    CmsBlock addBlock = new CmsBlock();

    addBlock.setKey(cmsBlock.getKey());
    addBlock.setName(cmsBlock.getName());
    addBlock.setDescription(cmsBlock.getDescription());
    addBlock.setContent(cmsBlock.getContent());

    return cmsBlockRepository.save(addBlock);
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Page<CmsBlock> findForPagedSearch(String searchOperation, String searchValue,
      Pageable pageable) {
    SearchBuilder<CmsBlock> searchBuilder = new SearchBuilder<>(cmsBlockRepository);

    Page<CmsBlock> cmsBlockPage = searchBuilder
        .where("or", SearchSpec.valueOf("key", searchOperation, searchValue),
            SearchSpec.valueOf("name", searchOperation, searchValue),
            SearchSpec.valueOf("description", searchOperation, searchValue),
            SearchSpec.valueOf("content", searchOperation, searchValue))
        .findForPagedSearch(pageable);

    return cmsBlockPage;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Set<CmsBlock> getAllCurrentBlocks() {
    return cmsBlockRepository.findAllCurrentBlocks();
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public CmsBlock getBlock(Long id) {
    return cmsBlockRepository.findOne(id);
  }

    @Override
  @Timed
  @Transactional(readOnly = true)
  public Set<CmsBlock> getBlocks(Set<String> keys) {
    return cmsBlockRepository.findByKeyIn(keys);
  }

  @Override
  @Timed
  @Transactional
  public CmsBlock updateBlock(Long cmsBlockId, CmsBlock cmsBlock) {
    if (!cmsBlockId.equals(cmsBlock.getId())) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Passed Block Id does not match Entity Block Id.");
    }

    CmsBlock updateBlock = cmsBlockRepository.findOne(cmsBlockId);

    updateBlock.setKey(cmsBlock.getKey());
    updateBlock.setName(cmsBlock.getName());
    updateBlock.setDescription(cmsBlock.getDescription());
    updateBlock.setContent(cmsBlock.getContent());

    return cmsBlockRepository.save(updateBlock);
  }
}
