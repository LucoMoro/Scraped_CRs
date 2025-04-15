/*apps/Phone: Add a RIL interface to change the transmit power

Change-Id:Ic450bc4359b8d78c610f0828c44f3af6d3f289b9*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..08cda34 100644

//Synthetic comment -- @@ -730,4 +730,17 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}

    /**
     * Sets the transmit power
     *
     * @param power - Specifies the transmit power that is allowed. One of
     *            TRANSMIT_POWER_DEFAULT      - restore default transmit power
     *            TRANSMIT_POWER_WIFI_HOTSPOT - reduce transmit power as per FCC
     *                               regulations (CFR47 2.1093) for WiFi hotspot
     */
    public void setTransmitPower(int powerLevel) {
        enforceModifyPermission();
        mPhone.setTransmitPower(powerLevel);
    }
}







