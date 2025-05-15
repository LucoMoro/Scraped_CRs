
//<Beginning of snippet n. 0>


}

void selectTheme(String theme) {
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : theme;
mThemeCombo.setData(theme);
if (theme != null) {
mThemeCombo.setText(getThemeLabel(theme, true));

try {
if (mEditedFile != null) {
                if (mState.theme == null || mState.theme.isEmpty()
                        || mListener.getIncludedWithin() != null) {
                    mState.theme = null;
getPreferredTheme();
}
assert mState.theme != null;

//<End of snippet n. 0>








