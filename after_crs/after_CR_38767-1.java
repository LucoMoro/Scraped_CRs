/*33851: Android project wizard bug (ADT R20)

(cherry picked from commit befff603176cd579832a03a613b0824d357dcaee)

Change-Id:I38452ae535e34de39d351a81ab699907b5d74ff2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java
//Synthetic comment -- index 7f67a0d..a74ffa8 100644

//Synthetic comment -- @@ -233,12 +233,14 @@
private void validatePage() {
IStatus status = null;

        if (mValues.createActivity) {
            if (mList.getSelectionCount() < 1) {
                status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                        "Select an activity type");
            } else {
                TemplateHandler templateHandler = mValues.activityValues.getTemplateHandler();
                status = templateHandler.validateTemplate(mValues.minSdkLevel);
            }
}

setPageComplete(status == null || status.getSeverity() != IStatus.ERROR);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 3e4e342..2daf5ed 100644

//Synthetic comment -- @@ -329,7 +329,11 @@
}

private IAndroidTarget[] getCompilationTargets() {
        Sdk current = Sdk.getCurrent();
        if (current == null) {
            return new IAndroidTarget[0];
        }
        IAndroidTarget[] targets = current.getTargets();
List<IAndroidTarget> list = new ArrayList<IAndroidTarget>();

for (IAndroidTarget target : targets) {







