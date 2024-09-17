# HvA Java practicumopdracht voor OOP2
Dit project is een JavaFX applicatie die een master-detail functionaliteit implementeert. 
De applicatie toont een lijst van personen met vliegtickets.

## Packaging voor Windows
Gebruik het PowerShell-script `.\package.ps1` om automatisch een installer voor Windows te genereren. 
Het script voert verschillende stappen uit, waaronder het bouwen van een runtime image met jlink en het maken van een installer met jpackage.

### Het script voert de volgende stappen uit:
1. Run de Maven wrapper script om ervoor te zorgen dat de juiste versie van Maven wordt gebruikt.
2. Het script begint met het verwijderen van de mappen `target`, `out`, en `JRE` via Maven: `./mvnw clean -Pall`
3. Vervolgens wordt een runtime image gegenereerd met jlink via Maven: `./mvnw clean javafx:jlink`
4. Na het genereren van het runtime image wordt de map `JRE` vanuit de `target` directory verplaatst naar de hoofdmap van het project.
5. Met Maven wordt een .jar bestand inclusief dependencies aangemaakt: `./mvnw clean package`
6. Alles in de `target` directory wordt verwijderd behalve de `jar-with-dependencies.jar` via Maven: `./mvnw clean`
7. Als laatste wordt jpackage gebruikt om een Windows MSI installer te maken. Het commando dat hiervoor wordt uitgevoerd: `jpackage --input target --name "Practicumopdracht OOP2" --main-class nl.hva.oop.practicumopdracht.Main --main-jar practicumopdracht-2.0.0-jar-with-dependencies.jar --type msi --icon .\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico --win-menu --win-shortcut --win-per-user-install --win-shortcut-prompt --vendor "Remzi Cavdar" --win-menu-group "Remzi Cavdar" --copyright "Remzi Cavdar" --description "HvA Java practicumopdracht voor OOP2" --app-version "2.0.0" --win-help-url "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht" --runtime-image .\JRE\ --dest .\out`
8. De installer staat in de `out` directory.

## Testen van de installer
Ik raad Windows Sandbox aan om de installer te testen.
- Deze feature is beschikbaar op Windows 10 of 11 (alle edities behalve Home).
- Schakel Windows Sandbox in via Windows-onderdelen.

Als je Windows Sandbox niet hebt of niet wilt gebruiken kan je de installer ook testen op een virtuele machine of op je eigen computer.