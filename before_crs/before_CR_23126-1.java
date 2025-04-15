/*Disable palette preview for some widgets on some platforms

ListView palette preview requires adapterview support in layoutlib,
and DatePicker and TimePicker require Holo themes on Honeycomb. This
changeset adds some conditional logic to the palette preview code to
drop rendering of these widgets based on the current render target,
layout library and theme.

Change-Id:Ic42a40faf817e60525485e0a46b7ad967ed1c363*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
//Synthetic comment -- index cee6f97..20379a6 100644

//Synthetic comment -- @@ -198,9 +198,21 @@
/** The fully qualified class name of an AdapterView */
public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$

/** The fully qualified class name of a GestureOverlayView */
public static final String FQCN_GESTURE_OVERLAY_VIEW = "android.gesture.GestureOverlayView"; //$NON-NLS-1$

/** The fully qualified class name of a RadioGroup */
public static final String FQCN_RADIO_GROUP = "android.widgets.RadioGroup";  //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 80cc87d..68372a2 100644

//Synthetic comment -- @@ -17,6 +17,10 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_URI;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

//Synthetic comment -- @@ -39,6 +43,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.util.Pair;

import org.eclipse.core.runtime.IPath;
//Synthetic comment -- @@ -164,6 +169,32 @@
String fqn = repository.getFullClassName(element);
assert fqn.length() > 0 : element.getNodeName();
RenderMode renderMode = repository.getRenderMode(fqn);
if (renderMode == RenderMode.ALONE) {
elements.add(Collections.singletonList(element));
} else if (renderMode == RenderMode.NORMAL) {
//Synthetic comment -- @@ -561,7 +592,7 @@
if (themeName.startsWith(themeNamePrefix)) {
themeName = themeName.substring(themeNamePrefix.length());
}
            String dirName = String.format("palette-preview-r11c-%s-%s-%s", cleanup(targetName),
cleanup(themeName), cleanup(mPalette.getCurrentDevice()));
IPath dirPath = pluginState.append(dirName);








