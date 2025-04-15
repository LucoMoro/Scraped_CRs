/*Fix aapt handling for skipped files

Aapt occasionally emits a "Skipping <file>" message. ADT treated these
error messages as a fatal error, adding an error marker and setting
the exit status to fail. The reason for that is that aapt will
generate this error message when it encounters files in the resource
directory that start with the letter "_", which is an invalid resource
name prefix.

However, aapt *also* generates this error message when it encounters
*benign* files, such as ".git", or emacs backup files such as
"layout.xml~". Luckily, it includes the "type" of the file in the
error message.

This changeset looks at the file type, and ignores skipping file
messages for benign file types.

Change-Id:If735e19d2dca4327be06bde9c6dfdcb2c95d0196*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptParser.java
//Synthetic comment -- index eb7e60d..3b9d136 100644

//Synthetic comment -- @@ -152,7 +152,7 @@
* invalid, such as a layout file name which starts with _.
*/
private static final Pattern sSkippingPattern =
        Pattern.compile("    \\(skipping .+ .+ '(.*)'\\)"); //$NON-NLS-1$

/**
* Suffix of error message which points to the first occurrence of a repeated resource
//Synthetic comment -- @@ -411,7 +411,16 @@

m = sSkippingPattern.matcher(p);
if (m.matches()) {
                String location = m.group(1);

// check the values and attempt to mark the file.
if (checkAndMark(location, null, p.trim(), osRoot, project,







