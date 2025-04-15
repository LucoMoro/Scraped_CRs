/*Guard against a possible NPE.

Change-Id:Ica10b0f9d672a7c7d4087362fa17415602a678cc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java
//Synthetic comment -- index aa0e736..5b0d185 100644

//Synthetic comment -- @@ -384,7 +384,10 @@
for (IResource member : members) {
if (member.getType() == IResource.FILE &&
SdkConstants.EXT_JAR.equalsIgnoreCase(member.getFileExtension())) {
                        IPath location = member.getLocation();
                        if (location != null) {
                            jarFiles.add(location.toFile());
                        }
}
}
} catch (CoreException e) {







