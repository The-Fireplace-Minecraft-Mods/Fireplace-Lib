# Fireplace-Lib
A library of common functionality used by my mods, in one place for easier maintenance and faster updates. Currently mainly focuses on multithreading, serialization to/from json, and client-side-optional translations.

Current functionality:

### Chat
- Translation Service that allows client-side-optional mods to send messages and still allow players' locale settings to be used when possible.
- Buffering utility for multiline messages
- Synchronized message queueing to help avoid overlap when preparing multiline messages from multiple threads
- Text pagination utility for a standard paginated chat, complete with buttons to go to the next and previous pages.

### IO Utilities
- Save timer that allows setting a save function to run at specified intervals, and automatically runs it before the server stops running
- Provides utilities for asynchronously serializing objects to json files.

### Multithreading
Concurrent Execution Manager provides a convenient way to run tasks on different threads and choose if the server should wait for them to finish when shutting down. Using this for nonessessential threads is still useful because it limits the number of threads running at once to prevent stack overflow errors.

### Misc
- EmptyUUID class to easily get the empty UUID and check if a UUID is empty.

### Usage
To use this with your mod, include the following in `build.gradle`:
```
dependencies {
  modImplementation "the_fireplace.lib:Fireplace-Lib:${project.fireplacelib_version}"
  //Optional, this uses Fabric's Jar-In-Jar to include this with your mod so users won't have to separately download it.
	include "the_fireplace.lib:Fireplace-Lib:${project.fireplacelib_version}"
}

repositories {
	maven {
		url  "https://dl.bintray.com/the-fireplace/mc-mods"
	}
}
```
And in `gradle.properties`:
```
fireplacelib_version=<minecraft version>-<mod version>
```
