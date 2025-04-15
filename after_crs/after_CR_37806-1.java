/*Fix bug in theme menu: don't strip style prefix from manifest

Change-Id:I32c1f47b7e6e7c1d91d7e3000075046d2c872ad9*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/SelectThemeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/SelectThemeAction.java
//Synthetic comment -- index b8ba48e..8d92d3c 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

//Synthetic comment -- @@ -30,6 +33,7 @@
public SelectThemeAction(ConfigurationComposite configuration, String title, String theme,
boolean selected) {
super(title, IAction.AS_RADIO_BUTTON);
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : theme;
mConfiguration = configuration;
mTheme = theme;
if (selected) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java
//Synthetic comment -- index 7e7d65b..0836709 100644

//Synthetic comment -- @@ -199,7 +199,6 @@
String current = mConfiguration.getSelectedTheme();
for (String theme : sorted) {
boolean selected = theme.equals(current);
addMenuItem(menu, theme, selected);
}
} else {







