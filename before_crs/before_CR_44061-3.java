/*Telephony: Support for LTE network modes

Change-Id:I8833fe3e56c94303f05111a94433b1b9e6568d2fDepends-On:I93c102f92ff2d10856d91108bb50fe5c0f06ad66*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 7502dc5..fbce476 100644

//Synthetic comment -- @@ -126,9 +126,12 @@
int NT_MODE_EVDO_NO_CDMA = RILConstants.NETWORK_MODE_EVDO_NO_CDMA;
int NT_MODE_GLOBAL       = RILConstants.NETWORK_MODE_GLOBAL;

    int NT_MODE_LTE_ONLY     = RILConstants.NETWORK_MODE_LTE_ONLY;
    int PREFERRED_NT_MODE    = RILConstants.PREFERRED_NETWORK_MODE;


// Used for CDMA roaming mode
static final int CDMA_RM_HOME        = 0;  // Home Networks only, as defined in PRL







