
//<Beginning of snippet n. 0>


}
}

    /**
     * @param attrId the attributeId of the theme-specific drawable
     * to resolve the resourceId for.
     *
     * @return resId the resourceId of the theme-specific drawable
     */
    public int getIconAttributeResId(int attrId) {
        TypedValue out = new TypedValue();
        mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }

public void setInverseBackgroundForced(boolean forceInverseBackground) {
mForceInverseBackground = forceInverseBackground;
}

public int mIconId = 0;
public Drawable mIcon;
        public int mIconAttrId = 0;
public CharSequence mTitle;
public View mCustomTitleView;
public CharSequence mMessage;
if (mIconId >= 0) {
dialog.setIcon(mIconId);
}
                if (mIconAttrId > 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(mIconAttrId));
                }
}
if (mMessage != null) {
dialog.setMessage(mMessage);

//<End of snippet n. 0>








