#!/bin/bash

#Copy all scalding related dependencies from S3
hadoop fs -copyToLocal s3://$1/resources/jars/scalding-dependencies/* /home/hadoop/share/hadoop/common/lib/
