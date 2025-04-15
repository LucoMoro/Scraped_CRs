/*Automatically pick the best render target

This changeset adds a new option, on by default, which causes the
layout editor to always pick the best rendering target. The best
rendering target is typically the most recent one. This option can be
toggled in the rendering target drop down menu (but it applies
globally), and choosing a specific rendering target turns it off.

Change-Id:I53e48a38741f364f2b68525a4898aca69b1ae7b1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 29dcb34..8ca0c26 100644

//Synthetic comment -- @@ -37,6 +37,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderService;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -832,27 +833,32 @@
}
locale = Locale.create(language, region);

                    if (AdtPrefs.getPrefs().isAutoPickRenderTarget()) {
                        target = ConfigurationMatcher.findDefaultRenderTarget(chooser);
                    } else {
                        String targetString = values[1];
                        target = stringToTarget(chooser, targetString);
                        // See if we should "correct" the rendering target to a
                        // better version. If you're using a pre-release version
                        // of the render target, and a final release is
                        // available and installed, we should switch to that
                        // one instead.
                        if (target != null) {
                            AndroidVersion version = target.getVersion();
                            List<IAndroidTarget> targetList = chooser.getTargetList();
                            if (version.getCodename() != null && targetList != null) {
                                int targetApiLevel = version.getApiLevel() + 1;
                                for (IAndroidTarget t : targetList) {
                                    if (t.getVersion().getApiLevel() == targetApiLevel
                                            && t.isPlatform()) {
                                        target = t;
                                        break;
                                    }
}
}
                        } else {
                            target = ConfigurationMatcher.findDefaultRenderTarget(chooser);
}
}
}

//Synthetic comment -- @@ -878,7 +884,7 @@
}
try {
// Generate a persistent string from locale+target
            StringBuilder sb = new StringBuilder(32);
Locale locale = getLocale();
if (locale != null) {
// locale[0]/[1] can be null sometimes when starting Eclipse








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/TargetMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/TargetMenuListener.java
//Synthetic comment -- index c0f361c..71905f7 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;

//Synthetic comment -- @@ -40,17 +41,36 @@
class TargetMenuListener extends SelectionAdapter {
private final ConfigurationChooser mConfigChooser;
private final IAndroidTarget mTarget;
    private final boolean mPickBest;

TargetMenuListener(
@NonNull ConfigurationChooser configChooser,
            @Nullable IAndroidTarget target,
            boolean pickBest) {
mConfigChooser = configChooser;
mTarget = target;
        mPickBest = pickBest;
}

@Override
public void widgetSelected(SelectionEvent e) {
        IAndroidTarget target = mTarget;
        AdtPrefs prefs = AdtPrefs.getPrefs();
        if (mPickBest) {
            boolean autoPick = prefs.isAutoPickRenderTarget();
            autoPick = !autoPick;
            prefs.setAutoPickRenderTarget(autoPick);
            if (autoPick) {
                target = ConfigurationMatcher.findDefaultRenderTarget(mConfigChooser);
            } else {
                // Turn it off, but keep current target until another one is chosen
                return;
            }
        } else {
            // Manually picked some other target: turn off auto-pick
            prefs.setAutoPickRenderTarget(false);
        }
        mConfigChooser.selectTarget(target);
mConfigChooser.onRenderingTargetChange();
}

//Synthetic comment -- @@ -61,6 +81,16 @@
List<IAndroidTarget> targets = chooser.getTargetList();
boolean haveRecent = false;

        MenuItem menuItem = new MenuItem(menu, SWT.CHECK);
        menuItem.setText("Automatically Pick Best");
        menuItem.addSelectionListener(new TargetMenuListener(chooser, null, true));
        if (AdtPrefs.getPrefs().isAutoPickRenderTarget()) {
            menuItem.setSelection(true);
        }

        @SuppressWarnings("unused")
        MenuItem separator = new MenuItem(menu, SWT.SEPARATOR);

// Process in reverse order: most important targets first
assert targets instanceof RandomAccess;
for (int i = targets.size() - 1; i >= 0; i--) {
//Synthetic comment -- @@ -84,7 +114,7 @@
item.setSelection(true);
}

            item.addSelectionListener(new TargetMenuListener(chooser, target, false));
}

Rectangle bounds = combo.getBounds();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index 8a2877e..002dc63 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
public final static String PREFS_FIX_LEGACY_EDITORS = AdtPlugin.PLUGIN_ID + ".fixLegacyEditors"; //$NON-NLS-1$
public final static String PREFS_SHARED_LAYOUT_EDITOR = AdtPlugin.PLUGIN_ID + ".sharedLayoutEditor"; //$NON-NLS-1$
public final static String PREFS_PREVIEWS = AdtPlugin.PLUGIN_ID + ".previews"; //$NON-NLS-1$
    public final static String PREFS_AUTO_PICK_TARGET = AdtPlugin.PLUGIN_ID + ".autoPickTarget"; //$NON-NLS-1$

/** singleton instance */
private final static AdtPrefs sThis = new AdtPrefs();
//Synthetic comment -- @@ -103,6 +104,7 @@
private boolean mLintOnExport;
private AttributeSortOrder mAttributeSort;
private boolean mSharedLayoutEditor;
    private boolean mAutoPickTarget;
private RenderPreviewMode mPreviewMode = RenderPreviewMode.NONE;
private int mPreferXmlEditor;

//Synthetic comment -- @@ -259,6 +261,10 @@
mSharedLayoutEditor = mStore.getBoolean(PREFS_SHARED_LAYOUT_EDITOR);
}

        if (property == null || PREFS_AUTO_PICK_TARGET.equals(property)) {
            mAutoPickTarget = mStore.getBoolean(PREFS_AUTO_PICK_TARGET);
        }

if (property == null || PREFS_PREVIEWS.equals(property)) {
mPreviewMode = RenderPreviewMode.NONE;
String previewMode = mStore.getString(PREFS_PREVIEWS);
//Synthetic comment -- @@ -501,6 +507,7 @@
store.setDefault(PREFS_SPACE_BEFORE_CLOSE, true);
store.setDefault(PREFS_LINT_ON_SAVE, true);
store.setDefault(PREFS_LINT_ON_EXPORT, true);
        store.setDefault(PREFS_AUTO_PICK_TARGET, true);

// Defaults already handled; no need to write into map:
//store.setDefault(PREFS_ATTRIBUTE_SORT, AttributeSortOrder.LOGICAL.key);
//Synthetic comment -- @@ -579,4 +586,28 @@
store.setToDefault(PREFS_PREVIEWS);
}
}

    /**
     * Sets whether auto-pick render target mode is enabled.
     *
     * @return whether the layout editor should automatically pick the best render target
     */
    public boolean isAutoPickRenderTarget() {
        return mAutoPickTarget;
    }

    /**
     * Sets whether auto-pick render target mode is enabled.
     *
     * @param autoPick if true, auto pick the best render target in the layout editor
     */
    public void setAutoPickRenderTarget(boolean autoPick) {
        mAutoPickTarget = autoPick;
        IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
        if (autoPick) {
            store.setToDefault(PREFS_AUTO_PICK_TARGET);
        } else {
            store.setValue(PREFS_AUTO_PICK_TARGET, autoPick);
        }
    }
}







