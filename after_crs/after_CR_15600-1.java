/*Defect 9249:7 days ago and date of call made are the two status
shown on 7th day in call log

Change-Id:Ib5accef29819b740a7a661d0181c0066a87d3a30*/




//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index 89b3cba..5f0e9fe 100644

//Synthetic comment -- @@ -495,25 +495,27 @@
resId = com.android.internal.R.plurals.in_num_hours;
}
}
        } else {
count = getNumberOfDaysPassed(time, now);
            if (count < (WEEK_IN_MILLIS / DAY_IN_MILLIS) && minResolution < WEEK_IN_MILLIS) {
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
}

String format = r.getQuantityString(resId, (int) count);







