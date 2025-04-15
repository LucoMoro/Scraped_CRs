/*Telephony: Fix unit test Wap230WspContentTypeTest

Wap230WspContentTypeTest:  WSP_DEFINED_LONG_MIME_TYPE_COUNT is fixed to 40
to match actual size of HashMap size of WELL_KNOWN_LONG_MIME_TYPES

Change-Id:I80fa6585f49a8cce914e1b88d717fd492686eac5*/
//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java
//Synthetic comment -- index d31b294..3a7454e 100644

//Synthetic comment -- @@ -188,7 +188,7 @@
}

final int WSP_DEFINED_SHORT_MIME_TYPE_COUNT = 85;
    final int WSP_DEFINED_LONG_MIME_TYPE_COUNT = 85;

private static final byte WSP_STRING_TERMINATOR = 0x00;
private static final byte WSP_SHORT_INTEGER_MASK = (byte) 0x80;







