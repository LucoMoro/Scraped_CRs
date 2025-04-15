/*Template support for minimum build target

This changeset adds a minBuildApi attribute to the <template>
element which can be used to require a minimum level of build
target (which can be higher than the template's required
minimum API). As with the minimum API, this can be specified
both on the root element, as well as on <option> elements.

Examples:
     name="New Blank Activity"
+    minBuildApi="12"
     description="Creates a new blank activity, with optional inner navigation.">
    <parameter id="navType" ...>
         <option id="tabs_pager" minApi="14">Tabs + Swipe</option>
+        <option id="pager_strip" minApi="14" minBuildApi="15">Swipe Views + Title Strip</option>
         <option id="dropdown" minApi="14">Dropdown</option>

Change-Id:I9d5b426bf2d440103a57f2a99ba6713cd6e7afaa*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/ActivityPage.java
//Synthetic comment -- index a74ffa8..4a7c2ca 100644

//Synthetic comment -- @@ -239,7 +239,8 @@
"Select an activity type");
} else {
TemplateHandler templateHandler = mValues.activityValues.getTemplateHandler();
                status = templateHandler.validateTemplate(mValues.minSdkLevel);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 69d3d1d..9377dc0 100644

//Synthetic comment -- @@ -698,7 +698,8 @@
// Validation

private void validatePage() {
        IStatus status = mValues.template.validateTemplate(mValues.minSdkLevel);
if (status != null && !status.isOK()) {
updateDecorator(mApplicationDec, null, true);
updateDecorator(mPackageDec, null, true);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index aa7e134..824c542 100644

//Synthetic comment -- @@ -68,6 +68,7 @@
private static final String ATTR_COPY_ICONS = "copyIcons";     //$NON-NLS-1$
static final String ATTR_TARGET_API = "targetApi";             //$NON-NLS-1$
static final String ATTR_MIN_API = "minApi";                   //$NON-NLS-1$
static final String ATTR_BUILD_API = "buildApi";               //$NON-NLS-1$
static final String ATTR_REVISION = "revision";                //$NON-NLS-1$
static final String ATTR_MIN_API_LEVEL = "minApiLevel";        //$NON-NLS-1$
//Synthetic comment -- @@ -155,7 +156,7 @@
mTemplatePage = new NewTemplatePage(activityValues, false);
addPage(mTemplatePage);
}
            mTemplatePage.setCustomMinSdk(mValues.minSdkLevel);
return mTemplatePage;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 7e8878f..5c219b7 100644

//Synthetic comment -- @@ -95,4 +95,13 @@

/** Folder where the project should be created. */
public String projectLocation;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index aa35bda..498d42a 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_DEFAULT;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_ID;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_NAME;
//Synthetic comment -- @@ -102,6 +103,7 @@
private final NewTemplateWizardState mValues;
private final boolean mChooseProject;
private int mCustomMinSdk = -1;
private boolean mIgnore;
private boolean mShown;
private Control mFirst;
//Synthetic comment -- @@ -131,13 +133,16 @@
}

/**
     * @param minSdk a minimum SDK to use, provided chooseProject is
     *            false. If it is true, then the minimum SDK used for validation
     *            will be the one of the project
*/
    void setCustomMinSdk(int minSdk) {
assert !mChooseProject;
mCustomMinSdk = minSdk;
}

@Override
//Synthetic comment -- @@ -353,6 +358,7 @@
int selected = 0;
List<String> ids = Lists.newArrayList();
List<Integer> minSdks = Lists.newArrayList();
List<String> labels = Lists.newArrayList();
for (int i = 0, n = options.size(); i < n; i++) {
Element option = options.get(i);
//Synthetic comment -- @@ -380,7 +386,20 @@
minSdk = 1;
}
}
minSdks.add(minSdk);
ids.add(optionId);
labels.add(optionLabel);
}
//Synthetic comment -- @@ -388,6 +407,8 @@
parameter.control = combo;
combo.setData(ATTR_ID, ids.toArray(new String[ids.size()]));
combo.setData(ATTR_MIN_API, minSdks.toArray(new Integer[minSdks.size()]));
assert labels.size() > 0;
combo.setItems(labels.toArray(new String[labels.size()]));
combo.select(selected);
//Synthetic comment -- @@ -534,9 +555,9 @@
// ---- Validation ----

private void validatePage() {
        int currentMinSdk = getMinSdk();
        int minSdk = currentMinSdk;
        IStatus status = mValues.getTemplateHandler().validateTemplate(minSdk);

if (status == null || status.isOK()) {
if (mChooseProject && mValues.project == null) {
//Synthetic comment -- @@ -569,14 +590,29 @@
Combo combo = (Combo) parameter.control;
Integer[] optionIds = (Integer[]) combo.getData(ATTR_MIN_API);
int index = combo.getSelectionIndex();
if (index != -1 && index < optionIds.length) {
Integer requiredMinSdk = optionIds[index];
                        if (requiredMinSdk > currentMinSdk) {
status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
String.format(
"This template requires a minimum SDK version of at " +
"least %1$d, and the current min version is %2$d",
                                        requiredMinSdk, currentMinSdk));
}
}
}
//Synthetic comment -- @@ -598,6 +634,10 @@
return mChooseProject ? mValues.getMinSdk() : mCustomMinSdk;
}

private void updateDecorator(ControlDecoration decorator, IStatus status, String help) {
if (help != null && !help.isEmpty()) {
decorator.setDescriptionText(status != null ? status.getMessage() : help);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java
//Synthetic comment -- index 02bbb10..9986810 100644

//Synthetic comment -- @@ -24,8 +24,6 @@

import com.android.annotations.NonNull;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;

import org.eclipse.core.resources.IProject;
import org.eclipse.ltk.core.refactoring.Change;
//Synthetic comment -- @@ -65,6 +63,10 @@
/** The minimum API level to use for this template */
public int minSdkLevel;

/** Location of the template being created */
private File mTemplateLocation;

//Synthetic comment -- @@ -112,6 +114,15 @@
return manifest.getMinSdkVersion();
}

/** Computes the changes this wizard will make */
@NonNull
List<Change> computeChanges() {
//Synthetic comment -- @@ -124,14 +135,7 @@
parameters.put(ATTR_MIN_API, manifest.getMinSdkVersion());
parameters.put(ATTR_MIN_API_LEVEL, manifest.getMinSdkName());
parameters.put(ATTR_TARGET_API, manifest.getTargetSdkVersion());
        IAndroidTarget target = Sdk.getCurrent().getTarget(project);
        int buildApi;
        if (target != null) {
            buildApi = target.getVersion().getApiLevel();
        } else {
            buildApi = manifest.getTargetSdkVersion();
        }
        parameters.put(NewProjectWizard.ATTR_BUILD_API, buildApi);

return getTemplateHandler().render(project, parameters);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 43ade9c..bdbe50d 100644

//Synthetic comment -- @@ -970,12 +970,13 @@
/**
* Validates this template to make sure it's supported
* @param currentMinSdk the minimum SDK in the project, or -1 or 0 if unknown (e.g. codename)
*
* @return a status object with the error, or null if there is no problem
*/
@SuppressWarnings("cast") // In Eclipse 3.6.2 cast below is needed
@Nullable
    public IStatus validateTemplate(int currentMinSdk) {
TemplateMetadata template = getTemplate();
if (template == null) {
return null;
//Synthetic comment -- @@ -996,6 +997,13 @@
"least %1$d, and the current min version is %2$d",
templateMinSdk, currentMinSdk));
}

return null;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java
//Synthetic comment -- index e4b117f..2e78093 100644

//Synthetic comment -- @@ -16,6 +16,7 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;

import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_MIN_API;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewProjectWizard.ATTR_REVISION;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_DESCRIPTION;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_FORMAT;
//Synthetic comment -- @@ -50,6 +51,7 @@
private final Map<String, Parameter> mParameterMap;
private List<Pair<String, Integer>> mDependencies;
private Integer mMinApi;
private Integer mRevision;

TemplateMetadata(@NonNull Document document) {
//Synthetic comment -- @@ -121,6 +123,24 @@
return mMinApi.intValue();
}

public int getRevision() {
if (mRevision == null) {
mRevision = 1;







