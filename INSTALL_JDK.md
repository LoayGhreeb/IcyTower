# Installing JDK and Setting the System Path

To run the Icy Tower Clone, you'll need to have Java JDK 8 or higher installed on your system. Follow the instructions below based on your operating system:

## For Windows:

### 1. Download JDK

Visit the [Oracle website](https://www.oracle.com/java/technologies/downloads/) and download the appropriate JDK version for Windows.

### 2. Install JDK

Run the downloaded installer and follow the on-screen instructions to install the JDK.

### 3. Set System Path

- Right-click on `This PC` or `Computer` on the desktop or in File Explorer.
- Choose `Properties`.
- Click on `Advanced system settings` on the left.
- Click on the `Environment Variables` button.
- Under `System Variables`, find

and select the `Path` variable, then click on `Edit`.
- Click on `New` and add the path to the `bin` directory of your JDK installation. For example: `C:\Program Files\Java\jdk-11.0.1\bin`.
- Click `OK` to close each window.

## For macOS:

### 1. Download JDK

Visit the [Oracle website](https://www.oracle.com/java/technologies/downloads/) and download the appropriate JDK version for macOS.

### 2. Install JDK

Open the downloaded `.dmg` file and drag the JDK into the Applications folder.

### 3. Set System Path (if not automatically set):

- Open Terminal.
- Edit the shell profile file (e.g., `~/.bash_profile` or `~/.zshrc` for newer macOS versions) using a text editor like `nano`:
  ```bash
  nano ~/.bash_profile
  ```
- Add the following line to the file:
  ```bash
  export PATH=$PATH:/Library/Java/JavaVirtualMachines/jdk-11.0.1.jdk/Contents/Home/bin
  ```
- Save and close the file.
- Reload the profile:
  ```bash
  source ~/.bash_profile
  ```

## For Linux:

### 1. Download JDK

Visit the [Oracle website](https://www.oracle.com/java/technologies/downloads/) and download the appropriate JDK version for Linux.

### 2. Install JDK

Extract the downloaded tarball and move it to the desired directory.

### 3. Set System Path:

- Open Terminal.
- Edit the shell profile file (e.g., `~/.bashrc`):
  ```bash
  nano ~/.bashrc
  ```
- Add the following line to the file, adjusting the path as necessary:
  ```bash
  export PATH=$PATH:/path_to_jdk_directory/bin
  ```
- Save and close the file.
- Reload the profile:
  ```bash
  source ~/.bashrc
  ```
