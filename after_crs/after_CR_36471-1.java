/*Handle empty theme strings

Change-Id:I0b6790f2018b7a5739cbf20c20672487a9051669*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 264512c..0a80b5e 100644

//Synthetic comment -- @@ -1024,7 +1024,7 @@
}

void selectTheme(String theme) {
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : theme;
mThemeCombo.setData(theme);
if (theme != null) {
mThemeCombo.setText(getThemeLabel(theme, true));
//Synthetic comment -- @@ -2164,7 +2164,9 @@

try {
if (mEditedFile != null) {
                if (mState.theme == null || mState.theme.isEmpty()
                        || mListener.getIncludedWithin() != null) {
                    mState.theme = null;
getPreferredTheme();
}
assert mState.theme != null;







