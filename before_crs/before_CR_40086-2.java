/*Rename AVD Creation / Edit dialog

We need to keep this around so people can edit the old AVDs they have
but want to make it clear this for legacy AVDs only.

Change-Id:If768ae66e98b6e76f1b7f2b3f7b0e5db2bd33174*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 3a76b2a..bf2389e 100644

//Synthetic comment -- @@ -872,7 +872,7 @@
}

private void onNew() {
        AvdCreationDialog dlg = new AvdCreationDialog(mTable.getShell(),
mAvdManager,
mImageFactory,
mSdkLog,
//Synthetic comment -- @@ -886,7 +886,7 @@
private void onEdit() {
AvdInfo avdInfo = getTableSelection();

        AvdCreationDialog dlg = new AvdCreationDialog(mTable.getShell(),
mAvdManager,
mImageFactory,
mSdkLog,








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/LegacyAvdEditDialog.java
similarity index 99%
rename from sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
rename to sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/LegacyAvdEditDialog.java
//Synthetic comment -- index 39a2710..d732bdb 100644

//Synthetic comment -- @@ -93,7 +93,7 @@
* - tooltips on widgets.
*
*/
final class AvdCreationDialog extends GridDialog {

private final AvdManager mAvdManager;
private final TreeMap<String, IAndroidTarget> mCurrentTargets =
//Synthetic comment -- @@ -228,7 +228,7 @@
* @param editAvdInfo An optional {@link AvdInfo}. When null, the dialog is used
*   to create a new AVD. When non-null, the dialog is used to <em>edit</em> this AVD.
*/
    protected AvdCreationDialog(Shell parentShell,
AvdManager avdManager,
ImageFactory imageFactory,
ISdkLog log,







