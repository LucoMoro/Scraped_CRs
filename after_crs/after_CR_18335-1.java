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
 * Copyright (C) 2010 Code Aurora Forum. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
//Synthetic comment -- @@ -16,7 +17,18 @@

package com.android.internal.telephony.cdma;
import static com.android.internal.telephony.RILConstants.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.android.internal.util.HexDump;
import android.os.Parcel;
import android.util.Log;

public final class CdmaInformationRecords {
public Object record;
//Synthetic comment -- @@ -39,8 +51,11 @@
public CdmaInformationRecords(Parcel p) {
int id = p.readInt();
switch (id) {
case RIL_CDMA_EXTENDED_DISPLAY_INFO_REC:
                byte []data = p.createByteArray();
                record = new CdmaDisplayInfoRec(id, data);
                break;
            case RIL_CDMA_DISPLAY_INFO_REC:
record  = new CdmaDisplayInfoRec(id, p.readString());
break;

//Synthetic comment -- @@ -128,21 +143,211 @@
}
}

    public static class ExtendedDisplayItemRec {
        // According to ANSI TI.610-1998, the info in these records should be
        // encoded as US-ASCII
        private final String INFO_CHARSET = "US-ASCII";
        public ExtendedDisplayItemRec(ExtendedDisplayTag tag, byte len, byte[] data) {
            init(tag,len,data);
        }
        public ExtendedDisplayItemRec(ExtendedDisplayTag tag, byte len) {
            init(tag,len,null);
        }
        private void init(ExtendedDisplayTag tag, byte len, byte[] data) {
            this.displayTag = tag;
            this.mLen = len;
            this.mData = data;
        }
        public byte[] getData()  {
            return mData;
        }
        public String getDataAsString() {
            String ret = null;
            Log.d("CdmaDisplayInfoRec","getDataAsString()");
            if(mData == null) return "";
            Charset chs = Charset.forName(INFO_CHARSET);
            CharsetDecoder d = chs.newDecoder();
            ByteBuffer b = ByteBuffer.wrap(mData);
            try {
                ret = d.decode(b).toString();
            } catch(CharacterCodingException e) {
                Log.e("CdmaDisplayInfoRec", "Error decoding",e);
            }
            return ret;
        }
        @Override
        public String toString() {
            String ret = "";
            ret += displayTag.toString() +
                "(" + mLen + "): " +
                getDataAsString();
            return ret;
        }
        public ExtendedDisplayTag displayTag;
        private byte mLen;
        private byte[] mData;
    }

    public enum ExtendedDisplayTag {
        X_DISPLAY_TAG_BLANK                   ((byte)0x80),
        X_DISPLAY_TAG_SKIP                    ((byte)0x81),
        X_DISPLAY_TAG_CONTINUATION            ((byte)0x82),
        X_DISPLAY_TAG_CALLED_ADDRESS          ((byte)0x83),
        X_DISPLAY_TAG_CAUSE                   ((byte)0x84),
        X_DISPLAY_TAG_PROGRESS_INDICATOR      ((byte)0x85),
        X_DISPLAY_TAG_NOTIFICATION_INDICATOR  ((byte)0x86),
        X_DISPLAY_TAG_PROMPT                  ((byte)0x87),
        X_DISPLAY_TAG_ACCUMULATED_DIGITS      ((byte)0x88),
        X_DISPLAY_TAG_STATUS                  ((byte)0x89),
        X_DISPLAY_TAG_INBAND                  ((byte)0x8a),
        X_DISPLAY_TAG_CALLING_ADDRESS         ((byte)0x8b),
        X_DISPLAY_TAG_REASON                  ((byte)0x8c),
        X_DISPLAY_TAG_CALLING_PARTY_NAME      ((byte)0x8d),
        X_DISPLAY_TAG_CALLED_PARTY_NAME       ((byte)0x8e),
        X_DISPLAY_TAG_ORIGINAL_CALLED_NAME    ((byte)0x8f),
        X_DISPLAY_TAG_REDIRECTING_NAME        ((byte)0x90),
        X_DISPLAY_TAG_CONNECTED_NAME          ((byte)0x91),
        X_DISPLAY_TAG_ORIGINATING_RESTRICTIONS((byte)0x92),
        X_DISPLAY_TAG_DATETIME                ((byte)0x93),
        X_DISPLAY_TAG_CALL_APPEARANCE_ID      ((byte)0x94),
        X_DISPLAY_TAG_FEATURE_ADDRESS         ((byte)0x95),
        X_DISPLAY_TAG_REDIRECTION_NAME        ((byte)0x96),
        X_DISPLAY_TAG_REDIRECTION_NUMBER      ((byte)0x97),
        X_DISPLAY_TAG_REDIRECTING_NUMBER      ((byte)0x98),
        X_DISPLAY_TAG_ORIGINAL_CALLED_NUMBER  ((byte)0x99),
        X_DISPLAY_TAG_CONNECTED_NUMBER        ((byte)0x9a),
        X_DISPLAY_TAG_TEXT                    ((byte)0x9e),
        ;

        private final byte mValue;
        ExtendedDisplayTag(byte value) {
            this.mValue = value;
        }
        public byte value() { return mValue; }

        /**
          * Return a 0-based ordinal number corresponding to the tag
          * (e.g. 0 for X_DISPLAY_TAG_BLANK, 1 for X_DISPLAY_TAG_SKIP, etc)
          */
        public int asIndex() {
            int ret = (mValue & 0xff) & ~0x80;
            Log.d("DisplayTag", toString() + " as index: " + ret);
            return ret;
        }

        /**
          * Create a new ExtendedDisplayTag from the wire byte it represents
          * (e.g. 0x80 for X_DISPLAY_TAG_BLANK, 0x81 for X_DISPLAY_TAG_SKIP, etc)
          */
        public static ExtendedDisplayTag fromByte(byte value) {
            Log.d("DisplayTag", "DisplayTag.fromByte(" + value + ")");
            ExtendedDisplayTag ret = null;
            for (ExtendedDisplayTag tag : ExtendedDisplayTag.values()) {
                if(tag.mValue == value)
                    ret = tag;
            }
            Log.d("DisplayTag","Tag for byte " + value + ": " + ret);
            return ret;
        }
    }

public static class CdmaDisplayInfoRec {
public int id;
public String alpha;

        public Vector<ExtendedDisplayItemRec> itemrecs;

public CdmaDisplayInfoRec(int id, String alpha) {
this.id = id;
this.alpha = alpha;
}
        public CdmaDisplayInfoRec(int id, byte[] data) {
            Log.d("CdmaInformationRecords","CdmaDisplayInfoRec(" + id + ", data: " + data + ")");
            this.id = id;
            this.alpha = "";
            readItems(data);
        }

        private void readItems(byte []data) {
            Log.d("CdmaInformationRecords","CdmaDisplayInfoRec.readItems(len: " + data.length + ")");
            Log.d("CdmaInformationRecords",HexDump.dumpHexString(data));

            int read = 0;
            int linelen = 0;
            StringBuffer buffer = new StringBuffer();
            if(itemrecs == null)
                itemrecs = new Vector<ExtendedDisplayItemRec>();
            for(read = 0; read < data.length; ) {
                ExtendedDisplayTag itag = ExtendedDisplayTag.fromByte(data[read++]);
                if(itag == null) {
                    Log.e("CdmaInformationRecords","itag for [" + data[read-1] + "] is null!!!");
                }
                byte ilen = data[read++];
                byte []idata = null;
                switch(itag) {
                case X_DISPLAY_TAG_BLANK:
                    // According to the standard, this should display a number (in this case,
                    // ilen) of blank characters on the terminal's display
                    // (used to format and to clear)
                    Log.d("CdmaInformationRecords","readItems: got a DISPLAY_TAG_BLANK(ilen:" + ilen + ")");
                    for(int i = 0 ; i < ilen ; i++, linelen++) {
                        buffer.append(" ");
                        // Limit the line length to the maximum standard display size
                        // (ANSI TI.610-1998)
                        if(linelen >= 40) {
                            buffer.append("\r\n");
                            linelen = 0;
                        }
                    }
                    break;
                case X_DISPLAY_TAG_SKIP:
                    Log.d("CdmaInformationRecords", "readItems: got a DISPLAY_TAG_SKIP(ilen: " + ilen + ")");
                    // Do nothing
                    // TODO:Interpret the DISPLAY_TAG_SKIP
                    break;
                default:
                    idata = new byte[ilen];
                    for(int i = 0; i< ilen; i++)
                        idata[i] = data[read++];
                    break;
                }
                Log.d("CdmaInformationRecords", "readItems: Creating a new DisplayItemRec");
                ExtendedDisplayItemRec item = new ExtendedDisplayItemRec(itag, ilen, idata);
                String s = item.getDataAsString();
                if(s != null) {
                    buffer.append(s);
                    linelen += s.length();
                }
                itemrecs.add(item);
                Log.d("CdmaInformationRecords", "readItems: Added a new DisplayItemRec");
            }
            alpha = buffer.toString();
        }

        public boolean isExtended() {
          return id == RIL_CDMA_EXTENDED_DISPLAY_INFO_REC;
        }

@Override
public String toString() {
            StringBuffer buffer = new StringBuffer(200);
            if(!isExtended()) {
                buffer.append("CdmaDisplayInfoRec: { id: ");
                buffer.append(CdmaInformationRecords.idToString(id));
                buffer.append(", alpha: ");
                buffer.append(alpha);
                buffer.append(" }");
                return buffer.toString();
            }
            buffer.append("CdmaDisplayInfoRec(extended): { id: ");
            buffer.append(CdmaInformationRecords.idToString(id));
            for (ExtendedDisplayItemRec rec : itemrecs) {
                buffer.append(" [");
                buffer.append(rec.toString());
                buffer.append("]");
            }
            buffer.append(" }");
            return buffer.toString();
}
}








