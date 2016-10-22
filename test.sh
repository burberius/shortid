#!/bin/bash

TEXT="Just some text"

RESULT=$(curl -s -X POST -H "Content-Type: application/json" -d "{\"aggregate\": \"$TEXT\"}" "http://localhost:8080/aggregate")

UUID=$(echo $RESULT | sed -e 's#{"uuid":"\([-0-9a-f]*\)",".*#\1#')

RESULT2=$(curl -s -H "Content-Type: application/json" "http://localhost:8080/aggregate?uuid=$UUID")

echo $RESULT2 | grep "$TEXT" > /dev/null && echo "SUCCESS" || echo "FAILED"
