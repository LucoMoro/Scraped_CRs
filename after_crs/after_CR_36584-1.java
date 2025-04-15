/*Fix new config chooser for show-included-layouts

Change-Id:I5e0bd0367292b3ab5d3773d4904161a7884ecddf*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 670568a..b255155 100644

//Synthetic comment -- @@ -2277,7 +2277,7 @@
if (mListener == null) {
return null;
}

IProject project = mEditedFile.getProject();
ManifestInfo manifest = ManifestInfo.get(project);

//Synthetic comment -- @@ -2299,17 +2299,10 @@
String defaultTheme = manifest.getDefaultTheme(mState.target, screenSize);

String preferred = defaultTheme;
        if (mState.theme == null) {
// If we are rendering a layout in included context, pick the theme
// from the outer layout instead

String activity = getSelectedActivity();
if (activity != null) {
Map<String, String> activityThemes = manifest.getActivityThemes();
//Synthetic comment -- @@ -2318,12 +2311,6 @@
if (preferred == null) {
preferred = defaultTheme;
}
mState.theme = preferred;
}








