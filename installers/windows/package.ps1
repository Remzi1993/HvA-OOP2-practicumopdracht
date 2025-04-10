# PowerShell script for automating the HvA JavaFX project packaging process.
# and generating an Inno Setup installer with the dynamic version from pom.xml.

# Extract version from pom.xml
[xml]$pomXml = Get-Content "..\..\pom.xml"
$appVersion = $pomXml.project.version

Write-Host "Building application version: $appVersion"

# Create package directory if it doesn't exist
$packageDir = ".\package"
$outDir = ".\out"

# Remove existing out and package directories
if (Test-Path $packageDir) {
    Remove-Item -Recurse -Force $packageDir
    Write-Host "Removed existing package directory."
}

if (Test-Path $outDir) {
    Remove-Item -Recurse -Force $outDir
    Write-Host "Removed existing out directory."
}

# Recreate the package directory
New-Item -ItemType Directory -Path $packageDir -Force | Out-Null
Write-Host "Created package directory for output files."

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
--copyright "MIT license - Remzi Cavdar - ict@remzi.info" `
--description "HvA OOP2 practicumopdracht JavaFX app" `
--app-version $appVersion `
--runtime-image JRE `
--dest installers\windows\package `
--java-options "--enable-native-access=javafx.graphics" `
--java-options "--add-opens=java.base/java.lang=ALL-UNNAMED"
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

# -------------------------------
# Generate the Inno Setup Script
# -------------------------------
$innoSetupScript = @"
; windows-installer.iss
; Inno Setup Script for HvA OOP2 practicumopdracht
; Created by Remzi Cavdar

#define MyAppName "HvA OOP2 practicumopdracht"
#define MyAppVersion "$appVersion"
#define MyAppFileVersion "$appVersion.0"
#define MyAppCopyright "MIT license - Remzi Cavdar - ict@remzi.info"
#define MyAppPublisher "Remzi Cavdar"
#define MyAppURL "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht"
#define MyAppExeName "HvA OOP2 practicumopdracht.exe"
#define MyAppUpdatesURL "https://github.com/Remzi1993/HvA-OOP2-practicumopdracht/releases"
#define MyAppGUID "5b9f447c-baa5-4751-b7c7-2667a78f63ea"

[Setup]
AppId={{5b9f447c-baa5-4751-b7c7-2667a78f63ea}}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
VersionInfoVersion={#MyAppFileVersion}
VersionInfoDescription={#MyAppName} setup
AppCopyright={#MyAppCopyright}
VersionInfoCopyright={#MyAppCopyright}
AppVerName={#MyAppName}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppUpdatesURL}

; Force 64-bit installation directory
DefaultDirName={autopf64}\{#MyAppPublisher}\{#MyAppName}
; Keep publisher's name for the Program Group
DefaultGroupName={#MyAppPublisher}

; Skip directory selection
DisableDirPage=yes
; Skip program group page
DisableProgramGroupPage=yes
; Skip Ready to Install page
DisableReadyPage=yes
; Show a Finished page
DisableFinishedPage=no

LicenseFile=..\..\LICENSE
OutputDir=out
OutputBaseFilename={#MyAppName}-{#MyAppVersion}-setup
SetupIconFile=..\..\src\main\resources\nl\hva\oop\practicumopdracht\images\icon.ico

Compression=lzma
SolidCompression=yes
UninstallDisplayIcon={app}\{#MyAppExeName}
WizardStyle=modern

; Require admin privileges (machine-wide installation)
PrivilegesRequired=admin

; Show language selection dialog only on first install
ShowLanguageDialog=yes

; Only allow on 64-bit Windows, force 64-bit mode
ArchitecturesAllowed=x64os
ArchitecturesInstallIn64BitMode=x64os

; Minimum OS version (Windows 10)
MinVersion=10.0

; Use the previous language automatically during upgrades
UsePreviousLanguage=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "dutch";  MessagesFile: "compiler:Languages\Dutch.isl"

[CustomMessages]
english.LaunchApp=Launch HvA OOP2 practicumopdracht
english.UpgradeDetected=An existing installation of HvA OOP2 practicumopdracht was detected. It will be upgraded without loss of your data.
english.OSVersionError=This application requires Windows 10 (64-bit) or later.
english.DowngradeError=A newer version of HvA OOP2 practicumopdracht is already installed. Downgrading is not supported.

dutch.LaunchApp=Start HvA OOP2 practicumopdracht
dutch.UpgradeDetected=Er is een bestaande installatie van HvA OOP2 practicumopdracht gedetecteerd. Deze zal worden bijgewerkt zonder verlies van uw gegevens.
dutch.OSVersionError=Deze applicatie vereist Windows 10 (64-bit) of hoger.
dutch.DowngradeError=Er is al een nieuwere versie van HvA OOP2 practicumopdracht geïnstalleerd. Downgraden wordt niet ondersteund.

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
; Copy the entire app directory
Source: ".\package\{#MyAppName}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
; Shortcut directly in "C:\ProgramData\Microsoft\Windows\Start Menu\Programs"
Name: "{commonprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"

; Optional Desktop shortcut (if user selects "desktopicon")
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: "desktopicon"

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchApp}"; Flags: nowait postinstall skipifsilent

[UninstallDelete]
Type: filesandordirs; Name: "{app}"

[Code]
const
  ApplicationDataDirectory = '{userappdata}\Remzi Cavdar\HvA OOP2 practicumopdracht';

function IsOperatingSystemWindows10OrLater(): Boolean;
var
  OperatingSystemVersion: TWindowsVersion;
begin
  GetWindowsVersionEx(OperatingSystemVersion);
  Result := (OperatingSystemVersion.Major >= 10);
end;

function IsOperatingSystem64Bit(): Boolean;
begin
  Result := IsWin64;
end;

function GetPreviousUninstallCommand(): String;
var
  UninstallRegistryPath: String;
  UninstallCommandLine: String;
begin
  UninstallRegistryPath := 'Software\Microsoft\Windows\CurrentVersion\Uninstall\{5b9f447c-baa5-4751-b7c7-2667a78f63ea}_is1';
  UninstallCommandLine := '';

  if not RegQueryStringValue(HKLM, UninstallRegistryPath, 'UninstallString', UninstallCommandLine) then
    RegQueryStringValue(HKCU, UninstallRegistryPath, 'UninstallString', UninstallCommandLine);

  Result := UninstallCommandLine;
end;

function GetCurrentlyInstalledVersion(): String;
var
  UninstallRegistryPath: String;
  InstalledDisplayVersion: String;
begin
  UninstallRegistryPath := 'Software\Microsoft\Windows\CurrentVersion\Uninstall\{5b9f447c-baa5-4751-b7c7-2667a78f63ea}_is1';
  InstalledDisplayVersion := '';

  if not RegQueryStringValue(HKLM, UninstallRegistryPath, 'DisplayVersion', InstalledDisplayVersion) then
    RegQueryStringValue(HKCU, UninstallRegistryPath, 'DisplayVersion', InstalledDisplayVersion);

  Result := InstalledDisplayVersion;
end;

function IsExistingInstallationUpgrade(): Boolean;
begin
  Result := (GetPreviousUninstallCommand() <> '');
end;

function CompareTwoVersionStrings(Version1, Version2: String): Integer;
var
  Version1Part, Version2Part: String;
  Version1Position, Version2Position: Integer;
  Version1DotPosition, Version2DotPosition: Integer;
  Number1, Number2: Integer;
begin
  Result := 0;
  if Version1 = Version2 then Exit;

  Version1Position := 1;
  Version2Position := 1;

  while (Version1Position <= Length(Version1)) and (Version2Position <= Length(Version2)) do
  begin
    Version1DotPosition := Pos('.', Copy(Version1, Version1Position, Length(Version1)));
    if Version1DotPosition > 0 then
      Version1DotPosition := Version1DotPosition + Version1Position - 1
    else
      Version1DotPosition := Length(Version1) + 1;

    Version2DotPosition := Pos('.', Copy(Version2, Version2Position, Length(Version2)));
    if Version2DotPosition > 0 then
      Version2DotPosition := Version2DotPosition + Version2Position - 1
    else
      Version2DotPosition := Length(Version2) + 1;

    Version1Part := Copy(Version1, Version1Position, Version1DotPosition - Version1Position);
    Version2Part := Copy(Version2, Version2Position, Version2DotPosition - Version2Position);

    Number1 := StrToIntDef(Version1Part, 0);
    Number2 := StrToIntDef(Version2Part, 0);

    if Number1 > Number2 then
    begin
      Result := 1;
      Exit;
    end
    else if Number1 < Number2 then
    begin
      Result := -1;
      Exit;
    end;

    Version1Position := Version1DotPosition + 1;
    Version2Position := Version2DotPosition + 1;
  end;

  if Version1Position <= Length(Version1) then
    Result := 1
  else if Version2Position <= Length(Version2) then
    Result := -1;
end;

function IsCurrentInstallDowngrade(): Boolean;
var
  InstalledVersion: String;
  CompareResult: Integer;
begin
  Result := False;
  InstalledVersion := GetCurrentlyInstalledVersion();
  if InstalledVersion <> '' then
  begin
    CompareResult := CompareTwoVersionStrings('{#MyAppVersion}', InstalledVersion);
    Result := (CompareResult < 0);
  end;
end;

procedure BackupApplicationData();
var
  BackupDirectory: String;
  ReturnCode: Integer;
begin
  if DirExists(ExpandConstant(ApplicationDataDirectory)) then
  begin
    BackupDirectory := ExpandConstant('{tmp}\AppDataBackup');
    if not DirExists(BackupDirectory) then
      CreateDir(BackupDirectory);

    Exec('cmd.exe',
         '/c xcopy "' + ExpandConstant(ApplicationDataDirectory) + '" "' + BackupDirectory + '" /E /I /H /Y',
         '',
         SW_HIDE,
         ewWaitUntilTerminated,
         ReturnCode);
  end;
end;

procedure RestoreApplicationData();
var
  BackupDirectory: String;
  ReturnCode: Integer;
begin
  BackupDirectory := ExpandConstant('{tmp}\AppDataBackup');
  if DirExists(BackupDirectory) then
  begin
    if not DirExists(ExpandConstant(ApplicationDataDirectory)) then
      ForceDirectories(ExpandConstant(ApplicationDataDirectory));

    Exec('cmd.exe',
         '/c xcopy "' + BackupDirectory + '" "' + ExpandConstant(ApplicationDataDirectory) + '" /E /I /H /Y',
         '',
         SW_HIDE,
         ewWaitUntilTerminated,
         ReturnCode);
  end;
end;

function InitializeSetup(): Boolean;
var
  UninstallerReturnCode: Integer;
  UninstallCommandLine: String;
begin
  Result := True;

  if not (IsOperatingSystemWindows10OrLater() and IsOperatingSystem64Bit()) then
  begin
    MsgBox(ExpandConstant('{cm:OSVersionError}'), mbError, MB_OK);
    Result := False;
    Exit;
  end;

  if IsExistingInstallationUpgrade() and IsCurrentInstallDowngrade() then
  begin
    MsgBox(ExpandConstant('{cm:DowngradeError}'), mbError, MB_OK);
    Result := False;
    Exit;
  end;

  if IsExistingInstallationUpgrade() then
  begin
    MsgBox(ExpandConstant('{cm:UpgradeDetected}'), mbInformation, MB_OK);

    BackupApplicationData();

    UninstallCommandLine := GetPreviousUninstallCommand();
    UninstallCommandLine := RemoveQuotes(UninstallCommandLine);
    Exec(UninstallCommandLine,
         '/SILENT /NORESTART /SUPPRESSMSGBOXES',
         '',
         SW_HIDE,
         ewWaitUntilTerminated,
         UninstallerReturnCode);
  end;
end;

function ShouldSkipPage(PageID: Integer): Boolean;
var
  PreviousDesktopIconSetting: String;
  RegistryPathForInstaller: String;
begin
  Result := False;

  if IsExistingInstallationUpgrade() then
  begin
    if PageID in [wpLicense, wpSelectDir, wpSelectComponents, wpSelectProgramGroup, wpInfoBefore] then
      Result := True;

    if PageID = wpSelectTasks then
    begin
      RegistryPathForInstaller := ExpandConstant('Software\{#MyAppPublisher}\{#MyAppName}');
      if RegQueryStringValue(HKLM, RegistryPathForInstaller, 'Installer_DesktopIcon', PreviousDesktopIconSetting) then
      begin
        if PreviousDesktopIconSetting = '1' then
          WizardSelectTasks('desktopicon');
      end;
      Result := True;
    end;
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  RegistryPathForInstaller: String;
begin
  if CurStep = ssPostInstall then
  begin
    RegistryPathForInstaller := ExpandConstant('Software\{#MyAppPublisher}\{#MyAppName}');

    if ActiveLanguage = 'dutch' then
      RegWriteStringValue(HKLM, RegistryPathForInstaller, 'Installer_Language', 'dutch')
    else
      RegWriteStringValue(HKLM, RegistryPathForInstaller, 'Installer_Language', 'english');

    if WizardIsTaskSelected('desktopicon') then
      RegWriteStringValue(HKLM, RegistryPathForInstaller, 'Installer_DesktopIcon', '1')
    else
      RegWriteStringValue(HKLM, RegistryPathForInstaller, 'Installer_DesktopIcon', '0');

    if IsExistingInstallationUpgrade() then
      RestoreApplicationData();
  end;
end;

{------------------------------------------------------
  Extra registry deletion for leftover keys on uninstall
-------------------------------------------------------}
procedure CurUninstallStepChanged(CurrentUninstallStep: TUninstallStep);
begin
  if CurrentUninstallStep = usUninstall then
  begin
    if not IsExistingInstallationUpgrade() and DirExists(ExpandConstant(ApplicationDataDirectory)) then
      DelTree(ExpandConstant(ApplicationDataDirectory), True, True, True);

    RegDeleteValue(
      HKCU,
      'Software\Microsoft\Windows NT\CurrentVersion\AppCompatFlags\Compatibility Assistant\Store',
      'C:\Program Files\Remzi Cavdar\HvA OOP2 practicumopdracht\HvA OOP2 practicumopdracht.exe'
    );

    RegDeleteValue(
      HKCU,
      'Software\Microsoft\Windows NT\CurrentVersion\AppCompatFlags\Compatibility Assistant\Store',
      'C:\Program Files\Remzi Cavdar\HvA OOP2 practicumopdracht\unins000.exe'
    );

    RegDeleteKeyIncludingSubkeys(HKCU, 'Software\ChangeTracker');
  end;
end;
"@

# Finally, write the Inno Setup script to a file
$innoSetupScript | Out-File -FilePath ".\windows-installer.iss" -Encoding utf8
Write-Host "Generated updated Inno Setup script with version $appVersion"