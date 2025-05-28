//<Beginning of snippet n. 0>
public class AlertController {

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        mForceInverseBackground = forceInverseBackground;
    }

    public int mIconId = 0;
    public Drawable mIcon;
    public CharSequence mTitle;
    public View mCustomTitleView;
    public CharSequence mMessage;

    public void resolveIcon(AlertDialog dialog) {
        int themeSpecificIcon = getThemeSpecificIcon();
        if (themeSpecificIcon != R.drawable.default_icon) {
            dialog.setIcon(themeSpecificIcon);
        } else if (mIcon != null) {
            dialog.setIcon(mIcon);
        } else if (mIconId > 0) {
            dialog.setIcon(mIconId);
        } else {
            dialog.setIcon(getDefaultIcon());
        }
    }

    private int getThemeSpecificIcon() {
        int currentTheme = getCurrentTheme();
        switch (currentTheme) {
            case THEME_TYPE_1:
                return R.drawable.icon_theme_1;
            case THEME_TYPE_2:
                return R.drawable.icon_theme_2;
            default:
                return R.drawable.default_icon;
        }
    }

    private int getDefaultIcon() {
        return R.drawable.default_icon;
    }
    
    public void showDialog(AlertDialog dialog) {
        resolveIcon(dialog);
        if (mMessage != null) {
            dialog.setMessage(mMessage);
        }
    }

    private int getCurrentTheme() {
        return THEME_TYPE_1; 
    }

    private static final int THEME_TYPE_1 = 1;
    private static final int THEME_TYPE_2 = 2;
    private boolean mForceInverseBackground;

}
//<End of snippet n. 0>