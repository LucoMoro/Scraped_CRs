/*Telephony: Enabling Network Personalization PUK

Network personalisation is the feature were a SIM is restricted
to be used in a specific operator networks only. If an unsupported
SIM is used in the device, the user is asked to enter an unlock
code, so that the SIM can be used in the network. If the user enters
wrong PIN for a preprogrammed number of times, the device is PUK
locked and needs to be taken to the customer care. Android by default
supports entering the PIN, but doesn't inform the user if the device
is PUK locked.

This patch introduces the required enumerations, and INTENTS to be
used in the framework, which can inform the NETWORK PUK lock to
keyguard and lock screen for a suitable display.

Note: This patch is related to a change in platforms/frameworks/base,
		platform/frameworks/opt/telephony and platform/packages/apps/Phone

Change-Id:Id3b93f9085e7508770eb232ba50b728ffa4d54e2Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18871*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccCardConstants.java b/telephony/java/com/android/internal/telephony/IccCardConstants.java
//Synthetic comment -- index 236bb2f..8dc9eaf 100644

//Synthetic comment -- @@ -46,6 +46,8 @@
public static final String INTENT_VALUE_LOCKED_NETWORK = "NETWORK";
/* PERM_DISABLED means ICC is permanently disabled due to puk fails */
public static final String INTENT_VALUE_ABSENT_ON_PERM_DISABLED = "PERM_DISABLED";
    /* NETWORK_PUK means Device is locked on NETWORK PERSONALIZATION PUK */
    public static final String INTENT_VALUE_LOCKED_NETWORK_PUK = "NETWORK_PUK";

/**
* This is combination of IccCardStatus.CardState and IccCardApplicationStatus.AppState
//Synthetic comment -- @@ -63,7 +65,8 @@
NETWORK_LOCKED,
READY,
NOT_READY,
        PERM_DISABLED,
        NETWORK_LOCKED_PUK;

public boolean isPinLocked() {
return ((this == PIN_REQUIRED) || (this == PUK_REQUIRED));
//Synthetic comment -- @@ -72,7 +75,7 @@
public boolean iccCardExist() {
return ((this == PIN_REQUIRED) || (this == PUK_REQUIRED)
|| (this == NETWORK_LOCKED) || (this == READY)
                    || (this == PERM_DISABLED) || (this == NETWORK_LOCKED_PUK));
}
}
}








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/RILConstants.java b/telephony/java/com/android/internal/telephony/RILConstants.java
//Synthetic comment -- index 077ad68..3bea0ad 100644

//Synthetic comment -- @@ -55,6 +55,7 @@
int ILLEGAL_SIM_OR_ME = 15;               /* network selection failure due
to wrong SIM/ME and no
retries needed */
    int NETWORK_PUK_REQUIRED = 16;            /* Network Personalization PUK required */

/* NETWORK_MODE_* See ril.h RIL_REQUEST_SET_PREFERRED_NETWORK_TYPE */
int NETWORK_MODE_WCDMA_PREF     = 0; /* GSM/WCDMA (WCDMA preferred) */







