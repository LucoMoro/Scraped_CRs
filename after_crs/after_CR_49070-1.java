/*42271: Open file input streams need to be closed at some point

By default, SAX and DOM parsers close the input source after
parsing. However, in SDK common we have a couple of custom parsers
using kxml, which does *not* close the input. Rather than fixing all
the call sites, this CL makes these parsers behave the same way as the
SAX/DOM parsers -- close the input at the end of parse().

Change-Id:Ib112c63de20ec82a430178ea4c69ede7821911ba*/




//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/resources/IdResourceParser.java b/sdk_common/src/main/java/com/android/ide/common/resources/IdResourceParser.java
//Synthetic comment -- index 66a72ce..60c1725 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ValueResourceParser.IValueResourceRepository;
import com.android.resources.ResourceType;
import com.google.common.io.Closeables;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -64,7 +65,7 @@
*
* @param type the type of resource being scanned
* @param path the full OS path to the file being parsed
     * @param input the input stream of the XML to be parsed (will be closed by this method)
* @return true if parsing succeeds and false if it fails
* @throws IOException if reading the contents fails
*/
//Synthetic comment -- @@ -104,6 +105,8 @@
path, parser.getLineNumber(), message);
mContext.addError(error);
return false;
        } finally {
            Closeables.closeQuietly(input);
}
}









//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/resources/ValidatingResourceParser.java b/sdk_common/src/main/java/com/android/ide/common/resources/ValidatingResourceParser.java
//Synthetic comment -- index c1e45a8..b477b8f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.common.resources;

import com.android.annotations.NonNull;
import com.google.common.io.Closeables;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -55,7 +56,7 @@
* the context is already tagged as needing a full aapt run.
*
* @param path the full OS path to the file being parsed
     * @param input the input stream of the XML to be parsed (will be closed by this method)
* @return true if parsing succeeds and false if it fails
* @throws IOException if reading the contents fails
*/
//Synthetic comment -- @@ -63,9 +64,11 @@
throws IOException {
// No need to validate framework files
if (mIsFramework) {
            Closeables.closeQuietly(input);
return true;
}
if (mContext.needsFullAapt()) {
            Closeables.closeQuietly(input);
return false;
}

//Synthetic comment -- @@ -103,6 +106,8 @@
path, parser.getLineNumber(), message);
mContext.addError(error);
return false;
        } finally {
            Closeables.closeQuietly(input);
}
}








