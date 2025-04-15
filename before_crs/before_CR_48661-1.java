/*Telephony: Definition of new data profile based on apn type

Definition of new data profiles MMS, HIPRI and SUPL.
The value of the data profile is determined based on the the apn type
define in Phone.java.

Change-Id:Idcd26ccc49f3e0f32bd8a79b7b94db6caa0ca3e2Author: Alain Kouassu <alain.kouassu@intel.com>
Signed-off-by: Alain Kouassu <alain.kouassu@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 32959*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index f501b21..3b00baa 100644

//Synthetic comment -- @@ -150,6 +150,9 @@
public static final int DATA_PROFILE_IMS       = 2;
public static final int DATA_PROFILE_FOTA      = 3;
public static final int DATA_PROFILE_CBS       = 4;
public static final int DATA_PROFILE_OEM_BASE  = 1000;

int RIL_REQUEST_GET_SIM_STATUS = 1;







