/*In CW Interrogation service class is not sent.

As per 3GPP TS 24.083, section 1.6 UE doesn't need to send service class
parameter in call waiting interrogation  to network. Some networks return error
if service class is sent. So setting service class to SERVICE_CLASS_NONE.

Change-Id:I396963d568573b88795ad2cf1844eb2e047b459e*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java b/telephony/java/com/android/internal/telephony/gsm/GSMPhone.java
//Synthetic comment -- index c7b1e5c..2786209 100644

//Synthetic comment -- @@ -972,7 +972,9 @@
}

public void getCallWaiting(Message onComplete) {
        //As per 3GPP TS 24.083, section 1.6 UE doesn't need to send service
        //class parameter in call waiting interrogation  to network
        mCM.queryCallWaiting(CommandsInterface.SERVICE_CLASS_NONE, onComplete);
}

public void setCallWaiting(boolean enable, Message onComplete) {







