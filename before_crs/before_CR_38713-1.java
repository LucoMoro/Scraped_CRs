/*Clarify the documentation of the Pattern character classes.

Android (via icu4c) supports the full Unicode TR-18 definitions
rather than the traditional ASCII-only ones. Document this fact.

Bug:http://code.google.com/p/android/issues/detail?id=21176Change-Id:If9b94d4e7769ec1ef7c443a7db47bc7586f49b44*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Pattern.java b/luni/src/main/java/java/util/regex/Pattern.java
//Synthetic comment -- index 46984b9..cbd5965 100644

//Synthetic comment -- @@ -82,16 +82,23 @@
* </table>
* <p>Most of the time, the built-in character classes are more useful:
* <table>
 * <tr> <td> \d </td> <td>Any digit character.</td> </tr>
 * <tr> <td> \D </td> <td>Any non-digit character.</td> </tr>
 * <tr> <td> \s </td> <td>Any whitespace character.</td> </tr>
 * <tr> <td> \S </td> <td>Any non-whitespace character.</td> </tr>
 * <tr> <td> \w </td> <td>Any word character.</td> </tr>
 * <tr> <td> \W </td> <td>Any non-word character.</td> </tr>
* <tr> <td> \p{<i>NAME</i>} </td> <td> Any character in the class with the given <i>NAME</i>. </td> </tr>
* <tr> <td> \P{<i>NAME</i>} </td> <td> Any character <i>not</i> in the named class. </td> </tr>
* </table>
 * <p>There are a variety of named classes:
* <ul>
* <li><a href="../../lang/Character.html#unicode_categories">Unicode category names</a>,
* prefixed by {@code Is}. For example {@code \p{IsLu}} for all uppercase letters.








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java b/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java
//Synthetic comment -- index 450b8d9..54f976d 100644

//Synthetic comment -- @@ -579,4 +579,14 @@
assertTrue(pattern.matcher("14pt").matches());
}

}







