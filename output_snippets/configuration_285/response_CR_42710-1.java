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
    setDialogIcon(dialog);
    setDialogMessage(dialog);
    dialog.show();
}

private void setDialogIcon(AlertDialog dialog) {
    if (resolveIconDrawable() != null) {
        dialog.setIcon(resolveIconDrawable());
    } else {
        dialog.setIcon(R.drawable.default_icon); // Fallback icon based on the theme
    }
}

private Drawable resolveIconDrawable() {
    if (mIcon != null) {
        return mIcon;
    } else if (mIconId > 0) {
        return ContextCompat.getDrawable(context, mIconId);
    }
    return null;
}

private void setDialogMessage(AlertDialog dialog) {
    if (mMessage != null) {
        dialog.setMessage(mMessage);
    } else {
        dialog.setMessage("Default message"); // Fallback message
    }
}
//<End of snippet n. 0>