/*DateView: Use Intent.ACTION_LOCALE_CHANGED to refresh notification bar clock

Add the Intent.ACTION_LOCALE_CHANGED to DateView BroadcastReceiver for detect when the
user change the locale. Otherwise, the notification bar clock is not refreshed until
the next tick or time changed occurs.

Change-Id:Ic8830fbabf24b62b60a6d3babe5e211f14948d08*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/DateView.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/DateView.java
//Synthetic comment -- index c19550b..1d3943a 100644

//Synthetic comment -- @@ -43,7 +43,8 @@
final String action = intent.getAction();
if (Intent.ACTION_TIME_TICK.equals(action)
|| Intent.ACTION_TIME_CHANGED.equals(action)
                    || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
                    || Intent.ACTION_LOCALE_CHANGED.equals(action)) {
updateClock();
}
}
//Synthetic comment -- @@ -119,6 +120,7 @@
filter.addAction(Intent.ACTION_TIME_TICK);
filter.addAction(Intent.ACTION_TIME_CHANGED);
filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
                filter.addAction(Intent.ACTION_LOCALE_CHANGED);
mContext.registerReceiver(mIntentReceiver, filter, null, null);
updateClock();
} else {







