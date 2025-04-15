/*Added view introspection to ChimpChat and MonkeyRunner

Change-Id:I0e44f6d2c51c99cb0409087a77e2916b630051da*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/ChimpManager.java b/chimpchat/src/com/android/chimpchat/ChimpManager.java
//Synthetic comment -- index c68b7df..73851b8 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.Lists;

import com.android.chimpchat.core.PhysicalButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//Synthetic comment -- @@ -28,6 +29,7 @@
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
//Synthetic comment -- @@ -158,6 +160,7 @@
*
* @param command the monkey command to send to the device
* @return the (unparsed) response returned from the monkey.
*/
private String sendMonkeyEventAndGetResponse(String command) throws IOException {
command = command.trim();
//Synthetic comment -- @@ -209,6 +212,7 @@
*
* @param command the monkey command to send to the device
* @return true on success.
*/
private boolean sendMonkeyEvent(String command) throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -243,6 +247,7 @@
*
* @param name name of static variable to get
* @return the value of the variable, or null if there was an error
*/
public String getVariable(String name) throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -255,7 +260,9 @@
}

/**
     * Function to get the list of static variables from the device.
*/
public Collection<String> listVariable() throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -270,7 +277,7 @@

/**
* Tells the monkey that we are done for this session.
     * @throws IOException
*/
public void done() throws IOException {
// this command just drops the connection, so handle it here
//Synthetic comment -- @@ -281,7 +288,7 @@

/**
* Tells the monkey that we are done forever.
     * @throws IOException
*/
public void quit() throws IOException {
// this command drops the connection, so handle it here
//Synthetic comment -- @@ -296,7 +303,6 @@
* @param x the x coordinate of where to click
* @param y the y coordinate of where to click
* @return success or not
     * @throws IOException
* @throws IOException on error communicating with the device
*/
public boolean tap(int x, int y) throws IOException {
//Synthetic comment -- @@ -308,7 +314,7 @@
*
* @param text the string to type
* @return success
     * @throws IOException
*/
public boolean type(String text) throws IOException {
// The network protocol can't handle embedded line breaks, so we have to handle it
//Synthetic comment -- @@ -336,7 +342,7 @@
*
* @param keyChar the character to type.
* @return success
     * @throws IOException
*/
public boolean type(char keyChar) throws IOException {
return type(Character.toString(keyChar));
//Synthetic comment -- @@ -344,9 +350,104 @@

/**
* Wake the device up from sleep.
     * @throws IOException
*/
public void wake() throws IOException {
sendMonkeyEvent("wake");
}
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index cfc0755..1878145 100644

//Synthetic comment -- @@ -29,7 +29,10 @@
import com.android.chimpchat.adb.LinearInterpolator.Point;
import com.android.chimpchat.core.IChimpImage;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.TouchPressType;
import com.android.chimpchat.hierarchyviewer.HierarchyViewer;

import java.io.IOException;
//Synthetic comment -- @@ -566,4 +569,24 @@
}
});
}
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/By.java b/chimpchat/src/com/android/chimpchat/core/By.java
new file mode 100644
//Synthetic comment -- index 0000000..d732c29

//Synthetic comment -- @@ -0,0 +1,32 @@








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ChimpRect.java b/chimpchat/src/com/android/chimpchat/core/ChimpRect.java
new file mode 100644
//Synthetic comment -- index 0000000..75ae300

//Synthetic comment -- @@ -0,0 +1,105 @@








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ChimpViewId.java b/chimpchat/src/com/android/chimpchat/core/ChimpViewId.java
new file mode 100644
//Synthetic comment -- index 0000000..b3ccfda

//Synthetic comment -- @@ -0,0 +1,92 @@








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java b/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java
//Synthetic comment -- index 03dc09d..82479ee 100644

//Synthetic comment -- @@ -201,4 +201,16 @@
* Wake up the screen on the device.
*/
void wake();
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpView.java b/chimpchat/src/com/android/chimpchat/core/IChimpView.java
new file mode 100644
//Synthetic comment -- index 0000000..aaec55d

//Synthetic comment -- @@ -0,0 +1,50 @@








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ISelector.java b/chimpchat/src/com/android/chimpchat/core/ISelector.java
new file mode 100644
//Synthetic comment -- index 0000000..8dc2d33

//Synthetic comment -- @@ -0,0 +1,27 @@








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/PhysicalButton.java b/chimpchat/src/com/android/chimpchat/core/PhysicalButton.java
//Synthetic comment -- index 1da571e..8faabdd 100644

//Synthetic comment -- @@ -16,10 +16,10 @@
package com.android.chimpchat.core;

public enum PhysicalButton {
    HOME("home"),
    SEARCH("search"),
    MENU("menu"),
    BACK("back"),
DPAD_UP("DPAD_UP"),
DPAD_DOWN("DPAD_DOWN"),
DPAD_LEFT("DPAD_LEFT"),








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/SelectorId.java b/chimpchat/src/com/android/chimpchat/core/SelectorId.java
new file mode 100644
//Synthetic comment -- index 0000000..87ec28a

//Synthetic comment -- @@ -0,0 +1,36 @@








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 61e92a0..d205b47 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.google.common.collect.Collections2;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.chimpchat.core.TouchPressType;
//Synthetic comment -- @@ -31,6 +33,7 @@
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyTuple;

//Synthetic comment -- @@ -357,4 +360,36 @@

impl.wake();
}
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRect.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRect.java
new file mode 100644
//Synthetic comment -- index 0000000..98b2ecc

//Synthetic comment -- @@ -0,0 +1,85 @@








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyView.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyView.java
new file mode 100644
//Synthetic comment -- index 0000000..3960fa0

//Synthetic comment -- @@ -0,0 +1,82 @@







