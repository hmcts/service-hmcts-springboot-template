 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.1
ARG JARPATH
FROM crmdvrepo01.azurecr.io/registry.hub.docker.com/library/openjdk:21-jdk-slim


COPY ${JARPATH} /opt/app/
COPY lib/applicationinsights.json /opt/app/

EXPOSE 4550
CMD [ "api-cp-springboot-template.jar" ]
