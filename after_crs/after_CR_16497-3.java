/*Fix Bluetooth Settings activity memory leak.

    There were two leaks in Bluetooth Settings: one when rotating the
    phone and other when scanning for bluetooth devices.*/




//Synthetic comment -- diff --git a/src/com/android/settings/ProgressCategory.java b/src/com/android/settings/ProgressCategory.java
//Synthetic comment -- index 15810b3..3f2ad5a 100644

//Synthetic comment -- @@ -26,7 +26,8 @@
public class ProgressCategory extends PreferenceCategory {

private boolean mProgress = false;
    private View oldView = null;

public ProgressCategory(Context context, AttributeSet attrs) {
super(context, attrs);
setLayoutResource(R.layout.preference_progress_category);
//Synthetic comment -- @@ -41,6 +42,14 @@
int visibility = mProgress ? View.VISIBLE : View.INVISIBLE;
textView.setVisibility(visibility);
progressBar.setVisibility(visibility);

        if(oldView != null)
        {
            oldView.findViewById(R.id.scanning_progress).setVisibility(View.GONE);
            oldView.findViewById(R.id.scanning_text).setVisibility(View.GONE);
            oldView.setVisibility(View.GONE);
        }
        oldView = view;
}

/**








//Synthetic comment -- diff --git a/src/com/android/settings/bluetooth/BluetoothSettings.java b/src/com/android/settings/bluetooth/BluetoothSettings.java
//Synthetic comment -- index 1e73b2d..78c531c 100644

//Synthetic comment -- @@ -188,7 +188,8 @@
super.onPause();

mLocalManager.setForegroundActivity(null);
        mDevicePreferenceMap.clear();
        mDeviceList.removeAll();
unregisterReceiver(mReceiver);

mLocalManager.unregisterCallback(this);







