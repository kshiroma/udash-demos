#!/usr/bin/env bash

ERRORS=0
for subproject in `ls */build.sbt`; do
  SUBDIR=`dirname $subproject`
  echo "Testing ${SUBDIR}..."
  cd "$SUBDIR"
  sbt compile > compilation.log
  if [ $? -eq 0 ]; then
    echo -e "Test \e[32msucceed\e[39m!"
  else
    echo -e "Test \e[31mfailed\e[39m!"
    ((ERRORS++))
  fi
  cd ..
done

exit ${ERRORS}