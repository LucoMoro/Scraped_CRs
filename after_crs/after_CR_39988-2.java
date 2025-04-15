/*Add support for theme selection in new projects

The project templates now contain a variable for controlling the
initial theme used with the project. This changeset adds support for
this in the New Project wizard, since it has custom UI code rather
than being data driven like most of the templates. In particular, it
conditionally adds the theme selector if it's available in the
template, and it handles validation based on the build target.
There's some code reorganization to be able to share UI code between
the New Template Page and the New Project Page.

Change-Id:I53e5602a0a983c4a1aea9ae7fc5cd1054ccd4e69*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 9377dc0..54e8780 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import static com.android.ide.eclipse.adt.AdtUtils.extractClassName;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewTemplatePage.WIZARD_PAGE_WIDTH;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.ATTR_ID;

import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -82,6 +83,7 @@

private final NewProjectWizardState mValues;
private Map<String, Integer> mMinNameToApi;
    private Parameter mThemeParameter;

private Text mProjectText;
private Text mPackageText;
//Synthetic comment -- @@ -108,6 +110,7 @@
private ControlDecoration mPackageDec;
private ControlDecoration mBuildTargetDec;
private ControlDecoration mMinSdkDec;
    private Combo mThemeCombo;

NewProjectPage(NewProjectWizardState values) {
super("newAndroidApp"); //$NON-NLS-1$
//Synthetic comment -- @@ -263,6 +266,22 @@
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);

        TemplateMetadata metadata = mValues.template.getTemplate();
        if (metadata != null) {
            mThemeParameter = metadata.getParameter("baseTheme"); //$NON-NLS-1$
            if (mThemeParameter != null && mThemeParameter.element != null) {
                Label themeLabel = new Label(container, SWT.NONE);
                themeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
                themeLabel.setText("Theme:");

                mThemeCombo = NewTemplatePage.createOptionCombo(mThemeParameter, container,
                        mValues.parameters, this, this);
                mThemeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                new Label(container, SWT.NONE);
                new Label(container, SWT.NONE);
            }
        }

new Label(container, SWT.NONE);
new Label(container, SWT.NONE);
new Label(container, SWT.NONE);
//Synthetic comment -- @@ -569,6 +588,18 @@
mLocationText.setText(dir);
mValues.projectLocation = dir;
}
        } else if (source == mThemeCombo) {
            String[] optionIds = (String[]) mThemeCombo.getData(ATTR_ID);
            int index = mThemeCombo.getSelectionIndex();
            if (index != -1 && index < optionIds.length) {
                String optionId = optionIds[index];
                Parameter parameter = NewTemplatePage.getParameter(mThemeCombo);
                if (parameter != null) {
                    parameter.value = optionId;
                    parameter.edited = optionId != null && !optionId.toString().isEmpty();
                    mValues.parameters.put(parameter.id, optionId);
                }
            }
}

validatePage();
//Synthetic comment -- @@ -755,6 +786,12 @@
}
}
}

            if (mThemeParameter != null
                    && (status == null || status.getSeverity() != IStatus.ERROR)) {
                status = NewTemplatePage.validateCombo(status, mThemeParameter,
                        mValues.minSdkLevel, mValues.getBuildApi());
            }
}

setPageComplete(status == null || status.getSeverity() != IStatus.ERROR);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizard.java
//Synthetic comment -- index 5c1ab96..bb20c39 100644

//Synthetic comment -- @@ -386,5 +386,6 @@
parameters.put(ATTR_TARGET_API, 15);
parameters.put(ATTR_BUILD_API, mValues.target.getVersion().getApiLevel());
parameters.put(ATTR_COPY_ICONS, !mValues.createIcon);
        parameters.putAll(mValues.parameters);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectWizardState.java
//Synthetic comment -- index 5c219b7..3af40a8 100644

//Synthetic comment -- @@ -21,6 +21,9 @@
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.sdklib.IAndroidTarget;

import java.util.HashMap;
import java.util.Map;

/**
* Value object which holds the current state of the wizard pages for the
* {@link NewProjectWizard}
//Synthetic comment -- @@ -96,6 +99,9 @@
/** Folder where the project should be created. */
public String projectLocation;

    /** Configured parameters, by id */
    public final Map<String, Object> parameters = new HashMap<String, Object>();

/**
* Returns the build target API level
*








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 2d5e095..0a62c8b 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateHandler.PREVIEW_WIDTH;
import static com.android.sdklib.SdkConstants.CLASS_ACTIVITY;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
//Synthetic comment -- @@ -344,80 +345,16 @@
1, 1));
label.setText(name);

                        Combo combo = createOptionCombo(parameter, container, mValues.parameters,
                                this, this);
combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
2, 1));

if (mFirst == null) {
mFirst = combo;
}

if (help != null && !help.isEmpty()) {
ControlDecoration decoration = createFieldDecoration(id, combo, help);
}
break;
//Synthetic comment -- @@ -466,6 +403,87 @@
parent.getParent().redraw();
}

    @NonNull
    static Combo createOptionCombo(
            @NonNull Parameter parameter,
            @NonNull Composite container,
            @NonNull Map<String, Object> valueMap,
            @NonNull SelectionListener selectionListener,
            @NonNull FocusListener focusListener) {
        Combo combo = new Combo(container, SWT.READ_ONLY);

        List<Element> options = parameter.getOptions();
        assert options.size() > 0;
        int selected = 0;
        List<String> ids = Lists.newArrayList();
        List<Integer> minSdks = Lists.newArrayList();
        List<Integer> minBuildApis = Lists.newArrayList();
        List<String> labels = Lists.newArrayList();
        for (int i = 0, n = options.size(); i < n; i++) {
            Element option = options.get(i);
            String optionId = option.getAttribute(ATTR_ID);
            assert optionId != null && !optionId.isEmpty() : ATTR_ID;
            String isDefault = option.getAttribute(ATTR_DEFAULT);
            if (isDefault != null && !isDefault.isEmpty() &&
                    Boolean.valueOf(isDefault)) {
                selected = i;
            }
            NodeList childNodes = option.getChildNodes();
            assert childNodes.getLength() == 1 &&
                    childNodes.item(0).getNodeType() == Node.TEXT_NODE;
            String optionLabel = childNodes.item(0).getNodeValue().trim();

            String minApiString = option.getAttribute(ATTR_MIN_API);
            int minSdk = 1;
            if (minApiString != null && !minApiString.isEmpty()) {
                try {
                    minSdk = Integer.parseInt(minApiString);
                } catch (NumberFormatException nufe) {
                    // Templates aren't allowed to contain codenames, should
                    // always be an integer
                    AdtPlugin.log(nufe, null);
                    minSdk = 1;
                }
            }
            String minBuildApiString = option.getAttribute(ATTR_MIN_BUILD_API);
            int minBuildApi = 1;
            if (minBuildApiString != null && !minBuildApiString.isEmpty()) {
                try {
                    minBuildApi = Integer.parseInt(minBuildApiString);
                } catch (NumberFormatException nufe) {
                    // Templates aren't allowed to contain codenames, should
                    // always be an integer
                    AdtPlugin.log(nufe, null);
                    minBuildApi = 1;
                }
            }
            minSdks.add(minSdk);
            minBuildApis.add(minBuildApi);
            ids.add(optionId);
            labels.add(optionLabel);
        }
        combo.setData(parameter);
        parameter.control = combo;
        combo.setData(ATTR_ID, ids.toArray(new String[ids.size()]));
        combo.setData(ATTR_MIN_API, minSdks.toArray(new Integer[minSdks.size()]));
        combo.setData(ATTR_MIN_BUILD_API, minBuildApis.toArray(
                new Integer[minBuildApis.size()]));
        assert labels.size() > 0;
        combo.setItems(labels.toArray(new String[labels.size()]));
        combo.select(selected);

        combo.addSelectionListener(selectionListener);
        combo.addFocusListener(focusListener);

        valueMap.put(parameter.id, ids.get(selected));

        if (parameter.help != null && !parameter.help.isEmpty()) {
            combo.setToolTipText(parameter.help);
        }

        return  combo;
    }

private void setPreview(String thumb) {
if (thumb == null) {
return;
//Synthetic comment -- @@ -543,7 +561,7 @@

/** Returns the parameter associated with the given control */
@Nullable
    static Parameter getParameter(Control control) {
return (Parameter) control.getData();
}

//Synthetic comment -- @@ -595,34 +613,7 @@

if (status == null || status.isOK()) {
if (parameter.control instanceof Combo) {
                    status = validateCombo(status, parameter, minSdk, buildApi);
}
}
}
//Synthetic comment -- @@ -638,6 +629,39 @@
}
}

    /** Validates the given combo */
    static IStatus validateCombo(IStatus status, Parameter parameter, int minSdk, int buildApi) {
        Combo combo = (Combo) parameter.control;
        Integer[] optionIds = (Integer[]) combo.getData(ATTR_MIN_API);
        int index = combo.getSelectionIndex();

        // Check minSdk
        if (index != -1 && index < optionIds.length) {
            Integer requiredMinSdk = optionIds[index];
            if (requiredMinSdk > minSdk) {
                status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    String.format(
                            "%1$s \"%2$s\" requires a minimum SDK version of at " +
                            "least %3$d, and the current min version is %4$d",
                            parameter.name, combo.getItems()[index], requiredMinSdk, minSdk));
            }
        }

        // Check minimum build target
        optionIds = (Integer[]) combo.getData(ATTR_MIN_BUILD_API);
        if (index != -1 && index < optionIds.length) {
            Integer requiredBuildApi = optionIds[index];
            if (requiredBuildApi > buildApi) {
                status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    String.format(
                        "%1$s \"%2$s\"  requires a build target API version of at " +
                        "least %3$d, and the current min version is %4$d",
                        parameter.name, combo.getItems()[index], requiredBuildApi, buildApi));
            }
        }
        return status;
    }

private int getMinSdk() {
return mChooseProject ? mValues.getMinSdk() : mCustomMinSdk;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateMetadata.java
//Synthetic comment -- index 699f7cc..4b26377 100644

//Synthetic comment -- @@ -406,4 +406,21 @@
List<Parameter> getParameters() {
return mParameters;
}

    /**
     * Returns the parameter of the given id, or null if not found
     *
     * @param id the id of the target parameter
     * @return the corresponding parameter, or null if not found
     */
    @Nullable
    public Parameter getParameter(@NonNull String id) {
        for (Parameter parameter : mParameters) {
            if (id.equals(parameter.id)) {
                return parameter;
            }
        }

        return null;
    }
}







