/*Disable background data restriction when mobile data limit is disabled

Restricting background data is possible only when mobile data limit is set.
But opposite logic is not followed, i.e.:
    1. Enable "Set mobile data limit".
    2. Enable "Restrict background date".
    3. Disable "Set mobile data limit".
    4. Background data restiction remains effective.

Change-Id:Ib43103420a4615821a3378f6a69db260f7de72afSigned-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/settings/DataUsageSummary.java b/src/com/android/settings/DataUsageSummary.java
//Synthetic comment -- index a2f0c3f..67cbeed 100644

//Synthetic comment -- @@ -1027,6 +1027,10 @@
ConfirmLimitFragment.show(DataUsageSummary.this);
} else {
setPolicyLimitBytes(LIMIT_DISABLED);
                if (mMenuRestrictBackground.isChecked()) {
                    // limit is disabled; disable background data restriction
                    setRestrictBackground(false);
                }
}
}
};







