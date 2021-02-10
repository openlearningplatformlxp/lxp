package com.redhat.uxl.commonjava.utils.solr;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The type String solr buffer test.
 */
public class StringSolrBufferTest {

  private StringSolrBuffer buffer;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    buffer = new StringSolrBuffer();
  }

  /**
   * Test add word.
   */
  @Test
  public void test_addWord() {
    buffer.addWord("My");
    buffer.addWord("first");
    buffer.addWord("phrase");
    assertEquals(1, buffer.getPhrases().size());
    assertEquals(StringSolrLogicOperator.OR, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first phrase", buffer.getPhrases().get(0).getPhrase());
    assertEquals("My first phrase", buffer.toString());
  }

  /**
   * Test add word quotes in one word.
   */
  @Test
  public void test_addWord_quotesInOneWord() {
    buffer.addWord("\"deals\"");
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("deals", buffer.getPhrases().get(0).getPhrase());
    assertEquals(1, buffer.getPhrases().size());
  }

  /**
   * Test add word quotes.
   */
  @Test
  public void test_addWord_quotes() {
    buffer.addWord("\"My");
    buffer.addWord("first");
    buffer.addWord("phrase\"");
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first phrase", buffer.getPhrases().get(0).getPhrase());
    assertEquals("My first phrase", buffer.toString());
    assertEquals(1, buffer.getPhrases().size());
  }

  /**
   * Test add word and operator.
   */
  @Test
  public void test_addWord_andOperator() {
    buffer.addWord("My");
    buffer.addWord("and");
    buffer.addWord("phrase");
    assertEquals(1, buffer.getPhrases().size());
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My phrase", buffer.getPhrases().get(0).getPhrase());
    assertEquals("My phrase", buffer.toString());
    buffer = new StringSolrBuffer();
    buffer.addWord("My");
    buffer.addWord("and");
    buffer.addWord("first");
    buffer.addWord("and");
    buffer.addWord("phrase");
    assertEquals(1, buffer.getPhrases().size());
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first phrase", buffer.getPhrases().get(0).getPhrase());
    assertEquals("My first phrase", buffer.toString());
  }

  /**
   * Test add word quotes ignoring and operator.
   */
  @Test
  public void test_addWord_quotesIgnoringAndOperator() {
    buffer.addWord("\"My");
    buffer.addWord("and");
    buffer.addWord("first");
    buffer.addWord("and");
    buffer.addWord("phrase\"");
    assertEquals("My and first and phrase", buffer.toString());
  }

  /**
   * Test add word and with or.
   */
  @Test
  public void test_addWord_andWithOr() {
    buffer.addWord("My");
    buffer.addWord("and");
    buffer.addWord("first");
    buffer.addWord("or");
    buffer.addWord("phrase");
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first", buffer.getPhrases().get(0).getPhrase());
    assertEquals(StringSolrLogicOperator.OR, buffer.getPhrases().get(1).getInternalOperator());
    assertEquals("phrase", buffer.getPhrases().get(1).getPhrase());
  }

  /**
   * Test add word and with or skip.
   */
  @Test
  public void test_addWord_andWithOrSkip() {
    buffer.addWord("My");
    buffer.addWord("and");
    buffer.addWord("first");
    buffer.addWord("-second");
    buffer.addWord("or");
    buffer.addWord("phrase");
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first", buffer.getPhrases().get(0).getPhrase());
    assertEquals(StringSolrLogicOperator.OR, buffer.getPhrases().get(1).getInternalOperator());
    assertEquals("phrase", buffer.getPhrases().get(1).getPhrase());
    assertEquals(1, buffer.getExcludedWords().size());
    assertEquals("second", buffer.getExcludedWords().get(0));
  }

  /**
   * Test add word and with or with space.
   */
  @Test
  public void test_addWord_andWithOrWithSpace() {
    buffer.addWord("My");
    buffer.addWord("and");
    buffer.addWord("first");
    buffer.addWord("phrase");
    assertEquals(StringSolrLogicOperator.AND, buffer.getPhrases().get(0).getInternalOperator());
    assertEquals("My first", buffer.getPhrases().get(0).getPhrase());
    assertEquals(StringSolrLogicOperator.OR, buffer.getPhrases().get(1).getInternalOperator());
    assertEquals("phrase", buffer.getPhrases().get(1).getPhrase());
  }
}
