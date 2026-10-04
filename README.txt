HINATA ARMOR - how to build

1. Install Java 21 (adoptium.net, pick "21 - LTS", Windows .msi, tick "Set JAVA_HOME" in the installer).
2. Open this folder in File Explorer, click the address bar at the top, type  cmd  and press Enter.
3. In the black window type:   gradlew build    and press Enter. Wait several minutes.
4. Your mod is in the build\libs folder: use the file named hinata_armor-1.0.0.jar (not the -sources one).
5. Put that jar + Fabric API in your .minecraft\mods folder.

Test with:  /give @s diamond_chestplate[custom_name='"Hinata"']
