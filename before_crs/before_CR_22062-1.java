/*Merging monkeyrunner changes from master into r10.

Squashed commit of the following:

commit 2480a0bd7ab68cbbabaeaf03ffe8a3b3ce3dcf57
Author: Bill Napier <napier@google.com>
Date:   Mon Mar 7 17:03:00 2011 -0800

    Add initial and final MOVE events on drag.

    Change-Id:I88dfc808f34a862941640ad8c9ddd49051e85f72commit b44e4e980043ea69252d7bf04272452c40ba316d
Author: Vijay Yellapragada <vijay.yellapragada@gmail.com>
Date:   Wed Mar 9 10:01:18 2011 -0600

    - Fix a NPE when arguments are not properly understood by MonkeyRunner
    - Add the ability to pass Boolean types for Intent extras (extras={'a':True, 'b':False})
    - Fix an improper flag being passed to am start
    - Pass the key and value to am start instead of just the value.

    Change-Id:Ifd0c69ccb4c2755a49efca2d3f8b3befa212a69fcommit e38e875f8bc8d5a6bd31cbc7a0c59cb56948ec8a
Author: Bill Napier <napier@google.com>
Date:   Thu Mar 3 16:00:22 2011 -0800

    Remove trailing comma that was causing compile problems.

    Change-Id:Ia80bd2c25b5898a6ad99b9404b89c1b41c8290bacommit c8586cafa328a21594e40bb4b6d5d4ffa2a7f2c5
Author: Bill Napier <napier@google.com>
Date:   Wed Feb 16 13:55:36 2011 -0800

    Allow loading MonkeyImages from a local filesystem.

    Change-Id:I995ac65f1e376b01312d66f82441043971acd16cChange-Id:I5c7b28309e7feddee16244cc70e86bdf0e55a97d*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java b/monkeyrunner/src/com/android/monkeyrunner/JythonUtils.java
//Synthetic comment -- index 864441e..7054695 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
//Synthetic comment -- @@ -73,6 +74,7 @@
// What python calls float, most people call double
builder.put(PyFloat.class, Double.class);
builder.put(PyInteger.class, Integer.class);

PYOBJECT_TO_JAVA_OBJECT_MAP = builder.build();
}
//Synthetic comment -- @@ -228,6 +230,8 @@
} else if (o instanceof Float) {
float f = (Float) o;
return new PyFloat(f);
}
return Py.None;
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index d506613..70201ee 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
//Synthetic comment -- @@ -42,6 +44,8 @@
*/
@MonkeyRunnerExported(doc = "An image")
public abstract class MonkeyImage extends PyObject implements ClassDictInit {
public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyImage.class, dict);
}
//Synthetic comment -- @@ -130,7 +134,7 @@
return writeToFile(path, "png");
}
ImageWriter writer = writers.next();
        BufferedImage image = getBufferedImage();
try {
File f = new File(path);
f.delete();
//Synthetic comment -- @@ -273,7 +277,26 @@
public BufferedImage createBufferedImage() {
return image;
}

}

@MonkeyRunnerExported(doc = "Copy a rectangular region of the image.",
//Synthetic comment -- @@ -295,4 +318,4 @@
BufferedImage image = getBufferedImage();
return new BufferedImageMonkeyImage(image.getSubimage(x, y, w, h));
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 8480223..5f137cd 100644

//Synthetic comment -- @@ -174,6 +174,21 @@
return choice(message, title, choices);
}

/**
* Display an alert dialog.
*








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunnerStarter.java
//Synthetic comment -- index 90fce6f..5244dbc 100644

//Synthetic comment -- @@ -191,13 +191,12 @@
public static void main(String[] args) {
MonkeyRunnerOptions options = MonkeyRunnerOptions.processOptions(args);

        // logging property files are difficult
        replaceAllLogFormatters(MonkeyFormatter.DEFAULT_INSTANCE, options.getLogLevel());

if (options == null) {
return;
}


MonkeyRunnerStarter runner = new MonkeyRunnerStarter(options);
int error = runner.run();








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/adb/AdbMonkeyDevice.java
//Synthetic comment -- index e7e2e1c..41c4d02 100644

//Synthetic comment -- @@ -419,9 +419,10 @@
} else {
// treat is as a string.
valueString = value.toString();
                arg = "--esmake";
}
parts.add(arg);
parts.add(valueString);
}

//Synthetic comment -- @@ -515,6 +516,7 @@
public void start(Point point) {
try {
manager.touchDown(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag start event", e);
}
//Synthetic comment -- @@ -528,6 +530,7 @@

public void end(Point point) {
try {
manager.touchUp(point.getX(), point.getY());
} catch (IOException e) {
LOG.log(Level.SEVERE, "Error sending drag end event", e);







