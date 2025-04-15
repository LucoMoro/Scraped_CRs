/*Allow loading MonkeyImages from a local filesystem.

Change-Id:I995ac65f1e376b01312d66f82441043971acd16c*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyImage.java
//Synthetic comment -- index d506613..70201ee 100644

//Synthetic comment -- @@ -32,6 +32,8 @@
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
//Synthetic comment -- @@ -42,6 +44,8 @@
*/
@MonkeyRunnerExported(doc = "An image")
public abstract class MonkeyImage extends PyObject implements ClassDictInit {
    private static Logger LOG = Logger.getLogger(MonkeyImage.class.getCanonicalName());

public static void classDictInit(PyObject dict) {
JythonUtils.convertDocAnnotationsForClass(MonkeyImage.class, dict);
}
//Synthetic comment -- @@ -130,7 +134,7 @@
return writeToFile(path, "png");
}
ImageWriter writer = writers.next();
        BufferedImage image = convertSnapshot();
try {
File f = new File(path);
f.delete();
//Synthetic comment -- @@ -273,7 +277,26 @@
public BufferedImage createBufferedImage() {
return image;
}
    }

    /* package */ static MonkeyImage loadImageFromFile(String path) {
        File f = new File(path);
        if (f.exists() && f.canRead()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                if (bufferedImage == null) {
                    LOG.log(Level.WARNING, "Cannot decode file %s", path);
                    return null;
                }
                return new BufferedImageMonkeyImage(bufferedImage);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Exception trying to decode image", e);
                return null;
            }
        } else {
            LOG.log(Level.WARNING, "Cannot read file %s", path);
            return null;
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

    @MonkeyRunnerExported(doc = "Loads a MonkeyImage from a file.",
            args = { "path" },
            argDocs = {
            "The path to the file to load.  This file path is in terms of the computer running " +
            "MonkeyRunner and not a path on the Android Device. ", },
            returns = "A new MonkeyImage representing the specified file")
    public static MonkeyImage loadImageFromFile(PyObject[] args, String kws[]) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);

        String path = ap.getString(0);

        return MonkeyImage.loadImageFromFile(path);
    }

/**
* Display an alert dialog.
*







