//<Beginning of snippet n. 0>
void selectTheme(String theme) {
    if (theme == null || theme.isEmpty()) {
        theme = DEFAULT_THEME; 
        notifyUser("Invalid theme selected, defaulting to " + DEFAULT_THEME);
        return;
    }
    
    assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : "Invalid theme prefix";
    
    mThemeCombo.setData(theme);
    mThemeCombo.setText(getThemeLabel(theme, true));

    try {
        if (mEditedFile != null) {
            if (mState.theme == null || mListener.getIncludedWithin() != null) {
                getPreferredTheme();
            }
            assert mState.theme != null : "Theme state cannot be null after preference retrieval";
        }
    } catch (Exception e) {
        notifyUser("An error occurred while selecting the theme: " + e.getMessage());
    }
}
//<End of snippet n. 0>