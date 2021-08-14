# Fireplace Lib
[![CurseForge](http://cf.way2muchnoise.eu/short_432845_downloads.svg)](https://minecraft.curseforge.com/projects/fireplace-lib)
[![Modrinth](https://img.shields.io/badge/dynamic/json?color=5da545&label=modrinth&prefix=downloads%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/ING0LAPF&style=plastic&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)](https://modrinth.com/mod/fireplace-lib)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.the-fireplace/Fireplace-Lib/badge.png)](https://maven-badges.herokuapp.com/maven-central/dev.the-fireplace/Fireplace-Lib)

A library of common functionality used by my mods, in one place for easier maintenance and faster updates.

Current functionality:

#### Chat
- Translation Service that allows client-side-optional mods to send messages and still allow players' locale settings to be used when possible.
- Buffering utility for multiline messages
- Synchronized message queueing to help avoid overlap when preparing multiline messages from multiple threads
- Text pagination utility for a standard paginated chat, complete with buttons to go to the next and previous pages.
- Library of common text styles

#### IO Utilities
- DirectoryResolver helps easily find commonly needed directories
- JarFileWalker has utilities for going through the contents of Jar files
- JsonFileReader turns a json file into a JsonObject.
- FilePathStorage allows storing keys and associated file paths
- (Client, 1.15+) FileDialogFactory to easily create file dialogs

#### Storage Utilities
- Config-based and Save-based data storage utilities designed so at some point support can be added for different storage types with no additional changes needed from mods that use it.
- Lazy config and threadsafe savable data system allows for easy creation of config files and save-specific data
- Save timer that allows setting a save function to run at specified intervals, and automatically runs it before the server stops running
- ReloadableManager allows reloading supported data from the files. LazyConfig is reloadable, so mods can easily implement commands to reload config without restarting the server.

#### Config
- (Client) ConfigScreenBuilder provides helper methods to reduce redundancy when making common Config GUI components, and allows setting up dependency between config options.

#### Multithreading
Concurrent Execution Manager provides a convenient way to run tasks on different threads and choose if the server should wait for them to finish when shutting down. Using this for nonessessential threads is still useful because it limits the number of threads running at once to prevent stack overflow errors.

#### Command Utilities
- Help Command builder for easier custom help commands, with support for sub-commands.
- Feedback Sender handles sending messages to various kinds of message targets.
- Requirements Helper provides a few basic types of requirements that are commonly given to commands.
- Argument Type Factory provides an extra argument type to be used by commands supporting offline players

#### Data Generator
Data Generator Factory provides methods to build an additive or destructive (normal) data generator. The additive generator won't clear the output directory before generating new data.

#### Networking
- Client and Server Packet Receiver helpers for registering slightly more easily

#### Misc
- EmptyUUID class to easily get the empty UUID and check if a UUID is empty.

## Usage
To use this with your mod, include the following in `build.gradle`:
```
repositories {
  maven {
		name 'Cloth Config'
		url 'https://maven.shedaniel.me/'
	}
	maven {
		name 'Mod Menu'
		url 'https://maven.terraformersmc.com/releases/'
	}
}
dependencies {
  modCompileOnly "dev.the-fireplace:Fireplace-Lib:${project.fireplacelib_version}:api"
  modRuntime "dev.the-fireplace:Fireplace-Lib:${project.fireplacelib_version}"
  // Only required if you're going to create your own Dependency Injections using @Implementation
  annotationProcessor "dev.the-fireplace:Annotated-DI:${project.annotateddi_version}:processor"
}
```
And in `gradle.properties`:
```
fireplacelib_version=<mod version>+<minecraft version>
# Only required if you're going to create your own Dependency Injections using @Implementation
annotateddi_version=<mod version>+<minecraft version>
```
