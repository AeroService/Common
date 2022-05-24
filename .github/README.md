# Common
[![license](https://img.shields.io/github/license/NatroxMC/Common?style=for-the-badge&color=b2204c)](../LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=for-the-badge)](https://github.com/RichardLitt/standard-readme)

A basic and common library for the development of Java projects

# Table of contents
- [Why use Common?](#why-use-common)
- [Advantages & Disadvantages](#advantages-and-disadvantages)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

# Why use Common

Coming soon.

# Advantages and Disadvantages

Coming soon.

## Advantages

- Coming soon
- Coming soon

## Disadvantages

- Coming soon
- Coming soon

# Usage
The following explains in detail how to use Common and what it does.

# Add to your project
- Gradle: (Not yet available)
- Maven: (Not yet available)

# Scheduler

**Step 1:** Instantiate an Scheduler.

Code example:
```java
Scheduler scheduler = Scheduler.create();
```

**Step 2:** Build and schedule an Task.

### Repeating task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        // Code here
    })
    .repeat(10, ChronoUnit.SECONDS)
    .schedule();
```

### Delayed task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        // Code here
    })
    .delay(5, ChronoUnit.SECONDS)
    .schedule();
```
### Delayed repeating task

Code example:
```java
Task task = scheduler
    .buildTask(() -> {
        // Code here
    })
    .delay(5, ChronoUnit.SECONDS)
    .repeat(10, ChronoUnit.SECONDS)
    .schedule();
```

# Counter
Comming soon.

# Taskbatch

**Step 1:** Instantiate an TaskBatch Factory.

Code example:
```java
TaskBatch.Factory taskBatchFactory = new SimpleTaskBatchFactory();
```

**Step 2:** Create and build an TaskBatch.
```java
TaskBatch taskBatch = taskBatchFactory
    .createTaskBatch()
    .sync(() -> {
        // Code here
    })
    .async(() -> {
        // Code here
    })
    .wait(10, ChronoUnit.SECONDS)
    .sync(() -> {
        // Code here
    });
```

**Step 3:** Execute the TaskBatch.

Code example:
```java
taskBatch.execute();
```

Or code exmaple:
```java
taskBatch.execute(() -> {
    // Callback
});
```

# Contributing
See [the contributing file](CONTRIBUTING.md)!
All WIP features are previewed as Draft PRs!

# License
This project is licensed under the [Apache License Version 2.0](../LICENSE).
