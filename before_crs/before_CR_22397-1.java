/*Suggest application and activity name in New Project wizard

This changeset tweaks the "New Android Project" wizard such that the
Application name and the Activity name fields are automatically
populated with some reasonable defaults.

This is done by syncing the project name while it is being edited to
the application name and to the activity name (plus "Activity").
Also, if the application name is manually edited it will also be
synced to the activity name.

Note that as soon as a text field is manually edited, it will no
longer be updated when the project name is edited.

Furthermore, selecting a target in the SDK target chooser will set the
API level of the chosen target as the minimum SDK in the min SDK value
field.

Change-Id:I2391eda334c6c422cce707971d340124742cbb52*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index 9eb572e..4d83b55 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -31,8 +32,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -98,6 +99,8 @@
* Do not derive from this class.
*/
public class NewProjectCreationPage extends WizardPage {

// constants
private static final String MAIN_PAGE_NAME = "newAndroidProjectPage"; //$NON-NLS-1$
//Synthetic comment -- @@ -162,6 +165,7 @@
private boolean mInternalActivityNameUpdate;
private boolean mProjectNameModifiedByUser;
private boolean mApplicationNameModifiedByUser;

private final ArrayList<String> mSamplesPaths = new ArrayList<String>();
private Combo mSamplesCombo;
//Synthetic comment -- @@ -438,6 +442,26 @@
public void handleEvent(Event event) {
if (!mInternalProjectNameUpdate) {
mProjectNameModifiedByUser = true;
}
updateLocationPathField(null);
}
//Synthetic comment -- @@ -642,6 +666,15 @@
public void handleEvent(Event event) {
if (!mInternalApplicationNameUpdate) {
mApplicationNameModifiedByUser = true;
}
}
});
//Synthetic comment -- @@ -958,6 +991,10 @@
* validate the page.
*/
private void onActivityNameFieldModified() {
if (mInfo.isNewProject() && !mInternalActivityNameUpdate) {
mUserActivityName = mInfo.getActivityName();
validatePageComplete();
//Synthetic comment -- @@ -973,6 +1010,10 @@
private void onSdkTargetModified() {
IAndroidTarget target = mInfo.getSdkTarget();

loadSamplesForTarget(target);
enableLocationWidgets();
onSampleSelected();







