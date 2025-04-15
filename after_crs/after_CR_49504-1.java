/*Telephony: Enabling Network Personalization PUK in Framework

Network personalisation is the feature were a SIM is restricted
to be used in a specific operator networks only. If an unsupported
SIM is used in the device, the user is asked to enter an unlock
code, so that the SIM can be used in the network. If the user enters
wrong PIN for a preprogrammed number of times, the device is PUK
locked and needs to be taken to the customer care. Android by default
supports entering the PIN, but doesn't inform the user if the device
is PUK locked.

This patch handles the NETWORK PUK required status from modem in
command exception, handles the new event registration and notifications
in IccCardProxy and UiccCardApplication.

Note: This patch is related to a change in platforms/frameworks/base,
		platform/frameworks/opt/telephony and platform/packages/apps/Phone

Change-Id:Id3b93f9085e7508770eb232ba50b728ffa4d54e2Author: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 18871*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CommandException.java b/src/java/com/android/internal/telephony/CommandException.java
//Synthetic comment -- index d1085f6..aacfdf7 100644

//Synthetic comment -- @@ -42,6 +42,7 @@
MODE_NOT_SUPPORTED,
FDN_CHECK_FAILURE,
ILLEGAL_SIM_OR_ME,
        NETWORK_PUK_REQUIRED,
}

public CommandException(Error e) {
//Synthetic comment -- @@ -83,6 +84,8 @@
return new CommandException(Error.FDN_CHECK_FAILURE);
case RILConstants.ILLEGAL_SIM_OR_ME:
return new CommandException(Error.ILLEGAL_SIM_OR_ME);
            case RILConstants.NETWORK_PUK_REQUIRED:
                return new CommandException(Error.NETWORK_PUK_REQUIRED);
default:
Rlog.e("GSM", "Unrecognized RIL errno " + ril_errno);
return new CommandException(Error.INVALID_RESPONSE);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index 1ec4f18..292a0b2 100644

//Synthetic comment -- @@ -67,6 +67,12 @@
public void unregisterForNetworkLocked(Handler h);

/**
     * Notifies handler of any transition into IccCardConstants.State.NETWORK_LOCKED_PUK
     */
    public void registerForNetworkLockedPuk(Handler h, int what, Object obj);
    public void unregisterForNetworkLockedPuk(Handler h);

    /**
* Notifies handler of any transition into IccCardConstants.State.isPinLocked()
*/
public void registerForLocked(Handler h, int what, Object obj);








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccCardApplicationStatus.java b/src/java/com/android/internal/telephony/uicc/IccCardApplicationStatus.java
//Synthetic comment -- index db5065b..27c7001 100644

//Synthetic comment -- @@ -94,6 +94,10 @@
boolean isPersoSubStateUnknown() {
return this == PERSOSUBSTATE_UNKNOWN;
}

        boolean isSubscriptionPukRequired() {
            return this == PERSOSUBSTATE_SIM_NETWORK_PUK;
        }
};

public AppType        app_type;








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccCardProxy.java b/src/java/com/android/internal/telephony/uicc/IccCardProxy.java
//Synthetic comment -- index e0e7124..94b843f 100644

//Synthetic comment -- @@ -83,6 +83,7 @@
private static final int EVENT_IMSI_READY = 8;
private static final int EVENT_NETWORK_LOCKED = 9;
private static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 11;
    private static final int EVENT_NETWORK_LOCKED_PUK = 12;

private final Object mLock = new Object();
private Context mContext;
//Synthetic comment -- @@ -91,6 +92,7 @@
private RegistrantList mAbsentRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedPukRegistrants = new RegistrantList();

private int mCurrentAppType = UiccController.APP_FAM_3GPP; //default to 3gpp?
private UiccController mUiccController = null;
//Synthetic comment -- @@ -237,6 +239,10 @@
case EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED:
updateQuietMode();
break;
            case EVENT_NETWORK_LOCKED_PUK:
                mNetworkLockedPukRegistrants.notifyRegistrants();
                setExternalState(State.NETWORK_LOCKED_PUK);
                break;
default:
loge("Unhandled message with number: " + msg.what);
break;
//Synthetic comment -- @@ -300,6 +306,8 @@
case APPSTATE_SUBSCRIPTION_PERSO:
if (mUiccApplication.getPersoSubState() == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
setExternalState(State.NETWORK_LOCKED);
                } else if (mUiccApplication.getPersoSubState().isSubscriptionPukRequired()) {
                   setExternalState(State.NETWORK_LOCKED_PUK);
} else {
setExternalState(State.UNKNOWN);
}
//Synthetic comment -- @@ -316,6 +324,7 @@
mUiccApplication.registerForReady(this, EVENT_APP_READY, null);
mUiccApplication.registerForLocked(this, EVENT_ICC_LOCKED, null);
mUiccApplication.registerForNetworkLocked(this, EVENT_NETWORK_LOCKED, null);
            mUiccApplication.registerForNetworkLockedPuk(this, EVENT_NETWORK_LOCKED_PUK, null);
}
if (mIccRecords != null) {
mIccRecords.registerForImsiReady(this, EVENT_IMSI_READY, null);
//Synthetic comment -- @@ -328,6 +337,7 @@
if (mUiccApplication != null) mUiccApplication.unregisterForReady(this);
if (mUiccApplication != null) mUiccApplication.unregisterForLocked(this);
if (mUiccApplication != null) mUiccApplication.unregisterForNetworkLocked(this);
        if (mUiccApplication != null) mUiccApplication.unregisterForNetworkLockedPuk(this);
if (mIccRecords != null) mIccRecords.unregisterForImsiReady(this);
if (mIccRecords != null) mIccRecords.unregisterForRecordsLoaded(this);
}
//Synthetic comment -- @@ -412,6 +422,7 @@
case READY: return IccCardConstants.INTENT_VALUE_ICC_READY;
case NOT_READY: return IccCardConstants.INTENT_VALUE_ICC_NOT_READY;
case PERM_DISABLED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
            case NETWORK_LOCKED_PUK: return IccCardConstants.INTENT_VALUE_LOCKED_NETWORK_PUK;
default: return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
}
}
//Synthetic comment -- @@ -502,6 +513,29 @@
}

/**
     * Notifies handler of any transition into State.NETWORK_LOCKED_PUK
     */
    @Override
    public void registerForNetworkLockedPuk(Handler h, int what, Object obj) {
        synchronized (mLock) {
            Registrant r = new Registrant (h, what, obj);

            mNetworkLockedPukRegistrants.add(r);

            if (getState() == State.NETWORK_LOCKED_PUK) {
                r.notifyRegistrant();
            }
        }
    }

    @Override
    public void unregisterForNetworkLockedPuk(Handler h) {
        synchronized (mLock) {
            mNetworkLockedPukRegistrants.remove(h);
        }
    }

    /**
* Notifies handler of any transition into State.isPinLocked()
*/
@Override








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java b/src/java/com/android/internal/telephony/uicc/UiccCardApplication.java
//Synthetic comment -- index 6618197..c781a91 100644

//Synthetic comment -- @@ -70,6 +70,7 @@
private RegistrantList mReadyRegistrants = new RegistrantList();
private RegistrantList mPinLockedRegistrants = new RegistrantList();
private RegistrantList mNetworkLockedRegistrants = new RegistrantList();
    private RegistrantList mNetworkLockedPukRegistrants = new RegistrantList();

UiccCardApplication(UiccCard uiccCard,
IccCardApplicationStatus as,
//Synthetic comment -- @@ -126,9 +127,12 @@
mIccRecords = createIccRecords(as.app_type, c, ci);
}

            if (mPersoSubState != oldPersoSubState ) {
                if (mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK) {
                    notifyNetworkLockedRegistrantsIfNeeded(null);
                } else if (mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK_PUK) {
                    notifyNetworkLockedPukRegistrantsIfNeeded(null);
                }
}

if (mAppState != oldAppState) {
//Synthetic comment -- @@ -388,6 +392,23 @@
}

/**
     * Notifies handler of any transition into State.NETWORK_LOCKED_PUK
     */
    public void registerForNetworkLockedPuk(Handler h, int what, Object obj) {
        synchronized (mLock) {
            Registrant r = new Registrant (h, what, obj);
            mNetworkLockedPukRegistrants.add(r);
            notifyNetworkLockedPukRegistrantsIfNeeded(r);
        }
    }

    public void unregisterForNetworkLockedPuk(Handler h) {
        synchronized (mLock) {
            mNetworkLockedPukRegistrants.remove(h);
        }
    }

    /**
* Notifies specified registrant, assume mLock is held.
*
* @param r Registrant to be notified. If null - all registrants will be notified
//Synthetic comment -- @@ -418,6 +439,28 @@
* Notifies specified registrant, assume mLock is held.
*
* @param r Registrant to be notified. If null - all registrants will be notified
    */
    private void notifyNetworkLockedPukRegistrantsIfNeeded(Registrant r) {
        if (mDestroyed) {
            return;
        }

        if (mAppState == AppState.APPSTATE_SUBSCRIPTION_PERSO &&
                mPersoSubState == PersoSubState.PERSOSUBSTATE_SIM_NETWORK_PUK) {
            if (r == null) {
                if (DBG) log("Notifying registrants: NETWORK_LOCKED_PUK");
                mNetworkLockedPukRegistrants.notifyRegistrants();
            } else {
                if (DBG) log("Notifying 1 registrant: NETWORK_LOCKED_PUK");
                r.notifyRegistrant(new AsyncResult(null, null, null));
            }
        }
    }

    /**
     * Notifies specified registrant, assume mLock is held.
     *
     * @param r Registrant to be notified. If null - all registrants will be notified
*/
private void notifyPinLockedRegistrantsIfNeeded(Registrant r) {
if (mDestroyed) {







