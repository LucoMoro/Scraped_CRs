/*Implement resolving of icon attributes to resIds in AlertController

Platform code using AlertController directly currently has
no way to set the correct theme-specific dialog icon in a
generic way. This adds code to handle such usage.

Change-Id:I48beafa1183e4edf6d5378b3985a7f9be5fcda8b*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/AlertController.java b/core/java/com/android/internal/app/AlertController.java
//Synthetic comment -- index 2061c90..56b5937 100644

//Synthetic comment -- @@ -347,6 +347,18 @@
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
//Synthetic comment -- @@ -740,6 +752,7 @@

public int mIconId = 0;
public Drawable mIcon;
        public int mIconAttrId = 0;
public CharSequence mTitle;
public View mCustomTitleView;
public CharSequence mMessage;
//Synthetic comment -- @@ -806,6 +819,9 @@
if (mIconId >= 0) {
dialog.setIcon(mIconId);
}
                if (mIconAttrId > 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(mIconAttrId));
                }
}
if (mMessage != null) {
dialog.setMessage(mMessage);







