/*ConcurrentModificationException in AbstractPropertiesFieldsPart

I ran into a ConcurrentModificationException when creating an XML file
using the plain XML wizard (not the Android XML wizard - because I
wanted to create an XML drawable which is not supported by the new
Android XML wizard.)

Change-Id:Id2ae606e9da47e6f7d42cf6a0dea61eb3e9ca15d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/AbstractPropertiesFieldsPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/AbstractPropertiesFieldsPart.java
//Synthetic comment -- index 06169d2..0d72614 100755

//Synthetic comment -- @@ -34,6 +34,7 @@

import java.util.HashMap;
import java.util.HashSet;

/**
* Section part for editing fields of a properties file in an Export editor.
//Synthetic comment -- @@ -302,13 +303,15 @@
}

// Clear the text of any keyword we didn't find in the document
        for (String key : allKeywords) {
Control field = mNameToField.get(key);
if (field != null) {
try {
mInternalTextUpdate = true;
setFieldText(field, "");
                    allKeywords.remove(key);
} finally {
mInternalTextUpdate = false;
}







