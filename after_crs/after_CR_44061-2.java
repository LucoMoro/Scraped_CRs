/*Telephony: Support for LTE network modes

Change-Id:I8833fe3e56c94303f05111a94433b1b9e6568d2f*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 34aa96c..acb2cf5 100644

//Synthetic comment -- @@ -125,9 +125,12 @@
int NT_MODE_EVDO_NO_CDMA = RILConstants.NETWORK_MODE_EVDO_NO_CDMA;
int NT_MODE_GLOBAL       = RILConstants.NETWORK_MODE_GLOBAL;

    int NT_MODE_LTE_CDMA_AND_EVDO        = RILConstants.NETWORK_MODE_LTE_CDMA_EVDO;
    int NT_MODE_LTE_GSM_WCDMA            = RILConstants.NETWORK_MODE_LTE_GSM_WCDMA;
    int NT_MODE_LTE_CMDA_EVDO_GSM_WCDMA  = RILConstants.NETWORK_MODE_LTE_CMDA_EVDO_GSM_WCDMA;
    int NT_MODE_LTE_ONLY                 = RILConstants.NETWORK_MODE_LTE_ONLY;
    int NT_MODE_LTE_WCDMA                = RILConstants.NETWORK_MODE_LTE_WCDMA;
    int PREFERRED_NT_MODE                = RILConstants.PREFERRED_NETWORK_MODE;

// Used for CDMA roaming mode
static final int CDMA_RM_HOME        = 0;  // Home Networks only, as defined in PRL







