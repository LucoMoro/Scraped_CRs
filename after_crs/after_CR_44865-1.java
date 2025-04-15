/*Fix a couple of bugs related to multiconfig switching

Change-Id:I0b441971de02fc4fc3da78e4e411828ca3eedbbc*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index d42c3f6..151301a 100644

//Synthetic comment -- @@ -102,6 +102,9 @@
@NonNull Configuration parent) {
ComplementingConfiguration configuration =
new ComplementingConfiguration(other.mConfigChooser, parent);
        configuration.setDisplayName(other.getDisplayName());
        configuration.setActivity(other.getActivity());
        configuration.mUpdateDisplayName = other.mUpdateDisplayName;
configuration.mOverrideLocale = other.mOverrideLocale;
configuration.mOverrideTarget = other.mOverrideTarget;
configuration.mOverrideDevice = other.mOverrideDevice;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 1615722..3476964 100644

//Synthetic comment -- @@ -197,6 +197,7 @@
copy.mLocale = original.getLocale();
copy.mUiMode = original.getUiMode();
copy.mNightMode = original.getNightMode();
        copy.mDisplayName = original.getDisplayName();

return copy;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/NestedConfiguration.java
//Synthetic comment -- index 432abdb..7c2559c 100644

//Synthetic comment -- @@ -83,6 +83,9 @@
@NonNull Configuration parent) {
NestedConfiguration configuration =
new NestedConfiguration(other.mConfigChooser, parent);
        configuration.setDisplayName(values.getDisplayName());
        configuration.setActivity(values.getActivity());

configuration.mOverrideLocale = other.mOverrideLocale;
if (configuration.mOverrideLocale) {
configuration.setLocale(values.getLocale(), true);







