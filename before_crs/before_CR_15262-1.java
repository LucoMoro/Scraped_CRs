/*Issue 3132:Call Log Reporting Wrong

Change-Id:I24f739f909bc2e7f1a91a164bdb237a3b306239c*/
//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 9dd8ceb..02c02cf 100644

//Synthetic comment -- @@ -496,27 +496,34 @@
resId = com.android.internal.R.plurals.in_num_hours;
}
}
        } else if (duration < WEEK_IN_MILLIS && minResolution < WEEK_IN_MILLIS) {
            count = duration / DAY_IN_MILLIS;
            if (past) {
                if (abbrevRelative) {
                    resId = com.android.internal.R.plurals.abbrev_num_days_ago;
                } else {
                    resId = com.android.internal.R.plurals.num_days_ago;
                }
            } else {
                if (abbrevRelative) {
                    resId = com.android.internal.R.plurals.abbrev_in_num_days;
                } else {
                    resId = com.android.internal.R.plurals.in_num_days;
                }
            }
} else {
// We know that we won't be showing the time, so it is safe to pass
// in a null context.
return formatDateRange(null, time, time, flags);
        }

String format = r.getQuantityString(resId, (int) count);
return String.format(format, count);
}







