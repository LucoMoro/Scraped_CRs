/*Replace Properties.load(Reader) with Properties.load(InputStream)

Change-Id:Ic378b53448f7ae73493ec692ba77ed35c908ed86*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 998b28a..38d947f 100644

//Synthetic comment -- @@ -40,11 +40,11 @@
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -348,16 +348,16 @@
File file = new File(imageDir, PREVIEW_INFO_FILE);
if (file.exists()) {
Properties properties = new Properties();
                Reader reader = null;
try {
                    reader = new BufferedReader(new FileReader(file));
                    properties.load(reader);
} catch (IOException e) {
AdtPlugin.log(e, "Can't read preview properties");
} finally {
                    if (reader != null) {
try {
                            reader.close();
} catch (IOException e) {
// Nothing useful can be done.
}







