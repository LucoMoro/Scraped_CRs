/*Rescale menu icons to fit menu item height*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/view/menu/IconMenuItemView.java b/core/java/com/android/internal/view/menu/IconMenuItemView.java
//Synthetic comment -- index 156e20a..e8424a2 100644

//Synthetic comment -- @@ -27,6 +27,11 @@
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Canvas;

/**
* The item view for each item in the {@link IconMenuView}.  
//Synthetic comment -- @@ -168,24 +173,6 @@

public void setIcon(Drawable icon) {
mIcon = icon;
}

public void setItemInvoker(ItemInvoker itemInvoker) {
//Synthetic comment -- @@ -226,6 +213,23 @@
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
super.onLayout(changed, left, top, right, bottom);

        if(mIcon!=null)
        {
            // Compute new icon size
            final int iconSize = getHeight() - getLayout().getHeight(); 
            if(iconSize < mIcon.getIntrinsicHeight())
                mIcon=resizeIcon(mIcon, iconSize);
            
            /* Set the bounds of the icon since setCompoundDrawables needs it. */
            mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());
            
            // Set the compound drawables
            setCompoundDrawables(null, mIcon, null, null);
            
            // When there is an icon, make sure the text is at the bottom
            setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        }
        
positionIcon();
}

//Synthetic comment -- @@ -273,5 +277,55 @@
public boolean showsIcon() {
return true;
}
        
    private static Drawable resizeIcon(Drawable icon, int row_height) {
	    int targetWidth;
	    int targetHeight;
            
        targetWidth = targetHeight = row_height;

        int width = targetWidth;
        int height = targetHeight;

        final int iconWidth = icon.getIntrinsicWidth();
        final int iconHeight = icon.getIntrinsicHeight();

        if (icon instanceof PaintDrawable) {
            PaintDrawable painter = (PaintDrawable) icon;
            painter.setIntrinsicWidth(width);
            painter.setIntrinsicHeight(height);
        }

        if (width > 0 && height > 0 && (width < iconWidth || height < iconHeight)) {
            final float ratio = (float) iconWidth / iconHeight;

            if (iconWidth > iconHeight) {
                height = (int) (width / ratio);
            } else if (iconHeight > iconWidth) {
                width = (int) (height * ratio);
            }

            final Bitmap.Config config = icon.getOpacity() != PixelFormat.OPAQUE ?
                        Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            final Bitmap resizedIcon = Bitmap.createBitmap(targetWidth, targetHeight, config);
            final Canvas canvas = new Canvas();
            canvas.setBitmap(resizedIcon);
            
            // Copy the old bounds to restore them later
            // If we were to do oldBounds = icon.getBounds(),
            // the call to setBounds() that follows would
            // change the same instance and we would lose the
            // old bounds
            
            final Rect sOldBounds = new Rect();
            
            sOldBounds.set(icon.getBounds());
            icon.setBounds((targetWidth - width) / 2, (targetHeight - height) / 2, width, height);
            icon.draw(canvas);
            icon.setBounds(sOldBounds);
            icon = new BitmapDrawable(resizedIcon);
        }
        return icon;
    }

}







