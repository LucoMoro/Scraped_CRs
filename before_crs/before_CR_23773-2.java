/*Fix parsing of the styleable enum/flag attribute values.

Better handling for hexa value > 0x7FFFFFFF

Change-Id:If0c2ed23847a720431bceee95f8b264ceb967e3e*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index e52c026..6bd085e 100644

//Synthetic comment -- @@ -150,8 +150,10 @@
String value = attributes.getValue(ATTR_VALUE);

try {
mCurrentDeclareStyleable.addValue(mCurrentAttribute,
                            name, Integer.decode(value));
} catch (NumberFormatException e) {
// pass, we'll just ignore this value
}







