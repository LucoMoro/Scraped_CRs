/*Adjust AaptParser to account for new aapt ignore pattern messages

In Tools 20 aapt's handling of ignored files and directories have
changed. This changeset updates the regular expressions and handling
for these mssages. It also changes the severity of markers for ignored
files from errors to warnings, since aapt does not consider these
fatal errors.

Change-Id:Id66270546c4f323ad78c9ad074464304ee43b880*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index 3b9d136..5742888 100644

//Synthetic comment -- @@ -150,11 +150,22 @@
/**
* Error message emitted when aapt skips a file because for example it's name is
* invalid, such as a layout file name which starts with _.
*/
private static final Pattern sSkippingPattern =
Pattern.compile("    \\(skipping (.+) .+ '(.*)'\\)"); //$NON-NLS-1$

/**
* Suffix of error message which points to the first occurrence of a repeated resource
* definition.
* Example:
//Synthetic comment -- @@ -409,6 +420,25 @@
continue;
}

m = sSkippingPattern.matcher(p);
if (m.matches()) {
String location = m.group(2);
//Synthetic comment -- @@ -424,7 +454,7 @@

// check the values and attempt to mark the file.
if (checkAndMark(location, null, p.trim(), osRoot, project,
                        AdtConstants.MARKER_AAPT_COMPILE, IMarker.SEVERITY_ERROR) == false) {
return true;
}








