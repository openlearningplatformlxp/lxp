package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraProfileDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagDAO;
import com.redhat.uxl.datalayer.dao.TotaraTagInstanceDAO;
import com.redhat.uxl.datalayer.repository.WikipageRepository;
import com.redhat.uxl.datalayer.solr.repository.CourseDocumentSolrRepository;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.Wikipage;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTagInstanceDTO;
import com.redhat.uxl.dataobjects.domain.types.WikipageStatusType;
import com.redhat.uxl.dataobjects.domain.types.WikipageTreeType;
import com.redhat.uxl.services.service.WikipageService;
import com.redhat.uxl.services.service.dto.TagDTO;
import com.redhat.uxl.services.service.dto.WikipageDTO;
import com.redhat.uxl.services.service.dto.WikipageTreeNodeDTO;
import com.redhat.uxl.services.service.search.SearchBuilder;
import com.redhat.uxl.services.service.search.SearchSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.redhat.uxl.dataobjects.domain.types.WikipageTreeType.FOLDER;
import static com.redhat.uxl.dataobjects.domain.types.WikipageTreeType.ITEM;

/**
 * The type Wikipage service.
 */
@Service
@Transactional
@Slf4j
public class WikipageServiceImpl implements WikipageService {

    /**
     * The constant PAGE.
     */
    public static final int PAGE = 5;
  @Inject
  private WikipageRepository wikipageRepository;
  @Inject
  private TotaraTagDAO totaraTagDAO;
  @Inject
  private TotaraTagInstanceDAO totaraTagInstanceDAO;

  @Inject
  private TotaraProfileDAO totaraProfileDAO;
  @Inject
  private CourseDocumentSolrRepository courseDocumentSolrRepository;

  @Override
  public Wikipage findPageById(Long id) {
    Validate.notNull(id);
    Wikipage wikipage = wikipageRepository.findOne(id);

    return wikipage;
  }

  @Override
  public WikipageDTO findPageByIdWithTags(Long id) {
    Validate.notNull(id);
    Wikipage wikipage = wikipageRepository.findOne(id);

    WikipageDTO wikipageDTO = new WikipageDTO(wikipage);
    wikipageDTO.setTags(totaraTagDAO.findWikiTags(id).stream()
        .map(t -> new TagDTO(t.getId(), t.getName())).collect(Collectors.toList()));

    return wikipageDTO;
  }

  @Override
  public WikipageDTO displayPage(String url) {
    Validate.notNull(url);
    Wikipage wikipage = wikipageRepository.findByUrlAndStatus(url, WikipageStatusType.PUBLISHED);
    WikipageDTO wikipageDTO = new WikipageDTO(wikipage);
    wikipageDTO.setTags(totaraTagDAO.findWikiTags(wikipage.getId()).stream()
        .map(t -> new TagDTO(t.getId(), t.getName())).collect(Collectors.toList()));

    return wikipageDTO;
  }

  @Override
  @Timed
  @Transactional
  public Page<Wikipage> findForPagedSearch(String searchOperation, String searchValue,
      WikipageStatusType statusType, Pageable pageable) {
    SearchBuilder<Wikipage> searchBuilder = new SearchBuilder<>(wikipageRepository);

    searchBuilder =
        searchBuilder.where("or", SearchSpec.valueOf("title", searchOperation, searchValue),
            SearchSpec.valueOf("url", searchOperation, searchValue));

    if (statusType != null) {
      searchBuilder = searchBuilder.where("or", //
          SearchSpec.equals("status", statusType.name()) //
      );
    }

    Page<Wikipage> feedbackPage = searchBuilder.findForPagedSearch(pageable);

    return feedbackPage;
  }

  @Override
  @Timed
  @Transactional
  public Wikipage createWikipage(Wikipage wikipage, List<TagDTO> tags) {
    return updateWikipage(wikipage, tags);
  }

  @Override
  public Wikipage updateWikipage(Wikipage wikipage, List<TagDTO> tags) {

    if (WikipageStatusType.PUBLISHED.equals(wikipage.getStatus())) {
      // Publish must validate required fields
      wikipage.validate();
    }

    final Wikipage updatedWikipage = wikipageRepository.save(wikipage);

    if (tags != null) {
      tags.forEach(tagDTO -> {
        TotaraTagInstanceDTO tagInstance = new TotaraTagInstanceDTO();
        tagInstance.setItemid(updatedWikipage.getId());
        tagInstance.setComponent("core");
        tagInstance.setItemType("wiki");
        tagInstance.setTagId(tagDTO.getId());
        tagInstance.setOrdering(0l);
        totaraTagInstanceDAO.save(tagInstance);

            });
        }

        CourseDocument courseDocument = new CourseDocument(updatedWikipage);
        courseDocumentSolrRepository.deleteById(courseDocument.getId());
        if (Boolean.TRUE.equals(wikipage.getIndexOnSearch())){
            courseDocumentSolrRepository.save(courseDocument);
        }

        return updatedWikipage;
    }

  @Override
  public List<TagDTO> getUnmatchedWikiTags(Long wikiId, String searchTerm) {
    return totaraTagDAO.findUnmatchedWikiRoleTags(wikiId, searchTerm, PAGE).stream()
        .map(t -> new TagDTO(t.getId(), t.getName())).collect(Collectors.toList());
  }

  @Override
  public void deleteWikipageTags(Long wikiId) {
    WikipageDTO storedWikipage = findPageByIdWithTags(wikiId);
    if (storedWikipage.getTags() != null) {
      storedWikipage.getTags().forEach(t -> {
        totaraTagInstanceDAO.deleteWikiId(wikiId, t.getId());
      });
    }
  }

  @Override
  public WikipageTreeNodeDTO buildTree() {
    List<Wikipage> wikipages = wikipageRepository.findPublishedWikipages();

    WikipageTreeNodeDTO root = new WikipageTreeNodeDTO();
    root.setType(FOLDER);
    root.setId(-1l);
    root.setSlug("root");
    root.setColumns(new ArrayList<>());

    wikipages.stream().forEach(w -> {
      buildNode(root, w);

    });

    return root;
  }

  @Override
  public void updateTree(WikipageTreeNodeDTO wikipageTree) {
    List<WikipageTreeNodeDTO> nodes = buildNodeList(null, wikipageTree.getColumns());

    List<Long> wikipageIds = nodes.stream().filter(n -> n.getId() != -1l)
        .map(WikipageTreeNodeDTO::getId).collect(Collectors.toList());

    List<Wikipage> wikipages = wikipageRepository.findAll(wikipageIds);

    wikipages.stream().forEach(w -> {
      for (WikipageTreeNodeDTO node : nodes) {
        if (node.getId().equals(w.getId())) {
          w.setUrl(node.getSlug());
          break;
        }
      }
    });

    wikipageRepository.save(wikipages);
  }

  private List<WikipageTreeNodeDTO> buildNodeList(String parentSlug,
      List<WikipageTreeNodeDTO> wikipageTree) {
    List<WikipageTreeNodeDTO> results = new ArrayList<>();
    wikipageTree.stream().forEach(n -> {
      String slug = n.getSlug();
      if (parentSlug != null) {
        slug = parentSlug + "/" + slug;
        slug = StringUtils.replace(slug, "//", "/");
      }
      if (WikipageTreeType.ITEM.equals(n.getType())) {
        n.setSlug(slug);
        results.add(n);
      } else {
        results.addAll(buildNodeList(slug, n.getColumns()));
      }
    });
    return results;
  }

  private void buildNode(WikipageTreeNodeDTO root, Wikipage wikipage) {
    String[] nodes = StringUtils.splitByWholeSeparator(wikipage.getUrl(), "/");

    List<String> validNodes = new ArrayList<>();

    for (String node : nodes) {
      if (StringUtils.isNotEmpty(StringUtils.trim(node))) {
        validNodes.add(node);
      }
    }
    WikipageTreeNodeDTO folder = root;

    if (validNodes.size() > 1) {
      // There are folders
      for (int i = 0; i < validNodes.size() - 1; i++) {
        String validNode = validNodes.get(i);

        folder = buildOrGetFolder(validNode, folder);
      }

    }

    String[] url = StringUtils.splitByWholeSeparator(wikipage.getUrl(), "/");

    WikipageTreeNodeDTO wikiNode = new WikipageTreeNodeDTO();
    wikiNode.setId(wikipage.getId());
    wikiNode.setType(ITEM);
    wikiNode.setSlug("/" + url[url.length - 1]);
    folder.getColumns().add(wikiNode);
  }

  private WikipageTreeNodeDTO buildOrGetFolder(String validNode, WikipageTreeNodeDTO folder) {
    // Look for folder if exists, otherwise create it
    WikipageTreeNodeDTO subfolder = null;
    for (WikipageTreeNodeDTO node : folder.getColumns()) {
      if (node.getSlug().equalsIgnoreCase("/" + validNode)) {
        subfolder = node;
        break;
      }
    }
    if (subfolder == null) {
      subfolder = new WikipageTreeNodeDTO();
      subfolder.setId(-1l);
      subfolder.setType(FOLDER);
      subfolder.setSlug("/" + validNode);
      subfolder.setColumns(new ArrayList<>());
      folder.getColumns().add(subfolder);
    }

    return subfolder;
  }
}
