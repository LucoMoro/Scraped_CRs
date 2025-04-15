/*CharPickerDialog on PassUnlockScreen show/hide when LockScr

See issue 16080.

In order to discovering  issue 16080:
LockScreen is just inflated on the same window as PasswordUnlockScreen, so CharacterPickerDialog that is attached to that
window does not hide when LockScreen is showing.

What was made:
Get the list of nested views when PasswordUnlockScreen shows/hides, check for consistance of
CharacterPickerDialog and set the appropriate visibility to it (if it exists).

Change-Id:Ida7ead7a918e6248ae102436cc7a7c68761407f3*/




//Synthetic comment -- diff --git a/core/java/android/view/WindowManagerImpl.java b/core/java/android/view/WindowManagerImpl.java
//Synthetic comment -- index 0973599..3035497 100644

//Synthetic comment -- @@ -16,6 +16,9 @@

package android.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.AndroidRuntimeException;
//Synthetic comment -- @@ -322,6 +325,15 @@
closeAll(null, null, null);
}

    /**
     * @return List<Views>
     */
    public List<View> getViews() {
    	List<View> target  = new ArrayList<View>();
    	target = java.util.Arrays.asList(mViews);
    	return target;
    }
    
public Display getDefaultDisplay() {
return new Display(Display.DEFAULT_DISPLAY);
}








//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/PasswordUnlockScreen.java b/policy/src/com/android/internal/policy/impl/PasswordUnlockScreen.java
//Synthetic comment -- index 39f2917..258ef5c 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManagerImpl;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
//Synthetic comment -- @@ -131,11 +132,26 @@

/** {@inheritDoc} */
public void onPause() {
    	setCharPickerVisible(false);
    }
    
	/**
     * Set visibility to CharacterPickerDialog instance (if exists)
     * @see {@link PasswordUnlockScreen.onPause()}
     * @see issue16080
     */    
    private void setCharPickerVisible(boolean visibility){
    	WindowManagerImpl wManImp = (WindowManagerImpl) mContext.getSystemService(Context.WINDOW_SERVICE);
    	View charPickerDialog = wManImp.getSubWindow();
    	if (charPickerDialog != null) {
    		charPickerDialog.setVisibility(visibility ? VISIBLE : INVISIBLE);
    	}    	
}

/** {@inheritDoc} */
public void onResume() {
    	setCharPickerVisible(true);   	
    	
// start fresh
mPasswordEntry.setText("");
mPasswordEntry.requestFocus();







