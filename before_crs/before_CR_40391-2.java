/*Template infrastructure fixes

First, the minBuildApi flag was set incorrectly for per-file templates
(it was using minSdkVersion instead). Second, boolean type variables
could sometimes provide a string version of the value instead to the
templates, which could blow up template rendering.

Change-Id:I6b1570277c30b84ef47857b16718d052ec7f74e5*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplatePage.java
//Synthetic comment -- index 0a62c8b..5f10ce0 100644

//Synthetic comment -- @@ -239,9 +239,9 @@

String id = parameter.id;
assert id != null && !id.isEmpty() : ATTR_ID;
                String value = defaults.get(id);
if (value == null) {
                    value = parameter.initial;
}

String name = parameter.name;
//Synthetic comment -- @@ -289,8 +289,8 @@
2, 1));
}

                        if (value != null && !value.isEmpty()){
                            text.setText(value);
mValues.parameters.put(id, value);
}

//Synthetic comment -- @@ -319,8 +319,8 @@
checkBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
2, 1));

                        if (value != null && !value.isEmpty()){
                            Boolean selected = Boolean.valueOf(value);
checkBox.setSelection(selected);
mValues.parameters.put(id, value);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewTemplateWizardState.java
//Synthetic comment -- index 00183d2..6101161 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import com.android.ide.eclipse.adt.internal.assetstudio.ConfigureAssetSetPage;
import com.android.ide.eclipse.adt.internal.assetstudio.CreateAssetSetWizardState;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
//Synthetic comment -- @@ -124,13 +126,17 @@
return manifest.getMinSdkVersion();
}

    /** Returns the min SDK version to use */
int getBuildApi() {
if (project == null) {
return -1;
}
        ManifestInfo manifest = ManifestInfo.get(project);
        return manifest.getMinSdkVersion();
}

/** Computes the changes this wizard will make */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/Parameter.java
//Synthetic comment -- index 9c31033..a9a3f33 100644

//Synthetic comment -- @@ -237,7 +237,11 @@
constraints = EnumSet.noneOf(Constraint.class);
}

        value = initial;
}

Parameter(@NonNull Type type, @NonNull String id, @NonNull String initialValue) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 1c3b862..e214788 100644

//Synthetic comment -- @@ -98,8 +98,16 @@
* and merging into existing files
*/
class TemplateHandler {
    /** Highest supported format; templates with a higher number will be skipped */
    static final int CURRENT_FORMAT = 1;

/**
* Special marker indicating that this path refers to the special shared
//Synthetic comment -- @@ -396,7 +404,14 @@
String id = attributes.getValue(ATTR_ID);
if (!paramMap.containsKey(id)) {
String value = attributes.getValue(ATTR_DEFAULT);
                            paramMap.put(id, value);
}
} else if (TAG_GLOBAL.equals(name)) {
String id = attributes.getValue(ATTR_ID);







