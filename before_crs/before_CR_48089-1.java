/*Telephony: Fix issues with cursor closing

There are unclosed cursor in telephony framework, which
creats issue with MMS downloading, in MTBF tests.

Change-Id:I1d6594a03b53cb49076656af34ed9dfa459f1ad4Author: Javen Ni <javen.ni@borqs.com>
Signed-off-by: Javen Ni <javen.ni@borqs.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 16858*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index e8b662c..e140055 100644

//Synthetic comment -- @@ -986,6 +986,9 @@
result.add(apn);
} while (cursor.moveToNext());
}
if (DBG) log("createApnList: X result=" + result);
return result;
}







