# README #

This is an application to fetch the top repositories from GitHub by leveraging GitHub api. The application is built 
using Java 17 and Spring Boot 3.2.2.

## Getting Started
- Run the command `mvn clean install` and `mvn spring-boot:run` to start the application
- The resource is available at `/api/v1/github`.
- Use the `date` param to specify the date from which the popular repositories need to be fetched. This param is a mandatory field.
- Use the `count` param to fetch the top n repositories. This param is optional. When not used, a default of 10 top repositories will be fetched.
- Use the `language` param to filter which programming language you would like to see in the results.

## Examples
Some example url for testing the implemented API:
- http://localhost:8080/api/v1/github?date=2023-02-07&language=java&count=50
- http://localhost:8080/api/v1/github?date=2023-02-07&count=15
- http://localhost:8080/api/v1/github?date=2023-02-07
