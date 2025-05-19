//<Beginning of snippet n. 0>
void selectTheme(String theme) {
        if (theme == null || theme.isEmpty() || 
            !(theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE))) {
            theme = DEFAULT_THEME; // Assign default theme
            System.out.println("Invalid theme provided. Defaulting to: " + DEFAULT_THEME);
            mThemeCombo.setData(theme);
            mThemeCombo.setText(getThemeLabel(theme, true));
            return; 
        }

        mThemeCombo.setData(theme);
        mThemeCombo.setText(getThemeLabel(theme, true));

        try {
            if (mEditedFile != null) {
                if (mState.theme == null || mListener.getIncludedWithin() != null) {
                    getPreferredTheme();
                }
                if (mState.theme == null || mState.theme.isEmpty() || 
                    !(mState.theme.startsWith(PREFIX_STYLE) || mState.theme.startsWith(PREFIX_ANDROID_STYLE))) {
                    throw new IllegalArgumentException("Theme state is null or invalid after getting preferred theme.");
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("An error occurred: " + e.getMessage());
            theme = DEFAULT_THEME; 
            System.out.println("Reverting to default theme: " + DEFAULT_THEME);
            mThemeCombo.setData(theme);
            mThemeCombo.setText(getThemeLabel(theme, true));
            return;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
//<End of snippet n. 0>