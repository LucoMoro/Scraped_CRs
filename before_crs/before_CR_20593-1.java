/*apps/Phone: Notify ril about the phone power-off

The change is to separately notify ril about the low power mode
and the actual phone power-off.

Change-Id:I0d75ecdd29b493c440f219442761886c2ff7a94c*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneInterfaceManager.java b/src/com/android/phone/PhoneInterfaceManager.java
//Synthetic comment -- index f3ee97c..b1f7695 100644

//Synthetic comment -- @@ -490,6 +490,12 @@
return true;
}

public boolean enableDataConnectivity() {
enforceModifyPermission();
return mPhone.enableDataConnectivity();







