/*Tests to demonstrate that KxmlSerializer and DOM writing both work.

A user reported that he expected &quot; instead of " in text nodes
of the emitted document. The user is wrong, but we don't have any
tests that demonstrate either case. I wrote tests for both DOM
writing and KxmlSerializer since there's no "RI" for the KxmlSerializer
and I wanted evidence that " is commonplace. (&quot; is permitted
according to the spec.)

(cherry-pick of 75595b3b8136ac524570125d74f748f0b0424428.)

Bug:http://code.google.com/p/android/issues/detail?id=21250Change-Id:I1b219a5c8d06551c4e7ac72a6df262df6b64209f*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/xml/DomSerializationTest.java b/luni/src/test/java/libcore/xml/DomSerializationTest.java
new file mode 100644
//Synthetic comment -- index 0000000..9015f97

//Synthetic comment -- @@ -0,0 +1,74 @@








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/xml/KxmlSerializerTest.java b/luni/src/test/java/libcore/xml/KxmlSerializerTest.java
new file mode 100644
//Synthetic comment -- index 0000000..94d4c3b

//Synthetic comment -- @@ -0,0 +1,58 @@







