# Common
[![license](https://img.shields.io/github/license/NatroxMC/Common?style=for-the-badge&color=b2204c)](../LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=for-the-badge)](https://github.com/RichardLitt/standard-readme)

A basic and common library for the development of Java projects

# Table of contents
- [Why use Common?](#why-use-common)
- [How to use](#how-to-use)
  - [Add to your project](#add-to-your-project)
  - [Scheduler](#scheduler)
- [Contributing](#contributing)
- [License](#license)

# Why use Common

Coming soon.

# How to Use
The following explains in detail how to use Common and what it does.

# Add to your project
- Gradle: (Not yet available)
- Maven: (Not yet available)

# Scheduler

**Step 1:** Instantiating a Scheduler.

Code example:
```java
Scheduler scheduler = Scheduler.create();
```

**Step 2:** Building and schedule a Task.

### Repeating task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        //Code here
    })
    .repeat(Duration.of(10, ChronoUnit.SECONDS))
    .schedule();
```

### Delayed task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        //Code here
    })
    .delay(Duration.of(5, ChronoUnit.SECONDS))
    .schedule();
```
### Delayed repeating task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        //Code here
    })
    .delay(Duration.of(5, ChronoUnit.SECONDS))
    .repeat(Duration.of(10, ChronoUnit.SECONDS))
    .schedule();
```

# Counter
Comming soon.

# Taskbatch
Comming soon.

# Contributing
See [the contributing file](CONTRIBUTING.md)!
All WIP features are previewed as Draft PRs

# License
This project is licensed under the [Apache License Version 2.0](../LICENSE).
