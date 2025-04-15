/*Backport bughttp://b/6299423fix from JB to ICS

commit b8ae50696316457d160fb925e06a85c67e873fa9
Fix expected output of testIndexOf with start == Integer.MAX_VALUE.
This fixes bughttp://b/6299423Change-Id:Ic81e7b8d17be760e50641e686c19fe2c0a07d54c*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/cts/TextUtilsTest.java b/tests/tests/text/src/android/text/cts/TextUtilsTest.java
//Synthetic comment -- index e85424f..3ba2350 100755

//Synthetic comment -- @@ -1255,7 +1255,7 @@
assertEquals(INDEX_OF_SECOND_R, TextUtils.indexOf(searchString, 'r', INDEX_OF_FIRST_R + 1));
assertEquals(-1, TextUtils.indexOf(searchString, 'r', searchString.length()));
assertEquals(INDEX_OF_FIRST_R, TextUtils.indexOf(searchString, 'r', Integer.MIN_VALUE));
        assertEquals(-1, TextUtils.indexOf(searchString, 'r', Integer.MAX_VALUE));

StringBuffer stringBuffer = new StringBuffer(searchString);
assertEquals(INDEX_OF_SECOND_R, TextUtils.indexOf(stringBuffer, 'r', INDEX_OF_FIRST_R + 1));
//Synthetic comment -- @@ -1377,7 +1377,7 @@
assertEquals(-1, TextUtils.indexOf(searchString, "string", INDEX_OF_SECOND_STRING + 1));
assertEquals(INDEX_OF_FIRST_STRING, TextUtils.indexOf(searchString, "string",
Integer.MIN_VALUE));
        assertEquals(-1, TextUtils.indexOf(searchString, "string", Integer.MAX_VALUE));

assertEquals(1, TextUtils.indexOf(searchString, "", 1));
assertEquals(Integer.MAX_VALUE, TextUtils.indexOf(searchString, "", Integer.MAX_VALUE));
//Synthetic comment -- @@ -1429,7 +1429,7 @@
INDEX_OF_SECOND_STRING - 1));
assertEquals(INDEX_OF_FIRST_STRING, TextUtils.indexOf(searchString, "string",
Integer.MIN_VALUE, INDEX_OF_SECOND_STRING - 1));
        assertEquals(-1, TextUtils.indexOf(searchString, "string", Integer.MAX_VALUE,
INDEX_OF_SECOND_STRING - 1));

assertEquals(INDEX_OF_SECOND_STRING, TextUtils.indexOf(searchString, "string",







