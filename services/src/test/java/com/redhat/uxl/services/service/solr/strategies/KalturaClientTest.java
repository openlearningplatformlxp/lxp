package com.redhat.uxl.services.service.solr.strategies;

import com.kaltura.client.KalturaClient;
import com.kaltura.client.types.KalturaMetadata;
import com.redhat.uxl.services.service.dto.SolrKalturaMediaEntrySearchableDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * The type Kaltura client test.
 */
public class KalturaClientTest {

  /**
   * The constant MAX_SIZE.
   */
  public static final int MAX_SIZE = 1000;
  private KalturaCourseSolrPagerServiceImpl kalturaCourseSolrPagerService;
  private KalturaClient client;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    kalturaCourseSolrPagerService = new KalturaCourseSolrPagerServiceImpl();
    client = kalturaCourseSolrPagerService.prepare();

  }

  /**
   * Test kaltura.
   */
  @Test
  public void test_kaltura() {

    Page<SolrKalturaMediaEntrySearchableDTO> page =
        kalturaCourseSolrPagerService.findActiveItems(client, 0, MAX_SIZE);
    SolrKalturaMediaEntrySearchableDTO courseFirstPage = page.getContent().get(1);
    assertEquals(7, page.getTotalElements());
  }

  /**
   * Test kaltura has metadata.
   */
  @Test
  public void test_kaltura_has_metadata() {
    boolean hasMetadata =
        kalturaCourseSolrPagerService.hasMetadata(client, "1_58c7yco4", "ShowInRHU", "Yes");
    assertEquals(true, hasMetadata);
  }

  /**
   * Test find real video url.
   */
  @Test
  public void test_findRealVideoUrl() {
    String dataUrl =
        "http://cdnapi.kaltura.com/p/2300461/sp/230046100/playManifest/entryId/0_pr1hcb2q/format/url/protocol/http";
    String url = kalturaCourseSolrPagerService.findRealUrl(dataUrl);
    String expected =
        "https://cfvod.kaltura.com/pd/p/2300461/sp/230046100/serveFlavor/entryId/0_pr1hcb2q/v/12/ev/10/flavorId/0_16v3aj10/name/a.mp4";
    assertEquals(expected, url);
  }

  /**
   * Test kaltura find metadata value.
   */
  @Test
  public void test_kaltura_findMetadataValue() {

    List<KalturaMetadata> metadataList =
        kalturaCourseSolrPagerService.findMetadata(client, "1_58c7yco4");
    String value = kalturaCourseSolrPagerService.findMetadataValue(metadataList, "Language");
    assertEquals("English", value);
    value = kalturaCourseSolrPagerService.findMetadataValue(metadataList, "Competencies");
    assertEquals("Continuous learning", value);
  }

}
