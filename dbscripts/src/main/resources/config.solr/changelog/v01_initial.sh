#!/usr/bin/env bash
BASE_SOLR_URL=http://localhost:8983/solr/reduxl
#BASE_SOLR_URL=https://rhl.uat.synegen.com/solr/reduxl
#BASE_SOLR_URL=https://start.learning.redhat.com/solr/reduxl
SOLR_URL=${BASE_SOLR_URL}/schema

curl_solr_json(){
    curl ${BASE_SOLR_URL}/config -X POST -H 'Content-type:application/json' --data-binary "$1"
}
curl_solr_xml(){
    curl ${BASE_SOLR_URL}/update?commit=true -X POST -H 'Content-type: text/xml' --data-binary "$1"
}
curl_solr_schema_json(){
    echo 'Executing POST: ' ${1}
    curl ${SOLR_URL} -X POST -H 'Content-type:application/json' --data-binary "$1"
}

setup(){
    curl_solr_json '{
    "set-property" : {"requestDispatcher.requestParsers.enableRemoteStreaming":true},
    "set-property" : {"requestDispatcher.requestParsers.enableStreamBody":true}
}'
}

delete_all_documents(){
    curl_solr_xml '<delete><query>*:*</query></delete>'
}

delete_field(){
    curl_solr_schema_json '{"delete-copy-field":{ "source":"'${1}'", "dest":"'${1}'_str" }}'
    curl_solr_schema_json '{"delete-field":{ "name":"'${1}'"}}'
}
delete_fields(){
    delete_field 'visibilityType'
    delete_field 'allowedUsers'
    delete_field 'cecredits'
    delete_field 'city'
    delete_field 'classType'
    delete_field 'contentSource'
    delete_field 'country'
    delete_field 'delivery'
    delete_field 'description'
    delete_field 'descriptionString'
    delete_field 'descriptionLowerString'
    delete_field 'duration'
    delete_field 'eventTime'
    delete_field 'eventTimezone'
    delete_field 'externalUrl'
    delete_field 'firstTopic'
    delete_field 'fullName'
    delete_field 'fullNameString'
    delete_field 'fullNameLowerString'
    delete_field 'language'
    delete_field 'shortName'
    delete_field 'shortNameString'
    delete_field 'skillLevel'
    delete_field 'sortOrder'
    delete_field 'tags'
    delete_field 'timeCreated'
    delete_field 'type'
    delete_field 'visibilityType'
}
create_fields(){
    curl_solr_schema_json '{"add-field":{"name":"allowedUsers","type":"plong","multiValued":true,"indexed":true,"stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"cecredits","type":"string","indexed":false,"stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"city","type":"string","default":"","indexed":true,"stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"classType","type":"string","default":"","indexed":true,"stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"contentSource",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"country",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"delivery",  "type":"string",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"description",  "type":"text_en",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"descriptionString",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"descriptionLowerString",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"duration",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"eventTime",  "type":"plong",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"eventTimezone",  "type":"string",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"externalUrl",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"firstTopic",  "type":"string",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"fullName",  "type":"text_en",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"fullNameString",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"fullNameLowerString",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"language",  "type":"string",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"shortName",  "type":"text_en",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"shortNameString",  "type":"string",  "indexed":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"skillLevel",  "type":"string",  "indexed":true,  "required":false,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"sortOrder",  "type":"pint",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"tags",  "type":"text_general"}}'
    curl_solr_schema_json '{"add-field":{"name":"timeCreated",  "type":"plong",  "omitNorms":true,  "omitTermFreqAndPositions":true,  "docValues":true,  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"type",  "type":"string",  "indexed":true,  "stored":true}}'
    curl_solr_schema_json '{"add-field":{"name":"visibilityType",  "type":"string",  "indexed":true,  "stored":true}}'
}
init(){
    setup
    delete_all_documents
    delete_fields
    create_fields
}

init

