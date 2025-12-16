![Java](https://img.shields.io/badge/Java-21-blue?style=flat-square)
![Fabric](https://img.shields.io/badge/Fabric-1.21.4-lightgrey?style=flat-square)

## Stormterror

Stormterror is a modular utility client for **Minecraft 1.21.4** built on **Fabric**.  
Lightweight, stable, and Linux-friendly, with all rendering handled through native Minecraft contexts (no AWT).

## Features

### Render
- **Freecam**
- **Xray**
- **Fullbright**

### Movement
- **Speed**
- **AirJump**
- **NoFall**

### Interface
- **ClickGUI**
- **Property System**

## Installation

### Requirements
- Java 21
- Minecraft 1.21.4
- Fabric Loader (latest)
- Fabric API

### Steps
1. Download the latest release from **Releases** [Will provide later.. for now just build it yourself ong]
2. Place the `.jar` in `.minecraft/mods`
3. Launch using the Fabric profile

## Building

### Linux / macOS
```bash
git clone https://github.com/YourUsername/stormterror.git
cd stormterror
./gradlew build
```

### Windows
```powershell
git clone https://github.com/YourUsername/stormterror.git
cd stormterror
gradlew build
```

The compiled JAR will be located in `build/libs/`.

## Controls
- **Right Shift** — Open ClickGUI
- **Left Click** — Toggle module
- **Right Click (module)** — Open settings

## Disclaimer

This project is intended for educational use.  
Use on public servers may violate server rules. You are responsible for how you use it.
Stormterror is in ultra pro max early stages! I am working on it to make it best!

## License

MIT License. See the LICENSE file for details.
