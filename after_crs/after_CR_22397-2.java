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
//Synthetic comment -- index 9eb572e..6bc6187 100644

//Synthetic comment -- @@ -24,6 +24,7 @@

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -31,8 +32,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -98,6 +99,8 @@
* Do not derive from this class.
*/
public class NewProjectCreationPage extends WizardPage {
    /** Suffix added by default to activity names */
    private static final String ACTIVITY_NAME_SUFFIX = "Activity"; //$NON-NLS-1$

// constants
private static final String MAIN_PAGE_NAME = "newAndroidProjectPage"; //$NON-NLS-1$
//Synthetic comment -- @@ -160,8 +163,11 @@
private boolean mInternalApplicationNameUpdate;
private boolean mInternalCreateActivityUpdate;
private boolean mInternalActivityNameUpdate;
    private boolean mInternalMinSdkUpdate;
private boolean mProjectNameModifiedByUser;
private boolean mApplicationNameModifiedByUser;
    private boolean mActivityNameModifiedByUser;
    private boolean mMinSdkModifiedByUser;

private final ArrayList<String> mSamplesPaths = new ArrayList<String>();
private Combo mSamplesCombo;
//Synthetic comment -- @@ -436,15 +442,11 @@
mProjectNameField.setFont(parent.getFont());
mProjectNameField.addListener(SWT.Modify, new Listener() {
public void handleEvent(Event event) {
                onProjectFieldModified();
}
});
}

/**
* Creates the group for the Project options:
* [radio] Create new project
//Synthetic comment -- @@ -640,9 +642,7 @@
mApplicationNameField.setFont(parent.getFont());
mApplicationNameField.addListener(SWT.Modify, new Listener() {
public void handleEvent(Event event) {
               onApplicationFieldModified();
}
});

//Synthetic comment -- @@ -703,6 +703,7 @@
mMinSdkVersionField.setFont(parent.getFont());
mMinSdkVersionField.addListener(SWT.Modify, new Listener() {
public void handleEvent(Event event) {
                onMinSdkFieldUpdated();
validatePageComplete();
}
});
//Synthetic comment -- @@ -900,6 +901,54 @@
}
}

    private void onProjectFieldModified() {
        if (!mInternalProjectNameUpdate) {
            mProjectNameModifiedByUser = true;

            if (!mApplicationNameModifiedByUser) {
                String name = DescriptorsUtils.capitalize(mProjectNameField.getText());
                try {
                    mInternalApplicationNameUpdate = true;
                    mApplicationNameField.setText(name);
                } finally {
                    mInternalApplicationNameUpdate = false;
                }
            }
            if (!mActivityNameModifiedByUser) {
                String name = DescriptorsUtils.capitalize(mProjectNameField.getText());
                try {
                    mInternalActivityNameUpdate = true;
                    mActivityNameField.setText(name + ACTIVITY_NAME_SUFFIX);
                } finally {
                    mInternalActivityNameUpdate = false;
                }

            }
        }
        updateLocationPathField(null);
    }

    private void onMinSdkFieldUpdated() {
        if (!mInternalMinSdkUpdate) {
            mMinSdkModifiedByUser = true;
        }
    }

    private void onApplicationFieldModified() {
        if (!mInternalApplicationNameUpdate) {
               mApplicationNameModifiedByUser = true;
               if (!mActivityNameModifiedByUser) {
                   String name = DescriptorsUtils.capitalize(mApplicationNameField.getText());
                   try {
                       mInternalActivityNameUpdate = true;
                       mActivityNameField.setText(name + ACTIVITY_NAME_SUFFIX);
                   } finally {
                       mInternalActivityNameUpdate = false;
                   }
               }
           }
    }

/**
* The location path field is either modified internally (from updateLocationPathField)
* or manually by the user when the custom_location mode is not set.
//Synthetic comment -- @@ -958,6 +1007,10 @@
* validate the page.
*/
private void onActivityNameFieldModified() {
        if (!mInternalActivityNameUpdate) {
            mActivityNameModifiedByUser = true;
        }

if (mInfo.isNewProject() && !mInternalActivityNameUpdate) {
mUserActivityName = mInfo.getActivityName();
validatePageComplete();
//Synthetic comment -- @@ -973,6 +1026,41 @@
private void onSdkTargetModified() {
IAndroidTarget target = mInfo.getSdkTarget();

        // Update the minimum SDK text field?
        // We do if one of two conditions are met:
        if (target != null) {
            boolean setMinSdk = false;
            int apiLevel = target.getVersion().getApiLevel();
            // 1. Has the user not manually edited the SDK field yet? If so, keep
            //    updating it to the selected value.
            if (!mMinSdkModifiedByUser) {
                setMinSdk = true;
            } else {
                // 2. Is the API level set to a higher level than the newly selected
                //    target SDK? If so, change it down to the new lower value.
                String s = mMinSdkVersionField.getText().trim();
                if (s.length() > 0) {
                    try {
                        int currentApi = Integer.parseInt(s);
                        if (currentApi > apiLevel) {
                            setMinSdk = true;
                        }
                    } catch (NumberFormatException nfe) {
                        // User may have typed something invalid -- ignore
                        ;
                    }
                }
            }
            if (setMinSdk) {
                try {
                    mInternalMinSdkUpdate = true;
                    mMinSdkVersionField.setText(Integer.toString(apiLevel));
                } finally {
                    mInternalMinSdkUpdate = false;
                }
            }
        }

loadSamplesForTarget(target);
enableLocationWidgets();
onSampleSelected();







