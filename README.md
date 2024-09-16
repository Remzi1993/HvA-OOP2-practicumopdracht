# HvA Java practicumopdracht voor OOP2
Een JavaFX applicatie die een master detail applicatie implementeert. De applicatie is een lijst van personen met vliegtickets.

## Packaging
- Gebruik jlink om een runtime image te maken: `./mvnw clean javafx:jlink` of via IntelliJ Maven plugin.
- Gebruik Maven maven-assembly-plugin om een .jar met dependencies te maken: `./mvnw clean package` of via IntelliJ Maven plugin.
- Gebruik jpackage om een installer te maken: `jpackage --input target --name "Practicumopdracht OOP2" --main-class nl.hva.oop.practicumopdracht.Main --main-jar practicumo
  pdracht-2.0.0-jar-with-dependencies.jar --type msi --icon .\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico --win-menu --win-shortcut --win-per-user-install --
  win-shortcut-prompt --vendor "Remzi Cavdar" --win-menu-group "Remzi Cavdar" --copyright "Remzi Cavdar" --description "HvA Java practicumopdracht voor OOP2" --app-version "2.0.0" --win-help-url "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht" --runtime-image .\JRE\ --dest .\out`
