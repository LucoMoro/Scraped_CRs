/*DOC: Updated doc for MonkeyRunner API

Change-Id:Id2682f2922a5ca509211a0c18c9c1366f0b020dc*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 87c54c2..4272993 100644

//Synthetic comment -- @@ -57,14 +57,17 @@
*/
public abstract void dispose();

    @MonkeyRunnerExported(doc =
    "Gets the device's screen buffer, yielding a screen capture of the entire display.",
            returns = "A MonkeyImage object (a bitmap wrapper)")
public abstract MonkeyImage takeSnapshot();

    @MonkeyRunnerExported(doc = "Given the name of a static variable on the device, " +
            "returns the variable's value",
args = {"key"},
            argDocs = {"The name of the variable. The available names are listed in " +
            "http://developer.android.com/guide/topics/testing/monkeyrunner.html."},
            returns = "The variable's value")
public String getProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -72,29 +75,30 @@
return getProperty(ap.getString(0));
}

    @MonkeyRunnerExported(doc = "Synonym for getProperty()",
args = {"key"},
            argDocs = {"The name of the system variable."},
            returns = "The variable's value.")
public String getSystemProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
return getSystemProperty(ap.getString(0));
}

    @MonkeyRunnerExported(doc = "Enumerates the possible touch and key event types.  Use this " +
            "with touch() or press() to specify the event type.",
            argDocs = {"Sends a DOWN event",
            "Sends an UP event",
            "Sends a DOWN event, immediately followed by an UP event"})
public enum TouchPressType {
DOWN, UP, DOWN_AND_UP
}

    @MonkeyRunnerExported(doc = "Sends a touch event at the specified location",
args = { "x", "y", "type" },
            argDocs = { "x coordinate in pixels",
                        "y coordinate in pixels",
                        "touch event type as returned by TouchPressType()"})
public void touch(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -114,11 +118,11 @@
touch(x, y, type);
}

    @MonkeyRunnerExported(doc = "Simulates dragging (touch, hold, and move) on the device screen.",
args = { "start", "end", "duration", "steps"},
            argDocs = { "The starting point for the drag (a tuple (x,y) in pixels)",
            "The end point for the drag (a tuple (x,y) in pixels",
            "Duration of the drag in seconds (default is 1.0 seconds)",
"The number of steps to take when interpolating points. (default is 10)"})
public void drag(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
//Synthetic comment -- @@ -149,9 +153,11 @@
drag(startx, starty, endx, endy, steps, ms);
}

    @MonkeyRunnerExported(doc = "Send a key event to the specified key",
args = { "name", "type" },
            argDocs = { "the keycode of the key to press (see android.view.KeyEvent)",
            "touch event type as returned by TouchPressType(). To simulate typing a key, " +
            "send DOWN_AND_UP"})
public void press(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -170,9 +176,10 @@
press(name, type);
}

    @MonkeyRunnerExported(doc = "Types the specified string on the keyboard. This is " +
            "equivalent to calling press(keycode,DOWN_AND_UP) for each character in the string.",
args = { "message" },
            argDocs = { "The string to send to the keyboard." })
public void type(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -181,10 +188,10 @@
type(message);
}

    @MonkeyRunnerExported(doc = "Executes an adb shell command and returns the result, if any.",
args = { "cmd"},
            argDocs = { "The adb shell command to execute." },
            returns = "The output from the command.")
public String shell(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -193,9 +200,9 @@
return shell(cmd);
}

    @MonkeyRunnerExported(doc = "Reboots the specified device into a specified bootloader.",
args = { "into" },
            argDocs = { "the bootloader to reboot into: bootloader, recovery, or None"})
public void reboot(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -205,10 +212,11 @@
reboot(into);
}

    @MonkeyRunnerExported(doc = "Installs the specified Android package (.apk file) " +
            "onto the device. If the package already exists on the device, it is replaced.",
args = { "path" },
            argDocs = { "The package's path and filename on the host filesystem." },
            returns = "True if the install succeeded")
public boolean installPackage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -217,10 +225,11 @@
return installPackage(path);
}

    @MonkeyRunnerExported(doc = "Deletes the specified package from the device, including its " +
            "associated data and cache.",
args = { "package"},
            argDocs = { "The name of the package to delete."},
            returns = "True if remove succeeded")
public boolean removePackage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -229,18 +238,22 @@
return removePackage(packageName);
}

    @MonkeyRunnerExported(doc = "Starts an Activity on the device by sending an Intent " +
            "constructed from the specified parameters.",
args = { "uri", "action", "data", "mimetype", "categories", "extras",
"component", "flags" },
            argDocs = { "The URI for the Intent.",
                        "The action for the Intent.",
                        "The data URI for the Intent",
                        "The mime type for the Intent.",
                        "A Python iterable containing the category names for the Intent.",
                        "A dictionary of extras to add to the Intent. Types of these extras " +
                        "are inferred from the python types of the values.",
                        "The component of the Intent.",
                        "An iterable of flags for the Intent." +
                        "All arguments are optional. The default value for each argument is null." +
                        "(see android.content.Intent)"})

public void startActivity(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -258,18 +271,21 @@
startActivity(uri, action, data, mimetype, categories, extras, component, flags);
}

    @MonkeyRunnerExported(doc = "Sends a broadcast intent to the device.",
args = { "uri", "action", "data", "mimetype", "categories", "extras",
"component", "flags" },
                     argDocs = { "The URI for the Intent.",
                             "The action for the Intent.",
                             "The data URI for the Intent",
                             "The mime type for the Intent.",
                             "An iterable of category names for the Intent.",
                             "A dictionary of extras to add to the Intent. Types of these extras " +
                             "are inferred from the python types of the values.",
                             "The component of the Intent.",
                             "An iterable of flags for the Intent." +
                             "All arguments are optional. " + "" +
                             "The default value for each argument is null." +
                             "(see android.content.Context.sendBroadcast(Intent))"})
public void broadcastIntent(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -287,12 +303,21 @@
broadcastIntent(uri, action, data, mimetype, categories, extras, component, flags);
}

    @MonkeyRunnerExported(doc = "Run the specified package with instrumentation and return " +
            "the output it generates. Use this to run a test package using " +
            "InstrumentationTestRunner.",
args = { "className", "args" },
            argDocs = { "The class to run with instrumentation. The format is " +
                        "packagename/classname. Use packagename to specify the Android package " +
                        "to run, and classname to specify the class to run within that package. " +
                        "For test packages, this is usually " +
                        "testpackagename/InstrumentationTestRunner",
                        "A map of strings to objects containing the arguments to pass to this " +
                        "instrumentation (default value is None)." },
            returns = "A map of strings to objects for the output from the package. " +
                      "For a test package, contains a single key-value pair: the key is 'stream' " +
                      "and the value is a string containing the test output.")

public PyDictionary instrument(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 7cff67f..0445383 100644

//Synthetic comment -- @@ -70,10 +70,14 @@
return img;
}

    @MonkeyRunnerExported(doc = "Converts the MonkeyImage into a particular format and returns " +
                                "the result as a String. Use this to get access to the raw" +
                                "pixels in a particular format. String output is for better " +
                                "performance.",
args = {"format"},
        argDocs = { "The destination format (for example, 'png' for Portable " +
            "Network Graphics format). The default is png." },
        returns = "The resulting image as a String.")
public byte[] convertToBytes(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -91,13 +95,15 @@
return os.toByteArray();
}

    @MonkeyRunnerExported(doc = "Write the MonkeyImage to a file.  If no " +
            "format is specified, this method guesses the output format " +
            "based on the extension of the provided file extension. If it is unable to guess the " +
            "format, it uses PNG.",
args = {"path", "format"},
            argDocs = {"The output filename, optionally including its path",
                       "The destination format (for example, 'png' for " +
                       " Portable Network Graphics format." },
            returns = "boolean true if writing succeeded.")
public boolean writeToFile(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -138,10 +144,13 @@
return true;
}

    @MonkeyRunnerExported(doc = "Get a single ARGB (alpha, red, green, blue) pixel at location " +
            "x,y. The arguments x and y are 0-based, expressed in pixel dimensions. X increases " +
            "to the right, and Y increases towards the bottom. This method returns a tuple.",
args = { "x", "y" },
argDocs = { "the x offset of the pixel", "the y offset of the pixel" },
            returns = "A tuple of (A, R, G, B) for the pixel. Each item in the tuple has the " +
                      "range 0-255.")
public PyObject getRawPixel(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -156,10 +165,13 @@
return new PyTuple(a, r, g ,b);
}

    @MonkeyRunnerExported(doc = "Get a single ARGB (alpha, red, green, blue) pixel at location " +
            "x,y. The arguments x and y are 0-based, expressed in pixel dimensions. X increases " +
            "to the right, and Y increases towards the bottom. This method returns an Integer.",
args = { "x", "y" },
argDocs = { "the x offset of the pixel", "the y offset of the pixel" },
            returns = "An unsigned integer pixel for x,y. The 8 high-order bits are A, followed" +
                    "by 8 bits for R, 8 for G, and 8 for B.")
public int getRawPixelInt(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -197,12 +209,13 @@
return true;
}

    @MonkeyRunnerExported(doc = "Compare this MonkeyImage object to aother MonkeyImage object.",
args = {"other", "percent"},
            argDocs = {"The other MonkeyImage object.",
                       "A float in the range 0.0 to 1.0, indicating the percentage " +
                       "of pixels that need to be the same for the method to return 'true'. " +
                       "Defaults to 1.0."},
            returns = "boolena 'true' if the two objects contain the same image.")
public boolean sameAs(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -257,10 +270,12 @@

}

    @MonkeyRunnerExported(doc = "Copy a rectangular region of the image.",
args = {"rect"},
            argDocs = {"A tuple (x, y, w, h) describing the region to copy. x and y specify " +
                       "upper lefthand corner of the region. w is the width of the region in " +
                       "pixels, and h is its height."},
            returns = "a MonkeyImage object representing the copied region.")
public MonkeyImage getSubImage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -274,4 +289,4 @@
BufferedImage image = getBufferedImage();
return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
}
\ No newline at end of file
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index cdab926..63ab9eb 100644

//Synthetic comment -- @@ -47,13 +47,12 @@
MonkeyRunner.backend = backend;
}

    @MonkeyRunnerExported(doc = "Waits for the workstation to connect to the device.",
args = {"timeout", "deviceId"},
            argDocs = {"The timeout in seconds to wait. The default is to wait indefinitely.",
            "A regular expression that specifies the device name. See the documentation " +
            "for 'adb' in the Developer Guide to learn more about device names."},
            returns = "A MonkeyDevice object representing the connected device.")
public static MonkeyDevice waitForConnection(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -70,10 +69,11 @@
ap.getString(1, ".*"));
}

    @MonkeyRunnerExported(doc = "Pause the currently running program for the specified " +
            "number of seconds.",
args = {"seconds"},
            argDocs = {"The number of seconds to pause."})
    public static void sleep(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

//Synthetic comment -- @@ -88,11 +88,11 @@
}
}

    @MonkeyRunnerExported(doc = "Format and display the API reference for MonkeyRunner.",
args = { "format" },
            argDocs = {"The desired format for the output, either 'text' for plain text or " +
            "'html' for HTML markup."},
            returns = "A string containing the help text in the desired format.")
public static String help(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -102,14 +102,14 @@
return MonkeyRunnerHelp.helpString(format);
}

    @MonkeyRunnerExported(doc = "Display an alert dialog to the process running the current " +
            "script.  The dialog is modal, so the script stops until the user dismisses the " +
            "dialog.",
args = { "message", "title", "okTitle" },
argDocs = {
            "The message to display in the dialog.",
            "The dialog's title. The default value is 'Alert'.",
            "The text to use in the dialog button. The default value is 'OK'."
})
public static void alert(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
//Synthetic comment -- @@ -122,14 +122,18 @@
alert(message, title, buttonTitle);
}

    @MonkeyRunnerExported(doc = "Display a dialog that accepts input. The dialog is ," +
            "modal, so the script stops until the user clicks one of the two dialog buttons. To " +
            "enter a value, the user enters the value and clicks the 'OK' button. To quit the " +
            "dialog without entering a value, the user clicks the 'Cancel' button. Use the " +
            "supplied arguments for this method to customize the text for these buttons.",
args = {"message", "initialValue", "title", "okTitle", "cancelTitle"},
argDocs = {
            "The prompt message to display in the dialog.",
            "The initial value to supply to the user. The default is an empty string)",
            "The dialog's title. The default is 'Input'",
            "The text to use in the dialog's confirmation button. The default is 'OK'." +
            "The text to use in the dialog's 'cancel' button. The default is 'Cancel'."
},
returns = "The test entered by the user, or None if the user canceled the input;"
)
//Synthetic comment -- @@ -144,14 +148,14 @@
return input(message, initialValue, title);
}

    @MonkeyRunnerExported(doc = "Display a choice dialog that allows the user to select a single " +
            "item from a list of items.",
args = {"message", "choices", "title"},
argDocs = {
            "The prompt message to display in the dialog.",
            "An iterable Python type containing a list of choices to display",
            "The dialog's title. The default is 'Input'" },
            returns = "The 0-based numeric offset of the selected item in the iterable.")
public static int choice(PyObject[] args, String kws[]) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);







