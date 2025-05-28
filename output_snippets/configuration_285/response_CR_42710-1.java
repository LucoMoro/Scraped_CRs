//<Beginning of snippet n. 0>
public void setInverseBackgroundForced(boolean forceInverseBackground) {
    mForceInverseBackground = forceInverseBackground;
}

public int mIconId = 0;
public Drawable mIcon;
public CharSequence mTitle;
public View mCustomTitleView;
public CharSequence mMessage;

public void setupDialog(AlertDialog dialog) {
    if (mIconId > 0) {
        int resolvedIconId = resolveIconResource(mIconId);
        if (resolvedIconId != 0) {
            Drawable iconDrawable = getResources().getDrawable(resolvedIconId, null);
            if (iconDrawable != null) {
                dialog.setIcon(iconDrawable);
                return;
            }
        }
    }

    if (mIcon != null) {
        dialog.setIcon(mIcon);
    } else {
        Drawable defaultIcon = getDefaultIcon();
        if (defaultIcon != null) {
            dialog.setIcon(defaultIcon);
        }
    }

    if (mMessage != null) {
        dialog.setMessage(mMessage);
    }
}

private int resolveIconResource(int iconId) {
    // Implement theme-specific resolution logic
    // Example: return getTheme().obtainStyledAttributes(new int[]{iconId}).getResourceId(0, 0);
    return iconId; 
}

private Drawable getDefaultIcon() {
    Drawable drawable = getResources().getDrawable(R.drawable.default_icon, null);
    return drawable != null ? drawable : getResources().getDrawable(R.drawable.fallback_icon, null);
}
//<End of snippet n. 0>