/*Handle the case when TP-PI reserved bits are set

Discard settings according to 3GPP TS 23.040,
9.2.3.27 TP-Parameter-Indicator (TP-PI).

Usually operators will set this byte to 0 to indicate
there is no more data, but a few operators sets a
reserved bit to 1 to indicate there is no more data instead,
so we need to handle this case also.

Change-Id:I58abc2473c79b65795be186182927c603b35305e*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SmsMessage.java b/src/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 76a4b7f..9421dd7 100644

//Synthetic comment -- @@ -966,18 +966,22 @@
// additional TP-PI octets.
moreExtraParams = p.getByte();
}
            // As per 3GPP 23.040 section 9.2.3.27 TP-Parameter-Indicator,
            // only process the byte if the reserved bits (bits3 to 6) are zero.
            if ((extraParams & 0x78) == 0) {
                // TP-Protocol-Identifier
                if ((extraParams & 0x01) != 0) {
                    protocolIdentifier = p.getByte();
                }
                // TP-Data-Coding-Scheme
                if ((extraParams & 0x02) != 0) {
                    dataCodingScheme = p.getByte();
                }
                // TP-User-Data-Length (implies existence of TP-User-Data)
                if ((extraParams & 0x04) != 0) {
                    boolean hasUserDataHeader = (firstByte & 0x40) == 0x40;
                    parseUserData(p, hasUserDataHeader);
                }
}
}
}







