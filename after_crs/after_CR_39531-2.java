/*Prevent NPE when activating layouts that have been deleted

Change-Id:I78af9a552757f3e28369d5f2de112541b1a8ea98*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 529a9a9..692d1ce 100644

//Synthetic comment -- @@ -3139,7 +3139,7 @@
Pair<ResourceQualifier[], IAndroidTarget> pair = loadRenderState();

// Only sync the locale if this layout is not already a locale-specific layout!
        if (pair != null && !isLocaleSpecificLayout()) {
ResourceQualifier[] locale = pair.getFirst();
if (locale != null) {
localeChanged = setLocaleCombo(locale[0], locale[1]);
//Synthetic comment -- @@ -3147,7 +3147,7 @@
}

// Sync render target
        IAndroidTarget target = pair != null ? pair.getSecond() : getSelectedTarget();
if (target != null) {
if (getRenderingTarget() != target) {
selectTarget(target);
//Synthetic comment -- @@ -3202,6 +3202,10 @@
*/
private Pair<ResourceQualifier[], IAndroidTarget> loadRenderState() {
IProject project = mEditedFile.getProject();
        if (!project.isAccessible()) {
            return null;
        }

try {
String data = project.getPersistentProperty(NAME_RENDER_STATE);
if (data != null) {







