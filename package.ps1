# PowerShell script for automating the HvA JavaFX project packaging process
# Save this as 'package.ps1' in the root directory of your project

# Step 1: Run the Maven wrapper script to ensure the correct version of Maven is used
Write-Host "Running Maven wrapper script..."
./mvnw clean install

# Step 2: Clean all directories (target, out, JRE) using Maven profile 'all'
Write-Host "Cleaning all directories with Maven..."
./mvnw clean -Pall

# Step 3: Create a runtime image using jlink via Maven
Write-Host "Creating runtime image with jlink..."
./mvnw clean javafx:jlink

# Step 4: Move JRE directory from target to project root if it exists
if (Test-Path "target/JRE") {
    Write-Host "Moving JRE directory to the root of the project..."
    if (Test-Path "JRE") { Remove-Item -Recurse -Force "JRE" }
    Move-Item -Path "target/JRE" -Destination "JRE"
} else {
    Write-Host "JRE directory not found, skipping move step."
}

# Step 5: Use Maven to package the project with dependencies
Write-Host "Packaging project into jar-with-dependencies.jar..."
./mvnw clean package

# Step 6: Delete everything in the target directory except the jar-with-dependencies.jar
Write-Host "Cleaning up target directory, keeping only jar-with-dependencies.jar..."
Get-ChildItem -Path "target" -Exclude "practicumopdracht-2.0.0-jar-with-dependencies.jar" | Remove-Item -Recurse -Force

# Step 7: Use jpackage to create an installer
Write-Host "Creating installer using jpackage..."
jpackage `
    --input target `
    --name "Practicumopdracht OOP2" `
    --main-class nl.hva.oop.practicumopdracht.Main `
    --main-jar practicumopdracht-2.0.0-jar-with-dependencies.jar `
    --type msi `
    --icon .\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico `
    --win-menu `
    --win-shortcut `
    --win-shortcut-prompt `
    --win-per-user-install `
    --vendor "Remzi Cavdar" `
    --win-menu-group "Remzi Cavdar" `
    --copyright "Remzi Cavdar" `
    --description "HvA Java practicumopdracht voor OOP2" `
    --app-version "2.0.0" `
    --win-help-url "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht" `
    --runtime-image .\JRE\ `
    --dest .\out

Write-Host "Installer created and saved in the 'out' directory."