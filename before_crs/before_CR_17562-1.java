/*Fix sometimes mExpandedView will appear while screen transforming.

While watching YouTube and receiving a call, after hang up the call,
mExpandedView may flash when screen transform from Phone to YouTube.

Change-Id:Ib25a0cd1086c278b91a2b34e21744a3c432a8f35*/
//Synthetic comment -- diff --git a/services/java/com/android/server/status/StatusBarService.java b/services/java/com/android/server/status/StatusBarService.java
//Synthetic comment -- index 93c8d34..b9a57d6 100644

//Synthetic comment -- @@ -289,6 +289,7 @@
mScrollView = (ScrollView)expanded.findViewById(R.id.scroll);
mNotificationLinearLayout = expanded.findViewById(R.id.notificationLinearLayout);

mOngoingTitle.setVisibility(View.GONE);
mLatestTitle.setVisibility(View.GONE);

//Synthetic comment -- @@ -970,6 +971,7 @@
mExpandedDialog.getWindow().setAttributes(mExpandedParams);
mExpandedView.requestFocus(View.FOCUS_FORWARD);
mTrackingView.setVisibility(View.VISIBLE);

if (!mTicking) {
setDateViewVisibility(true, com.android.internal.R.anim.fade_in);
//Synthetic comment -- @@ -1054,6 +1056,7 @@
mExpandedParams.flags &= ~WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
mExpandedDialog.getWindow().setAttributes(mExpandedParams);
mTrackingView.setVisibility(View.GONE);

if ((mDisabled & StatusBarManager.DISABLE_NOTIFICATION_ICONS) == 0) {
setNotificationIconVisibility(true, com.android.internal.R.anim.fade_in);







