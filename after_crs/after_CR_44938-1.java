/*Framework: System Property to Ignore Out of State Indications

Add the property persist.telephony.oosisdc in TelephonyProperties class

Change-Id:Ia96a1cd003e45b91f80e505b8977f3c2532ba7f7*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index f95e081..de86b2c 100644

//Synthetic comment -- @@ -187,4 +187,9 @@
* Ignore RIL_UNSOL_NITZ_TIME_RECEIVED completely, used for debugging/testing.
*/
static final String PROPERTY_IGNORE_NITZ = "telephony.test.ignore.nitz";

    /**
     * Indicates whether Out Of Service is considered as data call disconnect.
     */
    static final String PROPERTY_OOS_IS_DISCONNECT = "persist.telephony.oosisdc";
}







