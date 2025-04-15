/*WspTypeDecoder: fixed decoding of content parameters

The removed condition prevented any parameter to have
0 as integer value.

Change-Id:Idde6fa45df3594c0542ce4b8e30debdacd31b4e6*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/WspTypeDecoder.java b/telephony/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 73260fb..a19546a 100755

//Synthetic comment -- @@ -506,11 +506,7 @@
} else if (decodeIntegerValue(startIndex + totalRead)) {
totalRead += dataLength;
int intValue = (int) unsigned32bit;
                value = String.valueOf(intValue);
} else {
decodeTokenText(startIndex + totalRead);
totalRead += dataLength;








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java b/telephony/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java
//Synthetic comment -- index d31b294..38f47cb 100644

//Synthetic comment -- @@ -623,6 +623,28 @@

}

    public void testTypedParamWellKnownShortIntegerCompactIntegerValue_0()  {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(0x3);
        out.write(SHORT_MIME_TYPE_ROLLOVER_CERTIFICATE | WSP_SHORT_INTEGER_MASK);
        out.write(TYPED_PARAM_SEC | WSP_SHORT_INTEGER_MASK);
        out.write(0x00 | WSP_SHORT_INTEGER_MASK);

        WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
        assertTrue(unit.decodeContentType(0));

        String mimeType = unit.getValueString();

        assertEquals(STRING_MIME_TYPE_ROLLOVER_CERTIFICATE, mimeType);
        assertEquals(0x3F, unit.getValue32());
        assertEquals(4, unit.getDecodedDataLength());

        Map<String, String> params = unit.getContentParameters();
        assertEquals("0", params.get("SEC"));

    }

public void testTypedParamWellKnownShortIntegerMultipleParameters() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
//Synthetic comment -- @@ -850,4 +872,4 @@
WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertFalse(unit.decodeContentType(0));
}
\ No newline at end of file
}







