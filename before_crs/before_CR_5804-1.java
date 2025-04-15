/*Rescale menu icons to fit menu item height*/
//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/IconMenuItemView.java b/core/java/com/android/internal/view/menu/IconMenuItemView.java
//Synthetic comment -- index 156e20a..e8424a2 100644

//Synthetic comment -- @@ -27,6 +27,11 @@
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

/**
* The item view for each item in the {@link IconMenuView}.  
//Synthetic comment -- @@ -168,24 +173,6 @@

public void setIcon(Drawable icon) {
mIcon = icon;
        
        if (icon != null) {
            
            /* Set the bounds of the icon since setCompoundDrawables needs it. */
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            
            // Set the compound drawables
            setCompoundDrawables(null, icon, null, null);

            /*
             * Request a layout to reposition the icon. The positioning of icon
             * depends on this TextView's line bounds, which is only available
             * after a layout.
             */  
            requestLayout();
        } else {
            setCompoundDrawables(null, null, null, null);
        }
}

public void setItemInvoker(ItemInvoker itemInvoker) {
//Synthetic comment -- @@ -226,6 +213,23 @@
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
super.onLayout(changed, left, top, right, bottom);

positionIcon();
}

//Synthetic comment -- @@ -273,5 +277,55 @@
public boolean showsIcon() {
return true;
}

}







