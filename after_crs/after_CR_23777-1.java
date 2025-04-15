/*Merge df07f53c from master to r12. do not merge.

Fix parsing of the styleable enum/flag attribute values.

Better handling for hexa value > 0x7FFFFFFF

Change-Id:I1cbbed8f9d3ce9f1b7396e2d34c7bd33dc18e70f*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index e52c026..6bd085e 100644

//Synthetic comment -- @@ -150,8 +150,10 @@
String value = attributes.getValue(ATTR_VALUE);

try {
                    // Integer.decode/parseInt can't deal with hex value > 0x7FFFFFFF so we
                    // use Long.decode instead.
mCurrentDeclareStyleable.addValue(mCurrentAttribute,
                            name, (int)(long)Long.decode(value));
} catch (NumberFormatException e) {
// pass, we'll just ignore this value
}







