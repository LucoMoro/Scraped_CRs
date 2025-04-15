/*ADDS ISO_PCD_A_B card emulation

This patch adds the API to specify an intent for ISO_PCD_A or
ISO_PCD_B tag in an app.  It uses previously patches to the
NFC Service

Change-Id:Ifd46db6d1d60c0e92cb6f04e26949114319a5486*/
//Synthetic comment -- diff --git a/core/java/android/nfc/Tag.java b/core/java/android/nfc/Tag.java
old mode 100644
new mode 100755
//Synthetic comment -- index f2cd232..d73951d

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Parcel;
//Synthetic comment -- @@ -188,6 +190,12 @@
case TagTechnology.NFC_BARCODE:
strings[i] = NfcBarcode.class.getName();
break;
default:
throw new IllegalArgumentException("Unknown tech type " + techList[i]);
}








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/IsoPcdA.java b/core/java/android/nfc/tech/IsoPcdA.java
new file mode 100755
//Synthetic comment -- index 0000000..5d266de

//Synthetic comment -- @@ -0,0 +1,100 @@








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/IsoPcdB.java b/core/java/android/nfc/tech/IsoPcdB.java
new file mode 100755
//Synthetic comment -- index 0000000..f8658a9

//Synthetic comment -- @@ -0,0 +1,100 @@








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/TagTechnology.java b/core/java/android/nfc/tech/TagTechnology.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3493ea71..07b81c8

//Synthetic comment -- @@ -157,6 +157,24 @@
public static final int NFC_BARCODE = 10;

/**
* Get the {@link Tag} object backing this {@link TagTechnology} object.
* @return the {@link Tag} backing this {@link TagTechnology} object.
*/







