/*Fixes relative time formatting issue for dates in future

All variations of getRelativeTimeSpanString() now properly handle dates
that are in the future. Prior, the version used by
getRelativeDateTimeString() would occasionally show the time instead of
a date when the future date was the same weekday as the current weekday.
This resulted in the time output being duplicated, eg.: "11:23, 11:23"

Change-Id:If20972a6942cce792fa233437f94dedfb71379f3Signed-off-by: Steve Pomeroy <steve@staticfree.info>*/
//Synthetic comment -- diff --git a/core/java/android/text/format/DateUtils.java b/core/java/android/text/format/DateUtils.java
//Synthetic comment -- index da10311..211453d 100644

//Synthetic comment -- @@ -1618,7 +1618,7 @@

String result;
long now = System.currentTimeMillis();
        long span = now - millis;

synchronized (DateUtils.class) {
if (sNowTime == null) {







