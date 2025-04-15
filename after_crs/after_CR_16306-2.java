/*GlobalTime: Perform a null check on the gtView object on resume

Create a new gtView object, if the object is null when resuming the app.
This fixes the null pointer exception encountered when resuming the app
after receiving an MT call.

Change-Id:I33c8aadba81619d10a55c8ae3ea3870502cfd6ea*/




//Synthetic comment -- diff --git a/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java b/samples/GlobalTime/src/com/android/globaltime/GlobalTime.java
//Synthetic comment -- index e27ee56..16aacda 100644

//Synthetic comment -- @@ -1457,6 +1457,10 @@

@Override protected void onResume() {
super.onResume();
        if (gtView == null) {
           gtView = new GTView(this);
           setContentView(gtView);
        }
gtView.onResume();
Looper.myQueue().addIdleHandler(new Idler());
}







