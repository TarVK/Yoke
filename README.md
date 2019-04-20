# Yoke

Yoke is an android app that that provides extra macro keys for your computer. This allows you to add macro keys to your setup, without having to buy an expensive keyboard or other additional hardware.

## Installation
Yoke has only been tested and confirmed to work on windows 10, but might also work on different operating systems.

It consists of two parts: A receiver and the app. The receiver is a runnable jar file which you can just start whenever you want to use it, or you may enable auto startup from the tray menu after running it once. This jar can be obtained in the [releases tab](https://github.com/TarVK/Yoke/releases). The app may also be obtained in the [releases tab](https://github.com/TarVK/Yoke/releases), or obtained from [Google Play](https://play.google.com/store/apps/details?id=com.codepex.yoke).

## App details
### Profiles
The home screen provides several standard profiles which can be opened to execute any of their macros by tapping on them.

<img src=docs/screenshots/home.png width="250"><img src=docs/screenshots/profile.png width="250">

Profiles can be deleted, created and modified by users. 
When modifying a profile consists of several different parts:
* Changing the profile's name
* Removing or adding macros
* Modifying macros
* Reordering macros
* Changing the associated programs
  
<img src=docs/screenshots/editProfile.png width="250"><img src=docs/screenshots/editProfileDrag.png width="250">

The associated programs field allows you to define program names to which this profile should be associated.
These would be names that are shown in the window of an arbitrary program on your computer. Once a profile and program are associated, the profile will automatically open when the program receives focus on your computer. This allows you to always display a profile that makes sense for the task you are working on. It will only switch from one profile to another however, and won't force open the app if something different is running. You can define multiple programs in this field, by seperating them with a comma. In order to make use of this feature `program poll` must be enabled in the receiver.

As an example, you could type `chrome` in the associated programs field, and open another profile. Then once chrome gets focused, the initial profile will be opened.

### Macros
Macros consist of an appereance and an action. As you might expect, the appearance is what you see on your profile and the action is what happens when pressed.
This action is a list of commands. Each of these commands may also be repeated a number of times. There are several command types available:
* Open profile: Switches to another profile
* Open url: Opens a defined url on your computer
* Open program: Opens a defined program on your computer
* Key press: Presses a defined set of keys on your computer
* Mouse:
  * Press: Presses a defined mouse button on your computer
  * Trackpad: Opens a trackpad on your phone to control the mouse
* Computer command: performs a computer command such as `shutdown` or `next track`

<img src=docs/screenshots/selectMacroSearch.png width="250"><img src=docs/screenshots/editMacroAppearance.png width="250"><img src=docs/screenshots/editMacroSequence.png width="250">

By allowing you to perform a sequence of commands, you can create some pretty useful setups. You could for instance have a command that switches to another profile, and any macro in this profile could perform a task and switch back to the intial profile afterwards.
However, if Yoke's macro system isn't powerfull enough for your needs, you could always use something like [AHK](https://www.autohotkey.com/) to create your macro, and simply run the exe using Yoke.

### Miscellaneous
#### Connection
Currently Yoke can only connect to your computer over bluetooth, there were plans to also add USB and WIFI support but there unfortunately was too little time to implement this. The structure of the code allows for the addition of these new connection types rather easily however, so they could be added in the future.
#### Settings
Yoke has a settings activity in which a couple of things can be done, including changing Yoke main color theme and selecting one of the three supported languages:
* English
* Dutch
* Bulgarian
  
<img src=docs/screenshots/settings.png width="250">

#### Tutorial
Yoke also has a simple tutorial from which all basic required information can be optained to get started:

<img src=docs/screenshots/tutorial.png width="250">





## Project

This project has been created for a course at the TUe by group 19 with the following members:

-   Tar van Krieken
-   Bram van Leeuwen
-   Dylan Mijling
-   Karolina Strahilova
-   Yeochan Yoon
-   Ivo Zenden

The project had quite some emphasis on the design process, rather than just the code. As a result we have several documents for Yoke in the [docs](https://github.com/TarVK/Yoke/tree/master/docs) directory. These documents definitely aren't perfect, but they are kept here since they are part of the project.

The course ran from 06-02-2019 until 21-04-2019 after which the first working version was finished.

## Contributing

Any contributions to the project are welcome, but generally it is unlikely that the project will be maintained by any of the original creators. Feel free to fork this repository and do whatever you want with it, including copying code for other projects.

## Credits

We have used some codes and guides from other authors and want to properly credit them below.

[Journaldev’s draglistener](https://github.com/journaldev/journaldev/tree/master/Android/AndroidRecyclerViewDragAndDrop) was used as a base for the following files:
-ItemMoveCallback.java
-ButtonEditRecyclerViewAdapter.java: 
 
[https://demonuts.com/android-shake-detection/](https://demonuts.com/android-shake-detection/) was used as a guide for:
-ShakeService.java

