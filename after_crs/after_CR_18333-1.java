/*telephony: Release all calls on pressing end key.

End all call sessions (except waiting call) when
end key (hard key) is pressed as per 3GPP TS 22.030,
6.5.5.1.

Change-Id:I9631e35feaf583f7f1b494ee9abaaaf9019b65fd*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/Call.java b/telephony/java/com/android/internal/telephony/Call.java
//Synthetic comment -- index 4967ab8..2a6e3dd 100644

//Synthetic comment -- @@ -68,7 +68,7 @@
public abstract Phone getPhone();
public abstract boolean isMultiparty();
public abstract void hangup() throws CallStateException;
    public abstract void hangupAllCalls() throws CallStateException;

/**
* hasConnection








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaCall.java b/telephony/java/com/android/internal/telephony/cdma/CdmaCall.java
//Synthetic comment -- index c3bb01f..ea712ed 100644

//Synthetic comment -- @@ -92,6 +92,11 @@
owner.hangup(this);
}

    public void
    hangupAllCalls() throws CallStateException {
        throw new CallStateException ("hangupAllCalls: Unsupported Action");
    }

public String
toString() {
return state.toString();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCall.java b/telephony/java/com/android/internal/telephony/gsm/GsmCall.java
//Synthetic comment -- index 9542d20..33467cc 100644

//Synthetic comment -- @@ -88,6 +88,11 @@
owner.hangup(this);
}

    public void
    hangupAllCalls() throws CallStateException {
        owner.hangupAllCalls();
    }

public String
toString() {
return state.toString();








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmCallTracker.java
//Synthetic comment -- index 06f310c..5d43344 100644

//Synthetic comment -- @@ -759,6 +759,37 @@
phone.notifyPreciseCallStateChanged();
}

    void hangupAllCalls () throws CallStateException {
        boolean hungUp = false;
        if (!ringingCall.isIdle()) {
             // Do not hangup waiting call
             // as per 3GPP TS 22.030, 6.5.5.1.
             if (ringingCall.getState() != GsmCall.State.WAITING) {
                 log("hangupAllCalls: hang up ringing call");
                 cm.hangupWaitingOrBackground(obtainCompleteMessage());
                 ringingCall.onHangupLocal();
                 hungUp = true;
             }
        }
        if (!foregroundCall.isIdle()) {
            log("hangupAllCalls: hang up active call");
            hangupAllConnections(foregroundCall);
            foregroundCall.onHangupLocal();
            hungUp = true;
        }
        if (!backgroundCall.isIdle()) {
            log("hangupAllCalls: hang up held call");
            hangupAllConnections(backgroundCall);
            backgroundCall.onHangupLocal();
            hungUp = true;
        }
        if (hungUp) {
            phone.notifyPreciseCallStateChanged();
        } else {
            throw new CallStateException("no active connections to hangup");
        }
    }

/* package */
void hangupWaitingOrBackground() {
if (Phone.DEBUG_PHONE) log("hangupWaitingOrBackground");







