package com.redhat.uxl.datalayer.solr.repository;

import com.redhat.uxl.commonjava.utils.solr.StringSolrBuffer;
import com.redhat.uxl.commonjava.utils.solr.StringSolrLogicOperator;
import com.redhat.uxl.commonjava.utils.solr.StringSolrPhrase;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.FeaturedSearchInstance;
import com.redhat.uxl.dataobjects.domain.types.ContentSourceType;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.QueryParserBase;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;
import org.springframework.data.solr.core.mapping.SolrPersistentEntity;
import org.springframework.data.solr.core.mapping.SolrPersistentProperty;
import org.springframework.data.solr.core.query.AbstractQueryDecorator;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * The type Course document custom solr repository.
 */
@Repository
public class CourseDocumentCustomSolrRepositoryImpl implements CourseDocumentCustomSolrRepository {

    @Resource
    private SolrTemplate solrTemplate;

    @Override
    public HighlightPage<CourseDocument> findByCriteria(Long userId,
                                                        Set<FeaturedSearchInstance> featuredSearchInstanceList, StringSolrBuffer searchTerm,
                                                        List<ProgramType> programTypeList, List<ContentSourceType> contentSourceTypeList, String topic,
                                                        List<Integer> delivery, String skillLevels, String languages, String country, String city,
                                                        boolean isAdminSearch, Pageable pageable) {

        Criteria conditions = createSearchConditions(userId, featuredSearchInstanceList, searchTerm, programTypeList,
                contentSourceTypeList, topic, delivery, skillLevels, languages, country, city, isAdminSearch);
        if (conditions != null) {
            EdismaxQuery search = new EdismaxQuery(conditions);
            search.setDefType("edismax");
            search.setBoostQuery("contentSource:LMS^1");
            search.setPageRequest(pageable);
            search.addSort(Sort.by(Sort.Direction.DESC, "score"));
            search.addSort(Sort.by(Sort.Direction.DESC, CourseDocument.FIELD_TIME_CREATED));
            search.addSort(Sort.by(Sort.Direction.ASC, CourseDocument.FIELD_EVENT_TIME));
            solrTemplate.registerQueryParser(EdismaxQuery.class, new EdisMaxQueryParser(new SimpleSolrMappingContext()));
//            if (!searchTerm.equals("*")) {
//                return solrTemplate.queryForHighlightPage("reduxl", search, CourseDocument.class);
//            } else {
            SolrResultPage page = (SolrResultPage) solrTemplate.queryForPage("reduxl", search, CourseDocument.class);
            return page;
//            }
        } else {
            return new SolrResultPage(new ArrayList<>());
        }
    }

    private Criteria createSearchConditions(Long userId, Set<FeaturedSearchInstance> featuredIds,
                                            StringSolrBuffer searchTerm, List<ProgramType> type, List<ContentSourceType> contentSourceType,
                                            String topic, List<Integer> delivery, String skillLevels, String languages, String country, String city,
                                            boolean isAdminSearch) {

        Criteria conditions = null;

        if (type != null && !type.isEmpty()) {
            conditions = new Criteria(CourseDocument.FIELD_PROGRAM_TYPE).is(ProgramType.namesFrom(type));
        }
        if (contentSourceType != null && !contentSourceType.isEmpty()) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_CONTENT_SOURCE)
                        .in(ContentSourceType.namesFrom(contentSourceType));
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_CONTENT_SOURCE)
                        .in(ContentSourceType.namesFrom(contentSourceType)));
            }
        }
        if (topic != null) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_TAGS).is(topic);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_TAGS).is(topic));
            }
        }
        if (delivery != null && !delivery.isEmpty()) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_DELIVERY).in(delivery);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_DELIVERY).in(delivery));
            }
        }
        if (skillLevels != null) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_SKILL_LEVEL).is(skillLevels);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_SKILL_LEVEL).is(skillLevels));
            }
        }
        if (languages != null) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_LANGUAGE).is(languages);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_LANGUAGE).is(languages));
            }
        }
        if (country != null) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_COUNTRY).is(country);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_COUNTRY).is(country));
            }
        }
        if (city != null) {
            if (conditions == null) {
                conditions = new Criteria(CourseDocument.FIELD_CITY).is(city);
            } else {
                conditions = conditions.and(new Criteria(CourseDocument.FIELD_CITY).is(city));
            }
        }

        if (searchTerm != null && CollectionUtils.isNotEmpty(searchTerm.getExcludedWords())) {
            for (String excludedWord : searchTerm.getExcludedWords()) {
                if (conditions != null) {
                    conditions = conditions.and(new Criteria(CourseDocument.FIELD_FULL_NAME_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(excludedWord) + "*").not());
                    conditions = conditions.and(new Criteria(CourseDocument.FIELD_DESCRIPTION_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(excludedWord) + "*").not());
                } else {
                    conditions = new Criteria(CourseDocument.FIELD_FULL_NAME_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(excludedWord) + "*").not();
                    conditions = conditions.and(new Criteria(CourseDocument.FIELD_DESCRIPTION_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(excludedWord) + "*").not());
                }
            }
        }


        if (conditions != null) {
            if (searchTerm != null && StringUtils.isNotEmpty(searchTerm.getOriginal())) {
                conditions = conditions.and(createSearchName(featuredIds, searchTerm));
            }
        } else {
            conditions = createSearchName(featuredIds, searchTerm);
        }

        if (!isAdminSearch) {
            Criteria visibility = null;
            if (conditions != null) {
                visibility = new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE).is(CourseDocumentVisibilityType.PUBLIC) //
                        .or(new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE)
                                .is(CourseDocumentVisibilityType.RESTRICTED_AUDIENCE)
                                .and(new Criteria(CourseDocument.FIELD_ALLOWED_USERS).is(userId)));

                if (featuredIds != null && !featuredIds.isEmpty()) {
                    Set<String> ids = featuredIds.stream()
                            .map(f -> f.getInstanceType().name() + "{}" + f.getInstanceId())
                            .collect(Collectors.toSet());
                    for (String id : ids) {
                        visibility = visibility.or(new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE)
                                .is(CourseDocumentVisibilityType.RESTRICTED_AUDIENCE)
                                .and(new Criteria(CourseDocument.FIELD_ID).is(id)));
                    }
                }

                conditions = new Criteria(CourseDocument.FIELD_ID).isNotNull().and(visibility).and(conditions);
            } else {
                conditions = new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE).is(CourseDocumentVisibilityType.PUBLIC) //
                        .or(new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE)
                                .is(CourseDocumentVisibilityType.RESTRICTED_AUDIENCE)
                                .and(new Criteria(CourseDocument.FIELD_ALLOWED_USERS).is(userId)));

                if (featuredIds != null && !featuredIds.isEmpty()) {
                    Set<String> ids = featuredIds.stream()
                            .map(f -> f.getInstanceType().name() + "{}" + f.getInstanceId())
                            .collect(Collectors.toSet());
                    for (String id : ids) {
                        conditions = conditions.or(new Criteria(CourseDocument.FIELD_VISIBILITY_TYPE)
                                .is(CourseDocumentVisibilityType.RESTRICTED_AUDIENCE)
                                .and(new Criteria(CourseDocument.FIELD_ID).is(id)));
                    }
                }
            }

        }
        return conditions;

    }

    private Criteria createSearchName(Set<FeaturedSearchInstance> featured, StringSolrBuffer input) {
        if (StringUtils.isNotEmpty(input.toString())) {
            // First exact searches
            List<StringSolrPhrase> phrases = input.getPhrases();
            Criteria criteria = null;
            for (StringSolrPhrase p : phrases) {
                String phrase = StringUtils.replace(p.getPhrase(), " ", "\\ ");
                if (criteria == null) {
                    criteria = new Criteria(CourseDocument.FIELD_FULL_NAME_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(phrase) + "*").boost(1000);
                } else {
                    criteria = criteria.or(new Criteria(CourseDocument.FIELD_FULL_NAME_LOWER_STRING)
                            .expression("*" + StringUtils.lowerCase(phrase) + "*").boost(1000));
                }
                criteria = criteria
                        .or(new Criteria(CourseDocument.FIELD_TAGS).expression("*" + phrase + "*").boost(800))
                        .or(new Criteria(CourseDocument.FIELD_DESCRIPTION_LOWER_STRING)
                                .expression("*\\ " + StringUtils.lowerCase(phrase) + "\\ *").boost(500));
            }
            // Boost featured id
            if (featured != null && !featured.isEmpty()) {
                Set<String> ids = featured.stream().map(f -> f.getInstanceType().name() + "{}" + f.getInstanceId())
                        .collect(Collectors.toSet());
                for (String id : ids) {
                    criteria = criteria.or(new Criteria(CourseDocument.FIELD_ID).is(id).boost(10000));
                }
            }
            // Then similar searches in indexed fields
            for (StringSolrPhrase p : phrases) {
                if (StringSolrLogicOperator.OR.equals(p.getInternalOperator())) {
                    String phrase = p.getPhrase();
                    String searchPhrase = "\"" + phrase + "\"";
                    criteria = criteria
                            .or(new Criteria(CourseDocument.FIELD_FULL_NAME).expression(searchPhrase).boost(100))
                            .or(new Criteria(CourseDocument.FIELD_DESCRIPTION).expression(searchPhrase).boost(80));
                }
            }
            for (StringSolrPhrase p : phrases) {
                String phrase = p.getPhrase();
                if (StringSolrLogicOperator.OR.equals(p.getInternalOperator())) {
                    int proximity = 5;
                    String proximityTerm = phrase + "~" + proximity;
                    // For multiple words add proximity searches if they are don't have quotes
                    criteria = criteria
                            .or(new Criteria(CourseDocument.FIELD_FULL_NAME).expression(proximityTerm).boost(50))
                            .or(new Criteria(CourseDocument.FIELD_DESCRIPTION).expression(proximityTerm).boost(30)); //
                }
            }
            for (StringSolrPhrase p : phrases) {
                String phrase = p.getPhrase();
                if (StringSolrLogicOperator.OR.equals(p.getInternalOperator())) {
                    // There are multiple words, we can increase the search ratio
                    String[] words = StringUtils.splitByWholeSeparator(phrase, " ");
                    for (String w : words) {
                        String searchTerm = "\"" + w + "\"";
                        criteria = criteria
                                .or(new Criteria(CourseDocument.FIELD_FULL_NAME).expression(searchTerm).boost(10)) //
                                .or(new Criteria(CourseDocument.FIELD_TAGS).expression(searchTerm).boost(5))
                                .or(new Criteria(CourseDocument.FIELD_DESCRIPTION).expression(searchTerm).boost(1));
                    }
                }
            }
            return criteria;

        } else {
            return null;
        }
    }

    /**
     * The type Edismax query.
     */
    @Data
    class EdismaxQuery extends SimpleHighlightQuery implements HighlightQuery {
        // ... add stuff you need. Maybe `autoRelax`
        private String boostQuery;

        /**
         * Instantiates a new Edismax query.
         *
         * @param conditions the conditions
         */
        public EdismaxQuery(Criteria conditions) {
            super(conditions);
        }
    }


    /**
     * The type Edis max query parser.
     */
    class EdisMaxQueryParser extends QueryParserBase<AbstractQueryDecorator> {

        private final DefaultQueryParser defaultQueryParser;

        /**
         * Instantiates a new Edis max query parser.
         *
         * @param mappingContext the mapping context
         */
        public EdisMaxQueryParser(MappingContext<? extends SolrPersistentEntity<?>, SolrPersistentProperty> mappingContext) {
            super(mappingContext);
            defaultQueryParser = new DefaultQueryParser(mappingContext);
        }

        @Override
        public SolrQuery doConstructSolrQuery(AbstractQueryDecorator queryDecorator, Class<?> domainType) {
            EdismaxQuery source = (EdismaxQuery) queryDecorator.getDecoratedQuery();
            // just use the default parser to construct the query string in first place.
            // SolrQuery target = defaultQueryParser.constructSolrQuery(source, domainType);
            SolrQuery target = defaultQueryParser.doConstructSolrQuery(source, domainType);

            // add missing parameters
            target.add("defType", "edismax");
            target.add("bq", source.getBoostQuery());

            target.setHighlight(true);
            target.addHighlightField("fullName");
            target.addHighlightField("description");
            target.setHighlightSimplePre("<strong>");
            target.setHighlightSimplePost("</strong>");
            return target;
        }

    }

}
