/*DateView in the Notification bar is now formatted via com.android.internal.R.string.abbrev_wday_month_day_year

Change-Id:Ib2aa83ff742ef82099f89135295283da606857b0*/




//Synthetic comment -- diff --git a/packages/SystemUI/src/com/android/systemui/statusbar/DateView.java b/packages/SystemUI/src/com/android/systemui/statusbar/DateView.java
//Synthetic comment -- index e6d3a7e..e56ec74 100644

//Synthetic comment -- @@ -16,16 +16,19 @@

package com.android.systemui.statusbar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.text.format.DateFormat;

import android.util.AttributeSet;
import android.util.Slog;
import android.widget.TextView;
import android.view.MotionEvent;

import java.util.Date;

public final class DateView extends TextView {
//Synthetic comment -- @@ -67,7 +70,9 @@

private final void updateClock() {
Date now = new Date();
        Resources res = Resources.getSystem();
        setText(DateFormat.format(res.getString(com.android.internal.R.string.abbrev_wday_month_day_year),now));
        
}

void setUpdates(boolean update) {







