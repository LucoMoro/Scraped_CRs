/*Cleaned up LunarLander sample

Change-Id:If2cc44c8312cebce170d07c34c64ea2ce1e5b7ab*/
//Synthetic comment -- diff --git a/samples/LunarLander/src/com/example/android/lunarlander/LunarLander.java b/samples/LunarLander/src/com/example/android/lunarlander/LunarLander.java
//Synthetic comment -- index a4ffef5..15c5923 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.android.lunarlander.LunarView.LunarThread;
//Synthetic comment -- @@ -58,7 +57,7 @@

/**
* Invoked during init to give the Activity a chance to set up its Menu.
     * 
* @param menu the Menu to which entries may be added
* @return true
*/
//Synthetic comment -- @@ -79,7 +78,7 @@

/**
* Invoked when the user selects an item from the Menu.
     * 
* @param item the Menu entry which was selected
* @return true if the Menu item was legit (and we consumed it), false
*         otherwise
//Synthetic comment -- @@ -116,7 +115,7 @@

/**
* Invoked when the Activity is created.
     * 
* @param savedInstanceState a Bundle containing state saved from a previous
*        execution, or null if this is a new execution
*/
//Synthetic comment -- @@ -157,7 +156,7 @@
/**
* Notification that something is about to happen, to give the Activity a
* chance to save state.
     * 
* @param outState a Bundle into which this Activity should save its state
*/
@Override








//Synthetic comment -- diff --git a/samples/LunarLander/src/com/example/android/lunarlander/LunarView.java b/samples/LunarLander/src/com/example/android/lunarlander/LunarView.java
//Synthetic comment -- index c52c7ab..2a46147 100644

//Synthetic comment -- @@ -37,7 +37,7 @@

/**
* View that draws, takes keystrokes, etc. for a simple LunarLander game.
 * 
* Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
* current ship physics. All x/y etc. are measured with (0,0) at the lower left.
* updatePhysics() advances the physics based on realtime. draw() renders the
//Synthetic comment -- @@ -112,14 +112,14 @@

/**
* Current height of the surface/canvas.
         * 
* @see #setSurfaceSize
*/
private int mCanvasHeight = 1;

/**
* Current width of the surface/canvas.
         * 
* @see #setSurfaceSize
*/
private int mCanvasWidth = 1;
//Synthetic comment -- @@ -321,7 +321,7 @@
* Restores game state from the indicated Bundle. Typically called when
* the Activity is being restored after having been previously
* destroyed.
         * 
* @param savedState Bundle containing the game state
*/
public synchronized void restoreState(Bundle savedState) {
//Synthetic comment -- @@ -372,7 +372,7 @@
/**
* Dump game state to the provided Bundle. Typically called when the
* Activity is being suspended.
         * 
* @return Bundle with this view's state
*/
public Bundle saveState(Bundle map) {
//Synthetic comment -- @@ -400,7 +400,7 @@

/**
* Sets the current difficulty.
         * 
* @param difficulty
*/
public void setDifficulty(int difficulty) {
//Synthetic comment -- @@ -423,7 +423,7 @@
* Passing true allows the thread to run; passing false will shut it
* down if it's already running. Calling start() after this was most
* recently called with false will result in an immediate shutdown.
         * 
* @param b true to run, false to shut down
*/
public void setRunning(boolean b) {
//Synthetic comment -- @@ -433,7 +433,7 @@
/**
* Sets the game mode. That is, whether we are running, paused, in the
* failure state, in the victory state, etc.
         * 
* @see #setState(int, CharSequence)
* @param mode one of the STATE_* constants
*/
//Synthetic comment -- @@ -446,7 +446,7 @@
/**
* Sets the game mode. That is, whether we are running, paused, in the
* failure state, in the victory state, etc.
         * 
* @param mode one of the STATE_* constants
* @param message string to add to screen or null
*/
//Synthetic comment -- @@ -509,7 +509,7 @@
mCanvasHeight = height;

// don't forget to resize the background image
                mBackgroundImage = mBackgroundImage.createScaledBitmap(
mBackgroundImage, width, height, true);
}
}
//Synthetic comment -- @@ -527,7 +527,7 @@

/**
* Handles a key-down event.
         * 
* @param keyCode the key that was pressed
* @param msg the original event object
* @return true
//Synthetic comment -- @@ -539,8 +539,6 @@
if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) okStart = true;
if (keyCode == KeyEvent.KEYCODE_S) okStart = true;

                boolean center = (keyCode == KeyEvent.KEYCODE_DPAD_UP);

if (okStart
&& (mMode == STATE_READY || mMode == STATE_LOSE || mMode == STATE_WIN)) {
// ready-to-start -> start
//Synthetic comment -- @@ -579,7 +577,7 @@

/**
* Handles a key-up event.
         * 
* @param keyCode the key that was pressed
* @param msg the original event object
* @return true if the key was handled and consumed, or else false
//Synthetic comment -- @@ -807,7 +805,7 @@

/**
* Fetches the animation thread corresponding to this LunarView.
     * 
* @return the animation thread
*/
public LunarThread getThread() {







