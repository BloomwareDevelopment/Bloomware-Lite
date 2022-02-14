<h1 align="center">Bloomware</h1>
<div align="center">
  <img src="https://img.shields.io/github/contributors/TheBreakery/Bloomware" alt="GitHub contributors"/> <img src="https://img.shields.io/github/languages/code-size/TheBreakery/Bloomware" alt="GitHub code size in bytes"/> <img src="https://tokei.rs/b1/github/TheBreakery/Bloomware" alt="GitHub lines of code"/> <img src="https://img.shields.io/github/last-commit/TheBreakery/Bloomware" alt="GitHub last commit"/> <img src="https://img.shields.io/github/downloads/thebreakery/Bloomware/total?style=flat-square" alt="Total downloads"> <a href="https://www.codefactor.io/repository/github/thebreakery/bloomware"><img src="https://www.codefactor.io/repository/github/thebreakery/bloomware/badge" alt="CodeFactor" /></a>
</div>

# 🛠️ Bloomware
Bloomware is a paid utility mod for Minecraft 1.18+ versions
This is free version, feel free to contribute

We have a Discord server - [Discord](https://discord.gg/D4G7JN5d7m)

⚠️ Currently is in development, fixing bugs and other issues and adding new features. ⚠️

## How to install
1) Download latest mod .jar from repo's releases.
2) Drag it into `.minecraft/mods` directory.
3) Install [Fabric](https://fabricmc.net/).
4) Install [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files?sort=-name&__cf_chl_jschl_tk__=pmd_247af5374ad38c84fef2e144d9361c1f357f929b-1628948630-0-gqNtZGzNAk2jcnBszQdi) and drag it into `.minecraft/mods` directory.

## Prepare dev environment
This is required only for contributors, not for normal users.
### Using gradle tools
1) Clone the repo using `git clone`
2) Open command line (terminal if you're on linux);
3) Type `gradlew genSources` to command line;
4) To generate project for IntelliJ IDEA, run `gradlew idea`
5) To generate project for Eclipse, run `gradlew eclipse`
### Using GUI (IntelliJ IDEA only)
1) Clone the repo using `git clone`
2) Import Project -> Bloomware directory
3) Select the build.gradle in mod's directory file, and import the project
<br>Note: If gradle did it's tasks and project's configuration files aren't showing up, try to restart IDEA.
<br>Note: If the project's configuration files still don't show up, try reimporting the Gradle project.

## Developers
- [**OffeeX**](https://github.com/OffeeX) - Lead Dev
- [**DiOnFire**](https://github.com/DiOnFire) - Lead Dev
- [**Rikonardo**](https://github.com/Rikonardo) - Dev

## Contributors
- [**yoursleep**](https://github.com/fuckyouthinkimboogieman) (Changed GUI system)
- [**cattyn**](https://github.com/cattyngmd) (code improvements)
- [**srgantmoomoo**](https://github.com/srgantmoomoo) (client base [bedroom](https://github.com/beach-house-development/bedroom))
- [**BleachDrinker420**](https://github.com/BleachDrinker420) (some utils from [Bleachhack](https://github.com/BleachDrinker420/BleachHack))
- **ronmamo** (java lib - Reflections)
- **johnrengelman** (gradle plugin - ShadowJar)
- **Fabric Contributors** (for fabric)
- [**0x150**](https://github.com/0x150) (font renderer from [Atomic](https://github.com/0x151/Atomic)) 

## FAQ
- Default keybind for ClickGUI is Rshift
- Default prefix for commands is "$"
- Recommended version for Bloomware is 1.18 (FABRIC ONLY)
