/*Telephony: Initialize GsmCellLocation class members properly

Default values for class members mLac, mCid, mPsc would be 0.
Initialize these variables with -1 as 0 is considered as valid value.

Change-Id:Idb3d1737c7101b97a90eab3dc7436ee1806d0bc4CRs-Fixed: 406479*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/gsm/GsmCellLocation.java b/telephony/java/android/telephony/gsm/GsmCellLocation.java
//Synthetic comment -- index 313bc82..a37223f 100644

//Synthetic comment -- @@ -23,9 +23,9 @@
* Represents the cell location on a GSM phone.
*/
public class GsmCellLocation extends CellLocation {
    private int mLac;
    private int mCid;
    private int mPsc;

/**
* Empty constructor.  Initializes the LAC and CID to -1.







