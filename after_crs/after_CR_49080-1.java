/*42051: Subversion folder causes NPE in PreCompilerBuilder

Change-Id:Ie2846fdd25a4ea05b798c62ec6512bffa84819f9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/SourceProcessor.java
//Synthetic comment -- index 7932a70..4ecb91d 100644

//Synthetic comment -- @@ -366,15 +366,16 @@
for (IResource r : members) {
// get the type of the resource
switch (r.getType()) {
                   case IResource.FILE: {
// if this a file, check that the file actually exist
// and that it's the type of of file that's used in this processor
                       String extension = r.exists() ? r.getFileExtension() : null;
                       if (extension != null &&
                               getExtensions().contains(extension.toLowerCase(Locale.US))) {
mFiles.put((IFile) r, new SourceFileData((IFile) r));
}
break;
                   }
case IResource.FOLDER:
// recursively go through children
scanFolderForSourceFiles(sourceFolder, (IFolder)r);







