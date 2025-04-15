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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.draw2d.geometry.Rectangle;
//Synthetic comment -- @@ -97,9 +99,23 @@
*   loading.<br>
*/
public class ConfigurationComposite extends Composite {

public final static QualifiedName NAME_CONFIG_STATE =
new QualifiedName(AdtPlugin.PLUGIN_ID, "state");//$NON-NLS-1$
private final static String THEME_SEPARATOR = "----------"; //$NON-NLS-1$

private final static int LOCALE_LANG = 0;
//Synthetic comment -- @@ -199,9 +215,6 @@
* rendering to its original configuration.
*/
private class ConfigState {
        private final static String SEP = ":"; //$NON-NLS-1$
        private final static String SEP_LOCALE = "-"; //$NON-NLS-1$

LayoutDevice device;
String configName;
ResourceQualifier[] locale;
//Synthetic comment -- @@ -220,7 +233,7 @@
sb.append(SEP);
sb.append(configName);
sb.append(SEP);
                if (locale != null) {
if (locale[0] != null && locale[1] != null) {
// locale[0]/[1] can be null sometimes when starting Eclipse
sb.append(((LanguageQualifier) locale[0]).getValue());
//Synthetic comment -- @@ -235,10 +248,10 @@
sb.append(SEP);
sb.append(night.getResourceValue());
sb.append(SEP);
                if (target != null) {
                    sb.append(targetToString(target));
                    sb.append(SEP);
                }
}

return sb.toString();
//Synthetic comment -- @@ -254,6 +267,8 @@
if (config != null) {
configName = values[1];

locale = new ResourceQualifier[2];
String locales[] = values[2].split(SEP_LOCALE);
if (locales.length >= 2) {
//Synthetic comment -- @@ -275,12 +290,17 @@
night = NightMode.NOTNIGHT;
}

                            if (values.length == 7 && mTargetList != null) {
                                target = stringToTarget(values[6]);
                            } else {
                                // No render target stored; try to find the best default
                                target = findDefaultRenderTarget();
}

return true;
}
//Synthetic comment -- @@ -293,67 +313,40 @@

@Override
public String toString() {
            StringBuilder sb = new StringBuilder();
            if (device != null) {
                sb.append(device.getName());
            } else {
                sb.append("null");
            }
            sb.append(SEP);
            sb.append(configName);
            sb.append(SEP);
            if (locale != null) {
                sb.append(((LanguageQualifier) locale[0]).getValue());
                sb.append(SEP_LOCALE);
                sb.append(((RegionQualifier) locale[1]).getValue());
            }
            sb.append(SEP);
            sb.append(theme);
            sb.append(SEP);
            sb.append(dock.getResourceValue());
            sb.append(SEP);
            sb.append(night.getResourceValue());
            sb.append(SEP);

            if (target != null) {
                sb.append(targetToString(target));
                sb.append(SEP);
            }

            return sb.toString();
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
}

/**
//Synthetic comment -- @@ -377,11 +370,11 @@

GridLayout gl;
GridData gd;
        int cols = 6;  // device+config+separator*2+theme+apiLevel

        // ---- First line: locale, day/night, dock, editing config display.
Composite labelParent = new Composite(this, SWT.NONE);
        labelParent.setLayout(gl = new GridLayout(6, false));
gl.marginWidth = gl.marginHeight = 0;
gl.marginTop = 3;
labelParent.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -407,25 +400,13 @@
mLocaleCombo.add("Locale"); //$NON-NLS-1$  // Dummy place holders
mLocaleCombo.add("Locale"); //$NON-NLS-1$

        mDockCombo = new Combo(labelParent, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (DockMode mode : DockMode.values()) {
            mDockCombo.add(mode.getLongDisplayValue());
        }
        mDockCombo.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
                onDockChange();
            }
        });

        mNightCombo = new Combo(labelParent, SWT.DROP_DOWN | SWT.READ_ONLY);
        for (NightMode mode : NightMode.values()) {
            mNightCombo.add(mode.getLongDisplayValue());
        }
        mNightCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onDayChange();
}
});

//Synthetic comment -- @@ -474,6 +455,32 @@
GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
gd.heightHint = 0;

mThemeCombo = new Combo(this, SWT.READ_ONLY | SWT.DROP_DOWN);
mThemeCombo.setLayoutData(new GridData(
GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
//Synthetic comment -- @@ -485,22 +492,6 @@
onThemeChange();
}
});

        // second separator
        separator = new Label(this, SWT.SEPARATOR | SWT.VERTICAL);
        separator.setLayoutData(gd = new GridData(
                GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
        gd.heightHint = 0;

        mTargetCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        mTargetCombo.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        mTargetCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                onRenderingTargetChange();
            }
        });
}

// ---- Init and reset/reload methods ----
//Synthetic comment -- @@ -1008,8 +999,11 @@
* Finds a locale matching the config from a file.
* @param language the language qualifier or null if none is set.
* @param region the region qualifier or null if none is set.
*/
    private void setLocaleCombo(ResourceQualifier language, ResourceQualifier region) {
// find the locale match. Since the locale list is based on the content of the
// project resources there must be an exact match.
// The only trick is that the region could be null in the fileConfig but in our
//Synthetic comment -- @@ -1026,16 +1020,24 @@
if (RegionQualifier.FAKE_REGION_VALUE.equals(
((RegionQualifier)locale[LOCALE_REGION]).getValue())) {
// match!
                        mLocaleCombo.select(i);
break;
}
} else if (region.equals(locale[LOCALE_REGION])) {
// match!
                    mLocaleCombo.select(i);
break;
}
}
}
}

private void updateConfigDisplay(FolderConfiguration fileConfig) {
//Synthetic comment -- @@ -1786,6 +1788,9 @@
if (computeCurrentConfig() &&  mListener != null) {
mListener.onConfigurationChange();
}
}

private void onDockChange() {
//Synthetic comment -- @@ -1837,6 +1842,9 @@
if (computeOk &&  mListener != null) {
mListener.onConfigurationChange();
}
}

/**
//Synthetic comment -- @@ -2028,5 +2036,170 @@
mEditedConfig = null;
onXmlModelLoaded();
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 5506319..430ebce 100644

//Synthetic comment -- @@ -245,6 +245,14 @@
private int mTargetSdkVersion;
private LayoutActionBar mActionBar;

public GraphicalEditorPart(LayoutEditor layoutEditor) {
mLayoutEditor = layoutEditor;
setPartName("Graphical Layout");
//Synthetic comment -- @@ -866,8 +874,18 @@
* Responds to a page change that made the Graphical editor page the activated page.
*/
public void activated() {
        if (mNeedsRecompute) {
            recomputeLayout();
}
}

//Synthetic comment -- @@ -875,7 +893,7 @@
* Responds to a page change that made the Graphical editor page the deactivated page
*/
public void deactivated() {
        // nothing to be done here for now.
}

/**







