/*Add an API for Call Barring services

Call Barring services are specified in 3GPP TS 22.088 and allows
an user to have barring of certain categories of outgoing or
incoming calls. This commit adds an API for the framework for this
purpose.

Change-Id:I5f9f2b35749377dcc430318018ac5558a25af62a*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/Phone.java b/src/java/com/android/internal/telephony/Phone.java
//Synthetic comment -- index fbce476..0cb432d 100644

//Synthetic comment -- @@ -952,6 +952,50 @@
Message onComplete);

/**
     * getCallBarringOption gets a call barring option. The return value of
     * ((AsyncResult)onComplete.obj) will be an Integer representing the sum of
     * enabled serivice classes (sum of SERVICE_CLASS_*)
     *
     * @param facility is one of CB_FACILTY_*
     * @param password is password or "" if not required
     * @param serviceClass is a sum of SERVICE_CLASS_*
     * @param response is callback message when the action is completed.
     * */
    void getCallBarringOption(String facility,
                              String password,
                              int serviceClass,
                              Message onComplete);

    /**
     * setCallBarringOption sets a call barring option.
     *
     * @param facility is one of CB_FACILTY_*
     * @param lockState is true means lock, false means unlock
     * @param password is password or "" if not required
     * @param serviceClass is a sum of SERVICE_CLASS_*
     * @param response is callback message when the action is completed.
     */
    void setCallBarringOption(String facility,
                              boolean lockState,
                              String password,
                              int serviceClass,
                              Message onComplete);

    /**
     * changeCallBarringPasswordwhen the action is completed. changes access
     * code used for call barring
     *
     * @param facility is one of CB_FACILTY_*
     * @param oldPwd is old password
     * @param newPwd is new password
     * @param response is callback message when the action is completed.
     */
    void changeCallBarringPassword(String facility,
                                   String oldPwd,
                                   String newPwd,
                                   Message onComplete);

    /**
* getOutgoingCallerIdDisplay
* gets outgoing caller id display. The return value of
* ((AsyncResult)onComplete.obj) is an array of int, with a length of 2.








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/PhoneProxy.java b/src/java/com/android/internal/telephony/PhoneProxy.java
//Synthetic comment -- index 300529b..c4b0956 100644

//Synthetic comment -- @@ -631,6 +631,21 @@
commandInterfaceCFAction, dialingNumber, timerSeconds, onComplete);
}

    public void getCallBarringOption(String facility, String password, int serviceClass,
            Message onComplete) {
        mActivePhone.getCallBarringOption(facility, password, serviceClass, onComplete);
    }

    public void setCallBarringOption(String facility, boolean lockState, String password,
            int serviceClass, Message onComplete) {
        mActivePhone.setCallBarringOption(facility, lockState, password, serviceClass, onComplete);
    }

    public void changeCallBarringPassword(String facility, String oldPwd, String newPwd,
            Message onComplete) {
        mActivePhone.changeCallBarringPassword(facility, oldPwd, newPwd, onComplete);
    }

public void getOutgoingCallerIdDisplay(Message onComplete) {
mActivePhone.getOutgoingCallerIdDisplay(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/CDMAPhone.java b/src/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1465a17..a967c47 100755

//Synthetic comment -- @@ -783,6 +783,21 @@
Rlog.e(LOG_TAG, "setCallForwardingOption: not possible in CDMA");
}

    public void getCallBarringOption(String facility, String password, int serviceClass,
            Message onComplete) {
        Rlog.e(LOG_TAG, "getCallBarringOption: not possible in CDMA");
    }

    public void setCallBarringOption(String facility, boolean lockState, String password,
            int serviceClass, Message onComplete) {
        Rlog.e(LOG_TAG, "setCallCallBarringOption: not possible in CDMA");
    }

    public void changeCallBarringPassword(String facility, String oldPwd, String newPwd,
            Message onComplete) {
        Rlog.e(LOG_TAG, "changeCallBarringPassword: not possible in CDMA");
    }

public void
getOutgoingCallerIdDisplay(Message onComplete) {
Rlog.e(LOG_TAG, "getOutgoingCallerIdDisplay: not possible in CDMA");








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GSMPhone.java b/src/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index b930348..c238eed 100644

//Synthetic comment -- @@ -959,6 +959,21 @@
}
}

    public void getCallBarringOption(String facility, String password, int serviceClass,
            Message onComplete) {
        mCM.queryFacilityLock(facility, password, serviceClass, onComplete);
    }

    public void setCallBarringOption(String facility, boolean lockState, String password,
            int serviceClass, Message onComplete) {
        mCM.setFacilityLock(facility, lockState, password, serviceClass, onComplete);
    }

    public void changeCallBarringPassword(String facility, String oldPwd, String newPwd,
            Message onComplete) {
        mCM.changeBarringPassword(facility, oldPwd, newPwd, onComplete);
    }

public void getOutgoingCallerIdDisplay(Message onComplete) {
mCM.getCLIR(onComplete);
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/sip/SipPhoneBase.java b/src/java/com/android/internal/telephony/sip/SipPhoneBase.java
//Synthetic comment -- index 041ac79..e1c2675 100755

//Synthetic comment -- @@ -325,6 +325,21 @@
int timerSeconds, Message onComplete) {
}

    public void getCallBarringOption(String facility, String password, int serviceClass,
            Message onComplete) {
        Rlog.e(LOG_TAG, "Error! getCallBarringOption is not implemented for SIP.");
    }

    public void setCallBarringOption(String facility, boolean lockState, String password,
            int serviceClass, Message onComplete) {
        Rlog.e(LOG_TAG, "Error! setCallBarringOption is not implemented for SIP.");
    }

    public void changeCallBarringPassword(String facility, String oldPwd, String newPwd,
            Message onComplete) {
        Rlog.e(LOG_TAG, "Error! changeCallBarringPassword is not implemented for SIP.");
    }

public void getOutgoingCallerIdDisplay(Message onComplete) {
// FIXME: what to reply?
AsyncResult.forMessage(onComplete, null, null);







