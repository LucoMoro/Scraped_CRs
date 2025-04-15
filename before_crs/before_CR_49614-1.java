/*telephony: Fix issue in PhoneNumberUtils.compareLoosely

If the number of matched digits is greater than or
equal to MIN_MATCH and if one of the input strings
index has reached the lower limit,
PhoneNumberUtils.compareLoosely should return
true. Currently, it doesn't return true as it is
expecting the index to be < 0 which will not happen
for the following case(country code+number without
"+" prefix) and number without country code)

This patch fixes the issue by checking the index <= 0.

Change-Id:I82292279fd6e7961a00b48dc3c62a1e4d1179f70Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 64234*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 8b85d8c..f7d8761 100644

//Synthetic comment -- @@ -508,7 +508,7 @@
}

// At least one string has matched completely;
        if (matched >= MIN_MATCH && (ia < 0 || ib < 0)) {
return true;
}








