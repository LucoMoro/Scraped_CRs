/*Support MMS with empty subject

Phones fail to receive MMS messages that have empty subject.
There are operators who report this as an issue.

Change-Id:I553e4d6524638d12290d2c2f0c4640b261eeddf5*/
//Synthetic comment -- diff --git a/src/java/com/google/android/mms/pdu/PduParser.java b/src/java/com/google/android/mms/pdu/PduParser.java
//Synthetic comment -- index 015d864..8a1901a 100755

//Synthetic comment -- @@ -935,7 +935,7 @@
assert(-1 != temp);
int first = temp & 0xFF;
if (first == 0) {
            return null;    //  Blank subject, bail.
}

pduDataStream.reset();







