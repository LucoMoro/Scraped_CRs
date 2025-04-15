/*Fix for CharSequenceCreator test case

The test case testCharSequenceCreator() in TextUtilsTest fails
because the data position is not reset before calling
createFromParcel().

Change-Id:I6d5cc093ff65019ab0d2191bb0bd9c10ae49612b*/




//Synthetic comment -- diff --git a/core/tests/coretests/src/android/text/TextUtilsTest.java b/core/tests/coretests/src/android/text/TextUtilsTest.java
//Synthetic comment -- index 63dd0cc..a77aa64 100644

//Synthetic comment -- @@ -361,6 +361,7 @@
assertNull("null CharSequence should generate null from parcel", text);
p = Parcel.obtain();
TextUtils.writeToParcel("test", p, 0);
        p.setDataPosition(0);
text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(p);
assertEquals("conversion to/from parcel failed", "test", text);
}







