/*Telephony: Add support to read 3GPP2 sms from CSIM/RUIM

Add parser to parse 3GPP2 format sms stored in CSIM/RUIM card

Change-Id:I4720ba7602fcc1b078de0d5fea34247541242c1c*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java b/telephony/java/com/android/internal/telephony/cdma/SmsMessage.java
//Synthetic comment -- index f869dbd..30c8d95 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.HexDump;

import java.io.BufferedInputStream;
//Synthetic comment -- @@ -72,6 +73,16 @@
static final String LOG_TAG = "CDMA";
static private final String LOGGABLE_TAG = "CDMA:SMS";

    private final static byte TELESERVICE_IDENTIFIER                    = 0x00;
    private final static byte SERVICE_CATEGORY                          = 0x01;
    private final static byte ORIGINATING_ADDRESS                       = 0x02;
    private final static byte ORIGINATING_SUB_ADDRESS                   = 0x03;
    private final static byte DESTINATION_ADDRESS                       = 0x04;
    private final static byte DESTINATION_SUB_ADDRESS                   = 0x05;
    private final static byte BEARER_REPLY_OPTION                       = 0x06;
    private final static byte CAUSE_CODES                               = 0x07;
    private final static byte BEARER_DATA                               = 0x08;

/**
*  Status of a previously submitted SMS.
*  This field applies to SMS Delivery Acknowledge messages. 0 indicates success;
//Synthetic comment -- @@ -267,6 +278,7 @@
System.arraycopy(data, 2, pdu, 0, size);
// the message has to be parsed before it can be displayed
// see gsm.SmsMessage
            msg.parsePduFromEfRecord(pdu);
return msg;
} catch (RuntimeException ex) {
Log.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
//Synthetic comment -- @@ -539,6 +551,143 @@
}

/**
     * Decodes 3GPP2 sms stored in CSIM/RUIM cards As per 3GPP2 C.S0015-0
     */
    private void parsePduFromEfRecord(byte[] pdu) {
        ByteArrayInputStream bais = new ByteArrayInputStream(pdu);
        DataInputStream dis = new DataInputStream(bais);
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subAddr = new CdmaSmsSubaddress();

        try {
            env.messageType = dis.readByte();

            while (dis.available() > 0) {
                int parameterId = dis.readByte();
                int parameterLen = dis.readByte();
                byte[] parameterData = new byte[parameterLen];

                switch (parameterId) {
                    case TELESERVICE_IDENTIFIER:
                        /*
                         * 16 bit parameter that identifies which upper layer
                         * service access point is sending or should receive
                         * this message
                         */
                        env.teleService = dis.readUnsignedShort();
                        Log.i(LOG_TAG, "teleservice = " + env.teleService);
                        break;
                    case SERVICE_CATEGORY:
                        /*
                         * 16 bit parameter that identifies type of service as
                         * in 3GPP2 C.S0015-0 Table 3.4.3.2-1
                         */
                        env.serviceCategory = dis.readUnsignedShort();
                        break;
                    case ORIGINATING_ADDRESS:
                    case DESTINATION_ADDRESS:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream addrBis = new BitwiseInputStream(parameterData);
                        addr.digitMode = addrBis.read(1);
                        addr.numberMode = addrBis.read(1);
                        int numberType = 0;
                        if (addr.digitMode == CdmaSmsAddress.DIGIT_MODE_8BIT_CHAR) {
                            numberType = addrBis.read(3);
                            addr.ton = numberType;

                            if (addr.numberMode == CdmaSmsAddress.NUMBER_MODE_NOT_DATA_NETWORK)
                                addr.numberPlan = addrBis.read(4);
                        }

                        addr.numberOfDigits = addrBis.read(8);

                        byte[] data = new byte[addr.numberOfDigits];
                        byte b = 0x00;

                        if (addr.digitMode == CdmaSmsAddress.DIGIT_MODE_4BIT_DTMF) {
                            /* As per 3GPP2 C.S0005-0 Table 2.7.1.3.2.4-4 */
                            for (int index = 0; index < addr.numberOfDigits; index++) {
                                b = (byte) (0xF & addrBis.read(4));
                                // convert the value if it is 4-bit DTMF to 8
                                // bit
                                data[index] = convertDtmfToAscii(b);
                            }
                        } else if (addr.digitMode == CdmaSmsAddress.DIGIT_MODE_8BIT_CHAR) {
                            if (addr.numberMode == CdmaSmsAddress.NUMBER_MODE_NOT_DATA_NETWORK) {
                                for (int index = 0; index < addr.numberOfDigits; index++) {
                                    b = (byte) (0xFF & addrBis.read(8));
                                    data[index] = b;
                                }

                            } else if (addr.numberMode == CdmaSmsAddress.NUMBER_MODE_DATA_NETWORK) {
                                if (numberType == 2)
                                    Log.e(LOG_TAG, "TODO: Originating Addr is email id");
                                else
                                    Log.e(LOG_TAG,
                                          "TODO: Originating Addr is data network address");
                            } else {
                                Log.e(LOG_TAG, "Originating Addr is of incorrect type");
                            }
                        } else {
                            Log.e(LOG_TAG, "Incorrect Digit mode");
                        }
                        addr.origBytes = data;
                        Log.i(LOG_TAG, "Originating Addr=" + addr.toString());
                        break;
                    case ORIGINATING_SUB_ADDRESS:
                    case DESTINATION_SUB_ADDRESS:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream subAddrBis = new BitwiseInputStream(parameterData);
                        subAddr.type = subAddrBis.read(3);
                        subAddr.odd = subAddrBis.readByteArray(1)[0];
                        int subAddrLen = subAddrBis.read(8);
                        byte[] subdata = new byte[subAddrLen];
                        for (int index = 0; index < subAddrLen; index++) {
                            b = (byte) (0xFF & subAddrBis.read(4));
                            // convert the value if it is 4-bit DTMF to 8 bit
                            subdata[index] = convertDtmfToAscii(b);
                        }
                        subAddr.origBytes = subdata;
                        break;
                    case BEARER_REPLY_OPTION:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream replyOptBis = new BitwiseInputStream(parameterData);
                        env.bearerReply = replyOptBis.read(6);
                        break;
                    case CAUSE_CODES:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream ccBis = new BitwiseInputStream(parameterData);
                        env.replySeqNo = ccBis.readByteArray(6)[0];
                        env.errorClass = ccBis.readByteArray(2)[0];
                        if (env.errorClass != 0x00)
                            env.causeCode = ccBis.readByteArray(8)[0];
                        break;
                    case BEARER_DATA:
                        dis.read(parameterData, 0, parameterLen);
                        env.bearerData = parameterData;
                        break;
                    default:
                        throw new Exception("unsupported parameterId (" + parameterId + ")");
                }
            }
            bais.close();
            dis.close();
        } catch (Exception ex) {
            Log.e(LOG_TAG, "parsePduFromEfRecord: conversion from pdu to SmsMessage failed" + ex);
        }

        // link the filled objects to this SMS
        originatingAddress = addr;
        env.origAddress = addr;
        env.origSubaddress = subAddr;
        mEnvelope = env;
        mPdu = pdu;

        parseSms();
    }

    /**
* Parses a SMS message from its BearerData stream. (mobile-terminated only)
*/
protected void parseSms() {







