package com.redhat.uxl.services.service.solr.strategies;

import com.redhat.uxl.dataobjects.domain.dto.LinkedinCourseDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * The type Linkedin client test.
 */
public class LinkedinClientTest {

    /**
     * The constant MAX_SIZE.
     */
    public static final int MAX_SIZE = 1000;
  private LinkedinCourseSolrPagerServiceImpl linkedinCourseSolrPagerService;
  private HttpEntity httpEntity;

    /**
     * Sets up.
     */
    @Before
  public void setUp() {
    linkedinCourseSolrPagerService = new LinkedinCourseSolrPagerServiceImpl();
    linkedinCourseSolrPagerService.authUrl = "https://www.linkedin.com/oauth/v2/accessToken";
    linkedinCourseSolrPagerService.assetsUrl = "https://api.linkedin.com/v2/learningAssets";
    linkedinCourseSolrPagerService.appKey = "78v6mup5ac8vjb";
    linkedinCourseSolrPagerService.secretKey = "EMKSlMgT06FemIXb";
    httpEntity = linkedinCourseSolrPagerService.prepare();

  }

    /**
     * Test auth.
     */
    @Test
  public void test_auth() {
    assertNotNull(httpEntity);
    assertNotNull(httpEntity.getHeaders().get("Authorization"));
  }

    /**
     * Test linkedin.
     */
    @Test
  public void test_linkedin() {
    for (int i = 0; i < 13; i++) {
      Page<LinkedinCourseDTO> linkedinCoursePage =
          linkedinCourseSolrPagerService.findActiveItems(httpEntity, (100 * i) + 1, 100);
      assertNotNull(linkedinCoursePage);
      assertEquals(100, linkedinCoursePage.getSize());
    }
  }

}
