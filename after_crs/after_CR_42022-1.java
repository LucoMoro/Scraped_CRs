/*fix wrong substring

The tokenOffsets is the offset from trimed contentLine.
But it is used in substring as the offset from un-trimed contentLine.

How to reproduce bug (ICS and before):
1. Add a contact record with name and a phone number (ex. 090-1111-2222)
2. Search by "0" in Contacts app
3. Contacts shows a wrong number something like "0900111102222"

Change-Id:I1e1cb2f3bd135d98e1573a50aa6ddc021af35b9f*/




//Synthetic comment -- diff --git a/core/java/android/provider/ContactsContract.java b/core/java/android/provider/ContactsContract.java
old mode 100644
new mode 100755
//Synthetic comment -- index 8e123ac..e7b0579

//Synthetic comment -- @@ -8362,7 +8362,7 @@
// Line contains the query string - now search for it at the start of tokens.
List<String> lineTokens = new ArrayList<String>();
List<Integer> tokenOffsets = new ArrayList<Integer>();
                split(contentLine, lineTokens, tokenOffsets);

// As we find matches against the query, we'll populate this list with the marked
// (or unchanged) tokens.







