/*Call to equals() comparing different types

I have found this bug with the FindBugs tool

Change-Id:I7f157063b0a549cb4344c048ddb612a14a915a25Signed-off-by: László Dávid <laszlo.david@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/DataUsageSummary.java b/src/com/android/settings/DataUsageSummary.java
//Synthetic comment -- index 5e20a6e..edccf53 100644

//Synthetic comment -- @@ -1171,8 +1171,8 @@
final String rangePhrase = formatDateRange(context, start, end);

final int summaryRes;
        if (TAB_MOBILE.equals(mCurrentTab) || TAB_3G.equals(mCurrentApp)
                || TAB_4G.equals(mCurrentApp)) {
summaryRes = R.string.data_usage_total_during_range_mobile;
} else {
summaryRes = R.string.data_usage_total_during_range;







