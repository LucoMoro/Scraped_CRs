/*Suppress errors for missing markers

Change-Id:I2d34f5a4ebeaff0a75abc7cdbb41132d860c95eb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/BaseClasspathContainerInitializer.java
//Synthetic comment -- index ebcf9e3..d19b107 100644

//Synthetic comment -- @@ -67,7 +67,10 @@
fmessage, IMarker.SEVERITY_ERROR,
IMarker.PRIORITY_HIGH);
} catch (CoreException e2) {
                            AdtPlugin.log(e2, null);
                            // Don't return e2.getStatus(); the job control will then produce
                            // a popup with this error, which isn't very interesting for the
                            // user.
}

return Status.OK_STATUS;
//Synthetic comment -- @@ -97,7 +100,7 @@
IResource.DEPTH_INFINITE);
}
} catch (CoreException e2) {
                            AdtPlugin.log(e2, null);
}

return Status.OK_STATUS;







