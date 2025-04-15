/*Toast: fix force close if called from system service

If a toast is created from a system service, an application context
will not be available, use the service context instead.

Fixeshttp://code.google.com/p/android/issues/detail?id=40058http://code.google.com/p/android/issues/detail?id=40075Change-Id:Ic643f4557fa04476819cd2c3ee640b735eeae429*/
//Synthetic comment -- diff --git a/core/java/android/widget/Toast.java b/core/java/android/widget/Toast.java
//Synthetic comment -- index 485bd37..ab36139 100644

//Synthetic comment -- @@ -374,8 +374,13 @@
// remove the old view if necessary
handleHide();
mView = mNextView;
                mWM = (WindowManager)mView.getContext().getApplicationContext()
                        .getSystemService(Context.WINDOW_SERVICE);
// We can resolve the Gravity here by using the Locale for getting
// the layout direction
final Configuration config = mView.getContext().getResources().getConfiguration();







