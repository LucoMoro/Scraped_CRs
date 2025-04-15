/*fix wrong substring

The tokenOffsets is the offset from trimed contentLine.
But it is used in substring as the offset from un-trimed contentLine.

How to reproduce bug (ICS and before):
1. Add a contact record with name and a phone number (ex. 090-1111-2222)
2. Search by "0" in Contacts app
3. Contacts shows a wrong number something like "0900111102222"

Change-Id:I3b3195a430059cefdd2436fe1fdd5b70c44356c8*/




//Synthetic comment -- diff --git a/core/java/android/provider/ContactsContract.java b/core/java/android/provider/ContactsContract.java
//Synthetic comment -- index 8e123ac..dbbdc3b 100644

//Synthetic comment -- @@ -8406,7 +8406,7 @@
sb.append(markedToken);
if (i < lastToken - 1) {
// Add the characters that appeared between this token and the next.
                            sb.append(contentLine.trim().substring(
tokenOffsets.get(i) + originalToken.length(),
tokenOffsets.get(i + 1)));
}







