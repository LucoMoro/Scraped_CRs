/*Fix potential stackoverflow in theme detection.

Change-Id:I19d2a3c9c6802b3e1c065ca36828d2b672130630*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index fbca2f3..1ef6f80 100644

//Synthetic comment -- @@ -2275,9 +2275,12 @@
return parentStyle.equals("Theme") || parentStyle.startsWith("Theme.");
} else {
// if it's a project style, we check this is a theme.
                    value = styleMap.get(parentStyle);
                    if (value != null) {
                        return isTheme(value, styleMap);
}
}
}







