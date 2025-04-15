/*Crash when no item selected in ControlPanelPicker

mClickPos is not initialized properly and the application crashes
due to an exception when "Done" button is clicked without selecting
the item in the list.

Change-Id:If5b141ba816fcc0174ab78e557c5625130fce7d5*/
//Synthetic comment -- diff --git a/src/com/android/musicfx/ControlPanelPicker.java b/src/com/android/musicfx/ControlPanelPicker.java
//Synthetic comment -- index a01fc36..bee9924 100644

//Synthetic comment -- @@ -45,7 +45,6 @@
public class ControlPanelPicker extends AlertActivity implements OnClickListener, OnPrepareListViewListener {


    int mClickedPos = -1;

@Override
public void onCreate(final Bundle savedInstanceState) {
//Synthetic comment -- @@ -97,7 +96,7 @@

public void onClick(DialogInterface dialog, int which) {
// Save the position of most recently clicked item
            mClickedPos = which;
}

};
//Synthetic comment -- @@ -108,7 +107,7 @@
// set new default
Intent updateIntent = new Intent(this, Service.class);
Cursor c = mAlertParams.mCursor;
            c.moveToPosition(mClickedPos);
updateIntent.putExtra("defPackage", c.getString(2));
updateIntent.putExtra("defName", c.getString(3));
startService(updateIntent);







