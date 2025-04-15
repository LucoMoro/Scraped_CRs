/*Display of self explanatory basic icon during call

Change-Id:I30a54e01a427b3a624d925f2690c657319484577*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index a21b240..c536367 100644

//Synthetic comment -- @@ -690,10 +690,34 @@
}
msg.title = lastSelectedItem;

        Toast toast = Toast.makeText(mContext.getApplicationContext(), msg.text,
                Toast.LENGTH_LONG);
toast.setGravity(Gravity.BOTTOM, 0, 0);
toast.show();
}

private void launchIdleText() {







