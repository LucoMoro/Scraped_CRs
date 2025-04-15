/*Disable palette preview for some widgets on some platforms. DO NOT MERGE

ListView palette preview requires adapterview support in layoutlib,
and DatePicker and TimePicker require Holo themes on Honeycomb. This
changeset adds some conditional logic to the palette preview code to
drop rendering of these widgets based on the current render target,
layout library and theme.

Change-Id:I3ed30682485655ca7463baa8ea1133fddebe7091*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index dedd97b..5f8a6fa 100644

//Synthetic comment -- @@ -194,9 +194,21 @@
/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$

    /** The fully qualified class name of a ListView */
    public static final String FQCN_LIST_VIEW = "android.widget.ListView"; //$NON-NLS-1$

    /** The fully qualified class name of an ExpandableListView */
    public static final String FQCN_EXPANDABLE_LIST_VIEW = "android.widget.ExpandableListView"; //$NON-NLS-1$

/** The fully qualified class name of a GestureOverlayView */
public static final String FQCN_GESTURE_OVERLAY_VIEW = "android.gesture.GestureOverlayView"; //$NON-NLS-1$

    /** The fully qualified class name of a DatePicker */
    public static final String FQCN_DATE_PICKER = "android.widget.DatePicker"; //$NON-NLS-1$

    /** The fully qualified class name of a TimePicker */
    public static final String FQCN_TIME_PICKER = "android.widget.TimePicker"; //$NON-NLS-1$

/** The fully qualified class name of a RadioGroup */
public static final String FQCN_RADIO_GROUP = "android.widgets.RadioGroup";  //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 80cc87d..68372a2 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.common.layout.LayoutConstants.FQCN_DATE_PICKER;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TIME_PICKER;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

//Synthetic comment -- @@ -39,6 +43,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.sdklib.IAndroidTarget;
import com.android.util.Pair;

import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -164,6 +169,32 @@
String fqn = repository.getFullClassName(element);
assert fqn.length() > 0 : element.getNodeName();
RenderMode renderMode = repository.getRenderMode(fqn);

                // Temporary special cases
                if (fqn.equals(FQCN_LIST_VIEW) || fqn.equals(FQCN_EXPANDABLE_LIST_VIEW)) {
                    if (!mPalette.getEditor().renderingSupports(Capability.ADAPTER_BINDING)) {
                        renderMode = RenderMode.SKIP;
                    }
                } else if (fqn.equals(FQCN_DATE_PICKER) || fqn.equals(FQCN_TIME_PICKER)) {
                    IAndroidTarget renderingTarget = mPalette.getEditor().getRenderingTarget();
                    // In Honeycomb, these widgets only render properly in the Holo themes.
                    int apiLevel = renderingTarget.getVersion().getApiLevel();
                    if (apiLevel == 11) {
                        String themeName = mPalette.getCurrentTheme();
                        if (themeName == null || !themeName.startsWith("Theme.Holo")) { //$NON-NLS-1$
                            // Note - it's possible that the the theme is some other theme
                            // such as a user theme which inherits from Theme.Holo and that
                            // the render -would- have worked, but it's harder to detect that
                            // scenario, so we err on the side of caution and just show an
                            // icon + name for the time widgets.
                            renderMode = RenderMode.SKIP;
                        }
                    } else if (apiLevel >= 12) {
                        // Currently broken, even for Holo.
                        renderMode = RenderMode.SKIP;
                    } // apiLevel <= 10 is fine
                }

if (renderMode == RenderMode.ALONE) {
elements.add(Collections.singletonList(element));
} else if (renderMode == RenderMode.NORMAL) {
//Synthetic comment -- @@ -561,7 +592,7 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-preview-r11d-%s-%s-%s", cleanup(targetName),
cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);








