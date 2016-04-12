# Nao Tutoring

These instructions explain how to get the project's Python scripts and Android app running / compiling on a Unix machine for development.

## Python Scripts

The project's Python scripts are located in the `python_scripts/` directory. The main script is `nao_server.py`.

After cloning the repository, within the folder python_scripts/, type mkdir data. This creates a data directory that nao_server.py will look for when running the code.

### Installing naoqi

To run `nao_server.py`, the only module that you should need to install is `naoqi` (the Python NAOqi SDK).
This module can't be installed using `pip`.
You'll need to download the module from the [Aldebaran website](https://www.aldebaran.com/en).
Aldebaran provides installation instructions [here](http://doc.aldebaran.com/1-14/dev/python/install_guide.html).

Below are a few notes that supplement the linked instructions.

* To access the zip archives mentioned in Step 2, you'll need to log in to the [Aldebaran website](https://www.aldebaran.com/en).
Ask someone in the lab for the login credentials.

* Once you've logged in, navigate to *Resources > Software > Python NAOqi SDK*.
Download a zip archive for **version 1.14.5** of the SDK.
This is the version used by the project.

* Step 3 makes sure that Python can find the `naoqi` module.
After you've downloaded and unzipped your zip archive, the following command should do the trick for both Linux and MacOS:
    ```
    export PYTHONPATH=${PYTHONPATH}:/path/to/sdk/
    ```
where `/path/to/sdk/` is the path to your unzipped zip archive.
You can add this line to your `.bash_profile` or `.bashrc` to avoid having to type it into every new terminal.

### Running nao_server.py

You should now be able to run the script:
```
python nao_server.py <your IP address> 6789 [-robot]
```
where `6789` is a port number.
To determine your IP address, you can use `ifconfig` in your terminal.

**A quick but important note about Python binaries.**
The Python binary that you use to run the script should be a system binary.
A brew-installed binary likely won't work.
When I use my brew-installed Python 2.7 to run the script on Yosemite, Python crashes.

To check which Python binary you're actually using, type `which python` into your terminal.
If the binary is located in `/usr/local/bin/`, it's probably brew-installed.
To get the terminal to use the system binary instead, run
```
/usr/bin/python nao_server.py ...
```
instead of
```
python nao_server.py ...
```

## Android App

### Importing the Project into Android Studio

To get the project's tablet app compiling on your machine, first install [Android Studio](http://developer.android.com/sdk/index.html).
Once you've installed Android Studio, import the Nao tutoring project from Git.
The Android Studio welcome menu should give you an option to check out a project from version control.

### Updating the Project Configuration

Navigate to *Tools > Android > SDK Manager*.
Check **Android 5.0 (Lollipop)** (Android 21) and hit *Apply*.
This should install the Android 21 SDK.
As noted in `AndroidManifest.xml`, this is the version of Android used by the project's tablet app.

Once you've installed the Android 21 SDK, navigate to *File > Project Structure...*.
Click on the *Project* tab in the left menu.
Select **Android API 21 Platform** for the *Project SDK* and **5.0** for the *Project language level*.
Hit *Apply* when you're done.

### Compiling and Running the App

You should now be able to compile and run the app!
To do this, click the green run arrow or use the *Run* option in the menu bar.
A pop-up menu will ask you to select a deployment target.
You can run the app on an actual tablet or in an emulator. 
To run the app on a tablet, plug the tablet into your laptop and make sure that your laptop recognizes it.
The tablet should show up under the list of *Connected Devices*.
If you don't have access to a tablet, you can use the *Create New Emulator* option.
To manage your emulators, use *Tools > Android > AVD Manager*.
