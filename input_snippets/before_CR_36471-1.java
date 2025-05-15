
//<Beginning of snippet n. 0>


}

void selectTheme(String theme) {
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE);
mThemeCombo.setData(theme);
if (theme != null) {
mThemeCombo.setText(getThemeLabel(theme, true));

try {
if (mEditedFile != null) {
                if (mState.theme == null || mListener.getIncludedWithin() != null) {
getPreferredTheme();
}
assert mState.theme != null;

//<End of snippet n. 0>








