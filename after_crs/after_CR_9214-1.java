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
  static final int WIRETYPE_END_GROUP = 4;
  static final int WIRETYPE_FIXED32 = 5;
  static final int WIRETYPE_FIXED64 = 1;
  static final int WIRETYPE_LENGTH_DELIMITED = 2;
  static final int WIRETYPE_START_GROUP = 3;
  static final int WIRETYPE_VARINT = 0;

/** Maximum number of bytes for VARINT wire format (64 bit, 7 bit/byte) */
private static final int VARINT_MAX_BYTES = 10;
//Synthetic comment -- @@ -62,7 +62,7 @@
new Long(10), new Long(11), new Long(12), new Long(13), new Long(14),
new Long(15)};

  private ProtoBufType msgType;
private final Vector values = new Vector();

/** 
//Synthetic comment -- @@ -124,6 +124,20 @@
}

/**
   * Appends the given (repeated) tag with the given float value.
   */
  public void addFloat(int tag, float value) {
    insertFloat(tag, getCount(tag), value);
  }

  /**
   * Appends the given (repeated) tag with the given double value.
   */
  public void addDouble(int tag, double value) {
    insertDouble(tag, getCount(tag), value);
  }

  /**
* Appends the given (repeated) tag with the given group or message value.
*/
public void addProtoBuf(int tag, ProtoBuf value){
//Synthetic comment -- @@ -131,6 +145,28 @@
}

/**
   * Adds a new protobuf for the specified tag, setting the child protobuf's
   * type correctly for the tag.
   * @param tag the tag for which to create a new protobuf
   * @return the newly created protobuf
   */
  public ProtoBuf addNewProtoBuf(int tag) {
    ProtoBuf child = newProtoBufForTag(tag);
    addProtoBuf(tag, child);
    return child;
  }

  /**
   * Creates and returns a new protobuf for the specified tag, setting the new
   * protobuf's type correctly for the tag.
   * @param tag the tag for which to create a new protobuf
   * @return the newly created protobuf
   */
  public ProtoBuf newProtoBufForTag(int tag) {
      return new ProtoBuf((ProtoBufType) msgType.getData(tag));
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
   * Returns the float value for the given tag.
   */
  public float getFloat(int tag) {
    return Float.intBitsToFloat(getInt(tag));
  }

  /**
   * Returns the float value for the given repeated tag at the given index. 
   */
  public float getFloat(int tag, int index) {
    return Float.intBitsToFloat(getInt(tag, index));
  }

  /**
   * Returns the double value for the given tag.
   */
  public double getDouble(int tag) {
    return Double.longBitsToDouble(getLong(tag));
  }

  /**
   * Returns the double value for the given repeated tag at the given index. 
   */
  public double getDouble(int tag, int index) {
    return Double.longBitsToDouble(getLong(tag, index));
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
   * Sets the type definition of this protocol buffer. Used internally in
   * ProtoBufUtil for incremental reading.
   * 
   * @param type the new type
   */
  void setType(ProtoBufType type) {
    if (values.size() != 0 || 
        (msgType != null && type != null && type != msgType)) {
      throw new IllegalArgumentException();
    }
    this.msgType = type;
  }
  
  /**
* Convenience method for determining whether a tag has a value. Note: in 
* contrast to getCount(tag) &gt; 0, this method takes the default value
* into account.
//Synthetic comment -- @@ -652,6 +730,20 @@
? SMALL_NUMBERS[(int) value] : new Long(value));
}

  /**
   * Sets the given tag to the given double value.
   */
  public void setDouble(int tag, double value) {
    setLong(tag, Double.doubleToLongBits(value));
  }

  /**
   * Sets the given tag to the given float value.
   */
  public void setFloat(int tag, float value) {
    setInt(tag, Float.floatToIntBits(value));
  }

/** 
* Sets the given tag to the given Group or nested Message. 
*/
//Synthetic comment -- @@ -659,6 +751,18 @@
setObject(tag, pb);
}

  /**
   * Sets a new protobuf for the specified tag, setting the child protobuf's
   * type correctly for the tag.
   * @param tag the tag for which to create a new protobuf
   * @return the newly created protobuf
   */
  public ProtoBuf setNewProtoBuf(int tag) {
    ProtoBuf child = newProtoBufForTag(tag);
    setProtoBuf(tag, child);
    return child;
  }

/** 
* Sets the given tag to the given String value. 
*/
//Synthetic comment -- @@ -695,6 +799,20 @@
? SMALL_NUMBERS[(int) value] : new Long(value));
}

  /**
   * Inserts the given float value for the given tag at the given index.
   */
  public void insertFloat(int tag, int index, float value) {
    insertInt(tag, index, Float.floatToIntBits(value));
  }

  /**
   * Inserts the given double value for the given tag at the given index.
   */
  public void insertDouble(int tag, int index, double value) {
    insertLong(tag, index, Double.doubleToLongBits(value));
  }

/** 
* Inserts the given group or message for the given tag at the given index. 
*/
//Synthetic comment -- @@ -739,6 +857,8 @@
case ProtoBufType.TYPE_UINT64:
case ProtoBufType.TYPE_SINT32:
case ProtoBufType.TYPE_SINT64:
        case ProtoBufType.TYPE_FLOAT:
        case ProtoBufType.TYPE_DOUBLE:
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
// Copyright 2007 Google Inc.
// All Rights Reserved.

package com.google.common.io.protocol;
//Synthetic comment -- @@ -9,7 +9,6 @@
* This class can be used to create a memory model of a .proto file. Currently, 
* it is assumed that tags ids are not large. This could be improved by storing 
* a start offset, relaxing the assumption to a dense number space.
*/
public class ProtoBufType {
// Note: Values 0..15 are reserved for wire types!
//Synthetic comment -- @@ -121,4 +120,51 @@
public String toString() {
return typeName;
}
  
  /**
   * {@inheritDoc}
   * <p>Two ProtoBufTypes are equals if the fields types are the same.
   */
  public boolean equals(Object object) {
    if (null == object) {
      // trivial check
      return false;
    } else if (this == object) {
      // trivial check
      return true;
    } else if (this.getClass() != object.getClass()) {
      // different class
      return false;
    }
    ProtoBufType other = (ProtoBufType) object;

    return stringEquals(types, other.types);
  }
   
  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    if (types != null) {
      return types.hashCode();
    } else {
      return super.hashCode();
    }
  }

  public static boolean stringEquals(CharSequence a, CharSequence b) {
    if (a == b) return true;
    int length;
    if (a != null && b != null && (length = a.length()) == b.length()) {
      if (a instanceof String && b instanceof String) {
        return a.equals(b);
      } else {
        for (int i = 0; i < length; i++) {
          if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
      }
    }
    return false;
  }
}








//Synthetic comment -- diff --git a/src/com/google/common/io/protocol/ProtoBufUtil.java b/src/com/google/common/io/protocol/ProtoBufUtil.java
//Synthetic comment -- index 077bd3d..72e1bca 100644

//Synthetic comment -- @@ -1,7 +1,9 @@
// Copyright 2008 Google Inc. All Rights Reserved.

package com.google.common.io.protocol;

import java.io.*;

/**
* Utility functions for dealing with ProtoBuf objects consolidated from
* previous spot implementations across the codebase.
//Synthetic comment -- @@ -24,14 +26,39 @@
public static String getSubProtoValueOrEmpty(
ProtoBuf proto, int sub, int tag) {
try {
      return getProtoValueOrEmpty(getSubProtoOrNull(proto, sub), tag);
} catch (ClassCastException e) {
return "";
}
}

  /** Convenience method to get a subproto if the proto has it. */
  public static ProtoBuf getSubProtoOrNull(ProtoBuf proto, int sub) {
    return (proto != null && proto.has(sub)) ? proto.getProtoBuf(sub) : null;
  }

  /**
   * Get an int with "tag" from the proto buffer. If the given field can't be
   * retrieved, return the provided default value.
   * 
   * @param proto The proto buffer.
   * @param tag The tag value that identifies which protocol buffer field to
   *        retrieve.
   * @param defaultValue The value to return if the field can't be retrieved.
   * @return The result which should be an integer.
   */
  public static int getProtoValueOrDefault(ProtoBuf proto, int tag,
      int defaultValue) {
    try {
      return (proto != null && proto.has(tag))
          ? proto.getInt(tag) : defaultValue;
    } catch (IllegalArgumentException e) {
      return defaultValue;
    } catch (ClassCastException e) {
      return defaultValue;
    }
  }

/**
* Get an Int with "tag" from the proto buffer.
* If the given field can't be retrieved, return 0.
//Synthetic comment -- @@ -42,12 +69,25 @@
* @return The result which should be an integer.
*/
public static int getProtoValueOrZero(ProtoBuf proto, int tag) {
    return getProtoValueOrDefault(proto, tag, 0);
  }

  /**
   * Get an Long with "tag" from the proto buffer.
   * If the given field can't be retrieved, return 0.
   *
   * @param proto The proto buffer.
   * @param tag The tag value that identifies which protocol buffer field to
   * retrieve.
   * @return The result which should be an integer.
   */
  public static long getProtoLongValueOrZero(ProtoBuf proto, int tag) {
try {
      return (proto != null && proto.has(tag)) ? proto.getLong(tag) : 0L;
} catch (IllegalArgumentException e) {
      return 0L;
} catch (ClassCastException e) {
      return 0L;
}
}

//Synthetic comment -- @@ -71,6 +111,39 @@
}

/**
   * Reads a single protocol buffer from the given input stream. This method is
   * provided where the client needs incremental access to the contents of a 
   * protocol buffer which contains a sequence of protocol buffers.  
   * <p />
   * Please use {@link #getInputStreamForProtoBufResponse} to obtain an input 
   * stream suitable for this method.
   * 
   * @param umbrellaType the type of the "outer" protocol buffer containing
   *                    the message to read
   * @param is the stream to read the protocol buffer from
   * @param result the result protocol buffer (must be empty, will be filled
   *               with the data read and the type will be set)
   * @return the tag id of the message, -1 at the end of the stream
   */
  public static int readNextProtoBuf(ProtoBufType umbrellaType, 
      InputStream is, ProtoBuf result) throws IOException {
    long tagAndType = ProtoBuf.readVarInt(is, true /* permits EOF */);
    if (tagAndType == -1) {
      return -1;
    }
    
    if ((tagAndType & 7) != ProtoBuf.WIRETYPE_LENGTH_DELIMITED) {
      throw new IOException("Message expected");
    }
    int tag = (int) (tagAndType >>> 3);
    
    result.setType((ProtoBufType) umbrellaType.getData(tag));
    int length = (int) ProtoBuf.readVarInt(is, false);
    result.parse(is, length);
    return tag;
  }
  
  /**
* A wrapper for <code> getProtoValueOrNegativeOne </code> that drills into
* a sub message returning the long value if it exists, returning -1 if it
* does not.
//Synthetic comment -- @@ -85,13 +158,80 @@
public static long getSubProtoValueOrNegativeOne(
ProtoBuf proto, int sub, int tag) {
try {
      return getProtoValueOrNegativeOne(getSubProtoOrNull(proto, sub), tag);
} catch (IllegalArgumentException e) {
return -1;
} catch (ClassCastException e) {
return -1; 
}
}

  /**
   * A wrapper for {@link #getProtoValueOrDefault(ProtoBuf, int, int)} that
   * drills into a sub message returning the int value if it exists, returning
   * the given default if it does not.
   *
   * @param proto The proto buffer.
   * @param tag The tag value that identifies which protocol buffer field to
   * retrieve.
   * @param sub The sub tag value that identifies which protocol buffer
   * sub-field to retrieve.
   * @param defaultValue The value to return if the field is not present.
   * @return The result which should be a long.
   */
  public static int getSubProtoValueOrDefault(ProtoBuf proto, int sub, int tag,
      int defaultValue) {
    try {
      return getProtoValueOrDefault(getSubProtoOrNull(proto, sub), tag, 
          defaultValue);
    } catch (IllegalArgumentException e) {
      return defaultValue;
    } catch (ClassCastException e) {
      return defaultValue; 
    }
  }

  /**
   * Creates a sub ProtoBuf of the given Protobuf and sets it.
   *
   * @param proto The proto buffer.
   * @param tag The tag value that identifies which protocol buffer field to
   * create.
   * @return the sub ProtoBuf generated.
   */
  public static ProtoBuf createProtoBuf(ProtoBuf proto, int tag) {
    ProtoBuf child = proto.createGroup(tag);
    proto.setProtoBuf(tag, child);
    return child;
  }

  /**
   * Creates a sub ProtoBuf of the given Protobuf and adds it.
   *
   * @param proto The proto buffer.
   * @param tag The tag value that identifies which protocol buffer field to
   * add.
   * @return the sub ProtoBuf generated.
   */
  public static ProtoBuf addProtoBuf(ProtoBuf proto, int tag) {
    ProtoBuf child = proto.createGroup(tag);
    proto.addProtoBuf(tag, child);
    return child;
  }

  /**
   * Writes the ProtoBuf to the given DataOutput.  This is useful for unit
   * tests.
   *
   * @param output The data output to write to.
   * @param protoBuf The proto buffer.
   */
  public static void writeProtoBufToOutput(DataOutput output, ProtoBuf protoBuf)
      throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    protoBuf.outputTo(baos);
    byte[] bytes = baos.toByteArray();
    output.writeInt(bytes.length);
    output.write(bytes);
  }
}







