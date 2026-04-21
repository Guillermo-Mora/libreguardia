# Libreguardia
## Index
- [About Libreguardia](#about-libreguardia)
- [Application idea](#application-idea)
- [Current feature goals](#current-feature-goals)
- [Technologies and Languages](#technologies-and-languages)
- [Behind Technologies decision](#behind-technologies-decision)
- [Setup for coding](#setup-for-coding)
- [Contributing](#contributing)

## About Libreguardia
Libreguardia aims to solve the managment of teachers' absences and services in schools and universities.
It started as a final course project.

Managing which teacher has been absent, and who should cover it can be tedious and time consuming if done manually. And as for now, there's not any free and open source application to solve this issue.

We are not aiming to create a large software with many functionalities. But rather, a solution that is as simple and easy to use as possible. And that can be easily integrated into any educational center.
## Application idea
As said before, the idea of Libreguardia is to bring a simple and easy to use solution for managing teachers absences and services in educational centers.

In this section, I will explain as brief as possible the current idea of how the application would work, which may not match the final result.
### Users
The app itself, stores their users, with their sessions and data, without the need of additional software. Users would typically be the teachers of the educational center, and the administrators responsible for managing the app.

Currently, there are 3 type of user roles:
- USERS
- ADMINS
- VISUALIZERS

Users and admins represent real people from the educational center, but admins having more permissions inside the app. While visualizers are an special role to create users used for displaying the current live state of the services in screens on the educational center.

Users can be disabled and deleted, but the historic data from them will never be deleted from the database (absences and services). So it will always be available.
### Absences and Services
Every user will be able to register its own absences. Being able to also delete them if they put them wrong.

Each absence, will generate its service automatically, only if the activity performed in that time is marked as "generates_serivce".

Every service represents a schedule time period in which a teacher will be absent and someone would have to be there at that time.

Admins will be able to add/delete absences for all users. And will be responsible for managing who should cover the generated services, in case the auto-assignment of these is disabled.

In the application settings, there will be an option to enable/disable auto-assignment of services. If it is disabled, admins will have to manually assign the user to cover each service. While if its enabled, the app will automatically assing the user to cover the service based on a points system.
### The points system
Each school course, could have one to various groups. For example, you have the 1DAM course, with the groups 1DAM-A and 1DAM-B. Every group, will have a decimal difficulty score, from 1.0 to 2.0. The idea behind this, is that administrators and teachers can talk to set the difficulty of covering services with that groups. So that teachers who cover those services, will obtain more points.

*How the points are calculated:*

Every service has a duration in minutes marked by its start and end time. These represents the points that a teacher obtains when covering a service. Meaning a 55 minutes duration service, will give 55 points to the teacher who covers it. While the difficulty score of the group represents the multiplier. A good group could be marked with 1.0. While a bad group could be marked with 1.7. This means that if two different teachers cover a 55 minutes service, but one with a 1.0 group and the other with a 1.7 group. One teacher will obtain 55 points, while the other 93.5 points.

The point system is being implemented in a way that it doesn't affect previous registries. Meaning if a group marked as 1.0 then changes to 1.5 after some time. The previous points obtained when it was 1.0 will remain the same. This is very important, as the educational center will be able to change the group scores on time.

Now that the point system has been explained, the automatic service assignment will basically assign each service to the teacher with less points in that moment, that is available in the zone of the center where the service has to be covered. Next, I will be explaining what zones are and how they will work.
### Zones
In order to managing better where the teachers have to go for covering services, the system lets you register places and buildings. So that the location of the service to be covered can be displayed. However, some educational centers have their teachers responsible for services on each hour on different locations. With the goal that only teachers near the location of the absence, should cover it.

This is the purpose of the zones. While places represent a physical real place. Example: Building 2, floor 1, Classroom E-02. Zones Represent a group of places. This way, we can decide which places pertain to the same zone. So that only available teachers in that zone at that time period should cover services there. But how can the system know where teachers are at every time period and if they're available for covering services or they are doing other activities? In order to solve this, we simply store each user's schedule in the system.
### Schedules
For all this to work as expected, each teacher full weekly schedule would have to be registered in the system. Each schedule will consist of all of their time slots for every day of the week. And each slot will containt the day of the week, the start and end time, the group (if there's), the activity to be performed and the place.

This way, the system can automatically use each teacher schedule to realize the previous tasks. While registering each user's schedule can sound as a very tedious task (it is). I haven't found a better way to do this. As letting every user register absences without a marked schedule would result in no way of catching wrong registrations. And the system won't also be able to automatically assign or detect available teachers to cover the service that day at that time. Meaning the magic will disappear, and the application would work the same way as doing everything manually. In order to make this as easier as possible, one of the main objectives is to offer an easy way to create, edit and delete schedules. That's why we will implement schedule templates.
### Schedule templates
As said before, each schedule time slot consists of a lot of fields to be filled. Doing this for every time slot of every user of the system could take too much time if the educational center is very big. But with schedule templates, we solve most part of this problem.

In educational centers, usually groups of teachers share the same schedule pattern. For example, the users responsible for teaching x groups of the center, all start every day at the same time and end at the same time. Having the same time periods and the same breaks at the same times.

With schedule templates, we could create a template for these teachers' weekly schedule. Schedule templates are exactly the same as normal schedules, with the difference that we can decide which fields to leave empty or not. This way, we could only leave empty the group and place fields in this template. So then, when creating the schedules for these users, we can load the template and just fill the groups and places.

This will be fully editable, meaning there are no limitations when creating or using schedule templates. You could create a user schedule loading a template, but then delete some of the time slots from that user's schedule, without affecting the base template or facing limitations.

*This is the base idea behind how Libreguardia would work. As you can read, its simple. We want to focus on making something simple and easy to use rather than adding a lot of functionalities that would result in more time spent from the final user inside this app.*
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
- [ ] Application deployment with Docker.
- [ ] Implement DB connection pool.
- [ ] Implement forced HTTPS-Only.
- [ ] Live notifications on your phone (Proably by email or Telegram bot messages) when having to cover a service.
*This feature may require a lot of work, but as for now it will remain as the last thing to add*
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
## Behind Technologies decision
*This section not only explains the technical reasoning behind the choice of languages and technologies, but also provides a narrative of how I personally approached and experienced each decision throughout the project.*
### 1. Why Kotlin?
As Libreguardia started from a final course project, the initial idea was to use the language and framework we already studied for developing backend, which was Java with Spring Boot.

However, during this course we also saw Kotlin for Android Development. And since I started using it, I realized how much I liked using it for writting code. It reduces a lot of boiler plate code you get from Java, and has additional good features such as coroutines and null safety, preventing [The Billion-Dollar Mistake](https://en.wikipedia.org/wiki/Null_pointer#History).

Kotlin is a language that runs inside the JVM, and its fully interoperable with Java, meaning I could use any framework or technology designed for Java in Kotlin.

Knowing this let me to some questions, as if Kotlin was actually a good option for backend development. And after some research on Google, I read in a lot of articles, forums and more that Kotlin is actually being used for a lot of backend enterpise applications. In some cases, as a result of migrating from Java. So at this point I had no excuse not to use Kotlin.

Also, I want to recommend to all people that writes code in Java, to give Kotlin a try. As for me, I see no point in still using Java these days for developing new software.
### 2. The database
Our application needs to store a lot of data. Each user's information, all the services, absences, schedules, places, zones, etc. And some of them with clearly need of relations. So I knew we had to use a relational DB. I did a research and I decided to go with PostgreSQL, as it appears to be the standard for most applications that need a relational database.
### 3. Ktor as the backend framework
When we started this project, the development started using Spring Boot. However, after some weeks of developlment using Spring Boot, I realized that I was mostly writting Java code using Kotlin.

This occurs because Spring Boot and all their libraries and plugins are so attached to Java, that I couldn't really use the unique Kotlin syntax and features without breaking things, meaning I had to write verbose code again to use Spring Boot.

At first I simply kept going, but eventually I kept encountering more issues, not related to Kotlin, but from Spring Boot. I wanted to simply make role-based endpoints, so only users with specific roles could access them. Each user has a role in the database. When I searched for how I could implement this, I only found JPA Security. Which for me always felt like something very complex and big, while I only wanted to implement a simple feature my own way. I implemented it and then is when I thought about changing the framework, as I felt like Spring Boot was just too big and sometimes with too much magic, meaning I really didn't understand what was happening behind. So I wanted a simplier alternative, and with better Kotlin integration, so I could benefit from its syntax and features that differentiate it from Java.

I Googled for Spring Boot alternatives for Kotlin and mostly only found about Ktor.

Ktor is a very simple backend framework developed by JetBrains with fully Kotlin integration in mind. The idea of Ktor is that you simply start with practically a HTTP library for creating endpoints, that's Ktor without anything more. But the moment you need more features, you can easily add their own plugins, also developed and mantained for Ktor, or simply add the ones you used in your Java projects.

In addition to this, Ktor has some things in advantage from Spring Boot I instantly noticed:
- Fully Kotlin integration.
- Asynchronous by design: While with Spring Boot I was writting blocking code, unless I wanted to deep into WebFlux, which I read about it and added too much complexity to the code. Ktor is built from the ground using Kotlin coroutines, with asynchronous programming in mind. This meant that I could write non-blocking code without adding any complexity.
- Flexibility: In Spring Boot I had to adapt to the framework rules, but with Ktor I could do things the way I wanted without facing any issues. This meant no more need of JPA Security or magic libraries that Spring Boot force you to use. But also take in mind that this also results in more code having to be writeen from scratch by you. However, for me this hasn't been an issue, as I prefer to understand the code rather than using magic dependencies.
- Lots of native plugins: Of course, you can use the plugins you used in your Java project, but Ktor has a lot of useful plugins ported for seamless integration with Kotlin and Ktor. And for me, they fully acomplished all my needs. I only used non Ktor plugins for things like Bcrypt, DB migration and some more I can't remember now. So the same I had with the Spring Boot project.
- Better peformance: The fact of Ktor being a much smaller and simplier framework, and being asynchronous by design, results in a better performance and less resource ussage compared to Spring Boot. I read throught a lot of articles, and all came to the same conclusion, Ktor has performance advantage to Spring Boot.

[Spring Boot vs Ktor](https://www.boundev.com/blog/kotlin-server-side-development-spring-boot-ktor)

*However, the Spring Boot ecosystem is extremly big and mature, so I think it depends on your needs which framework to use. In my case I went with Ktor, as my software is very simple and I really didn't need all the complexity and features that Spring Boot adds by default. I think we should always try to take advanthe from simplier software if we can.*
### 4. Old style frontend
The frontend world has always seemed too overwhelming for me. There are thousands of technologies, and every day they get updated, others get outdated, and new ones appear. And if you want to create something simple for just displaying information or filling forms, you end up with lots of dependencies that you don't even need for anything. Just for them to get outdated in 2 months.

Also, I really didn't have very much experience with any frontend framework. All the work I did with frontend, I simply used HTML, CSS and JS. However, using only this has a lot of limitations, and I would end up with a lot of repeated code.

So I did a research and found out about HTMX. Which is a very basic JS library. The idea of HTMX is that the server returns HTML pages and fragments instead of json. In HTMX, you can make any HTML tag to do GET, POST, PUT, PATCH or DELETE operations on an action performed. For example, you could make that clicking a button would make a GET to the server to obtain an HTML fragment and replace de "main" HTML tag content from the page with the HTML fragment recieved from the server. This way you can easily create an interactive frontend, with the state being managed on the backend and without the need of refreshing the page. While this could look as something too simple, is more than enough for the needs of Libreguardia.

In addition, I read some interesting articles about real life software where HTMX is used for the frontend. [A real world React -> htmx Port](https://htmx.org/essays/a-real-world-react-to-htmx-port/)

As you can read in this article, the migration to HTMX resulted in:
- Code base size reduced by 67%.
- Reduced JS dependencies from 255 to 9.
- Reduced web build time from 40 seconds to 5.
- First load time-to-interactive from 2-6 seconds to 1-2 seconds.
- Much larger data were sets possible.
- Web application memory usage from 75MB to 45MB.

*This may seem like a total win for HTMX. However I don't think this. While HTMX is a very simple and performant approach to build a frontend, it may not be enough for a frontend with very complex interactions, animations, etc. Where React could easily do that job. So as with the decision of using Ktor, I also decided to use HTMX because for the development of Libreguarida I don't need most of the complex features that React gives you, which would end up in a harder to maintain code for me. As I really don't have much experience with any frontend framework.*

In Libreguardia, the frontend is diretly built in the backend and sent to the browser. And in order to recicle code and implement logic easily inside HTML pages, I used Ktor's HTML DSL, which lets me directly code HTML using Kotlin and adding logic. This ends up in a very attached frontend and backend development, which as for now is making things easy to debug, refactor and create in a fast way.
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