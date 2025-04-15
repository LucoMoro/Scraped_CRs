/*Adding isIccInvalidCard to the PhoneInterfaceManager

Updating the PhoneIntefaceManager so it is in synch
with frameworks/base.

Change-Id:Iefaa748d41351ef03ac3a2d21d7eec9f96d624ee*/




//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index ab6011c..94ae92c 100644

//Synthetic comment -- @@ -794,4 +794,13 @@
public int getLteOnCdmaMode() {
return mPhone.getLteOnCdmaMode();
}

    /**
     * return true if an ICC card is invalid
     *
     * @hide
     */
    public boolean isIccInvalidCard() {
        return mPhone.getIccCard().isIccInvalidCard();
    }
}







