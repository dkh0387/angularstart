#!/usr/bin/env bash

echo "Waiting for MySQL..."

# I'm assuming you're running it in a default port (3306)
until nc -vz "mysql-service" 3306; do
  >&2 echo "Waiting for MySQL to be available..."
  sleep 0.1
done

echo "MySQL started"

java -jar /opt/app/backend.jar