/*Telephony(DSDS): MMS Auto Download

Change-Id:I95359fd544de89816673a66e6ea82f38dcdf9487*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/TelephonyProperties.java b/telephony/java/com/android/internal/telephony/TelephonyProperties.java
//Synthetic comment -- index b79adbb..904f642 100644

//Synthetic comment -- @@ -203,4 +203,10 @@
* Property to store default subscription.
*/
static final String PROPERTY_DEFAULT_SUBSCRIPTION = "persist.default.subscription";

    /**
     * Property to enable MMS Mode.
     * Type: string ( default = silent, enable to = prompt )
     */
    static final String PROPERTY_MMS_TRANSACTION = "mms.transaction";
}







