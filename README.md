# Lunatech People API in Kotlin

## Introduction

### Overview

A simple API providing information about Lunatech employees. 
Ported from Scala to Kotlin.

The following operations can be performed on the API:
* Filter People by Property Value
* Filter Fields by Field Name

### Reference

There is only one main endpoint in this API - */api/people*,  which returns a list of all Lunatech employees.

An API key is required to access this endpoint - */api/people?apiKey={apiKey}*, where '{apiKey}' is replaced with an apiKey.

The real filtering functionality of the API lies in the request parameters specified after the '?' in the URL. Each of these request parameters is separated by a '&'.

The following table provides reference for each paramater.

| Parameter Name| Description                   | Usage  | Notes |
| -------------: | :----------------------------- | :----- | :---: |
| country       | filter by country of employee | */api/people?apiKey={apiKey}&country={country}  | |
| fullName      | filter by full name of employee | */api/people?apiKey={apiKey}&fullName={fullName}  | |
| level       | filter by skill level of employee | */api/people?apiKey={apiKey}&level={level}  | |
| managers       | filter by managers of employee | */api/people?apiKey={apiKey}&managers={managers}  | |
| roles       | filter by roles of employee | */api/people?apiKey={apiKey}&roles={role1,role2}  | This is a special filter where both the AND and OR operation can be used |

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)      Version 11.0.1
* [INTELLIJ](https://www.jetbrains.com/idea/download) Version 2018.2.5

### Installing

Follow these instructions.

---

Navigate to the directory where this git repo will be cloned

```
cd path/to/directory
```

Clone the git repo

```
git clone https://github.com/lunatech-labs/lunatech-people-api-kotlin.git
```

Click IntelliJ app icon

![alt text](src/main/resources/images/installation-guide/3.png)


Click 'Open'

![alt text](src/main/resources/images/installation-guide/4.png)

Navigate to the root directory of git repo

![alt text](src/main/resources/images/installation-guide/5.png)

Click 'Open'

![alt text](src/main/resources/images/installation-guide/6.png)

Click Play Button

![alt text](src/main/resources/images/installation-guide/7.png)

The app should now be running

![alt text](src/main/resources/images/installation-guide/8.png)

Open a web browser

![alt text](src/main/resources/images/installation-guide/9.png)

Navigate to URL 'localhost:8080/api/people/apiKey={apiKey}

![alt text](src/main/resources/images/installation-guide/10.png)

A list of JSON objects should be displayed in your browser

---

All done! Feel free to play around with the filter parameters.

## Built With

* [Kotlin](https://kotlinlang.org/) - Programming Language
* [SpringBoot](http://spring.io/projects/spring-boot) - Web Framework
* [Gradle](https://gradle.org/) - Dependency Management
* [IntelliJ](https://www.jetbrains.com/idea/) - IDE
* [GSON](https://github.com/google/gson) - JSON Serialization & Deserialization


## Authors

* **Muratcan Celayir**
* **Hrishi Mukherjee**
* **Mohammad Fazel**

