/*Guard against a possible NPE.

(cherry picked from commit ca699a738a30beb12fb384b07fd69f8d12649e5b)

Change-Id:Ie8dc7576dfc97888631e9b85b28848aaa4607f45*/




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







