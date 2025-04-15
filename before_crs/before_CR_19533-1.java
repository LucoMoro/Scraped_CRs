/*Fix non-externalized-string references

I had accidentally used //NON-NLS- instead of //$NON-NLS- in some
code.  That explains why Eclipse would sometimes insert a space
between the // and the NON part...

This changeset replaces these with proper //$NON-NLS- entries.

Change-Id:Icf4251a352895293ebe82d8207a4dbfe7d8126d5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index 4ce6675..f3da152 100644

//Synthetic comment -- @@ -427,7 +427,7 @@
contents.close();
}
} catch (IOException e) {
                AdtPlugin.log(e, "Can't read file %1$s", file); //NON-NLS-1$
}
}

//Synthetic comment -- @@ -448,7 +448,7 @@
String charset = file.getCharset();
return streamContains(new InputStreamReader(contents, charset), string);
} catch (Exception e) {
            AdtPlugin.log(e, "Can't read file %1$s", file); //NON-NLS-1$
}

return false;
//Synthetic comment -- @@ -493,14 +493,14 @@
}
}
} catch (Exception e) {
            AdtPlugin.log(e, "Can't read stream"); //NON-NLS-1$
} finally {
try {
if (reader != null) {
reader.close();
}
} catch (IOException e) {
                AdtPlugin.log(e, "Can't read stream"); //NON-NLS-1$
}
}

//Synthetic comment -- @@ -598,7 +598,7 @@
contents.close();
}
} catch (IOException e) {
                AdtPlugin.log(e, "Can't read layout file"); //NON-NLS-1$
}
}

//Synthetic comment -- @@ -632,7 +632,7 @@
reader.close();
}
} catch (IOException e) {
                AdtPlugin.log(e, "Can't read input stream"); //NON-NLS-1$
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
old mode 100755
new mode 100644
//Synthetic comment -- index b707034..3f58df1

//Synthetic comment -- @@ -209,7 +209,7 @@

// Not yet enabled because we need to backport layoutlib changes to Android 2.0, 2.1, 2.2
// first:
        if (System.getenv("ADT_TEST") != null) { //NON-NLS-1$
insertShowIncludedMenu(endId);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GestureManager.java
//Synthetic comment -- index 6cc65bb..bbd62fe 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
*/
public class GestureManager {
/** Drag source data key */
    private static String KEY_DRAG_PREVIEW = "dragpreview"; //NON-NLS-1$

/** The canvas which owns this GestureManager. */
private final LayoutCanvas mCanvas;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 920a8e4..a964c13 100644

//Synthetic comment -- @@ -1515,7 +1515,7 @@
topParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
topParser.setInput(new FileReader(layoutFile));
} catch (XmlPullParserException e) {
                        AdtPlugin.log(e, ""); //NON-NLS-1$
} catch (FileNotFoundException e) {
// this will not happen since we check above.
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 07c52fe..f064c14 100644

//Synthetic comment -- @@ -487,7 +487,7 @@
* if it detects the string &lt;include in the file.
*/
private List<String> findIncludes(String xml) {
        int index = xml.indexOf("<include"); //NON-NLS-1$
if (index != -1) {
return findIncludesInXml(xml);
}
//Synthetic comment -- @@ -569,7 +569,7 @@
}

typeBegin = colon + 1;
            assert "layout".equals(url.substring(typeBegin, typeEnd)); //NON-NLS-1$
}

return url.substring(nameBegin);
//Synthetic comment -- @@ -720,7 +720,7 @@
}

/** Format to chain include cycles in: a=>b=>c=>d etc */
    private final String CHAIN_FORMAT = "%1$s=>%2$s"; //NON-NLS-1$

private String dfs(String from, Set<String> seen) {
seen.add(from);
//Synthetic comment -- @@ -881,7 +881,7 @@
*         given reference
*/
public static String getProjectRelativePath(String reference) {
        if (!reference.contains(WS_SEP)) { //NON-NLS-1$
reference = SdkConstants.FD_LAYOUT + WS_SEP + reference;
}
return SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;
//Synthetic comment -- @@ -995,10 +995,10 @@

@Override
public String toString() {
            return "Reference [getId()=" + getId() // NON-NLS-1$
                    + ", getDisplayName()=" + getDisplayName() // NON-NLS-1$
                    + ", getName()=" + getName() // NON-NLS-1$
                    + ", getFile()=" + getFile() + "]"; // NON-NLS-1$
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PropertySheetPage.java
old mode 100755
new mode 100644
//Synthetic comment -- index c786695..d33fffc

//Synthetic comment -- @@ -85,7 +85,7 @@
// Strip out newlines to make this a single line entry
help = help.replace('\n', ' ');
// Remove repeated spaces in case there were trailing spaces
                            help = help.replaceAll("  ", " "); //NON-NLS-1$ //NON-NLS-2$
IActionBars actionBars = getSite().getActionBars();
IStatusLineManager status = actionBars.getStatusLineManager();
status.setMessage(help);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index 2cb1bb7..4c6945f 100644

//Synthetic comment -- @@ -127,7 +127,7 @@
/** Regular expression matching a FQCN for a view class */
@VisibleForTesting
/* package */ static final Pattern CLASS_PATTERN = Pattern.compile(
        "(([a-zA-Z_\\$][a-zA-Z0-9_\\$]*)+\\.)+[a-zA-Z_\\$][a-zA-Z0-9_\\$]*"); //NON-NLS-1$

/** Determines whether the given attribute <b>name</b> is linkable */
private static boolean isAttributeNameLink(@SuppressWarnings("unused") XmlContext context) {
//Synthetic comment -- @@ -158,7 +158,7 @@
}

String value = attribute.getValue();
        if (value.startsWith("@+")) { //NON-NLS-1$
// It's a value -declaration-, nowhere else to jump
// (though we could consider jumping to the R-file; would that
// be helpful?)
//Synthetic comment -- @@ -494,7 +494,7 @@
if (valueStr.startsWith("?")) { //$NON-NLS-1$
// FIXME: It's a reference. We should resolve this properly.
return false;
                        } else if (valueStr.startsWith("@")) { //NON-NLS-1$
// Refers to a different resource; resolve it iteratively
if (seen.contains(valueStr)) {
return false;
//Synthetic comment -- @@ -534,7 +534,7 @@

// Handle inner classes
if (fqcn.indexOf('$') != -1) {
            fqcn = fqcn.replaceAll("\\$", "."); //NON-NLS-1$ //NON-NLS-2$
}

try {
//Synthetic comment -- @@ -546,7 +546,7 @@
}
}
} catch (Throwable e) {
            AdtPlugin.log(e, "Can't open class %1$s", fqcn); //NON-NLS-1$
}

return false;
//Synthetic comment -- @@ -616,7 +616,7 @@
}
}
} catch (CoreException e) {
            AdtPlugin.log(e, ""); //NON-NLS-1$
}

return null;
//Synthetic comment -- @@ -651,7 +651,7 @@
}
}
} catch (CoreException e) {
                AdtPlugin.log(e, ""); //NON-NLS-1$
}
}

//Synthetic comment -- @@ -689,7 +689,7 @@
}
}
} catch (CoreException e) {
                AdtPlugin.log(e, ""); //NON-NLS-1$
}
}
return null;
//Synthetic comment -- @@ -715,9 +715,9 @@
return findValueInDocument(type, name, file, document);
}
} catch (IOException e) {
            AdtPlugin.log(e, "Can't parse %1$s", file); //NON-NLS-1$
} catch (CoreException e) {
            AdtPlugin.log(e, "Can't parse %1$s", file); //NON-NLS-1$
} finally {
if (model != null) {
model.releaseFromRead();
//Synthetic comment -- @@ -733,7 +733,7 @@
String targetTag = type.getName();
if (type == ResourceType.ID) {
// Ids are recorded in <item> tags instead of <id> tags
            targetTag = "item"; //NON-NLS-1$
}
Element root = document.getDocumentElement();
if (root.getTagName().equals(ROOT_ELEMENT)) {
//Synthetic comment -- @@ -782,9 +782,9 @@
return findIdInDocument(id, file, document);
}
} catch (IOException e) {
            AdtPlugin.log(e, "Can't parse %1$s", file); //NON-NLS-1$
} catch (CoreException e) {
            AdtPlugin.log(e, "Can't parse %1$s", file); //NON-NLS-1$
} finally {
if (model != null) {
model.releaseFromRead();
//Synthetic comment -- @@ -797,7 +797,7 @@
/** Looks within an XML DOM document for the given resource name and returns it */
private static Pair<IFile, IRegion> findIdInDocument(String id, IFile file,
Document document) {
        String targetAttribute = "@+id/" + id; //NON-NLS-1$
return findIdInElement(document.getDocumentElement(), file, targetAttribute);
}

//Synthetic comment -- @@ -852,7 +852,7 @@
int nameBegin = typeEnd + 1;

// Skip @ and @+
        int typeBegin = url.startsWith("@+") ? 2 : 1; //NON-NLS-1$

int colon = url.lastIndexOf(':', typeEnd);
if (colon != -1) {
//Synthetic comment -- @@ -955,7 +955,7 @@
IJavaElement element = elements[0];
if (element.getElementType() == IJavaElement.FIELD) {
IJavaElement unit = element.getAncestor(IJavaElement.COMPILATION_UNIT);
                        if ("R.java".equals(unit.getElementName())) { // NON-NLS-1$
// Yes, we're in an R class. Offer hyperlink navigation to XML
// resource
// files for the various definitions
//Synthetic comment -- @@ -1141,7 +1141,7 @@
}
}
} catch (BadLocationException e) {
                AdtPlugin.log(e, ""); //NON-NLS-1$
}
return new Region(start, length);
}







