/*Default decoding scheme set GMS 7-bits packed

In function retrieveTextString handle all unknown coding schemes
as GMS 7-bits packed text strings.

Change-Id:I67c232725580d3e2c5369c4a81fb5144b96c1d5d*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/ValueParser.java b/telephony/java/com/android/internal/telephony/cat/ValueParser.java
//Synthetic comment -- index 584d96c..cfca46e 100644

//Synthetic comment -- @@ -327,7 +327,8 @@
} else if (codingScheme == 0x08) { // UCS2
text = new String(rawValue, valueIndex + 1, textLen, "UTF-16");
} else {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
}

return text;







