/*Document that SimpleDateFormat L and c weren't available until Gingerbread.

Bug:http://code.google.com/p/android/issues/detail?id=40268Change-Id:I07f8f4cbd97dfd5b56ed4a1e1338027eb472fa95*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/text/SimpleDateFormat.java b/luni/src/main/java/java/text/SimpleDateFormat.java
//Synthetic comment -- index 7c3187c..d845d45 100644

//Synthetic comment -- @@ -108,7 +108,8 @@
* </ul>
*
* <p>The two pattern characters {@code L} and {@code c} are ICU-compatible extensions, not
 * available in the RI or in Android before Android 2.3 "Gingerbread" (API level 9). These
 * extensions are necessary for correct localization in languages such as Russian
* that distinguish between, say, "June" and "June 2010".
*
* <p>When numeric fields are adjacent directly, with no intervening delimiter







