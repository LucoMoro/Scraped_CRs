//<Beginning of snippet n. 0>
void selectTheme(String theme) {
    if (theme == null || theme.isEmpty()) {
        // Notify user about invalid input
        System.out.println("Invalid theme input. Using default theme.");
        mThemeCombo.setText(getThemeLabel("default_theme", true));
        return;
    }
    
    if (!(theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE))) {
        System.out.println("Theme does not start with valid prefix. Using default theme.");
        mThemeCombo.setText(getThemeLabel("default_theme", true));
        return;
    }

    mThemeCombo.setData(theme);
    mThemeCombo.setText(getThemeLabel(theme, true));

    try {
        if (mEditedFile != null) {
            if (mState.theme == null || mListener.getIncludedWithin() != null) {
                getPreferredTheme();
            }
            if (mState.theme == null) {
                System.out.println("Preferred theme is null. Using default theme.");
                mThemeCombo.setText(getThemeLabel("default_theme", true));
                return;
            }
        }
    } catch (Exception e) {
        System.out.println("An error occurred while selecting theme: " + e.getMessage());
    }
}
//<End of snippet n. 0>