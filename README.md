# HvA Java Practical Assignment for OOP2
This project is a JavaFX application that implements a master-detail functionality.
The application displays a list of people with flight tickets.
The previous version did not use Java Modules.
This version uses recent Java standards and utilizes Java Modules, jlink, and jpackage to generate a runtime image and an installer.

## Demo
![Screenshot 2024-09-23 112638](https://github.com/user-attachments/assets/67478baa-f9e1-4102-8ad7-5709bef4aabb)
![Screenshot 2024-09-23 112658](https://github.com/user-attachments/assets/cd77e584-b449-4f88-b240-8baf2443adec)
![Screenshot 2024-09-23 112735](https://github.com/user-attachments/assets/bc04f7d2-7c35-4018-8fa9-d42978d2f38c)

## Packaging for Windows
Use the PowerShell script `.\package.ps1` to automatically generate an installer for Windows.
The script performs several steps, including building a runtime image with jlink and creating an application image with jpackage.

### Version Management
To change the app version, update the version in the following file:
- `pom.xml`

### Packaging Process
The script performs the following key steps:
1. **Read and set version** from `pom.xml` (stores it in `$appVersion`).
2. **Clean existing build artifacts** by removing `package`, `out`, and running `mvnw clean -Pall`.
3. **Create a custom Java runtime** using `mvnw javafx:jlink` (places a JRE in `target\JRE`).
4. **Move the JRE** to the project root (`..\..\JRE`) if it exists.
5. **Build the “fat” JAR** using `mvnw clean package`, then **keep only** the `jar-with-dependencies`.
6. **Create a fresh `input-dir`** and copy the fat JAR into it.
7. **Use `jpackage`** to build an application image in `.\package`.
8. **Clean up** by deleting temporary directories (`input-dir`, `target`, `JRE`, etc.).
9. **Generate** a versioned **Inno Setup script** (`windows-installer.iss`) referencing `$appVersion`.

## Testing the Installer
There are several recommended methods for testing the installer:

1. **Virtual Machine**:
    - Use virtualization software like VirtualBox, VMware, or Hyper-V
    - Create a clean Windows 10 or 11 test environment
    - Install the application and verify functionality

2. **Separate User Account**:
    - Create a new local user account on your Windows machine
    - Install the application in this separate account
    - Test the full installation and application functionality

3. **Staging Computer**:
    - Use a dedicated test computer or a spare machine
    - Perform a clean installation and thorough testing

### Testing Best Practices
- Verify the installer runs without errors
- Check that the application launches correctly
- Test all main features of the application
- Confirm that user data is preserved between updates

Note: In the `data` folder, you'll find dummy data for the app (for each data type). When testing, you can copy this data to the `%APPDATA%\Remzi Cavdar\HvA OOP2 practicumopdracht` directory to simulate a real-world scenario.

## System Requirements

### User Requirements
- Operating System: Windows 10 or higher (64-bit only)

### Development Requirements
- Operating System: Windows 10 or later (64-bit)
- Java Development Kit (JDK) with JavaFX support
- Maven
- Inno Setup (for creating Windows installers)
