/*Telephony: Implement parsing of CDMA Extended Display Records

-Read the byte buffer as a byte array instead of a String to prevent interpretation as UTF-8
-Modify CdmaDisplayInfoRec to handle Extended Display Info Records
-Create ExtendedDisplayItemRec class to help in parsing the record's data
-Add function asIndex() to ExtendedDisplayTag
-Make the function readItems() private
-Modify CdmaDisplayInfoRec to use a Vector instead of a Map to hold the
-ExtendedDisplayItemRecs to preserve ordering of records.

Change-Id:I28f16438f0a40e03dfbe106651c5beabf468fc83*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/CdmaInformationRecords.java b/telephony/java/com/android/internal/telephony/cdma/CdmaInformationRecords.java
//Synthetic comment -- index ce6530a..73dc9ad 100644

//Synthetic comment -- @@ -1,5 +1,6 @@
/*
* Copyright (C) 2009 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,7 +17,18 @@

package com.android.internal.telephony.cdma;
import static com.android.internal.telephony.RILConstants.*;
import android.os.Parcel;

public final class CdmaInformationRecords {
public Object record;
//Synthetic comment -- @@ -39,8 +51,11 @@
public CdmaInformationRecords(Parcel p) {
int id = p.readInt();
switch (id) {
            case RIL_CDMA_DISPLAY_INFO_REC:
case RIL_CDMA_EXTENDED_DISPLAY_INFO_REC:
record  = new CdmaDisplayInfoRec(id, p.readString());
break;

//Synthetic comment -- @@ -128,21 +143,211 @@
}
}

public static class CdmaDisplayInfoRec {
public int id;
public String alpha;

public CdmaDisplayInfoRec(int id, String alpha) {
this.id = id;
this.alpha = alpha;
}

@Override
public String toString() {
            return "CdmaDisplayInfoRec: {" +
                    " id: " + CdmaInformationRecords.idToString(id) +
                    ", alpha: " + alpha +
                    " }";
}
}








