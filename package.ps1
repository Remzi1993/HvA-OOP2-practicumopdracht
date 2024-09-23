# PowerShell script for automating the HvA JavaFX project packaging process.

# Define the app version as a variable
$appVersion = "2.0.3"
$appWebsite = "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht"
$appReleases = "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht/releases"

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
if (Test-Path "target/JRE")
{
    Write-Host "Moving JRE directory to the root of the project..."
    if (Test-Path "JRE")
    {
        Remove-Item -Recurse -Force "JRE"
    }
    Move-Item -Path "target/JRE" -Destination "JRE"
}
else
{
    Write-Host "JRE directory not found, skipping move step."
}

# Step 5: Use Maven to package the project with dependencies
Write-Host "Packaging project into jar-with-dependencies.jar..."
./mvnw clean package

# Step 6: Delete everything in the target directory except the jar-with-dependencies.jar
Write-Host "Cleaning up target directory, keeping only jar-with-dependencies.jar..."
Get-ChildItem -Path "target" -Exclude "practicumopdracht-$appVersion-jar-with-dependencies.jar" | Remove-Item -Recurse -Force

# Step 7: Use jpackage to create an installer
Write-Host "Creating installer using jpackage..."
jpackage `
    --input target `
    --name "HvA OOP2 practicumopdracht" `
    --main-class nl.hva.oop.practicumopdracht.Main `
    --main-jar practicumopdracht-$appVersion-jar-with-dependencies.jar `
    --type msi `
    --icon .\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico `
    --win-menu `
    --win-shortcut `
    --win-shortcut-prompt `
    --vendor "Remzi Cavdar" `
    --win-menu-group "Remzi Cavdar" `
    --copyright "Remzi Cavdar - MIT license" `
    --description "HvA OOP2 practicumopdracht JavaFX app" `
    --app-version $appVersion `
    --win-help-url $appWebsite `
    --win-update-url $appReleases `
    --runtime-image .\JRE\ `
    --install-dir "Remzi Cavdar\HvA OOP2 practicumopdracht" `
    --dest .\out

Write-Host "Installer created and saved in the 'out' directory."

# Step 8: Remove the target and JRE directories
Write-Host "Removing target and JRE directories..."

if (Test-Path "target")
{
    Remove-Item -Recurse -Force "target"
    Write-Host "target directory removed."
}
else
{
    Write-Host "target directory not found."
}

if (Test-Path "JRE")
{
    Remove-Item -Recurse -Force "JRE"
    Write-Host "JRE directory removed."
}
else
{
    Write-Host "JRE directory not found."
}

Write-Host "Cleanup complete."