/*ADT: Fix display of unknown Manifest elements.

SDK Bug: 3135772

Change-Id:I4f7c84c20b31e70db20a202f168158d500b3c09d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index 55a635c..3383eae 100644

//Synthetic comment -- @@ -515,7 +515,7 @@
if (!stylesDeclared.isEmpty()) {
sb.append("Warning, ADT/SDK Mismatch! The following elements are declared by the SDK but unknown to ADT: ");
for (String name : stylesDeclared) {
                sb.append(guessXmlName(name));

if (name != stylesDeclared.last()) {
sb.append(", ");    //$NON-NLS-1$







