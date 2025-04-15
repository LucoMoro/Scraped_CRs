/*Add support for TTY in GSM mode

Change-Id:I66dd2f152c49fea7cb4e1d7bbdf66cb8af8eb568*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/PhoneBase.java b/telephony/java/com/android/internal/telephony/PhoneBase.java
//Synthetic comment -- index c3c8f5e..d2a95ef 100644

//Synthetic comment -- @@ -692,14 +692,18 @@
mCM.setSmscAddress(address, result);
}

    /**
     * Set the TTY mode
     */
public void setTTYMode(int ttyMode, Message onComplete) {
        mCM.setTTYMode(ttyMode, onComplete);
}

    /**
     * Queries the TTY mode
     */
public void queryTTYMode(Message onComplete) {
        mCM.queryTTYMode(onComplete);
}

public void enableEnhancedVoicePrivacy(boolean enable, Message onComplete) {








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java b/telephony/java/com/android/internal/telephony/cdma/CDMAPhone.java
//Synthetic comment -- index 1c81a07..28320ca 100755

//Synthetic comment -- @@ -1145,20 +1145,6 @@
}

/**
* Activate or deactivate cell broadcast SMS.
*
* @param activate 0 = activate, 1 = deactivate







