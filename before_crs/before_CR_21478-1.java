/*Allow loading MonkeyImages from a local filesystem.

Change-Id:I995ac65f1e376b01312d66f82441043971acd16c*/
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
//Synthetic comment -- index 8480223..5d12955 100644

//Synthetic comment -- @@ -174,6 +174,21 @@
return choice(message, title, choices);
}

/**
* Display an alert dialog.
*







