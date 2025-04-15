/*Switch to "android sdk" and "android avd" command line.

The default with no argument is "android sdk".

Change-Id:I208d7b78dcbeab44a135e4d5bbb4b568b9ce09d7*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 89fb015..ee5879c 100644

//Synthetic comment -- @@ -288,13 +288,11 @@
updateAdb();

}
        } else if (SdkCommandLine.VERB_DISPLAY.equals(verb)) {
            if (SdkCommandLine.OBJECT_AVD.equals(directObject)) {
                showAvdManagerWindow();

            } else if (SdkCommandLine.OBJECT_SDK.equals(directObject)) {
                showSdkManagerWindow(false /*autoUpdate*/);
            }

} else if (SdkCommandLine.VERB_DELETE.equals(verb) &&
SdkCommandLine.OBJECT_AVD.equals(directObject)) {








//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index 7b73f65..66e05f1 100644

//Synthetic comment -- @@ -43,7 +43,8 @@
public final static String VERB_MOVE    = "move";                               //$NON-NLS-1$
public final static String VERB_DELETE  = "delete";                             //$NON-NLS-1$
public final static String VERB_UPDATE  = "update";                             //$NON-NLS-1$
    public final static String VERB_DISPLAY = "display";                            //$NON-NLS-1$

public static final String OBJECT_SDK            = "sdk";                       //$NON-NLS-1$
public static final String OBJECT_AVD            = "avd";                       //$NON-NLS-1$
//Synthetic comment -- @@ -146,11 +147,11 @@
{ VERB_UPDATE, OBJECT_SDK,
"Updates the SDK by suggesting new platforms to install if available." },

            { VERB_DISPLAY, OBJECT_SDK,
"Displays the SDK Manager window." },
            { VERB_DISPLAY, OBJECT_AVD,
"Displays the AVD Manager window.",
                OBJECT_AVDS },
};

public SdkCommandLine(ISdkLog logger) {








//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java b/sdkmanager/app/tests/com/android/sdkmanager/SdkCommandLineTest.java
//Synthetic comment -- index 036d2ec..9a201d0 100644

//Synthetic comment -- @@ -165,4 +165,25 @@
assertEquals("myProject", c.getParamName());
assertFalse(c.isVerbose());
}
}







