# Libreguardia
## Setup for coding
### Installation
- IntelliJ IDEA
- PostgreSQL 18.3
- PgAdmin
- Git
### PostgreSQL configuration
*Note that this configuration only purpose is for an easy and fast setup and will not be used as a final solution for setting up the application.*
 - Port default -> 5432
 - Create user with all privileges ->  
 name: postgres  
 passowrd: libreguardia
 - Create database ->  
 name: libreguardia  
 owner: postgres  
### Development
- Make sure PostgreSQL is running.
- Clone the "develop" branch from this repository.
- In the cloned project, open the folder "libreguardia.ktor" with IntelliJ IDEA.
- Run the project from the "Application.kt" file for the first time and the PostgreSQL tables will be created automatically.
- Open PgAdmin in case you need it for modifying the database, testig, doing queries, etc.
### Adding features
- In your cloned repository, create a new branch with this format: "feature/feature-to-be-added"
- Code the features with the needed commits. When the feature is implemented and tested to work correctly, make sure to push the branch with the latest changes, open the GitHub repository an make a pull request of:  
base:develop <- compare:feature/feature-to-be-added  
The changes will be checked by the owner and a feedback will be given. If they are correct, the pull request will be merged and closed. So the develop branch will have the new changes.
- **Important**: Never work directly on the develop or main branch. You have to always create a new one as discussed in the previous points of this section.

### Recommended documentation
- Flyway: https://documentation.red-gate.com/flyway
- Ktor: https://ktor.io/docs/welcome.html
- PostgreSQL: https://www.postgresql.org/docs/18/index.html
- Kotlin: https://kotlinlang.org/docs/home.html
- Exposed: https://www.jetbrains.com/help/exposed/home.html