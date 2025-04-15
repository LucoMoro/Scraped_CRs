/*Adding isIccCardValid to the PhoneInterfaceManager

Updating the PhoneIntefaceManager so it is in synch
with frameworks/base.

Change-Id:Iefaa748d41351ef03ac3a2d21d7eec9f96d624ee*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index ab6011c..c8a4de6 100644

//Synthetic comment -- @@ -794,4 +794,13 @@
public int getLteOnCdmaMode() {
return mPhone.getLteOnCdmaMode();
}

    /**
     * Return true if an ICC card is valid
     *
     * @hide
     */
    public boolean isIccCardValid() {
        return mPhone.getIccCard().isIccCardValid();
    }
}







