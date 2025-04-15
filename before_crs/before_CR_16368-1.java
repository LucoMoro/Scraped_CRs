/*DOC: Updated doc for MonkeyRunner API

Change-Id:Id2682f2922a5ca509211a0c18c9c1366f0b020dc*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 87c54c2..4272993 100644

//Synthetic comment -- @@ -57,14 +57,17 @@
*/
public abstract void dispose();

    @MonkeyRunnerExported(doc = "Fetch the screenbuffer from the device and return it.",
            returns = "The captured snapshot.")
public abstract MonkeyImage takeSnapshot();

    @MonkeyRunnerExported(doc = "Get a MonkeyRunner property (like build.fingerprint)",
args = {"key"},
            argDocs = {"The key of the property to return"},
            returns = "The value of the property")
public String getProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -72,29 +75,30 @@
return getProperty(ap.getString(0));
}

    @MonkeyRunnerExported(doc = "Get a system property (returns the same value as getprop).",
args = {"key"},
            argDocs = {"The key of the property to return"},
            returns = "The value of the property")
public String getSystemProperty(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
return getSystemProperty(ap.getString(0));
}

    @MonkeyRunnerExported(doc = "Enumeration of possible touch and press event types.  This gets " +
            "passed into a press or touch call to specify the event type.",
            argDocs = {"Indicates the down part of a touch/press event",
            "Indicates the up part of a touch/press event.",
            "Indicates that the monkey should send a down event immediately " +
                "followed by an up event"})
public enum TouchPressType {
DOWN, UP, DOWN_AND_UP
}

    @MonkeyRunnerExported(doc = "Send a touch event at the specified location",
args = { "x", "y", "type" },
            argDocs = { "x coordinate", "y coordinate", "the type of touch event to send"})
public void touch(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -114,11 +118,11 @@
touch(x, y, type);
}

    @MonkeyRunnerExported(doc = "Simulate a drag on the screen.",
args = { "start", "end", "duration", "steps"},
            argDocs = { "The starting point for the drag (a tuple of x,y)",
            "The end point for the drag (a tuple of x,y)",
            "How long (in seconds) should the drag take (default is 1.0 seconds)",
"The number of steps to take when interpolating points. (default is 10)"})
public void drag(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
//Synthetic comment -- @@ -149,9 +153,11 @@
drag(startx, starty, endx, endy, steps, ms);
}

    @MonkeyRunnerExported(doc = "Send a key press event to the specified button",
args = { "name", "type" },
            argDocs = { "the name of the key to press", "the type of touch event to send"})
public void press(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -170,9 +176,10 @@
press(name, type);
}

    @MonkeyRunnerExported(doc = "Type the specified string on the keyboard.",
args = { "message" },
            argDocs = { "the message to type." })
public void type(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -181,10 +188,10 @@
type(message);
}

    @MonkeyRunnerExported(doc = "Execute the given command on the shell.",
args = { "cmd"},
            argDocs = { "The command to execute" },
            returns = "The output of the command")
public String shell(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -193,9 +200,9 @@
return shell(cmd);
}

    @MonkeyRunnerExported(doc = "Reboot the specified device",
args = { "into" },
            argDocs = { "the bootloader to reboot into (bootloader, recovery, or None)"})
public void reboot(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -205,10 +212,11 @@
reboot(into);
}

    @MonkeyRunnerExported(doc = "Install the specified apk onto the device.",
args = { "path" },
            argDocs = { "The path on the host filesystem to the APK to install." },
            returns = "True if install succeeded")
public boolean installPackage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -217,10 +225,11 @@
return installPackage(path);
}

    @MonkeyRunnerExported(doc = "Remove the specified package from the device.",
args = { "package"},
            argDocs = { "The name of the package to uninstall"},
            returns = "'True if remove succeeded")
public boolean removePackage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -229,18 +238,22 @@
return removePackage(packageName);
}

    @MonkeyRunnerExported(doc = "Start the Activity specified by the intent.",
args = { "uri", "action", "data", "mimetype", "categories", "extras",
"component", "flags" },
            argDocs = { "The URI for the intent",
                        "The action for the intent",
                        "The data URI for the intent",
                        "The mime type for the intent",
                        "The list of category names for the intent",
                        "A dictionary of extras to add to the intent.  Types of these extras " +
                            "are inferred from the python types of the values",
                        "The component of the intent",
                        "A list of flags for the intent" })
public void startActivity(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -258,18 +271,21 @@
startActivity(uri, action, data, mimetype, categories, extras, component, flags);
}

    @MonkeyRunnerExported(doc = "Start the specified broadcast intent on the device.",
args = { "uri", "action", "data", "mimetype", "categories", "extras",
"component", "flags" },
            argDocs = { "The URI for the intent",
                        "The action for the intent",
                        "The data URI for the intent",
                        "The mime type for the intent",
                        "The list of category names for the intent",
                        "A dictionary of extras to add to the intent.  Types of these extras " +
                            "are inferred from the python types of the values",
                        "The component of the intent",
                        "A list of flags for the intent" })
public void broadcastIntent(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -287,12 +303,21 @@
broadcastIntent(uri, action, data, mimetype, categories, extras, component, flags);
}

    @MonkeyRunnerExported(doc = "Instrument the specified package and return the results from it.",
args = { "className", "args" },
            argDocs = { "The class name to instrument (like com.android.test/.TestInstrument)",
                        "A Map of String to Objects for the aruments to pass to this " +
                        "instrumentation (default value is None)" },
            returns = "A map of string to objects for the results this instrumentation returned")
public PyDictionary instrument(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index 7cff67f..0445383 100644

//Synthetic comment -- @@ -70,10 +70,14 @@
return img;
}

    @MonkeyRunnerExported(doc = "Encode the image into a format and return the bytes.",
args = {"format"},
        argDocs = { "The (optional) format in which to encode the image (PNG for example)" },
        returns = "A String containing the bytes.")
public byte[] convertToBytes(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -91,13 +95,15 @@
return os.toByteArray();
}

    @MonkeyRunnerExported(doc = "Write out the file to the specified location.  If no " +
            "format is specified, this function tries to guess at the output format " +
            "depending on the file extension given.  If unable to determine, it uses PNG.",
args = {"path", "format"},
            argDocs = {"Where to write out the file",
                       "The format in which to encode the image (PNG for example)"},
            returns = "True if writing succeeded.")
public boolean writeToFile(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -138,10 +144,13 @@
return true;
}

    @MonkeyRunnerExported(doc = "Get a single ARGB pixel from the image",
args = { "x", "y" },
argDocs = { "the x offset of the pixel", "the y offset of the pixel" },
            returns = "A tuple of (A, R, G, B) for the pixel")
public PyObject getRawPixel(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -156,10 +165,13 @@
return new PyTuple(a, r, g ,b);
}

    @MonkeyRunnerExported(doc = "Get a single ARGB pixel from the image",
args = { "x", "y" },
argDocs = { "the x offset of the pixel", "the y offset of the pixel" },
            returns = "An integer for the ARGB pixel")
public int getRawPixelInt(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -197,12 +209,13 @@
return true;
}

    @MonkeyRunnerExported(doc = "Compare this image to the other image.",
args = {"other", "percent"},
            argDocs = {"The other image.",
                       "A float from 0.0 to 1.0 indicating the percentage " +
                           "of pixels that need to be the same.  Defaults to 1.0"},
            returns = "True if they are the same image.")
public boolean sameAs(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -257,10 +270,12 @@

}

    @MonkeyRunnerExported(doc = "Get a sub-image of this image.",
args = {"rect"},
            argDocs = {"A Tuple of (x, y, w, h) representing the area of the image to extract."},
            returns = "The newly extracted image.")
public MonkeyImage getSubImage(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -274,4 +289,4 @@
BufferedImage image = getBufferedImage();
return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index cdab926..63ab9eb 100644

//Synthetic comment -- @@ -47,13 +47,12 @@
MonkeyRunner.backend = backend;
}

    @MonkeyRunnerExported(doc = "Wait for the specified device to connect.",
args = {"timeout", "deviceId"},
            argDocs = {"The timeout in seconds to wait for the device to connect. (default " +
                "is to wait forever)",
            "A regular expression that specifies the device of for valid devices" +
                " to wait for."},
    returns = "A MonkeyDevice representing the connected device.")
public static MonkeyDevice waitForConnection(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -70,10 +69,11 @@
ap.getString(1, ".*"));
}

    @MonkeyRunnerExported(doc = "Pause script processing for the specified number of seconds",
args = {"seconds"},
            argDocs = {"The number of seconds to pause processing"})
            public static void sleep(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);

//Synthetic comment -- @@ -88,11 +88,11 @@
}
}

    @MonkeyRunnerExported(doc = "Simple help command to dump the MonkeyRunner supported " +
            "commands",
args = { "format" },
            argDocs = {"The format to return the help text in. (default is text)"},
            returns = "The help text")
public static String help(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);
//Synthetic comment -- @@ -102,14 +102,14 @@
return MonkeyRunnerHelp.helpString(format);
}

    @MonkeyRunnerExported(doc = "Put up an alert dialog to inform the user of something that " +
            "happened.  This is modal dialog and will stop processing of " +
            "the script until the user acknowledges the alert message",
args = { "message", "title", "okTitle" },
argDocs = {
            "The contents of the message of the dialog box",
            "The title to display for the dialog box.  (default value is \"Alert\")",
            "The title to use for the acknowledgement button (default value is \"OK\")"
})
public static void alert(PyObject[] args, String[] kws) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
//Synthetic comment -- @@ -122,14 +122,18 @@
alert(message, title, buttonTitle);
}

    @MonkeyRunnerExported(doc = "Put up an input dialog that allows the user to input a string." +
            "  This is a modal dialog that will stop processing of the script until the user " +
            "inputs the requested information.",
args = {"message", "initialValue", "title", "okTitle", "cancelTitle"},
argDocs = {
            "The message to display for the input.",
            "The initial value to supply the user (default is empty string)",
            "The title of the dialog box to display. (default is \"Input\")"
},
returns = "The test entered by the user, or None if the user canceled the input;"
)
//Synthetic comment -- @@ -144,14 +148,14 @@
return input(message, initialValue, title);
}

    @MonkeyRunnerExported(doc = "Put up a choice dialog that allows the user to select a single " +
            "item from a list of items that were presented.",
args = {"message", "choices", "title"},
argDocs = {
            "The message to display for the input.",
            "The list of choices to display.",
            "The title of the dialog box to display. (default is \"Input\")" },
            returns = "The numeric offset of the choice selected.")
public static int choice(PyObject[] args, String kws[]) {
ArgParser ap = JythonUtils.createArgParser(args, kws);
Preconditions.checkNotNull(ap);







