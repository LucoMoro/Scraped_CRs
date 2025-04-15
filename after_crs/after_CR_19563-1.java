/*Fix NPE when deleting a layout resource file.

The IncludeFinder tries to read the file, but the workspace
is not in sync so although the resource exists in the
workspace there is no XML file to read anymore.

Change-Id:If0ca5cca8e6978f1777531d6413d7c30a54c9d7d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index f3da152..5f7489d 100644

//Synthetic comment -- @@ -419,15 +419,22 @@
return readFile(new InputStreamReader(contents, charset));
} catch (CoreException e) {
// pass -- ignore files we can't read
        } catch (IOException e) {
            // pass -- ignore files we can't read.

            // Note that IFile.getContents() indicates it throws a CoreException but
            // experience shows that if the file does not exists it really throws
            // IOException.
            // New InputStreamReader() throws UnsupportedEncodingException
            // which is handled by this IOException catch.

} finally {
try {
if (contents != null) {
contents.close();
}
} catch (IOException e) {
                // ignore
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index f064c14..888f674 100644

//Synthetic comment -- @@ -450,15 +450,20 @@
}
}

                    // If no XML model we have to read the XML contents and (possibly) parse it.
                    // The actual file may not exist anymore (e.g. when deleting a layout file
                    // or when the workspace is out of sync.)
if (!hadXmlModel) {
String xml = AdtPlugin.readFile(file);
                        if (xml != null) {
                            includes = findIncludes(xml);
                        }
}
} else {
String xml = AdtPlugin.readFile(resourceFile);
                    if (xml != null) {
                        includes = findIncludes(xml);
                    }
}

String key = getMapKey(resourceFile);







