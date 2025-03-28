# PowerShell script for automating the HvA JavaFX project packaging process.

# Extract version from pom.xml
[xml]$pomXml = Get-Content "..\..\pom.xml"
$appVersion = $pomXml.project.version

Write-Host "Building application version: $appVersion"

# Create a version-specific Inno Setup script by replacing the version in the template
$innoSetupScript = @"
; windows-installer.iss
; Inno Setup Script for HvA OOP2 practicumopdracht
; Created by Remzi Cavdar

#define MyAppName "HvA OOP2 practicumopdracht"
#define MyAppVersion "$appVersion"
#define MyAppPublisher "Remzi Cavdar"
#define MyAppURL "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht"
#define MyAppExeName "HvA OOP2 practicumopdracht.exe"
#define MyAppUpdatesURL "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht/releases"
#define MyAppGUID "5b9f447c-baa5-4751-b7c7-2667a78f63ea"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
AppId={{{#MyAppGUID}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppUpdatesURL}
; Force 64-bit installation directory
DefaultDirName={autopf64}\{#MyAppPublisher}\{#MyAppName}
DefaultGroupName={#MyAppPublisher}
; Skip directory selection
DisableDirPage=yes
; Skip program group page
DisableProgramGroupPage=yes
; Skip the Ready to Install page (confirmation)
DisableReadyPage=yes
; Keep the finished page (with launch option)
DisableFinishedPage=no
LicenseFile=..\..\LICENSE
OutputDir=out
OutputBaseFilename={#MyAppName}-{#MyAppVersion}-setup
SetupIconFile=..\..\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico
Compression=lzma
SolidCompression=yes
UninstallDisplayIcon={app}\{#MyAppExeName}
WizardStyle=modern
; Require administrator privileges (machine-wide installation)
PrivilegesRequired=admin
; Allow user to select language
ShowLanguageDialog=yes
; Only allow installation on 64-bit Windows
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64
; Minimum OS version (Windows 10)
MinVersion=10.0

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "dutch"; MessagesFile: "compiler:Languages\Dutch.isl"

[CustomMessages]
english.LaunchApp=Launch HvA OOP2 practicumopdracht
english.UpgradeDetected=An existing installation of HvA OOP2 practicumopdracht was detected. It will be upgraded without loss of your data.
english.OSVersionError=This application requires Windows 10 or later.
dutch.LaunchApp=Start HvA OOP2 practicumopdracht
dutch.UpgradeDetected=Er is een bestaande installatie van HvA OOP2 practicumopdracht gedetecteerd. Deze zal worden bijgewerkt zonder verlies van uw gegevens.
dutch.OSVersionError=Deze applicatie vereist Windows 10 of hoger.

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
; Copy the entire app directory
Source: ".\package\{#MyAppName}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
; Create a direct shortcut in the Start Menu Programs folder
Name: "{commonprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
; Desktop shortcut (still optional)
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchApp}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
Type: filesandordirs; Name: "{app}"

[Code]
const
  AppDataDir = '{userappdata}\Remzi Cavdar\HvA OOP2 practicumopdracht';

// Function to check if a previous installation exists
function GetUninstallString(): String;
var
  sUnInstPath: String;
  sUnInstallString: String;
begin
  sUnInstPath := ExpandConstant('Software\Microsoft\Windows\CurrentVersion\Uninstall\{#emit SetupSetting("AppId")}_is1');
  sUnInstallString := '';
  if not RegQueryStringValue(HKLM, sUnInstPath, 'UninstallString', sUnInstallString) then
    RegQueryStringValue(HKCU, sUnInstPath, 'UninstallString', sUnInstallString);
  Result := sUnInstallString;
end;

// Function to determine if this is an upgrade
function IsUpgrade(): Boolean;
begin
  Result := (GetUninstallString() <> '');
end;

// Function to back up user data
procedure BackupUserData();
var
  BackupDir: String;
  ResultCode: Integer;
begin
  if DirExists(ExpandConstant(AppDataDir)) then
  begin
    // Create a temporary backup directory
    BackupDir := ExpandConstant('{tmp}\AppDataBackup');
    if not DirExists(BackupDir) then
      CreateDir(BackupDir);

    // Use xcopy to copy the app data directory to the backup
    Exec('cmd.exe', '/c xcopy "' + ExpandConstant(AppDataDir) + '" "' + BackupDir + '" /E /I /H /Y',
          '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
  end;
end;

// Function to restore user data
procedure RestoreUserData();
var
  BackupDir: String;
  ResultCode: Integer;
begin
  BackupDir := ExpandConstant('{tmp}\AppDataBackup');
  if DirExists(BackupDir) then
  begin
    // Ensure target directory exists
    if not DirExists(ExpandConstant(AppDataDir)) then
      ForceDirectories(ExpandConstant(AppDataDir));

    // Use xcopy to restore the backup to the app data directory
    Exec('cmd.exe', '/c xcopy "' + BackupDir + '" "' + ExpandConstant(AppDataDir) + '" /E /I /H /Y',
          '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
  end;
end;

// Called at the beginning of setup
function InitializeSetup(): Boolean;
var
  iResultCode: Integer;
  sUnInstallString: String;
begin
  Result := True;

  if IsUpgrade() then
  begin
    // Inform the user that an upgrade is happening
    MsgBox(ExpandConstant('{cm:UpgradeDetected}'), mbInformation, MB_OK);

    // Backup user data before uninstalling
    BackupUserData();

    // Uninstall previous version
    sUnInstallString := GetUninstallString();
    sUnInstallString := RemoveQuotes(sUnInstallString);

    // Execute the uninstaller silently
    Exec(sUnInstallString, '/SILENT /NORESTART /SUPPRESSMSGBOXES', '', SW_HIDE, ewWaitUntilTerminated, iResultCode);
  end;
end;

// Called at the end of the installation
procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    if IsUpgrade() then
      RestoreUserData();
  end;
end;

// Called when the uninstaller runs
procedure CurUninstallStepChanged(CurUninstallStep: TUninstallStep);
begin
  // If this is a regular uninstall (not an upgrade), remove app data
  if CurUninstallStep = usUninstall then
  begin
    if DirExists(ExpandConstant(AppDataDir)) then
      DelTree(ExpandConstant(AppDataDir), True, True, True);
  end;
end;
"@

# Write the Inno Setup script
$innoSetupScript | Out-File -FilePath ".\windows-installer.iss" -Encoding utf8
Write-Host "Generated Inno Setup script with version $appVersion"

# Create package directory if it doesn't exist
$packageDir = ".\package"
if (!(Test-Path $packageDir)) {
    New-Item -ItemType Directory -Path $packageDir -Force | Out-Null
    Write-Host "Created package directory for output files."
}

# Save current directory
$currentDir = Get-Location

# Step 1: Clean all directories (target, out, JRE) using Maven profile 'all'
Write-Host "Cleaning all directories with Maven..."
Set-Location ..\..\
& ./mvnw clean -Pall
Set-Location $currentDir

# Step 2: Create a runtime image using jlink via Maven
Write-Host "Creating runtime image with jlink..."
Set-Location ..\..\
& ./mvnw clean javafx:jlink
Set-Location $currentDir

# Step 3: Move JRE directory from target to project root if it exists
if (Test-Path "..\..\target\JRE")
{
    Write-Host "Moving JRE directory to the root of the project..."
    if (Test-Path "..\..\JRE")
    {
        Remove-Item -Recurse -Force "..\..\JRE"
    }
    Move-Item -Path "..\..\target\JRE" -Destination "..\..\JRE"
}
else
{
    Write-Host "JRE directory not found, skipping move step."
}

# Step 4: Use Maven to package the project with dependencies
Write-Host "Packaging project into jar-with-dependencies.jar..."
Set-Location ..\..\
& ./mvnw clean package
Set-Location $currentDir

# Step 5: Delete everything in the target directory except the jar-with-dependencies.jar
Write-Host "Cleaning up target directory, keeping only jar-with-dependencies.jar..."
Get-ChildItem -Path "..\..\target" -Exclude "practicumopdracht-$appVersion-jar-with-dependencies.jar" | Remove-Item -Recurse -Force

# Step 6: Create a clean directory for the jar file (instead of using target directly)
Write-Host "Creating clean input directory..."
$inputDir = "..\..\input-dir"
if (Test-Path $inputDir) {
    Remove-Item -Recurse -Force $inputDir
}
New-Item -ItemType Directory -Path $inputDir | Out-Null

# Step 7: Copy only the necessary jar to the input directory
Copy-Item "..\..\target\practicumopdracht-$appVersion-jar-with-dependencies.jar" -Destination $inputDir

# Step 8: Create an application image using jpackage
Write-Host "Creating app image using jpackage..."
Set-Location ..\..\
jpackage `
--type app-image `
--input input-dir `
--name "HvA OOP2 practicumopdracht" `
--main-class nl.hva.oop.practicumopdracht.Main `
--main-jar practicumopdracht-$appVersion-jar-with-dependencies.jar `
--icon src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico `
--vendor "Remzi Cavdar" `
--copyright "Remzi Cavdar - MIT license" `
--description "HvA OOP2 practicumopdracht JavaFX app" `
--app-version $appVersion `
--runtime-image JRE `
--dest installers\windows\package
Set-Location $currentDir

# Step 9: Clean up temporary input directory
Remove-Item -Recurse -Force $inputDir

Write-Host "App image created and saved in the 'package' directory."

# Step 10: Remove the target and JRE directories
Write-Host "Removing target and JRE directories..."

if (Test-Path "..\..\target")
{
    Remove-Item -Recurse -Force "..\..\target"
    Write-Host "target directory removed."
}
else
{
    Write-Host "target directory not found."
}

if (Test-Path "..\..\JRE")
{
    Remove-Item -Recurse -Force "..\..\JRE"
    Write-Host "JRE directory removed."
}
else
{
    Write-Host "JRE directory not found."
}

Write-Host "Cleanup complete."