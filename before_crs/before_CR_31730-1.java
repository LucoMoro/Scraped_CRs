/*Do not set minimum value for mobile data limit dialog higher than the actual set value

When mobile data limit and warning limit are set to 0, pressing mobile data limit text
(0.0 GB) pops up mobile data limit dialog with minimum value 1. But the value set is 0,
which is causing the dialog to show its maximum value 2^31-1.

Change-Id:I79ed4df5fbf095a7e60bda640ff2ca26bcbd3cc4Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/DataUsageSummary.java b/src/com/android/settings/DataUsageSummary.java
//Synthetic comment -- index 46d6c65..a2f0c3f 100644

//Synthetic comment -- @@ -1761,7 +1761,7 @@
final long limitBytes = editor.getPolicyLimitBytes(template);

bytesPicker.setMaxValue(Integer.MAX_VALUE);
            if (warningBytes != WARNING_DISABLED) {
bytesPicker.setMinValue((int) (warningBytes / MB_IN_BYTES) + 1);
} else {
bytesPicker.setMinValue(0);







