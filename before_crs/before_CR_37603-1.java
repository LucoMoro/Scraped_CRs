/*Fix new project wizard's package validation

The wizard suggests "com.example." as a package prefix. However, the
validation would catch this as an invalid package and would just say
"Enter a package name". If you were editing "com.example.foo" this was
misleading. You now get a more specific warning when you've entered a
package name that has the example package as a prefix.

This CL also tweaks the focus handler which selects the prefix on
focus-entry to not include the separating dot in the prefix.

Change-Id:I659bb03515057eb5c760c51366869f4e0983f58a*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 800366a..869fc2f 100644

//Synthetic comment -- @@ -518,7 +518,12 @@
} else if (source == mPackageText) {
tip = mPackageDec.getDescriptionText();
if (mPackageText.getText().startsWith(SAMPLE_PACKAGE_PREFIX)) {
                mPackageText.setSelection(0, SAMPLE_PACKAGE_PREFIX.length());
}
}
mTipLabel.setText(tip);
//Synthetic comment -- @@ -539,12 +544,14 @@
IStatus status = appStatus;

IStatus projectStatus = validateProjectName();
        if (projectStatus != null && (status == null || status.getSeverity() != IStatus.ERROR)) {
status = projectStatus;
}

IStatus packageStatus = validatePackageName();
        if (packageStatus != null && (status == null || status.getSeverity() != IStatus.ERROR)) {
status = packageStatus;
}

//Synthetic comment -- @@ -600,8 +607,18 @@

IStatus status;
if (mValues.packageName == null || mValues.packageName.startsWith(SAMPLE_PACKAGE_PREFIX)) {
            status = new Status(IStatus.ERROR, AdtPlugin.PLUGIN_ID,
                    "Package name must be specified.");
} else {
status = ApplicationInfoPage.validatePackage(mValues.packageName);
}







