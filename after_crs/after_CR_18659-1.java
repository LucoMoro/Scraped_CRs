/*SMS character counter miscalculated at full segment

When a part of a concatenated SMS is full, for example when a message
contains an exact multiple of 153 characters in case of 7 bit
encoding, the message was considered to need one extra segment, which
would just be empty.*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java b/telephony/java/com/android/internal/telephony/gsm/SmsMessage.java
//Synthetic comment -- index 4fd62fb..955d7e0 100644

//Synthetic comment -- @@ -800,9 +800,10 @@
int septets = GsmAlphabet.countGsmSeptets(msgBody, !use7bitOnly);
ted.codeUnitCount = septets;
if (septets > MAX_USER_DATA_SEPTETS) {
                ted.msgCount = (septets + MAX_USER_DATA_SEPTETS_WITH_HEADER - 1)
                               / MAX_USER_DATA_SEPTETS_WITH_HEADER;
                ted.codeUnitsRemaining = (ted.msgCount * MAX_USER_DATA_SEPTETS_WITH_HEADER)
                                         - septets;
} else {
ted.msgCount = 1;
ted.codeUnitsRemaining = MAX_USER_DATA_SEPTETS - septets;
//Synthetic comment -- @@ -812,9 +813,10 @@
int octets = msgBody.length() * 2;
ted.codeUnitCount = msgBody.length();
if (octets > MAX_USER_DATA_BYTES) {
                ted.msgCount = (octets + MAX_USER_DATA_BYTES_WITH_HEADER - 1)
                               / MAX_USER_DATA_BYTES_WITH_HEADER;
                ted.codeUnitsRemaining = ((ted.msgCount * MAX_USER_DATA_BYTES_WITH_HEADER)
                                          - octets) / 2;
} else {
ted.msgCount = 1;
ted.codeUnitsRemaining = (MAX_USER_DATA_BYTES - octets)/2;







