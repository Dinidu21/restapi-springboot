#!/bin/sh

# Check for required environment variables
required_vars="SPRING_DATASOURCE_URL SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD SERVER_SERVLET_CONTEXT_PATH SPRING_JPA_HIBERNATE_DDL_AUTO"
missing_vars=""

for var in $required_vars; do
    eval val=\$$var
    if [ -z "$val" ]; then
        missing_vars="$missing_vars $var"
    fi
done

if [ -n "$missing_vars" ]; then
    echo "ERROR: Missing required environment variables:$missing_vars"
    exit 1
fi

# Generate application.properties
echo "Generating application.properties..."
cat << EOF > /app/application.properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
server.servlet.context-path=${SERVER_SERVLET_CONTEXT_PATH}
spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO}
spring.jpa.show-sql=true
spring.datasource.hikari.maximum-pool-size=10
logging.level.org.springframework=INFO
logging.level.com.dinidu=DEBUG
logging.file.name=/app/logs/app.log

management.endpoints.web.exposure.include=health,info
management.endpoint.health.probes.enabled=true
management.endpoints.enabled-by-default=true
management.server.port=8080
management.server.base-path=/actuator
EOF

# Verify application.properties creation
if [ ! -f /app/application.properties ]; then
    echo "ERROR: Failed to create application.properties"
    exit 1
fi

echo "application.properties content:"
cat /app/application.properties

# Start the Spring Boot application
echo "Starting application..."
exec java $JAVA_OPTS -jar app.jar