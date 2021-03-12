# RAGCore
[![GitHub version](https://github.com/TheDarkSword/RAGCore/blob/master/icons/maven.svg)](https://search.maven.org/search?q=ragcore)

### Common library for Java programming
This library contains a bunch of frequently used code snippets and classes that will
help you work faster without wasting your time with always the same codes.

## Features
- [x] SQL libraries (Include MySQL & SQLite)
    - Database connection
    - Connection pool
    - Automatic reconnection
    - Table creation and alteration
    - Pre-made getters and setters to set and retrieve data to/from database
    - Custom queries
    - Easy method to close ResultSets, Statements and Connections
- [x] YAML library
    - Creation, reading and management of [YAML](https://yaml.org/) files
- [x] Colors
    - Translate a string with color codes (&a, &b, &c, ...) to a colored string
- [x] Support same NMS function
    - Title
    - ActionBar
    - Holograms
- [x] Custom Menu System
- [x] Communications
    - Communicate with other application using Sockets or Redis
- [x] More content will come later

## Requirements
- You need at least JDK8.

## Getting started
Add the dependency to your project using maven:
```xml
<dependency>
    <groupId>it.revarmygaming.ragcore</groupId>
    <artifactId>module-name</artifactId>
    <version>2.0</version>
</dependency>
```
- Modules are: common-api, spigot-api, bungeecord-api, velocity-api

You can also download the plugin from [here](https://repo1.maven.org/maven2/it/revarmygaming/ragcore/plugin/2.0/plugin-2.0.jar) and import it as a library.

## Contributing
To contribute to this repository just fork this repository make your changes or add your code and make a pull request.

## License
RAGCore is released under "The 3-Clause BSD License". You can find a copy [here](https://github.com/TheDarkSword/RAGCore/blob/master/LICENSE)
