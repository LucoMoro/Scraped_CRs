/*The following changes improve the reporting done in HierarchyViewer via the ViewDebug interface.

Wind River has built a system testing platform for Android.  It is called STP - for System Testing Platform.
We use Android's adb to access ViewDebug in Android phones.  The ViewDebug interface is missing a few important
values that we can use to run tests.  These changes add access to those values, as described below;

view/ViewDebug.java:  This change adds a screenX and screenY position for the overall view.  Without these
values, we cannot tell where a popup window is on the screen.  We use Monkey's network interface to click the
Android screen in specific locations, and we need to know where windows are positioned.

widget/AnalogClock.java:  We'd like to know the time that the clock is showing to verify it.

widget/CheckedTextView.java:  We'd like to know if the item is checked on not.

widget/CompoundButton.java:  We'd like to know if the item is checked on not.

widget/ProgressBar.java:  we need to know isIndeterminate(), getProgress(), getSecondaryProgress(), and
getMax() so we can understand what sliders and progress bars are showing.

widget/TextView.java: we need to know the current selection:  getSelectionStart() and getSelectionEnd()

We've been testing versions of this code on Cupcake, Donut and Eclair on emulators, dev phones and other
devices.  We'd like these changes in soon to be able to run our suites without asking our partners to change
their Android sources and so we can test new phones without any patching, just by plugging them in.

Thank you for your consideration.*/




//Synthetic comment -- diff --git a/core/java/android/view/ViewDebug.java b/core/java/android/view/ViewDebug.java
//Synthetic comment -- index 4baf612..54bec0f 100644

//Synthetic comment -- @@ -981,6 +981,7 @@
out.write(Integer.toHexString(view.hashCode()));
out.write(' ');
dumpViewProperties(context, view, out);
            dumpViewLocation(view, out);
out.newLine();
} catch (IOException e) {
Log.w("View", "Error while dumping hierarchy tree");
//Synthetic comment -- @@ -989,6 +990,19 @@
return true;
}

    private static void dumpViewLocation(View view, BufferedWriter out) {
        if(view != null && view.mAttachInfo != null) {
            int screenLocation[] = new int[2];
	    view.getLocationOnScreen(screenLocation);
            try {
                writeEntry(out, "", "screenX", "", Integer.toString(screenLocation[0]));
                writeEntry(out, "", "screenY", "", Integer.toString(screenLocation[1]));
            } catch  (IOException e) {
                Log.w("View", "Error while dumping dumpViewLocation: " + e.getMessage());
            }
        }
    }

private static Field[] getExportedPropertyFields(Class<?> klass) {
if (sFieldsForClasses == null) {
sFieldsForClasses = new HashMap<Class<?>, Field[]>();








//Synthetic comment -- diff --git a/core/java/android/widget/AnalogClock.java b/core/java/android/widget/AnalogClock.java
//Synthetic comment -- index f847bc3..ac096e0 100644

//Synthetic comment -- @@ -25,9 +25,11 @@
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.widget.RemoteViews.RemoteView;

import java.util.TimeZone;
//Synthetic comment -- @@ -243,4 +245,12 @@
invalidate();
}
};

    /**
     * {@hide}
     */
    @ViewDebug.ExportedProperty
    public String getCurrentTime() {
        return DateFormat.format("h:mm:ss aa", mCalendar.toMillis(false)).toString();
    }
}








//Synthetic comment -- diff --git a/core/java/android/widget/CheckedTextView.java b/core/java/android/widget/CheckedTextView.java
//Synthetic comment -- index fd590ed..f13d438 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;


//Synthetic comment -- @@ -73,7 +74,8 @@
public void toggle() {
setChecked(!mChecked);
}

@ViewDebug.ExportedProperty
public boolean isChecked() {
return mChecked;
}








//Synthetic comment -- diff --git a/core/java/android/widget/CompoundButton.java b/core/java/android/widget/CompoundButton.java
//Synthetic comment -- index 98b0976..0d84dd0 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;

/**
//Synthetic comment -- @@ -98,6 +99,7 @@
return super.performClick();
}

@ViewDebug.ExportedProperty
public boolean isChecked() {
return mChecked;
}








//Synthetic comment -- diff --git a/core/java/android/widget/DigitalClock.java b/core/java/android/widget/DigitalClock.java
//Synthetic comment -- index 379883a..5ad6425 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.ViewDebug;

import java.util.Calendar;

//Synthetic comment -- @@ -126,4 +127,12 @@
setFormat();
}
}

    /**
     * {@hide}
     */
    @ViewDebug.ExportedProperty
    public String getCurrentTime() {
        return DateFormat.format(m12, mCalendar).toString();
    }
}








//Synthetic comment -- diff --git a/core/java/android/widget/ProgressBar.java b/core/java/android/widget/ProgressBar.java
//Synthetic comment -- index 1dcb203..6dc9f78 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
//Synthetic comment -- @@ -335,6 +336,7 @@
*
* @return true if the progress bar is in indeterminate mode
*/
    @ViewDebug.ExportedProperty
public synchronized boolean isIndeterminate() {
return mIndeterminate;
}
//Synthetic comment -- @@ -607,6 +609,7 @@
* @see #setMax(int)
* @see #getMax()
*/
    @ViewDebug.ExportedProperty
public synchronized int getProgress() {
return mIndeterminate ? 0 : mProgress;
}
//Synthetic comment -- @@ -623,6 +626,7 @@
* @see #setMax(int)
* @see #getMax()
*/
    @ViewDebug.ExportedProperty
public synchronized int getSecondaryProgress() {
return mIndeterminate ? 0 : mSecondaryProgress;
}
//Synthetic comment -- @@ -636,6 +640,7 @@
* @see #getProgress()
* @see #getSecondaryProgress()
*/
    @ViewDebug.ExportedProperty
public synchronized int getMax() {
return mMax;
}








//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index f55ca3f..8980c17 100644

//Synthetic comment -- @@ -5726,6 +5726,7 @@
/**
* Convenience for {@link Selection#getSelectionStart}.
*/
    @ViewDebug.ExportedProperty
public int getSelectionStart() {
return Selection.getSelectionStart(getText());
}
//Synthetic comment -- @@ -5733,6 +5734,7 @@
/**
* Convenience for {@link Selection#getSelectionEnd}.
*/
    @ViewDebug.ExportedProperty
public int getSelectionEnd() {
return Selection.getSelectionEnd(getText());
}







