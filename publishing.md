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



