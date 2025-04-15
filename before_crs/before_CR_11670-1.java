/*Fix an NPE in InputMethodService when mExtractAction is null.  This
happens with certain third-party IMEs.

Fixes:http://code.google.com/p/cyanogenmod/issues/detail?id=231*/
//Synthetic comment -- diff --git a/core/java/android/inputmethodservice/InputMethodService.java b/core/java/android/inputmethodservice/InputMethodService.java
//Synthetic comment -- index 6ee92ce..2d2b7d2 100644

//Synthetic comment -- @@ -1958,15 +1958,19 @@
ei.inputType != InputType.TYPE_NULL);
if (hasAction) {
mExtractAccessories.setVisibility(View.VISIBLE);
            if (ei.actionLabel != null) {
                mExtractAction.setText(ei.actionLabel);
            } else {
                mExtractAction.setText(getTextForImeAction(ei.imeOptions));
}
            mExtractAction.setOnClickListener(mActionClickListener);
} else {
mExtractAccessories.setVisibility(View.GONE);
            mExtractAction.setOnClickListener(null);
}
}








