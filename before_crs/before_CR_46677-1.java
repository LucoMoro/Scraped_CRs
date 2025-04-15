/*40170: Fix namespace handling in resource file template merging

Change-Id:If96987025accac0a11b860580a6935020044e0d9*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 3fac79e..8c310ec 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.EXT_XML;
import static com.android.SdkConstants.FD_NATIVE_LIBS;
import static com.android.ide.eclipse.adt.internal.wizards.templates.InstallDependencyPage.SUPPORT_LIBRARY_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateManager.getTemplateRootFolder;

//Synthetic comment -- @@ -81,8 +82,10 @@
import org.eclipse.text.edits.TextEdit;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
//Synthetic comment -- @@ -99,6 +102,7 @@
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -672,8 +676,8 @@
}
}

        Document currentManifest = DomUtilities.parseStructuredDocument(currentXml);
        assert currentManifest != null : currentXml;
Document fragment = DomUtilities.parseStructuredDocument(xml);
assert fragment != null : xml;

//Synthetic comment -- @@ -682,7 +686,7 @@
boolean ok;
String fileName = to.getName();
if (fileName.equals(SdkConstants.FN_ANDROID_MANIFEST_XML)) {
            modified = ok = mergeManifest(currentManifest, fragment);
} else {
// Merge plain XML files
String parentFolderName = to.getParent().getName();
//Synthetic comment -- @@ -693,7 +697,7 @@
formatStyle = XmlFormatStyle.FILE;
}

            modified = mergeResourceFile(currentManifest, fragment, folderType, paramMap);
ok = true;
}

//Synthetic comment -- @@ -701,7 +705,7 @@
String contents = null;
if (ok) {
if (modified) {
                contents = XmlPrettyPrinter.prettyPrint(currentManifest,
XmlFormatPreferences.create(), formatStyle, null);
}
} else {
//Synthetic comment -- @@ -728,10 +732,22 @@

/** Merges the given resource file contents into the given resource file
* @param paramMap */
    private boolean mergeResourceFile(Document currentManifest, Document fragment,
ResourceFolderType folderType, Map<String, Object> paramMap) {
boolean modified = false;

// For layouts for example, I want to *append* inside the root all the
// contents of the new file.
// But for resources for example, I want to combine elements which specify
//Synthetic comment -- @@ -747,8 +763,9 @@
nodes.add(child);
root.removeChild(child);
}

        root = currentManifest.getDocumentElement();

if (folderType == ResourceFolderType.VALUES) {
// Try to merge items of the same name
//Synthetic comment -- @@ -808,7 +825,7 @@
}

/** Merges the given manifest fragment into the given manifest file */
    private boolean mergeManifest(Document currentManifest, Document fragment) {
// TODO change MergerLog.wrapSdkLog by a custom IMergerLog that will create
// and maintain error markers.








