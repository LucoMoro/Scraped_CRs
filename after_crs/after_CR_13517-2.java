/*Text disappears when selecting text in text editor.

The problem appears when the user selects, copies and paste text
and then selects text again. The second time text is selected,
the copied text becomes visible in the text field.

Change-Id:Iae19d1659f58ad2e1c6f1a98bdef8bbedabd447f*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index 6418dad..ed2fc6b 100644

//Synthetic comment -- @@ -4549,6 +4549,10 @@
outText.text = TextUtils.substring(content, partialStartOffset,
partialEndOffset);
}
            } else {
                outText.partialStartOffset = 0;
                outText.partialEndOffset = 0;
                outText.text = "";
}
outText.flags = 0;
if (MetaKeyKeyListener.getMetaState(mText, MetaKeyKeyListener.META_SELECTING) != 0) {







