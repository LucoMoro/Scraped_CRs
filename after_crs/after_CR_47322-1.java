/*Week info update delayed in notification bar when language changed

DateView in status bar doesn't listen Locale change, therefore
no immediate date update after change the language in Settings

Change-Id:Iee21858c6b4e42538f30197a63fb4abe0caa70b1*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/policy/DateView.java b/packages/SystemUI/src/com/android/systemui/statusbar/policy/DateView.java
//Synthetic comment -- index 1d6b3d1..a7baebe 100644

//Synthetic comment -- @@ -43,7 +43,8 @@
final String action = intent.getAction();
if (Intent.ACTION_TIME_TICK.equals(action)
|| Intent.ACTION_TIME_CHANGED.equals(action)
                    || Intent.ACTION_TIMEZONE_CHANGED.equals(action)
                    || Intent.ACTION_LOCALE_CHANGED.equals(action)) {
updateClock();
}
}
//Synthetic comment -- @@ -116,6 +117,7 @@
filter.addAction(Intent.ACTION_TIME_TICK);
filter.addAction(Intent.ACTION_TIME_CHANGED);
filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
                filter.addAction(Intent.ACTION_LOCALE_CHANGED);
mContext.registerReceiver(mIntentReceiver, filter, null, null);
updateClock();
} else {







