package com.redhat.uxl.commonjava.utils;

import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The type Solr utils.
 */
public class SolrUtils {

    private static final String[] solrSpecialChars = new String[] { ":", "/", "^", "~", "(", ")", "$", "%", "@", "#",
            "+", "\\", "*", "?", "'", "…", ",", "." };
    /**
     * The constant TAG_STRONG.
     */
    public static final String TAG_STRONG = "strong";

    /**
     * Sanitize input string solr buffer.
     *
     * @param input the input
     * @return the string solr buffer
     */
    public static StringSolrBuffer sanitizeInput(String input) {
        String sanitized = sanitizeInput(input, " ");
        // Replace 'or' words for spaces
        sanitized = org.apache.commons.lang3.StringUtils.replaceIgnoreCase(sanitized, " or ", " ");
        // Remove multiple consecutive spaces
        while (StringUtils.contains(sanitized, "  ")) {
            sanitized = StringUtils.replace(sanitized, "  ", " ");
        }
        sanitized = StringUtils.trimToEmpty(sanitized);
        // Remove multiple consecutive double quotes
        while (StringUtils.contains(sanitized, "\"\"")) {
            sanitized = StringUtils.replace(sanitized, "\"\"", "");
        }
        sanitized = StringUtils.trimToEmpty(sanitized);
        // And words will be converted to phrases inside quotes, except we are inside quotes
        String[] words = StringUtils.splitByWholeSeparator(sanitized, " ");
        StringSolrBuffer buffer = new StringSolrBuffer();
        for (String word : words) {
            buffer.addWord(word);
        }
        sanitized = buffer.toString();
        return buffer;
    }

    /**
     * Sanitize input string.
     *
     * @param input       the input
     * @param replacement the replacement
     * @return the string
     */
    public static String sanitizeInput(String input, String replacement) {
        if (input != null) {
            for (String s : solrSpecialChars) {
                input = StringUtils.replace(input, s, replacement);
            }
        }
        return input;
    }

    /**
     * Split input in phrases list.
     *
     * @param input the input
     * @return the list
     */
    public static List<String> splitInputInPhrases(String input) {
        Validate.notNull(input);
        List<String> phrases = new ArrayList<>();
        if (StringUtils.contains(input, "\"")) {
            String[] split = StringUtils.splitByWholeSeparator(input, "\"");
            for (String p : split) {
                p = StringUtils.trimToEmpty(p);
                if (StringUtils.isNotEmpty(p)) {
                    phrases.add("\"" + p + "\"");
                }
            }
        } else {
            phrases.add(input);
        }
        return phrases;
    }

    /**
     * Highlight original input string.
     *
     * @param snipplets       the snipplets
     * @param nonIndexedField the non indexed field
     * @return the string
     */
    public static String highlightOriginalInput(List<String> snipplets, String nonIndexedField) {
        Validate.notNull(nonIndexedField);
        String highlightedWord = nonIndexedField;
        if (snipplets != null && !snipplets.isEmpty()) {
            // Search for the highlighted words
            Set<String> wordsToReplace = new TreeSet<>();
            for (String snipplet : snipplets) {
                wordsToReplace.addAll(Jsoup.parse(snipplet).getElementsByTag(TAG_STRONG).stream().map(e -> e.text())
                        .collect(Collectors.toSet()));
            }
            // Build the list of non indexed words
            String[] nonIndexedWords = StringUtils.splitByWholeSeparator(nonIndexedField, " ");
            int index = 0;
            StringBuffer newPhrase = new StringBuffer();
            for (String nonIndexedWord : nonIndexedWords) {
                // Add spaces between words
                if (index > 0) {
                    newPhrase.append(" ");
                }
                boolean shouldHighlight = false;
                for (String w : wordsToReplace) {
                    if (StringUtils.equalsIgnoreCase(SolrUtils.sanitizeInput(nonIndexedWord, ""), w)) {
                        // Word matches. Highlight
                        shouldHighlight = true;
                        break;
                    }
                }
                if (shouldHighlight) {
                    // Word matches. Highlight
                    newPhrase.append("<strong>" + nonIndexedWord + "</strong>");
                } else {
                    newPhrase.append(nonIndexedWord);
                }
                index++;
            }
            highlightedWord = newPhrase.toString();
        }
        return highlightedWord;
    }

    /**
     * Focus highlight string.
     *
     * @param highlightedPhrase the highlighted phrase
     * @param maxLength         the max length
     * @return the string
     */
    public static String focusHighlight(String highlightedPhrase, int maxLength) {
        String focusPhrase = highlightedPhrase;
        if (highlightedPhrase != null && highlightedPhrase.length() > maxLength) {
            // get first highlighed phrase
            int index = StringUtils.indexOf(highlightedPhrase, "<strong>");
            if (index > maxLength - 30) {
                index = index - 100;
                boolean addEllipsis = false;
                if (index - 100 > 0) {
                    addEllipsis = true;
                } else {
                    index = 0;
                }
                focusPhrase = StringUtils.substring(highlightedPhrase, index, highlightedPhrase.length());
                // Remove first partial word
                String[] words = StringUtils.splitByWholeSeparator(focusPhrase, " ");
                List<String> phraseSplitted = new ArrayList<>();
                int i = 0;
                for (String w : words) {
                    if (i == 0) {
                        // skip first word
                    } else {
                        phraseSplitted.add(w);
                    }
                    i++;
                }
                focusPhrase = StringUtils.join(phraseSplitted, " ");
                if (addEllipsis) {
                    focusPhrase = "..." + focusPhrase;
                }
            }
        }
        return focusPhrase;
    }
}
