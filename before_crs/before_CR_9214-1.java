/*Merge commit 'remotes/korg/cupcake' into merge*/
//Synthetic comment -- diff --git a/src/com/google/common/io/protocol/ProtoBuf.java b/src/com/google/common/io/protocol/ProtoBuf.java
//Synthetic comment -- index 679a4da..ff7a324 100644

//Synthetic comment -- @@ -32,8 +32,8 @@
* but only the simple methods take default values into account. The reason for
* this behavior is that default values cannot be removed -- they would reappear
* after a serialization cycle. If a tag has repeated values, setXXX(tag, value)
 * will overwrite all of them and getXXX(tag) will throw an exception. 
 * 
*/

public class ProtoBuf {
//Synthetic comment -- @@ -46,12 +46,12 @@
private static final String MSG_UNSUPPORTED = "Unsupp.Type";

// names copied from //net/proto2/internal/wire_format.cc
  private static final int WIRETYPE_END_GROUP = 4;
  private static final int WIRETYPE_FIXED32 = 5;
  private static final int WIRETYPE_FIXED64 = 1;
  private static final int WIRETYPE_LENGTH_DELIMITED = 2;
  private static final int WIRETYPE_START_GROUP = 3;
  private static final int WIRETYPE_VARINT = 0;

/** Maximum number of bytes for VARINT wire format (64 bit, 7 bit/byte) */
private static final int VARINT_MAX_BYTES = 10;
//Synthetic comment -- @@ -62,7 +62,7 @@
new Long(10), new Long(11), new Long(12), new Long(13), new Long(14),
new Long(15)};

  private final ProtoBufType msgType;
private final Vector values = new Vector();

/** 
//Synthetic comment -- @@ -124,6 +124,20 @@
}

/**
* Appends the given (repeated) tag with the given group or message value.
*/
public void addProtoBuf(int tag, ProtoBuf value){
//Synthetic comment -- @@ -131,6 +145,28 @@
}

/**
* Appends the given (repeated) tag with the given String value.
*/
public void addString(int tag, String value){
//Synthetic comment -- @@ -167,7 +203,7 @@
return (byte[]) getObject(tag, index, ProtoBufType.TYPE_DATA);
}

  /** 
* Returns the integer value for the given tag. 
*/
public int getInt(int tag) {
//Synthetic comment -- @@ -182,7 +218,7 @@
ProtoBufType.TYPE_INT32)).longValue();
}

  /** 
* Returns the long value for the given tag. 
*/
public long getLong(int tag) {
//Synthetic comment -- @@ -196,7 +232,35 @@
return ((Long) getObject(tag, index, ProtoBufType.TYPE_INT64)).longValue();
}

  /** 
* Returns the group or nested message for the given tag.
*/
public ProtoBuf getProtoBuf(int tag) {
//Synthetic comment -- @@ -227,7 +291,7 @@
return (String) getObject(tag, index, ProtoBufType.TYPE_TEXT);
}

  /** 
* Returns the type definition of this protocol buffer or group -- if set. 
*/
public ProtoBufType getType() {
//Synthetic comment -- @@ -235,6 +299,20 @@
}

/**
* Convenience method for determining whether a tag has a value. Note: in 
* contrast to getCount(tag) &gt; 0, this method takes the default value
* into account.
//Synthetic comment -- @@ -652,6 +730,20 @@
? SMALL_NUMBERS[(int) value] : new Long(value));
}

/** 
* Sets the given tag to the given Group or nested Message. 
*/
//Synthetic comment -- @@ -659,6 +751,18 @@
setObject(tag, pb);
}

/** 
* Sets the given tag to the given String value. 
*/
//Synthetic comment -- @@ -695,6 +799,20 @@
? SMALL_NUMBERS[(int) value] : new Long(value));
}

/** 
* Inserts the given group or message for the given tag at the given index. 
*/
//Synthetic comment -- @@ -739,6 +857,8 @@
case ProtoBufType.TYPE_UINT64:
case ProtoBufType.TYPE_SINT32:
case ProtoBufType.TYPE_SINT64:
return;
}
} else if (object instanceof byte[]){
//Synthetic comment -- @@ -1078,21 +1198,21 @@
/**
* Encodes the given string to UTF-8 in the given buffer or calculates
* the space needed if the buffer is null.
   * 
* @param s the string to be UTF-8 encoded
* @param buf byte array to write to
   * @return new buffer position after writing (which equals the required size 
*    if pos is 0)
*/
static int encodeUtf8(String s, byte[] buf, int pos){
int len = s.length();
for (int i = 0; i < len; i++){
int code = s.charAt(i);
      
// surrogate 0xd800 .. 0xdfff?
if (code >= 0x0d800 && code <= 0x0dfff && i + 1 < len){
int codeLo = s.charAt(i + 1);
        
// 0xfc00 is the surrogate id mask (first six bit of 16 set)
// 0x03ff is the surrogate data mask (remaining 10 bit)
// check if actually a surrogate pair (d800 ^ dc00 == 0400)
//Synthetic comment -- @@ -1137,35 +1257,35 @@
buf[pos + 2] = (byte) ((0x80 | ((code >> 6) & 0x3F)));
buf[pos + 3] = (byte) ((0x80 | (code & 0x3F)));
}
        pos += 4;        
}
}
    
return pos;
}

/**
   * Decodes an array of UTF-8 bytes to a Java string (UTF-16). The tolerant 
   * flag determines what to do in case of illegal or unsupported sequences. 
   * 
   * @param data input byte array containing UTF-8 data 
* @param start decoding start position in byte array
* @param end decoding end position in byte array
   * @param tolerant if true, an IllegalArgumentException is thrown for illegal 
*    UTF-8 codes
* @return the string containing the UTF-8 decoding result
*/
  static String decodeUtf8(byte[] data, int start, int end, 
boolean tolerant){
    
StringBuffer sb = new StringBuffer(end - start);
int pos = start;
    
while (pos < end){
int b = data[pos++] & 0x0ff;
if (b <= 0x7f){
sb.append((char) b);
      } else if (b >= 0xf5){ // byte sequence too long 
if (!tolerant){
throw new IllegalArgumentException("Invalid UTF8");
}
//Synthetic comment -- @@ -1173,16 +1293,16 @@
} else {
int border = 0xe0;
int count = 1;
        int minCode = 128; 
int mask = 0x01f;
while (b >= border){
border = (border >> 1) | 0x80;
          minCode = minCode << (count == 1 ? 4 : 5); 
count++;
mask = mask >> 1;
}
int code = b & mask;
        
for (int i = 0; i < count; i++){
code = code << 6;
if (pos >= end){
//Synthetic comment -- @@ -1197,7 +1317,7 @@
code |= (data[pos++] & 0x3f); // six bit
}
}
        
// illegal code or surrogate code
if (!tolerant && code < minCode || (code >= 0xd800 && code <= 0xdfff)){
throw new IllegalArgumentException("Invalid UTF8");








//Synthetic comment -- diff --git a/src/com/google/common/io/protocol/ProtoBufType.java b/src/com/google/common/io/protocol/ProtoBufType.java
//Synthetic comment -- index 1aec8f9..4b6408e 100644

//Synthetic comment -- @@ -1,4 +1,4 @@
// Copyright 2007 The Android Open Source Project
// All Rights Reserved.

package com.google.common.io.protocol;
//Synthetic comment -- @@ -9,7 +9,6 @@
* This class can be used to create a memory model of a .proto file. Currently, 
* it is assumed that tags ids are not large. This could be improved by storing 
* a start offset, relaxing the assumption to a dense number space.
 * 
*/
public class ProtoBufType {
// Note: Values 0..15 are reserved for wire types!
//Synthetic comment -- @@ -121,4 +120,51 @@
public String toString() {
return typeName;
}
}








//Synthetic comment -- diff --git a/src/com/google/common/io/protocol/ProtoBufUtil.java b/src/com/google/common/io/protocol/ProtoBufUtil.java
//Synthetic comment -- index 077bd3d..72e1bca 100644

//Synthetic comment -- @@ -1,7 +1,9 @@
// Copyright 2008 The Android Open Source Project

package com.google.common.io.protocol;

/**
* Utility functions for dealing with ProtoBuf objects consolidated from
* previous spot implementations across the codebase.
//Synthetic comment -- @@ -24,14 +26,39 @@
public static String getSubProtoValueOrEmpty(
ProtoBuf proto, int sub, int tag) {
try {
      ProtoBuf subProto =
          (proto != null && proto.has(sub)) ? proto.getProtoBuf(sub) : null;
      return getProtoValueOrEmpty(subProto, tag);
} catch (ClassCastException e) {
return "";
}
}

/**
* Get an Int with "tag" from the proto buffer.
* If the given field can't be retrieved, return 0.
//Synthetic comment -- @@ -42,12 +69,25 @@
* @return The result which should be an integer.
*/
public static int getProtoValueOrZero(ProtoBuf proto, int tag) {
try {
      return (proto != null && proto.has(tag)) ? proto.getInt(tag) : 0;
} catch (IllegalArgumentException e) {
      return 0;
} catch (ClassCastException e) {
      return 0;
}
}

//Synthetic comment -- @@ -71,6 +111,39 @@
}

/**
* A wrapper for <code> getProtoValueOrNegativeOne </code> that drills into
* a sub message returning the long value if it exists, returning -1 if it
* does not.
//Synthetic comment -- @@ -85,13 +158,80 @@
public static long getSubProtoValueOrNegativeOne(
ProtoBuf proto, int sub, int tag) {
try {
      ProtoBuf subProto =
          (proto != null && proto.has(sub)) ? proto.getProtoBuf(sub) : null;
      return getProtoValueOrNegativeOne(subProto, tag);
} catch (IllegalArgumentException e) {
return -1;
} catch (ClassCastException e) {
return -1; 
}
}
}







