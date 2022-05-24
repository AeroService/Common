# EventBus
[![license](https://img.shields.io/github/license/NatroxMC/EventBus?style=for-the-badge&color=b2204c)](../LICENSE)
[![standard-readme compliant](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=for-the-badge)](https://github.com/RichardLitt/standard-readme)

A simple event bus for Java.

# Table of contents
- [Why use the EventBus?](#why-use-the-eventbus)
- [Advantages & Disadvantages](#advantages-and-disadvantages)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

# Why use the EventBus

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
The following explains in detail how to use the EventBus and what it does.

# Add to your project
- Gradle: (Not yet available)
- Maven: (Not yet available)

# EventBus
**Step 1:** Instantiate an EventBus.

Code example:
```java
EventBus eventBus = EventBus.create();
```

**Step 2:** Create an event class.

Code example:
```java
public record TestEvent(String value) {

}
```

**Step 3:** Register an listener.

### Using an Consumer.

Code example:
```java
eventBus.register(TestEvent.class, event -> {
    // Code here
});
```

### Using an EventListener.

Code example:
```java
eventBus.register(
    EventListener
        .builder(TestEvent.class)
        .condition(event -> true) // Optional
        .handler(event -> {
            // Code here
        })
        .build()
);
```

### Using an EventListener and an Consumer.

Code example:
```java
eventBus.register(EventListener.of(TestEvent.class, event -> {
    // Code here
}));
```

**Step 4:** Call an event.

Code example:
```java
eventBus.call(new TestEvent("value"));
```

# Contributing
See [the contributing file](CONTRIBUTING.md)!
All WIP features are previewed as Draft PRs!

# License
This project is licensed under the [Apache License Version 2.0](../LICENSE).
