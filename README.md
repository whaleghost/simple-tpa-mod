# Introduction

This mod allows you to teleport to your friends and vice versa with cheat mode disabled, while preserving the teleport functionality.

We introduce four commands to implement the teleport function:

- /tpa \<player\>: Sends a request to \<player\> to teleport to their location
- /tpahere \<player\>: Sends a request to \<player\> to teleport them to your location
- /tpaccept: Accepts the teleport request.
- /tpdeny: Denies the teleport request.

Each player can only have one pending request at a time. New requests will replace existing ones. Each request expires after one minute.

# Requirements

- Minecraft: 1.21.1
- Mod loader: NeoForge (any versions)

# Installation information


This repository is based on MDK-1.21.1-ModDevGradle and can be directly cloned to your device.
After cloning, simply open the repository in your preferred IDE. We recommend using either IntelliJ IDEA or Eclipse.
If you encounter missing libraries in your IDE or other issues, you can run `gradlew --refresh-dependencies` to refresh the local cache, or `gradlew clean` to reset everything (this does not affect your code) and restart the build process.