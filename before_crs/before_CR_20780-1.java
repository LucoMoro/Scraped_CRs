/*Adding isIccInvalidCard to the PhoneInterfaceManager

Updating the PhoneIntefaceManager so it is in synch
with frameworks/base.

Change-Id:Iefaa748d41351ef03ac3a2d21d7eec9f96d624ee*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..d1ead45 100644

//Synthetic comment -- @@ -730,4 +730,11 @@
public boolean hasIccCard() {
return mPhone.getIccCard().hasIccCard();
}
}







