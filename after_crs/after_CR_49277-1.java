/*Fix a Dead Loop issue in RecipientEditTextView

The old code logic will cause dead loop when pasting phone number
to recipient.

Change-Id:I01183680289919105dadc28ffd40c8e60dfd6b7eAuthor: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Jianping Li <jianpingx.li@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 58793*/




//Synthetic comment -- diff --git a/chips/src/com/android/ex/chips/RecipientEditTextView.java b/chips/src/com/android/ex/chips/RecipientEditTextView.java
//Synthetic comment -- index 011b1e9..989365d 100644

//Synthetic comment -- @@ -2264,12 +2264,12 @@
int originalTokenStart = mTokenizer.findTokenStart(text, getSelectionEnd());
String lastAddress = text.substring(originalTokenStart);
int tokenStart = originalTokenStart;
        int prevTokenStart = 0;
RecipientChip findChip = null;
ArrayList<RecipientChip> created = new ArrayList<RecipientChip>();
if (tokenStart != 0) {
// There are things before this!
            while (tokenStart != 0 && findChip == null && tokenStart != prevTokenStart) {
prevTokenStart = tokenStart;
tokenStart = mTokenizer.findTokenStart(text, tokenStart);
findChip = findChip(tokenStart);







