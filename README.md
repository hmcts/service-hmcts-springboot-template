# API-CP Template Repository

This is a template repository for Common Platform (CP) APIs in HMCTS. It defines naming conventions, structure, and validation tooling for OpenAPI specifications.

> 🔗 API definitions should follow the [HMCTS RESTful API Standards](https://hmcts.github.io/restful-api-standards/).  
> 📘 See [APIVERSIONING.md](./APIVERSIONING.md) for our CP API versioning strategy based on media types and SemVer.

## Naming Convention

> NOTE: Avoid using terms like `common, core, base, utils, helpers, misc, or shared`.
> These names often allow for ambiguous ownership and quickly become black holes where cohesion goes to die.

Repository names follow a pattern from generic to specific:

```
api-cp-[case-type]-{product-domain}-{name-of-entity}
```
The optional `case-type` parameter could be:

* civil 
* crime 
* family 
* tribunal

HMCTS manages all Civil, Criminal, Family (separate from civil), and Tribunal cases.

### Reference Data Repositories

Reference data APIs use the following naming format:

```
api-cp-refdata-{product-domain}-{name-of-entity}
```
Some might argue that `product-domain` should be optional for reference data, placing it under global ownership. But global ownership often means no ownership — and no accountability. Therefore, `product-domain` is **required**.

## Post-Template Manual Steps

### Setup

* Go to settings of the repository -> General -> check "Automatically delete head branches"
* Import the ruleset `.github/rulesets/master.json`  
  To import the ruleset, follow GitHub’s instructions here:  
  👉 [Importing a ruleset](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-rulesets/managing-rulesets-for-a-repository#importing-a-ruleset)
  
Once the ruleset has been successfully imported via GitHub Settings, the new repository no longer requires `.github/rulesets/master.json` so it **should be deleted**:

### Clean Up

After using this template to create your repository, the following files are no longer needed and **should be deleted**:

- `./APIVERSIONING.md`
- `./openapi/deleteme`

Update the `./README.md` to reflect the context of the new created repository


## OpenAPI Specification and Data Schema Validation

This repository includes a GitHub Action to validate OpenAPI specifications and data payload JSON Schemas.

### Run Validation Locally

> Node must be installed on your machine.

Install dependencies:
```bash
npm install -g @stoplight/spectral-cli ajv-cli jsonlint
```

Validate the OpenAPI specification:
```bash
spectral lint "openapi/**/*.{yml,yaml}"
```
Documentation: https://stoplight.io/open-source/spectral

Validate the data payload JSON Schemas:
```bash
jsonlint -q ./openapi/path/to/example_payload.json
```

Validate the data payload JSON Schemas:
```bash
ajv --spec=draft2020 --strict=false -s "./openapi/path/to/schema.json" -d "./openapi/path/to/example_payload.json"
```
Documentation: https://ajv.js.org/

## License

This project is licensed under the [MIT License](LICENSE).

