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
    Drawable iconDrawable = null;

    if (mIconId > 0) {
        int resolvedIconId = resolveIconResource(mIconId);
        if (resolvedIconId != 0) {
            iconDrawable = getResources().getDrawable(resolvedIconId, null);
        }
    }

    if (iconDrawable == null && mIcon != null) {
        iconDrawable = mIcon;
    }

    if (iconDrawable == null) {
        Drawable defaultIcon = getDefaultIcon();
        if (defaultIcon != null) {
            iconDrawable = defaultIcon;
        }
    }

    if (iconDrawable != null) {
        dialog.setIcon(iconDrawable);
    }

    if (mMessage != null) {
        dialog.setMessage(mMessage);
    }
}

private int resolveIconResource(int iconId) {
    TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{iconId});
    int resourceId = typedArray.getResourceId(0, 0);
    typedArray.recycle();
    return resourceId;
}

private Drawable getDefaultIcon() {
    Drawable drawable = getResources().getDrawable(R.drawable.default_icon, null);
    return drawable != null ? drawable : getResources().getDrawable(R.drawable.fallback_icon, null);
}
//<End of snippet n. 0>