/*WspTypeDecoder: fixed decoding of content parameters

The removed condition prevented any parameter to have
0 as integer value.

Change-Id:Iaa8e106e49a51322c07f647f2f66bb7bd6055c89*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/WspTypeDecoder.java b/src/java/com/android/internal/telephony/WspTypeDecoder.java
//Synthetic comment -- index 73260fb..a19546a 100755

//Synthetic comment -- @@ -506,11 +506,7 @@
} else if (decodeIntegerValue(startIndex + totalRead)) {
totalRead += dataLength;
int intValue = (int) unsigned32bit;
                if (intValue == 0) {
                    value = "";
                } else {
                    value = String.valueOf(intValue);
                }
} else {
decodeTokenText(startIndex + totalRead);
totalRead += dataLength;








//Synthetic comment -- diff --git a/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java b/tests/telephonytests/src/com/android/internal/telephony/Wap230WspContentTypeTest.java
//Synthetic comment -- index d31b294..9c2b7ef 100644

//Synthetic comment -- @@ -623,6 +623,26 @@

}

public void testTypedParamWellKnownShortIntegerMultipleParameters() throws Exception {

ByteArrayOutputStream out = new ByteArrayOutputStream();
//Synthetic comment -- @@ -850,4 +870,4 @@
WspTypeDecoder unit = new WspTypeDecoder(out.toByteArray());
assertFalse(unit.decodeContentType(0));
}
}
\ No newline at end of file







