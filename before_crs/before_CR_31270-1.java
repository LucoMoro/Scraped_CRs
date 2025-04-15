/*Fix NPE in `onActivityResult` when target fragment no longer exists.

If an activity result comes in which originated from a fragment that
can no longer be found an attempt is still made to call a method
on the instance. The check for null already existed to produce a
logging statement but the subsequent method call was not placed
into an `else` result in the attempt to call being made every time.

Change-Id:I0378d516a16e511a6195608243c049b1cb50a8c8*/
//Synthetic comment -- diff --git a/v4/java/android/support/v4/app/FragmentActivity.java b/v4/java/android/support/v4/app/FragmentActivity.java
//Synthetic comment -- index 2112fb3..6665d5e 100644

//Synthetic comment -- @@ -148,8 +148,9 @@
if (frag == null) {
Log.w(TAG, "Activity result no fragment exists for index: 0x"
+ Integer.toHexString(requestCode));
}
            frag.onActivityResult(requestCode&0xffff, resultCode, data);
return;
}








