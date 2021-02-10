package com.redhat.uxl.commonjava.utils.solr;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type String solr buffer.
 */
@Data
public class StringSolrBuffer {
    private LinkedList<StringSolrPhrase> phrases = new LinkedList<>();
    private StringSolrPhrase currentPhrase = new StringSolrPhrase();
    private boolean addNewPhrase = false;
    private StringSolrWord lastWord;
    private String original;
    private List<String> excludedWords = new ArrayList<>();

    /**
     * Instantiates a new String solr buffer.
     */
    public StringSolrBuffer() {
        this.phrases.add(currentPhrase);
    }

    /**
     * Add word.
     *
     * @param w the w
     */
    public void addWord(String w) {
        Validate.notNull(w);
        if (original == null) {
            original = w;
        } else {
            original += " " + w;
        }
        StringSolrWord word = new StringSolrWord(w);
        if (word.isStartByQuote()) {
            currentPhrase.setStartByQuote(true);
        }
        if (word.isStartByQuote() || word.isAnd()) {
            currentPhrase.setInternalOperator(StringSolrLogicOperator.AND);
        }

        if (StringSolrLogicOperator.AND.equals(currentPhrase.getInternalOperator())) {
            if (word.isOr() || (!currentPhrase.isStartByQuote() && !lastWord.isAnd() && !word.isAnd())) {
                addNewPhrase = true;
                currentPhrase = new StringSolrPhrase();
            }
        }
        if (!word.ignoreWord() || currentPhrase.isStartByQuote()) {
            if (addNewPhrase) {
                phrases.add(currentPhrase);
                addNewPhrase = false;
            }
            currentPhrase.addWord(word.getWord());
        } else if (word.isExcludedWord()) {
            excludedWords.add(StringUtils.remove(word.getWord(), "-"));
        }
        if (word.isEndByQuote()) {
            addNewPhrase = true;
            currentPhrase = new StringSolrPhrase();
        }
        lastWord = word;
    }

    public String toString() {
        return StringUtils.join(phrases.stream().map(StringSolrPhrase::getPhrase).filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList()), " ");
    }
}
