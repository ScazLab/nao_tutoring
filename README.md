# Nao Tutoring

## Installation Instructions: Python Scripts

The project's Python scripts are located in the `python_scripts/` directory.
The main script is `nao_server.py`.

After cloning the repository, within the folder python_scripts/, type 'mkdir data'.
This creates a data directory that nao_server.py will look for when running the code.

### Installing naoqi

To run `nao_server.py`, the only module that you should need to install is `naoqi` (the Python NAOqi SDK).
This module can't be installed using `pip`.
You'll need to download the module from the [Aldebaran website](https://www.aldebaran.com/en).
Aldebaran provides installation instructions [here](http://doc.aldebaran.com/1-14/dev/python/install_guide.html).

Below are a few notes that supplement the linked instructions:

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

## Installation Instructions: Android App

The following instructions explain how to get the project's Android app compiling and running.

### Installing Android Studio and the Android SDK

To get the project's tablet app compiling on your machine, first install [Android Studio](http://developer.android.com/sdk/index.html).

Once you've installed Android Studio, use it to install the Android 21 SDK:

1. If you're at the Android Studio welcome menu, navigate to *Configure > SDK Manager*.
   If you're inside a project, navigate to *Tools > Android > SDK Manager*.
2. Check **Android 5.0 (Lollipop)** (Android 21) and hit *Apply*.

Android 21 is the target SDK for the project as noted in `AndroidManifest.xml`.
You can install other Android SDKs as well.
For example, you may want to install Android 16, which is the minimum SDK that the project supports.
This is also noted in `AndroidManifest.xml`.

### Importing the Project

If you're at the welcome menu, navigate to *Check out project from Version Control > Git*.
If you're inside a project, navigate to *File > New > Project from Version Control > Git*.
After you hit *Clone*, you may or may not be given the following option:
*Would you like to create a Studio project for the sources you have checked out to...?*

**If you are given this option...**

1. Click *Yes*.
2. Hit *Next* repeatedly until you see the screen that asks you to select a project SDK.
3. If **Android 21** isn't included in the list of options,
   1. Click the plus and then *Android SDK*.
   2. Navigate to your SDK directory and hit *OK*.
      On a Mac, your SDK directory is probably `/Users/{username}/Library/Android/sdk/`.
4. You should now be able to select **Android 21**.
5. Hit *Next* and then *Finish*.

**If you aren't given this option...**

1. Navigate to *File > Project Structure...*.
2. Click the *Project* tab on the left.
3. If **Android 21** isn't included in the list of options for *Project SDK*,
   1. Click *New...* and then *Android SDK*.
   2. Navigate to your SDK directory and hit *OK*.
      On a Mac, your SDK directory is probably `/Users/{username}/Library/Android/sdk/`.
4. You should now be able to select **Android 21**.
5. For the *Project language level*, select any value greater than or equal to **6**.
6. For the *Project compiler output*, `/path/to/repo/out/` is a good option.
7. Now click the *Modules* tab on the left.
8. Even if **nao_tutoring** is already listed as a module, click on it and hit the minus to remove it.
   We're going to re-add it; this forces Android Studio to do some indexing.
9. Click the plus and then *Import Module*.
10. Navigate to the repo and hit *OK*.
11. Hit *Next* repeatedly and eventually *Finish*.
12. Click *Apply*.

### Compiling and Running the App

You should now be able to compile and run the app!
To do this, click the green run arrow.
A pop-up menu will ask you to select a deployment target.
You can run the app on an actual tablet or in an emulator. 
To run the app on a tablet, plug the tablet into your laptop and make sure that your laptop recognizes it.
The tablet should show up under the list of *Connected Devices*.
If you don't have access to a tablet, you can use the *Create New Emulator* option.
To manage your emulators, use *Tools > Android > AVD Manager*.

## Development Instructions: Adding New Break Activities

See `src/tutoringproject/breaks/TicTacToeActivity.java` and `src/tutoringproject/breaks/StretchBreakActivity.java` for examples of already implemented break activities.

### Creating a New XML File

Create a new XML file in the `res/layout/` directory for the activity.
See `activity_tictactoe.xml` and `activity_stretchbreak.xml` for examples.

### Creating a New Class

Create a new class in the `src/tutoringproject/breaks/` directory.
The class should extend the `Activity` class and implement the `TCPClientOwner` interface:
```
public class <ActivityName> extends Activity implements TCPClientOwner {
    ...
}
```

Be sure to add an activity entry to the project's `AndroidManifest.xml`:
```
<activity
    android:name="tutoringproject.breaks.<ActivityName>"
    android:label="@string/app_name"
    android:screenOrientation="portrait"
    android:windowSoftInputMode="stateAlwaysHidden">
</activity>
```

### Implementing the TCPClientOwner Interface

Implementing the `TCPClientOwner` interface allows the class to own the project's TCP Client.
This further allows the class to send and receive messages from `nao_server.py`.

If you look at `src/tutoringproject/breaks/TCPClientOwner.java`, you'll see that three methods need to be defined for a class to implement the `TCPClientOwner`.
The first of these, `getMainLooper()`, is already implemented by extending the `Activity` class.
The remaining two, `disableButtons()` and `messageReceived()`, both need to be defined.
As its name suggests, `disableButtons()` simply needs to disable all of the buttons in the XML file associated with the class.
`messageReceived()` should process messages sent from `nao_server.py` to the tablet.

### Transferring Control of the TCP Client

Once you've implemented the `TCPClientOwner` interface, you should transfer control of the project's TCP client from `MathActivity` to your class in your class's `onCreate()` method:
```
if (TCPClient.singleton != null ) {
    TCPClient.singleton.setSessionOwner(this);
}
```
Don't worry about transferring control back to `MathActivity` once you're ready to close your activity.
This is already handled by `MathActivity`'s `onResume()` method, which is run every time `MathActivity()` is started or restarted.

### Closing the Activity

Calling the class's `finish()` method will close the activity and restore control to the parent `MathActivity`.

Both `TicTacToeActivity` and `StretchBreakActivity` include a return button in their associated XML.
The handler for this return button calls `finish()`.
The button XML and class method, respectively, thus look as follows:
```
<Button
    android:id="@+id/returnButton"
    android:clickable="true"
    android:onClick="returnButtonPressed" 
    ...
    android:text="@string/return_to_tutoring_session"
    android:textSize="20sp" />
```
```
public void returnButtonPressed(View view) {
    finish();
}
```
This button should likely only be enabled once the activity has completed.
In the case of `TicTacToeActivity` for example, the button is only enabled when the tablet receives a `TICTACTOE-END` message from `nao_server.py`.
This is handled in the class's `messageReceived()` method:
```
} else if (msg.equals("TICTACTOE-END")) {
    enableReturnButton();
    instructions.setText(CLICK_RETURN_BUTTON_TEXT);
}
```

### Adding Cases to nao_server.py

Cases need to be added to `nao_server.py`'s tutoring while loop.
These cases should process the messages sent from the new activity to `nao_server.py`.

For `TicTacToeActivity`, the following case was added:
```
elif msgType.startswith('TICTACTOE'):
    id, otherInfo = self.handle_tictactoe_msg(msgType, robot_speech)
    returnMessage = msgType
```
Given the many messages that `TicTacToeActivity` sends, the processing was moved to a helper method, `handle_tictactoe_msg()`.
Note that the `returnMessage` is set to the incoming `msgType`.
This means that, if `TicTacToeActivity` sends a `TICTACTOE-START` message to `nao_server.py`, it will receive a `TICTACTOE-START` message from `nao_server.py` in return.
This approach is helpful for activities that require significant back-and-forth communication.

The case for the `TICTACTOE-START` message in `handle_tictactoe_msg()` is particularly interesting.
It's in this case that the introductory break message is constructed:
```
robot_speech_base = (
    "Let's play a game of tic-tac-toe. You will be exes, and I will be ohs. You can "
    "go first. Click any square on the board."
)
if int(self.expGroup) == 1:
    robot_speech = get_break_speech(1, -1, -1) + " " + robot_speech_base
else:
    robot_speech = get_break_speech(
        int(self.expGroup),
        self.current_session.breaks[-1].b_super,
        self.current_session.breaks[-1].b_type
    ) + " " + robot_speech_base
```
This structure generates a break-trigger-specific start message.
The `if` case is for the fixed condition, and the `else` case is for the two personalized conditions.
You'll likely need to use a similar structure for your break.

### Starting the Activity from MathActivity

To start the break activity from `MathActivity`, first write a helper method.
Below is the helper method for the stretch break:
```
public void startStretchBreak() {
    Intent intent = new Intent(this, StretchBreakActivity.class);
    intent.putExtra("expGroup", "" + expGroup);
    startActivity(intent);
}
```
You can add fields to `intent` as necessary.
Then call this method from the `if` statement near the top of the `NextQuestion()` method:
```
if (takeBreak) {
    stopTimerTask();
    if (numberBreaksGiven % 4 == 0) {
        numberBreaksGiven++;
        startTicTacToe();
    } else if (numberBreaksGiven % 4 == 1) {
        numberBreaksGiven++;
        startStretchBreak();
    } else if (numberBreaksGiven % 4 == 2) {
        numberBreaksGiven++;
        System.out.println("Break 3");
    } else if (numberBreaksGiven % 4 == 3) {
        numberBreaksGiven++;
        System.out.println("Break 4");
    }
    takeBreak = false;
    return;
}
```
Note that there are currently placeholder print statements for break activities 3 and 4.

This should all work!
