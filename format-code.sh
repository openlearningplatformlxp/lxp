#!/bin/sh
./cleanup.sh
mvn formatter:format
mvn xml-format:xml-format
find . -name "*.java" -print -exec java -jar commonjava/src/main/resources/java-format.jar -r --fix-imports-only "{}" +
find . -name '*.js' | xargs js-beautify
find . -name '*.html' | xargs html-beautify
find . -name '*.css' | xargs css-beautify
find . -name '*.scss' | xargs css-beautify
