/*GlobalTime: Fix NullPointerException.

While running monkey, if the device goes to sleep when GlobalTime is
running, it goes to paused state. After being in this state for
sometime, onStop() of GlobalTime Activity is being called after the
gtView got destroyed.

Change-Id:I51f29993ad196602c3234459332e168127411a20*/
//Synthetic comment -- diff --git a/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java b/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java
//Synthetic comment -- index e27ee56..bbce155 100644

//Synthetic comment -- @@ -1463,13 +1463,18 @@

@Override protected void onPause() {
super.onPause();
        gtView.onPause();
}

@Override protected void onStop() {
super.onStop();
        gtView.destroy();
        gtView = null;
}

// Allow the activity to go idle before its animation starts







