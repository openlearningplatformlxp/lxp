package com.redhat.uxl.commonjava.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The type Solr utils test.
 */
public class SolrUtilsTest {

  /**
   * Test sanitize input.
   */
  @Test
  public void test_sanitizeInput() {
    assertEquals("My sanitized input", SolrUtils.sanitizeInput("My sanitized or input").toString());
    assertEquals("My sanitized & input",
        SolrUtils.sanitizeInput("My sanitized %&^ input").toString());
    assertEquals("My sanitized input",
        SolrUtils.sanitizeInput("\"My sanitized\"\" input\"").toString());
    assertEquals("My sanitized input and word",
        SolrUtils.sanitizeInput("My and sanitized or \"input and word\"").toString());
    assertEquals("My sanitized input word",
        SolrUtils.sanitizeInput("My and sanitized or input and word").toString());
  }

  /**
   * Test highlight original input.
   */
  @Test
  public void test_highlightOriginalInput() {
    String input = "Manager Series (M) series (Management)";
    List<String> snipplets = new ArrayList<>();
    snipplets.add("<strong>manager</strong> series  m series <strong>management</strong>");
    String result = SolrUtils.highlightOriginalInput(snipplets, input);
    assertEquals("<strong>Manager</strong> Series (M) series <strong>(Management)</strong>",
        result);
  }

  /**
   * Test split input in phrases.
   */
  @Test
  public void test_splitInputInPhrases() {
    List<String> phrases =
        SolrUtils.splitInputInPhrases("This is a \"Red Hat\" phrase \"with operators\"");
    assertEquals(4, phrases.size());
    assertEquals("This is a", phrases.get(0));
    assertEquals("Red Hat", phrases.get(1));
    assertEquals("phrase", phrases.get(2));
    assertEquals("with operators", phrases.get(3));
  }

  /**
   * Test focus highlight.
   */
  @Test
  public void test_focusHighlight() {
    String input =
        "This program is designed to provide role-specific content and experiences to help you predictably drive revenue from and strengthen the relationship with your partner through a high-impact plan. What's in it for you? Understand the importance of and what is expected in the Partner Account <strong>manager</strong> role in an open organization Think strategically about your partner(s) and create a plan to execute against your strategy Discover tools and resources that guide critical Partner";
    String result = SolrUtils.focusHighlight(input, 280);
    assertEquals(
        "...What's in it for you? Understand the importance of and what is expected in the Partner Account <strong>manager</strong> role in an open organization Think strategically about your partner(s) and create a plan to execute against your strategy Discover tools and resources that guide critical Partner",
        result);
    input =
        "What is expected in the Partner Account <strong>manager</strong> role in an open organization Think strategically about your partner(s) and create a plan to execute against your strategy Discover tools and resources that guide critical Partner";
    result = SolrUtils.focusHighlight(input, 280);
    assertEquals(
        "What is expected in the Partner Account <strong>manager</strong> role in an open organization Think strategically about your partner(s) and create a plan to execute against your strategy Discover tools and resources that guide critical Partner",
        result);
    input =
        "The Process Automation Manager 7 and <strong>Decision</strong> Manager 7 technical field enablement session is targeted at Middleware Solution Architects, Consultants, and others with a need to understand the features of the new Process Automation Manager 7 and <strong>Decision</strong> and fast this is a really long and valuable string";
    result = SolrUtils.focusHighlight(input, 280);
    assertEquals("What <strong>manager</strong> role ", result);
  }
}
