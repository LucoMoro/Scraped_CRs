/*Keypress wakes up modem if in power save mode

Added function to read mmodem power save status.

Dependencies: frameworks/base, frameworks/policies/base
Change-Id:I162159c57044e471aadf7409babea0afb2212d01*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index b684df5..bc91725 100644

//Synthetic comment -- @@ -476,6 +476,10 @@
return mPhone.getServiceState().getState() != ServiceState.STATE_POWER_OFF;
}

public void toggleRadioOnOff() {
enforceModifyPermission();
mPhone.setRadioPower(!isRadioOn());







