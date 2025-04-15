/*Properly configure KXml parsers.

Change-Id:If307659aabe37c5204f7e40c10fc93576de0e145*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 655097d..bee1289 100644

//Synthetic comment -- @@ -56,8 +56,8 @@

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//Synthetic comment -- @@ -322,7 +322,7 @@
mProjectCallback.setLayoutParser(queryLayoutName, modelParser);
topParser = new ContextPullParser(mProjectCallback);
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        topParser.setInput(new FileReader(layoutFile));
} catch (XmlPullParserException e) {
AdtPlugin.log(e, ""); //$NON-NLS-1$
} catch (FileNotFoundException e) {







