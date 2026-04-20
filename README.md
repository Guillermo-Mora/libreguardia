# Libreguardia
## Index
- [About Libreguardia](#about-libreguardia)
- [Application idea](#application-idea)
- [Current feature goals](#current-feature-goals)
- [Technologies decision](#technologies-decision)
- [Technologies and Languages](#technologies-and-languages)
- [Setup for coding](#setup-for-coding)
- [Contributing](#contributing)

## About Libreguardia
Libreguardia is a project that aims to solve the managment of teachers' absences and services in schools and universities.
It started as a final course project.

Managing which teacher has been absent, and who should cover it can be tedious and time consuming if done manually. And as for now, there's not any free and open source application to solve this issue.

We are not aiming to create a large software with many functionalities, but rather, a solution that is as simple and easy to use as possible. That can be easily integrated into any educational center.
## Application idea
As said before, the idea of Libreguardia is to bring a simple and easy to use solution for managing teachers absences and services in educational centers.

In this section, I will explain the current idea of how the application would work, which may not match the final result.


## Current feature goals
*Work in progress*
- [✓] Password-protected login access with cookie session.
- [✓] Role and session protected resources and actions.
- [ ] Own user profile visualization with some editable fields.
- [ ] Schedule templates creation, edition and usage, for admins.
- [ ] Users registration and edit with their schedule, for admins.
- [ ] Registries creation, edition and deletion, for admins (Users, Places, Zones, Buildings, Activities, Courses, Groups, etc.).
- [ ] Own absences registration and cancellation.
- [ ] Responsive visualization of the application (Phones, tablets, computers, etc.)
- [ ] Automatic generation of services from user absences.
- [ ] Manual assignation of user to cover a servie, for admins.
- [ ] Automatic assignation of user to cover a serviced, based on a points system. This can be enabled or disabled from the application system settings.
- [ ] Points system for covering services. Each group has a difficulty value of (1.0 - 2.0) assigned by the admins. Each user that covers a service will obtain (service duration in minutes) * (group coverede difficulty) points.
- [ ] Page for seeing live services for today without needing to refresh the page. And with the possibility of navigating and seeing other days services.
- [ ] Special user type for displaying live view of services in screens always on.
- [ ] Being able to select the language for the application (English, Spanish, Valencian).
- [ ] Being able to select to use light/dark theme.
- [ ] Implement DB indexes where needed.
- [ ] Page to display historical statistics of absences and services by academic year, or by other filters.
- [ ] Password recovery with automatic mail.
- [ ] Live notifications on your phone (Proably by email or Telegram bot messages) when having to cover a service.
*This feature may require a lot of work, but as for now it will remain as the last thing to add*
## Technologies decision
...
## Technologies and Languages
### Database
*Languages*
- SQL

*Technologies*
- PostgreSQL [Docs](https://www.postgresql.org/docs/18/index.html)
### Backend
*Languages*
- Kotlin [Docs](https://kotlinlang.org/docs/home.html)

*Technologies*
- Ktor [Docs](https://ktor.io/docs/welcome.html)
- Exposed [Docs](https://www.jetbrains.com/help/exposed/home.html)
- Flyway [Docs](https://documentation.red-gate.com/fd)
- Bcrypt [Docs](https://github.com/patrickfav/bcrypt)
- Testcontainers [Docs](https://java.testcontainers.org/)
### Frontend
*Languages*
- HTML (DSL - Ktor) [Docs](https://ktor.io/docs/server-html-dsl.html)
- CSS
- JS

*Technologies*
- HTMX (Ktor integration) [Docs](https://htmx.org/docs/) [Ktor-Docs](https://ktor.io/docs/htmx-integration.html)
## Setup for coding
### Installation
- IntelliJ IDEA
- PostgreSQL 18.3
- PgAdmin
- Git
- Docker (For DB tests with "testcontainers"). Docker deployment still not implemented.
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
## Contributing
If you are interested in contributing to this project, let me know: guillermomora@tutamail.com