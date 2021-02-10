package com.redhat.uxl.commonjava.utils.solr;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type String solr phrase.
 */
@Data
public class StringSolrPhrase {
    // By default use OR operator
    private StringSolrLogicOperator internalOperator = StringSolrLogicOperator.OR;
    private String phrase;
    private boolean startByQuote;

    /**
     * Add word.
     *
     * @param word the word
     */
    public void addWord(String word) {
        word = StringUtils.replace(word, "\"", "");
        if (phrase == null) {
            phrase = word;
        } else {
            phrase += " " + word;
        }
    }
}
