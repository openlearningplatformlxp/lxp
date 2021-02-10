package com.redhat.uxl.commonjava.utils.solr;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type String solr word.
 */
@Data
public class StringSolrWord {
    private String word;
    private boolean startByQuote;
    private boolean endByQuote;
    private boolean and;
    private boolean or;

    /**
     * Instantiates a new String solr word.
     *
     * @param word the word
     */
    public StringSolrWord(String word) {
        this.word = word;
        if (StringUtils.startsWith(word, "\"")) {
            startByQuote = true;
        }
        if (StringUtils.endsWith(word, "\"")) {
            endByQuote = true;
        }
        if (StringUtils.equalsIgnoreCase(word, "and")) {
            and = true;
        }
        if (StringUtils.equalsIgnoreCase(word, "or")) {
            or = true;
        }
    }

    /**
     * Ignore word boolean.
     *
     * @return the boolean
     */
    public boolean ignoreWord() {
        return StringUtils.equalsIgnoreCase(word, "and") || StringUtils.equalsIgnoreCase(word, "or")
                || StringUtils.startsWith(word, "-");
    }

    /**
     * Is excluded word boolean.
     *
     * @return the boolean
     */
    public boolean isExcludedWord() {
        return StringUtils.startsWith(word, "-");
    }
}
