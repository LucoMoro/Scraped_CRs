/*ADT: Fix NPE when converting legacy default.properties

SDK Bug: 29627 for details.

- in Sdk.getProjectState, return the new property state
  instead of the legacy one.
- in AndroidPropertyPage.performOk don't fail if the ws
  doesn't contain the project.properties that needs to be refreshed.

Change-Id:I78fe3cdfd37b412fb572faf2abe283a3f9f7c24c*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/properties/AndroidPropertyPage.java
//Synthetic comment -- index 584d49f..212263a 100644

//Synthetic comment -- @@ -143,7 +143,9 @@
mPropertiesWorkingCopy.save();

IResource projectProp = mProject.findMember(SdkConstants.FN_PROJECT_PROPERTIES);
                    if (projectProp != null) {
                        projectProp.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
                    }
} catch (Exception e) {
String msg = String.format(
"Failed to save %1$s for project %2$s",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 2a1fba9..4f9bb09 100644

//Synthetic comment -- @@ -442,6 +442,10 @@

// delete the old file.
ProjectProperties.delete(projectLocation, PropertyType.LEGACY_DEFAULT);

                            // make sure to use the new properties
                            properties = ProjectProperties.load(projectLocation,
                                    PropertyType.PROJECT);
} catch (Exception e) {
AdtPlugin.log(IStatus.ERROR,
"Failed to rename properties file to %1$s for project '%s2$'",







