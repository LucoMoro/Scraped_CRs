/*Telephony: Handle ICC IO error & display PIN/PUK count

frameworks/base: Handle ICC IO error & display PIN/PUK count.

Following changes have been made as part of this:
-> At present in Android all ICC Card states other than ICC
   PRESENT are treated as ICC ABSENT. Adding functionality
   to handle ICC IO error card state.
-> Changes done to display retry counter on wrong entry of
   PIN1,and message to indicate Code accepted/PIN1 blocked
   during PIN1 verification as per certain carrier requirements.
->The current APIs that are used to verify the PIN and PUK only convey
  whether the operation succeeded or failed. As a result on ANY failure
  clients ask the user to re-enter the PIN.
  Add 2 new APIs that report the actual error code in case of failure.

Change-Id:If45cf01f567c97de45fd06fb405062dd2673ed49*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCard.java b/src/java/com/android/internal/telephony/IccCard.java
//Synthetic comment -- index f1ac581..33018b2 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;

import com.android.internal.telephony.IccCardConstants.State;

//Synthetic comment -- @@ -110,6 +111,13 @@
public void supplyNetworkDepersonalization (String pin, Message onComplete);

/**
     * Check whether fdn (fixed dialing number) service is available.
     * @return true if ICC fdn service available
     *         false if ICC fdn service not available
    */
    public boolean getIccFdnAvailable();

    /**
* Check whether ICC pin lock is enabled
* This is a sync call which returns the cached pin enabled state
*
//Synthetic comment -- @@ -125,7 +133,18 @@
* @return true for ICC fdn enabled
*         false for ICC fdn disabled
*/
     public boolean getIccFdnEnabled();

     /**
     * @return No. of Attempts remaining to unlock PIN1/PUK1
     */
    public int getIccPin1RetryCount();

    /**
     * @return No. of Attempts remaining to unlock PIN2/PUK2
     */
    public int getIccPin2RetryCount();


/**
* Set the ICC pin lock enabled or disabled
//Synthetic comment -- @@ -211,4 +230,15 @@
* @return true if a ICC card is present
*/
public boolean hasIccCard();

    /**
     * @return true if ICC card is PIN2 blocked
     */
    public boolean getIccPin2Blocked();

    /**
     * @return true if ICC card is PUK2 blocked
     */
    public boolean getIccPuk2Blocked();

}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardProxy.java b/src/java/com/android/internal/telephony/IccCardProxy.java
//Synthetic comment -- index eef0c6f..57e5936 100644

//Synthetic comment -- @@ -268,7 +268,7 @@

if (mUiccCard.getCardState() == CardState.CARDSTATE_ERROR ||
mUiccApplication == null) {
            setExternalState(State.CARD_IO_ERROR);
return;
}

//Synthetic comment -- @@ -338,6 +338,33 @@
}
}

    public boolean getIccFdnAvailable() {
        boolean retValue = mUiccApplication != null ? mUiccApplication.getIccFdnAvailable() : false;
        return retValue;
    }

    public int getIccPin1RetryCount() {
        int retValue = mUiccApplication != null ? mUiccApplication.getIccPin1RetryCount() : -1;
        return retValue;
    }

    public int getIccPin2RetryCount() {
        int retValue = mUiccApplication != null ? mUiccApplication.getIccPin2RetryCount() : -1;
        return retValue;
    }

    public boolean getIccPin2Blocked() {
        /* defaults to disabled */
        Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccPin2Blocked() : false;
        return retValue;
    }

    public boolean getIccPuk2Blocked() {
        /* defaults to disabled */
        Boolean retValue = mUiccApplication != null ? mUiccApplication.getIccPuk2Blocked() : false;
        return retValue;
    }

private void processLockedState() {
synchronized (mLock) {
if (mUiccApplication == null) {
//Synthetic comment -- @@ -397,12 +424,14 @@
case READY: return IccCardConstants.INTENT_VALUE_ICC_READY;
case NOT_READY: return IccCardConstants.INTENT_VALUE_ICC_NOT_READY;
case PERM_DISABLED: return IccCardConstants.INTENT_VALUE_ICC_LOCKED;
            case CARD_IO_ERROR: return IccCardConstants.INTENT_VALUE_ICC_CARD_IO_ERROR;
default: return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
}
}

/**
     * Locked state have a reason (PIN, PUK, NETWORK, PERM_DISABLED,
     *  CARD_IO_ERROR)
* @return reason
*/
private String getIccStateReason(State state) {
//Synthetic comment -- @@ -411,6 +440,7 @@
case PUK_REQUIRED: return IccCardConstants.INTENT_VALUE_LOCKED_ON_PUK;
case NETWORK_LOCKED: return IccCardConstants.INTENT_VALUE_LOCKED_NETWORK;
case PERM_DISABLED: return IccCardConstants.INTENT_VALUE_ABSENT_ON_PERM_DISABLED;
            case CARD_IO_ERROR: return IccCardConstants.INTENT_VALUE_ICC_CARD_IO_ERROR;
default: return null;
}
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardStatus.java b/src/java/com/android/internal/telephony/IccCardStatus.java
//Synthetic comment -- index b4a5e68..bfc731d 100644

//Synthetic comment -- @@ -34,7 +34,11 @@
boolean isCardPresent() {
return this == CARDSTATE_PRESENT;
}

        boolean isCardFaulty() {
            return this == CARDSTATE_ERROR;
        }
    };

public enum PinState {
PINSTATE_UNKNOWN,








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index 34aa96c..60acc0e 100644

//Synthetic comment -- @@ -78,6 +78,14 @@
static final String FEATURE_ENABLE_CBS = "enableCBS";

/**
     * Return codes for supplyPinReturnResult and
     * supplyPukReturnResult APIs
     */
    static final int PIN_RESULT_SUCCESS = 0;
    static final int PIN_PASSWORD_INCORRECT = 1;
    static final int PIN_GENERAL_FAILURE = 2;

    /**
* Optional reasons for disconnect and connect
*/
static final String REASON_ROAMING_ON = "roamingOn";








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/UiccCardApplication.java b/src/java/com/android/internal/telephony/UiccCardApplication.java
//Synthetic comment -- index 2718af6..473adf6 100644

//Synthetic comment -- @@ -42,10 +42,14 @@
private static final String LOG_TAG = "RIL_UiccCardApplication";
private static final boolean DBG = true;

    private static final int EVENT_PIN1PUK1_DONE = 1;
    private static final int EVENT_CHANGE_FACILITY_LOCK_DONE = 2;
    private static final int EVENT_CHANGE_PIN1_DONE = 3;
    private static final int EVENT_CHANGE_PIN2_DONE = 4;
    private static final int EVENT_QUERY_FACILITY_FDN_DONE = 5;
    private static final int EVENT_CHANGE_FACILITY_FDN_DONE = 6;
    private static final int EVENT_PIN2PUK2_DONE = 7;
    private static final int EVENT_QUERY_FACILITY_LOCK_DONE = 8;

private final Object  mLock = new Object();
private UiccCard      mUiccCard; //parent
//Synthetic comment -- @@ -61,6 +65,9 @@
private boolean       mDesiredFdnEnabled;
private boolean       mIccLockEnabled;
private boolean       mDesiredPinLocked;
    private int mPin1RetryCount = -1;
    private int mPin2RetryCount = -1;


private CommandsInterface mCi;
private Context mContext;
//Synthetic comment -- @@ -228,6 +235,9 @@
if (DBG) log("EVENT_CHANGE_FACILITY_FDN_DONE: " +
"mIccFdnEnabled=" + mIccFdnEnabled);
} else {
                if (ar.result != null) {
                    parsePinPukErrorResult(ar, false);
                }
loge("Error change facility fdn with exception " + ar.exception);
}
Message response = (Message)ar.userObj;
//Synthetic comment -- @@ -235,6 +245,22 @@
response.sendToTarget();
}
}
    /**
     * Parse the error response to obtain No of attempts remaining to unlock PIN1/PUK1
     */
    private void parsePinPukErrorResult(AsyncResult ar, boolean isPin1) {
        int[] intArray = (int[]) ar.result;
        int length = intArray.length;
        mPin1RetryCount = -1;
        mPin2RetryCount = -1;
        if (length > 0) {
            if (isPin1) {
                mPin1RetryCount = intArray[0];
            } else {
                mPin2RetryCount = intArray[0];
            }
        }
    }

/** REMOVE when mIccLockEnabled is not needed, assumes mLock is held */
private void queryPin1State() {
//Synthetic comment -- @@ -319,6 +345,23 @@
}

switch (msg.what) {
                case EVENT_PIN1PUK1_DONE:
                case EVENT_PIN2PUK2_DONE:
                    // a PIN/PUK/PIN2/PUK2/Network Personalization
                    // request has completed. ar.userObj is the response Message
                    ar = (AsyncResult)msg.obj;
                    // TODO should abstract these exceptions
                    if ((ar.exception != null) && (ar.result != null)) {
                        if (msg.what == EVENT_PIN1PUK1_DONE) {
                            parsePinPukErrorResult(ar, true);
                        } else {
                            parsePinPukErrorResult(ar, false);
                        }
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
case EVENT_QUERY_FACILITY_FDN_DONE:
ar = (AsyncResult)msg.obj;
onQueryFdnEnabled(ar);
//Synthetic comment -- @@ -327,6 +370,32 @@
ar = (AsyncResult)msg.obj;
onChangeFdnDone(ar);
break;
                case EVENT_CHANGE_PIN1_DONE:
                    ar = (AsyncResult)msg.obj;
                    if(ar.exception != null) {
                        loge("Error in change icc app password with exception"
                            + ar.exception);
                        if (ar.result != null) {
                            parsePinPukErrorResult(ar, true);
                        }
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
                case EVENT_CHANGE_PIN2_DONE:
                    ar = (AsyncResult)msg.obj;
                    if(ar.exception != null) {
                        loge("Error in change icc app password with exception"
                            + ar.exception);
                        if (ar.result != null) {
                            parsePinPukErrorResult(ar, false);
                        }
                    }
                    AsyncResult.forMessage(((Message)ar.userObj)).exception
                                                        = ar.exception;
                    ((Message)ar.userObj).sendToTarget();
                    break;
case EVENT_QUERY_FACILITY_LOCK_DONE:
ar = (AsyncResult)msg.obj;
onQueryFacilityLock(ar);
//Synthetic comment -- @@ -533,25 +602,28 @@
*/
public void supplyPin (String pin, Message onComplete) {
synchronized (mLock) {
        mCi.supplyIccPinForApp(pin, mAid, mHandler.obtainMessage(EVENT_PIN1PUK1_DONE, onComplete));
}
}

public void supplyPuk (String puk, String newPin, Message onComplete) {
synchronized (mLock) {
        mCi.supplyIccPukForApp(puk, newPin, mAid,
                mHandler.obtainMessage(EVENT_PIN1PUK1_DONE, onComplete));
}
}

public void supplyPin2 (String pin2, Message onComplete) {
synchronized (mLock) {
        mCi.supplyIccPin2ForApp(pin2, mAid,
                mHandler.obtainMessage(EVENT_PIN2PUK2_DONE, onComplete));
}
}

public void supplyPuk2 (String puk2, String newPin2, Message onComplete) {
synchronized (mLock) {
        mCi.supplyIccPuk2ForApp(puk2, newPin2, mAid,
                mHandler.obtainMessage(EVENT_PIN2PUK2_DONE, onComplete));
}
}

//Synthetic comment -- @@ -595,6 +667,30 @@
}

/**
     * Check whether fdn (fixed dialing number) service is available.
     * @return true if ICC fdn service available
     *         false if ICC fdn service not available
     */
    public boolean getIccFdnAvailable() {
        return mIccFdnAvailable;
    }

     /**
     * @return No. of Attempts remaining to unlock PIN1/PUK1
     */
     public int getIccPin1RetryCount() {
         return mPin1RetryCount;
     }

     /**
      * @return No. of Attempts remaining to unlock PIN2/PUK2
     */
     public int getIccPin2RetryCount() {
         return mPin2RetryCount;
     }


    /**
* Set the ICC pin lock enabled or disabled
* When the operation is complete, onComplete will be sent to its handler
*
//Synthetic comment -- @@ -689,6 +785,20 @@
}
}

    /**
     * @return true if ICC card is PIN2 blocked
     */
    public boolean getIccPin2Blocked() {
        return mPin2State == PinState.PINSTATE_ENABLED_BLOCKED;
    }

    /**
     * @return true if ICC card is PUK2 blocked
     */
    public boolean getIccPuk2Blocked() {
        return mPin2State == PinState.PINSTATE_ENABLED_PERM_BLOCKED;
    }

private void log(String msg) {
Log.d(LOG_TAG, msg);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..1a05f18 100644

//Synthetic comment -- @@ -993,6 +993,16 @@
if (sc.equals(SC_PUK) || sc.equals(SC_PUK2)) {
sb.append(context.getText(
com.android.internal.R.string.badPuk));
                            // Get the No. of attempts remaining to unlock PUK1 from the result
                            if (ar.result != null) {
                                int[] pukAttemptsRemaining = (int[]) ar.result;
                                if ((pukAttemptsRemaining.length > 0) &&
                                        (pukAttemptsRemaining[0] >= 0)) {
                                    sb.append(context.getText(
                                        com.android.internal.R.string.pinpuk_attempts));
                                    sb.append(pukAttemptsRemaining[0]);
                                }
                            }
} else {
sb.append(context.getText(
com.android.internal.R.string.badPin));







