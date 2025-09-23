# Pipeline Overview

## Current Pipeline

This diagram summarises the flow up to publishing an image to Azure Container Registry (ACR).

```mermaid
sequenceDiagram
    participant Dev as Developer
    participant Repo as (Micro)Service Repository
    participant GH as GitHub Actions
    participant ADO as Azure DevOps
    participant JFA as JFrog Artefactory
    participant Art as Azure Artifacts (Public)
    participant ACR as Azure Container Registry

    Dev->>Repo: Pull Request
    Repo->>GH: Various Actions Workflows are triggered
    note right of GH: Actions Workflows are run in parallel
    GH->>GH: Security Scans (CodeQL, Trufflehog, Gitleaks)
    GH->>GH: Linting & Static Code Analysis
    GH->>GH: Unit & Integration Tests
    GH->>GH: Build (Gradle)

    note right of GH: All assurance workflow steps are complete
    GH->>Art: Publish artefact [DRAFT] (versioned via git tag & commit SHA)
    GH->>ADO: Trigger ADO pipeline (hmcts/trigger-ado-pipeline)

    ADO->>JFA: Pull libraries from Azure Artefact
    JFA->>Art: Upstream dependency
    ADO->>ACR: Build & publish container

```