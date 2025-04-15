/*Make project-wide locale and render target settings

This changeset makes the locale and render target settings
project-wide, meaning that whenever you change the locale, or the
render target, the given locale or render target will be shown in all
layouts from the same project that you switch to.

The Locale and Render Target combo boxes are moved in to the top line
of the configuration chooser, and the dock mode and daytime mode
toggles are moved to the second line, such that the project-wide
settings are on the first line and the layout-specific settings are on
the second line.

Now, whenever you switch to a new language and open a different
layout, the language of the new layout will be forced to the current
locale. The only exception to this is for locale-specific layouts. If
you create a specific layout for a given locale, then that layout will
always be shown using its own locale, regardless of the project-wide
setting.

Change-Id:Idbe7e465a74afccfc0c68005a8784eafb2e8878d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 0e927fa..5c111c2 100644

//Synthetic comment -- @@ -47,10 +47,12 @@
import com.android.resources.ScreenOrientation;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import com.android.util.Pair;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.draw2d.geometry.Rectangle;
//Synthetic comment -- @@ -97,9 +99,23 @@
*   loading.<br>
*/
public class ConfigurationComposite extends Composite {
    private final static String SEP = ":"; //$NON-NLS-1$
    private final static String SEP_LOCALE = "-"; //$NON-NLS-1$

    /**
     * Setting name for project-wide setting controlling rendering target and locale which
     * is shared for all files
     */
    public final static QualifiedName NAME_RENDER_STATE =
        new QualifiedName(AdtPlugin.PLUGIN_ID, "render");//$NON-NLS-1$

    /**
     * Settings name for file-specific configuration preferences, such as which theme or
     * device to render the current layout with
     */
public final static QualifiedName NAME_CONFIG_STATE =
new QualifiedName(AdtPlugin.PLUGIN_ID, "state");//$NON-NLS-1$

private final static String THEME_SEPARATOR = "----------"; //$NON-NLS-1$

private final static int LOCALE_LANG = 0;
//Synthetic comment -- @@ -199,9 +215,6 @@
* rendering to its original configuration.
*/
private class ConfigState {
LayoutDevice device;
String configName;
ResourceQualifier[] locale;
//Synthetic comment -- @@ -220,7 +233,7 @@
sb.append(SEP);
sb.append(configName);
sb.append(SEP);
                if (isLocaleSpecificLayout() && locale != null) {
if (locale[0] != null && locale[1] != null) {
// locale[0]/[1] can be null sometimes when starting Eclipse
sb.append(((LanguageQualifier) locale[0]).getValue());
//Synthetic comment -- @@ -235,10 +248,10 @@
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);

                // We used to store the render target here in R9. Leave a marker
                // to ensure that we don't reuse this slot; add new extra fields after it.
                sb.append(SEP);
}

return sb.toString();
//Synthetic comment -- @@ -254,6 +267,8 @@
if (config != null) {
configName = values[1];

                            // Load locale. Note that this can get overwritten by the
                            // project-wide settings read below.
locale = new ResourceQualifier[2];
String locales[] = values[2].split(SEP_LOCALE);
if (locales.length >= 2) {
//Synthetic comment -- @@ -275,12 +290,17 @@
night = NightMode.NOTNIGHT;
}

                            // element 7/values[6]: used to store render target in R9.
                            // No longer stored here. If adding more data, make
                            // sure you leave 7 alone.

                            Pair<ResourceQualifier[], IAndroidTarget> pair = loadRenderState();

                            // We only use the "global" setting
                            if (!isLocaleSpecificLayout()) {
                                locale = pair.getFirst();
}
                            target = pair.getSecond();

return true;
}
//Synthetic comment -- @@ -293,67 +313,40 @@

@Override
public String toString() {
            return getData();
}
    }

    /**
     * Returns a String id to represent an {@link IAndroidTarget} which can be translated
     * back to an {@link IAndroidTarget} by the matching {@link #stringToTarget}. The id
     * will never contain the {@link #SEP} character.
     *
     * @param target the target to return an id for
     * @return an id for the given target; never null
     */
    private String targetToString(IAndroidTarget target) {
        return target.getFullName().replace(SEP, "");  //$NON-NLS-1$
    }

    /**
     * Returns an {@link IAndroidTarget} that corresponds to the given id that was
     * originally returned by {@link #targetToString}. May be null, if the platform is no
     * longer available, or if the platform list has not yet been initialized.
     *
     * @param id the id that corresponds to the desired platform
     * @return an {@link IAndroidTarget} that matches the given id, or null
     */
    private IAndroidTarget stringToTarget(String id) {
        if (mTargetList != null && mTargetList.size() > 0) {
            for (IAndroidTarget target : mTargetList) {
                if (id.equals(targetToString(target))) {
                    return target;
}
}
}

        return null;
}

/**
//Synthetic comment -- @@ -377,11 +370,11 @@

GridLayout gl;
GridData gd;
        int cols = 7;  // device+config+dock+day+separator*2+theme

        // ---- First line: editing config display, locale, theme, create-button
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(5, false));
gl.marginWidth = gl.marginHeight = 0;
gl.marginTop = 3;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -407,25 +400,13 @@
mLocaleCombo.add("Locale"); //$NON-NLS-1$  // Dummy place holders
mLocaleCombo.add("Locale"); //$NON-NLS-1$

        mTargetCombo = new Combo(labelParent, SWT.DROP_DOWN | SWT.READ_ONLY);
        mTargetCombo.add("Android AOSP"); //$NON-NLS-1$  // Dummy place holders
        mTargetCombo.add("Android AOSP"); //$NON-NLS-1$
        mTargetCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                onRenderingTargetChange();
}
});

//Synthetic comment -- @@ -474,6 +455,32 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

        mDockCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mDockCombo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
        for (DockMode mode : DockMode.values()) {
            mDockCombo.add(mode.getLongDisplayValue());
        }
        mDockCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onDockChange();
            }
        });

        mNightCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mNightCombo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
        for (NightMode mode : NightMode.values()) {
            mNightCombo.add(mode.getLongDisplayValue());
        }
        mNightCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onDayChange();
            }
        });

mThemeCombo = new Combo(this, SWT.READ_ONLY | SWT.DROP_DOWN);
mThemeCombo.setLayoutData(new GridData(
GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
//Synthetic comment -- @@ -485,22 +492,6 @@
onThemeChange();
}
});
}

// ---- Init and reset/reload methods ----
//Synthetic comment -- @@ -1008,8 +999,11 @@
* Finds a locale matching the config from a file.
* @param language the language qualifier or null if none is set.
* @param region the region qualifier or null if none is set.
     * @return true if there was a change in the combobox as a result of applying the locale
*/
    private boolean setLocaleCombo(ResourceQualifier language, ResourceQualifier region) {
        boolean changed = false;

// find the locale match. Since the locale list is based on the content of the
// project resources there must be an exact match.
// The only trick is that the region could be null in the fileConfig but in our
//Synthetic comment -- @@ -1026,16 +1020,24 @@
if (RegionQualifier.FAKE_REGION_VALUE.equals(
((RegionQualifier)locale[LOCALE_REGION]).getValue())) {
// match!
                        if (mLocaleCombo.getSelectionIndex() != i) {
                            mLocaleCombo.select(i);
                            changed = true;
                        }
break;
}
} else if (region.equals(locale[LOCALE_REGION])) {
// match!
                    if (mLocaleCombo.getSelectionIndex() != i) {
                        mLocaleCombo.select(i);
                        changed = true;
                    }
break;
}
}
}

        return changed;
}

private void updateConfigDisplay(FolderConfiguration fileConfig) {
//Synthetic comment -- @@ -1786,6 +1788,9 @@
if (computeCurrentConfig() &&  mListener != null) {
mListener.onConfigurationChange();
}

        // Store locale project-wide setting
        saveRenderState();
}

private void onDockChange() {
//Synthetic comment -- @@ -1837,6 +1842,9 @@
if (computeOk &&  mListener != null) {
mListener.onConfigurationChange();
}

        // Store project-wide render-target setting
        saveRenderState();
}

/**
//Synthetic comment -- @@ -2028,5 +2036,170 @@
mEditedConfig = null;
onXmlModelLoaded();
}

    /**
     * Syncs this configuration to the project wide locale and render target settings. The
     * locale may ignore the project-wide setting if it is a locale-specific
     * configuration.
     *
     * @return true if one or both of the toggles were changed, false if there were no
     *         changes
     */
    public boolean syncRenderState() {
        if (mEditedConfig == null) {
            // Startup; ignore
            return false;
        }

        boolean localeChanged = false;
        boolean renderTargetChanged = false;

        // When a page is re-activated, force the toggles to reflect the current project
        // state

        Pair<ResourceQualifier[], IAndroidTarget> pair = loadRenderState();

        // Only sync the locale if this layout is not already a locale-specific layout!
        if (!isLocaleSpecificLayout()) {
            ResourceQualifier[] locale = pair.getFirst();
            if (locale != null) {
                localeChanged = setLocaleCombo(locale[0], locale[1]);
            }
        }

        // Sync render target
        IAndroidTarget target = pair.getSecond();
        if (target != null) {
            int targetIndex = mTargetList.indexOf(target);
            if (targetIndex != mTargetCombo.getSelectionIndex()) {
                mTargetCombo.select(targetIndex);
                renderTargetChanged = true;
            }
        }

        if (!renderTargetChanged && !localeChanged) {
            return false;
        }

        // Update the locale and/or the render target. This code contains a logical
        // merge of the onRenderingTargetChange() and onLocaleChange() methods, combined
        // such that we don't duplicate work.

        if (renderTargetChanged) {
            if (mListener != null && mRenderingTarget != null) {
                mListener.onRenderingTargetPreChange(mRenderingTarget);
            }
            int targetIndex = mTargetCombo.getSelectionIndex();
            mRenderingTarget = mTargetList.get(targetIndex);
        }

        // Compute the new configuration; we want to do this both for locale changes
        // and for render targets.
        boolean computeOk = computeCurrentConfig();

        if (renderTargetChanged) {
            // force a theme update to reflect the new rendering target.
            // This must be done after computeCurrentConfig since it'll depend on the currentConfig
            // to figure out the theme list.
            updateThemes();

            if (mListener != null && mRenderingTarget != null) {
                mListener.onRenderingTargetPostChange(mRenderingTarget);
            }
        }

        // For both locale and render target changes
        if (computeOk &&  mListener != null) {
            mListener.onConfigurationChange();
        }

        return true;
    }

    /**
     * Loads the render state (the locale and the render target, which are shared among
     * all the layouts meaning that changing it in one will change it in all) and returns
     * the current project-wide locale and render target to be used.
     *
     * @return a pair of locale resource qualifiers and render target
     */
    private Pair<ResourceQualifier[], IAndroidTarget> loadRenderState() {
        IProject project = mEditedFile.getProject();
        try {
            String data = project.getPersistentProperty(NAME_RENDER_STATE);
            if (data != null) {
                ResourceQualifier[] locale = null;
                IAndroidTarget target = null;

                String[] values = data.split(SEP);
                if (values.length == 2) {
                    locale = new ResourceQualifier[2];
                    String locales[] = values[0].split(SEP_LOCALE);
                    if (locales.length >= 2) {
                        if (locales[0].length() > 0) {
                            locale[0] = new LanguageQualifier(locales[0]);
                        }
                        if (locales[1].length() > 0) {
                            locale[1] = new RegionQualifier(locales[1]);
                        }
                    }
                    target = stringToTarget(values[1]);
                }

                return Pair.of(locale, target);
            }

            ResourceQualifier[] any = new ResourceQualifier[] {
                    new LanguageQualifier(LanguageQualifier.FAKE_LANG_VALUE),
                    new RegionQualifier(RegionQualifier.FAKE_REGION_VALUE)
            };

            return Pair.of(any, findDefaultRenderTarget());
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }

        return null;
    }

    /** Returns true if the current layout is locale-specific */
    private boolean isLocaleSpecificLayout() {
        return mEditedConfig == null || mEditedConfig.getLanguageQualifier() != null;
    }

    /**
     * Saves the render state (the current locale and render target settings) into the
     * project wide settings storage
     */
    private void saveRenderState() {
        IProject project = mEditedFile.getProject();
        try {
            int index = mLocaleCombo.getSelectionIndex();
            ResourceQualifier[] locale = mLocaleList.get(index);
            index = mTargetCombo.getSelectionIndex();
            IAndroidTarget target = mTargetList.get(index);

            // Generate a persistent string from locale+target
            StringBuilder sb = new StringBuilder();
            if (locale != null) {
                if (locale[0] != null && locale[1] != null) {
                    // locale[0]/[1] can be null sometimes when starting Eclipse
                    sb.append(((LanguageQualifier) locale[0]).getValue());
                    sb.append(SEP_LOCALE);
                    sb.append(((RegionQualifier) locale[1]).getValue());
                }
            }
            sb.append(SEP);
            if (target != null) {
                sb.append(targetToString(target));
                sb.append(SEP);
            }

            String data = sb.toString();
            project.setPersistentProperty(NAME_RENDER_STATE, data);
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }
    }
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5506319..430ebce 100644

//Synthetic comment -- @@ -245,6 +245,14 @@
private int mTargetSdkVersion;
private LayoutActionBar mActionBar;

    /**
     * Flags which tracks whether this editor is currently active which is set whenever
     * {@link #activated()} is called and clear whenever {@link #deactivated()} is called.
     * This is used to suppress repeated calls to {@link #activate()} to avoid doing
     * unnecessary work.
     */
    private boolean mActive;

public GraphicalEditorPart(LayoutEditor layoutEditor) {
mLayoutEditor = layoutEditor;
setPartName("Graphical Layout");
//Synthetic comment -- @@ -866,8 +874,18 @@
* Responds to a page change that made the Graphical editor page the activated page.
*/
public void activated() {
        if (!mActive) {
            mActive = true;

            boolean changed = mConfigComposite.syncRenderState();
            if (changed) {
                // Will also force recomputeLayout()
                return;
            }

            if (mNeedsRecompute) {
                recomputeLayout();
            }
}
}

//Synthetic comment -- @@ -875,7 +893,7 @@
* Responds to a page change that made the Graphical editor page the deactivated page
*/
public void deactivated() {
        mActive = false;
}

/**







