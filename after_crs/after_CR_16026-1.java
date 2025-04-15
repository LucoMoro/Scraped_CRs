/*Fix QualifierListTest and ConfigMatchTest unittests.

testQualifierList() checks the number of qualifiers
against FolderConfiguration.INDEX_COUNT except there's
a mismatch since there was no index 13.

ConfigMatchTest was missing the new dock/night and nav state.

Change-Id:I5df0b1375a209cca5a39d93946b62b88f2688fb2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index 6eed4d6..42f41cf 100644

//Synthetic comment -- @@ -41,11 +41,11 @@
private final static int INDEX_TOUCH_TYPE         = 10;
private final static int INDEX_KEYBOARD_STATE     = 11;
private final static int INDEX_TEXT_INPUT_METHOD  = 12;
    private final static int INDEX_NAVIGATION_STATE   = 13;
    private final static int INDEX_NAVIGATION_METHOD  = 14;
    private final static int INDEX_SCREEN_DIMENSION   = 15;
    private final static int INDEX_VERSION            = 16;
    private final static int INDEX_COUNT              = 17;

/**
* Returns the number of {@link ResourceQualifier} that make up a Folder configuration.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/resources/manager/ConfigMatchTest.java
//Synthetic comment -- index 117654d..8a676ee 100644

//Synthetic comment -- @@ -29,9 +29,12 @@
import com.android.ide.eclipse.mock.FileMock;
import com.android.ide.eclipse.mock.FolderMock;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.resources.DockMode;
import com.android.sdklib.resources.Keyboard;
import com.android.sdklib.resources.KeyboardState;
import com.android.sdklib.resources.Navigation;
import com.android.sdklib.resources.NavigationState;
import com.android.sdklib.resources.NightMode;
import com.android.sdklib.resources.ScreenOrientation;
import com.android.sdklib.resources.TouchScreen;

//Synthetic comment -- @@ -90,11 +93,14 @@
null, // screen size
null, // screen ratio
null, // screen orientation
                null, // dock mode
                null, // night mode
null, // dpi
null, // touch mode
null, // keyboard state
null, // text input
                null, // navigation state
                null, // navigation method
null, // screen dimension
null);// version

//Synthetic comment -- @@ -108,11 +114,14 @@
null, // screen size
null, // screen ratio
null, // screen orientation
                null, // dock mode
                null, // night mode
null, // dpi
null, // touch mode
KeyboardState.EXPOSED.getResourceValue(), // keyboard state
null, // text input
                null, // navigation state
                null, // navigation method
null, // screen dimension
null);// version

//Synthetic comment -- @@ -126,11 +135,14 @@
null, // screen size
null, // screen ratio
null, // screen orientation
                null, // dock mode
                null, // night mode
null, // dpi
null, // touch mode
KeyboardState.HIDDEN.getResourceValue(), // keyboard state
null, // text input
                null, // navigation state
                null, // navigation method
null, // screen dimension
null);// version

//Synthetic comment -- @@ -144,11 +156,14 @@
null, // screen size
null, // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                null, // dock mode
                null, // night mode
null, // dpi
null, // touch mode
null, // keyboard state
null, // text input
                null, // navigation state
                null, // navigation method
null, // screen dimension
null);// version

//Synthetic comment -- @@ -162,11 +177,14 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                DockMode.DESK.getResourceValue(), // dock mode
                NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode
KeyboardState.EXPOSED.getResourceValue(), // keyboard state
Keyboard.QWERTY.getResourceValue(), // text input
                NavigationState.EXPOSED.getResourceValue(), // navigation state
                Navigation.DPAD.getResourceValue(), // navigation method
"480x320", // screen dimension
"v3"); // version

//Synthetic comment -- @@ -188,11 +206,14 @@
"normal", // screen size
"notlong", // screen ratio
ScreenOrientation.LANDSCAPE.getResourceValue(), // screen orientation
                DockMode.DESK.getResourceValue(), // dock mode
                NightMode.NIGHT.getResourceValue(), // night mode
"mdpi", // dpi
TouchScreen.FINGER.getResourceValue(), // touch mode
KeyboardState.EXPOSED.getResourceValue(), // keyboard state
Keyboard.QWERTY.getResourceValue(), // text input
                NavigationState.EXPOSED.getResourceValue(), // navigation state
                Navigation.DPAD.getResourceValue(), // navigation method
"480x320", // screen dimension
"v3"); // version








