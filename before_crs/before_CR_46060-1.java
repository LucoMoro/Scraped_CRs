/*Add some more logging.

Change-Id:Ie39f5e38f6f457d118e9cd05f4e515a7c4712b15*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 4f5c31e..3dfcd1e 100644

//Synthetic comment -- @@ -355,13 +355,6 @@
// remove older packaging markers.
removeMarkersFromContainer(javaProject.getProject(), AdtConstants.MARKER_PACKAGING);

            if (androidOutputFolder == null) {
                // mark project and exit
                markProject(AdtConstants.MARKER_PACKAGING, Messages.Failed_To_Get_Output,
                        IMarker.SEVERITY_ERROR);
                return allRefProjects;
            }

// finished with the common init and tests. Special case of the library.
if (isLibrary) {
// check the jar output file is present, if not create it.
//Synthetic comment -- @@ -641,6 +634,8 @@
Messages.ApkBuilder_Update_or_Execute_manually_s,
e.getCommandLine());

return allRefProjects;
} catch (ApkCreationException e) {
String eMessage = e.getMessage();
//Synthetic comment -- @@ -649,6 +644,8 @@
String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (AndroidLocationException e) {
String eMessage = e.getMessage();

//Synthetic comment -- @@ -656,6 +653,7 @@
String msg = String.format(Messages.Final_Archive_Error_s, eMessage);
BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (NativeLibInJarException e) {
String msg = e.getMessage();

//Synthetic comment -- @@ -669,6 +667,7 @@
AdtPlugin.printErrorToConsole(project, msg);
BaseProjectHelper.markResource(project, AdtConstants.MARKER_PACKAGING, msg,
IMarker.SEVERITY_ERROR);
} catch (DuplicateFileException e) {
String msg1 = String.format(
"Found duplicate file for APK: %1$s\nOrigin 1: %2$s\nOrigin 2: %3$s",







