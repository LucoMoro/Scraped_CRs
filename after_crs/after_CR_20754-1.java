/*Replace Properties.load(Reader) with Properties.load(InputStream)

Change-Id:Ic378b53448f7ae73493ec692ba77ed35c908ed86*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 998b28a..38d947f 100644

//Synthetic comment -- @@ -40,11 +40,11 @@
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
//Synthetic comment -- @@ -348,16 +348,16 @@
File file = new File(imageDir, PREVIEW_INFO_FILE);
if (file.exists()) {
Properties properties = new Properties();
                InputStream is = null;
try {
                    is = new BufferedInputStream(new FileInputStream(file));;
                    properties.load(is);
} catch (IOException e) {
AdtPlugin.log(e, "Can't read preview properties");
} finally {
                    if (is != null) {
try {
                            is.close();
} catch (IOException e) {
// Nothing useful can be done.
}







