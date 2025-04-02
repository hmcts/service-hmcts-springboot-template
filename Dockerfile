 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.1
FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/api-cp-springboot-template.jar /opt/app/

EXPOSE 4550
CMD [ "api-cp-springboot-template.jar" ]
