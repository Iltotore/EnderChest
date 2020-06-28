# EnderChest 
![JOIN ECS](https://discordapp.com/api/guilds/718109282406498415/embed.png?style=shield) 
![Downloads](https://img.shields.io/github/downloads/Iltotore/EnderChest/total?label=Server%20downloads&style=flat-square)
![Maven](https://img.shields.io/maven-central/v/io.github.iltotore/ec-client_2.13?color=blue&style=flat-square)

# Introduction
When Minecraft developers create a launcher, they often use the S-Update system. 
Unfortunately, this system is outdated and unmaintained.

While discussing with Litarvan about it,
I decided to create this system written in Scala.

**I highly recommend to use Scala, but you can use any JVM Language like Java or Kotlin.**

# Features
## Fast
This system uses the xxHash32 algorithm to generate checksums.
This operation is ~16.36x faster than md5.

## Concurrent and lightweight
EnderChest uses Scala's futures and akka streams to asynchronously process data.
The EnderChest's parallel system make data processing lightweight and reduce the memory footprint.

# Install
EnderChest (server & client) only requires Java 8+ to be installed.

## Server
Download the `enderchest-server-xxxx.jar`in the release section
You now just need to run the server in an empty directory with permissions `rwx` using `java -jar`

Start the server for the first time will generate the config.yml file and the `files` directory.
Now, you can put your files in the `files` directory, then reload/restart the server.

## Client
You just need to import the client library.

### Using a build tool
I highly recommend developers to use a dependency management system like Gradle or SBT.

<details>
<summary>Using Gradle</summary>

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'io.github.iltotore:ec-client_2.13:version'
}
```
</details>

<details>
<summary>Using SBT</summary>

```sbt
libraryDependencies += "io.github.iltotore" %% "ec-client_2.13" % "version"
```
</details>

### Using the local file
You can download the `client-xxx-withDependencies.jar` file in the releases tab, then simply add it to your IDE.
Please not you must have the dependency in your classpath or directly in your archive.



**You can now use EnderChest's client! See the wiki page for more informations**

# Support
## Issues
If you experience a bug/issue using EnderChest, you can create a new github issue.

## Useful links

- [Client - Getting started](https://github.com/Iltotore/EnderChest/wiki/Client-Getting-started)
- [Server - Getting started](https://github.com/Iltotore/EnderChest/wiki/Server-Getting-started)
- Join us on Discord:

[![JOIN ECS](https://discordapp.com/api/guilds/718109282406498415/embed.png?style=banner3)](https://discord.gg/zX3A8Nb)
