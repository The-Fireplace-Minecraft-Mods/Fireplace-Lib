# Fireplace Lib
[![Curse Forge](http://cf.way2muchnoise.eu/short_432845_downloads.svg)](https://minecraft.curseforge.com/projects/fireplace-lib)

A library of common functionality used by my mods, in one place for easier maintenance and faster updates.

Current functionality:

#### Chat
- Translation Service that allows client-side-optional mods to send messages and still allow players' locale settings to be used when possible.
- Buffering utility for multiline messages
- Synchronized message queueing to help avoid overlap when preparing multiline messages from multiple threads
- Text pagination utility for a standard paginated chat, complete with buttons to go to the next and previous pages.

#### IO Utilities
- DirectoryResolver helps easily find commonly needed directories
- JarFileWalker has utilities for going through the contents of Jar files
- JsonFileReader turns a json file into a JsonObject.

#### Storage Utilities
- Config-based and Save-based data storage utilities designed so at some point support can be added for different storage types with no additional changes needed from mods that use it.
- Lazy config and threadsafe savable data system allows for easy creation of config files and save-specific data
- Save timer that allows setting a save function to run at specified intervals, and automatically runs it before the server stops running
- ReloadableManager allows reloading supported data from the files. LazyConfig is reloadable, so mods can easily implement commands to reload config without restarting the server.

#### Config
- LazyConfig class can be extended for an easy config system
- (Client) ConfigScreenBuilder provides helper methods to reduce redundancy when making common Config GUI components

#### Multithreading
Concurrent Execution Manager provides a convenient way to run tasks on different threads and choose if the server should wait for them to finish when shutting down. Using this for nonessessential threads is still useful because it limits the number of threads running at once to prevent stack overflow errors.

#### Command Utilities
- Help Command builder for easier custom help commands, with support for sub-commands.
- Feedback Sender handles sending messages to various kinds of message targets.
- Requirements Helper provides a few basic types of requirements that are commonly given to commands.

#### Data Generator
Data Generator Factory provides methods to build an additive or destructive (normal) data generator. The additive generator won't clear the output directory before generating new data.

#### Misc
- EmptyUUID class to easily get the empty UUID and check if a UUID is empty.

## Usage
To use this with your mod, include the following in `build.gradle`:
```
dependencies {
  modImplementation "dev.the-fireplace:Fireplace-Lib:${project.fireplacelib_version}"
}
```
And in `gradle.properties`:
```
fireplacelib_version=<mod version>+<minecraft version>
```
