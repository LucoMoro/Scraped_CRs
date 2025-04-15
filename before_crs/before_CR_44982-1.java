/*Frameworks: Enhanced Operator Name String (EONS) algorithm.

The operator name for registered PLMN is displayed in the
following order of preference.

1) Name from EF_OPL/EF_PNN files.

2) Name from NITZ messge.

3) Name from ME database.

4) Name from Network.

This algorithm implements this operator name deriving logic.
3GPP specs referred
1) TS 22.101 A.3 - operator name priority.
2) TS 31.102 - for EF data description.
3) TS 24.008 - for PLMN and LAC coding details.
4) TS 23.122 - for PLMN matching.

Add support to derive EONS for LTE networks based on TAC.

Change-Id:I9c2d42ebdb0890f10ec46dda534c1669a66ccba1*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index f95e081..fcbb95a 100644

//Synthetic comment -- @@ -187,4 +187,11 @@
* Ignore RIL_UNSOL_NITZ_TIME_RECEIVED completely, used for debugging/testing.
*/
static final String PROPERTY_IGNORE_NITZ = "telephony.test.ignore.nitz";
}







