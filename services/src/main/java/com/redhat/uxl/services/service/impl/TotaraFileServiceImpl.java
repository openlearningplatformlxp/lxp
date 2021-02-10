package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.datalayer.dao.TotaraFileDAO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFileDTO;
import com.redhat.uxl.services.service.TotaraFileService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * The type Totara file service.
 */
@Service
public class TotaraFileServiceImpl implements TotaraFileService {

  private static final String PLUGIN_FILE = "@@PLUGINFILE@@";
    /**
     * The Totara file dao.
     */
    @Inject
  TotaraFileDAO totaraFileDAO;

  @Timed
  @Override
  @Transactional(readOnly = true)
  public String buildPluginUrls(Long sectionId, String content) {
    if (content != null) {
      Document doc = Jsoup.parse(content, "UTF-8");
      Elements links = doc.select("a[href]"); // a with href
      for (Element link : links) {
        String linkHref = link.attr("href");

        if (StringUtils.contains(linkHref, PLUGIN_FILE)) {
          String[] path = StringUtils.splitByWholeSeparator(linkHref, "/");
          if (path.length > 0) {
            String filename = path[path.length - 1];
            TotaraFileDTO file = totaraFileDAO.getCourseFileBySectionAndName(sectionId, filename);
            String newUrl = String.format("/totara/pluginfile.php/%d/course/section/%d%s%s",
                file.getContextid(), sectionId, file.getFilepath(), file.getFilename());
            link.attr("href", newUrl);
          }
        }
      } ;
      return doc.html();
    }
    return content;
  }
}
