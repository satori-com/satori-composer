
### release workflow
- build may be required before pushing changes, as some committed sources are generated 
  and may be need to be updated, say for example if you changed version, readme.md file 
  should be regenerated 
- push changes
- ensure CI tests passed (https://travis-ci.org/satori-com/satori-composer)
- publish github releases
- publish artifacts to public/snapshot maven repo

### publishing github releases
- add `github.gardle` file to `.gradle` directory, with following content:
  ```groovy
  ext {
    githubAuthToken = "..."
  }
  ```
- run gradle task `githubPublishRelease`

### publishing maven artifacts to nexus repo

- add `publishing.gardle` file to `.gradle` directory, with following content:
  ```groovy
  ext {
    nexusUsername = '...'
    nexusPassword = '...'
  }
  ```
- run gradle task `publishJarPublicationToSnapshotRepository` for snapshots or
  `publishJarPublicationToReleasesRepository` for releases


### publishing maven artifacts to maven local

- run gradle task `publishJarPublicationToMavenLocal`

### publishing maven artifacts to local directory '.repo'

- run gradle task `publishJarPublicationToRootRepository`



