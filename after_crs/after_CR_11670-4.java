/*Fix an NPE in InputMethodService when mExtractAction is null.

This happens with certain third-party IMEs.

Fixes:http://code.google.com/p/cyanogenmod/issues/detail?id=231Change-Id:Idf4349ee9c7c8f73c255745bc65b49ba9b593874*/




//Synthetic comment -- diff --git a/core/java/android/inputmethodservice/InputMethodService.java b/core/java/android/inputmethodservice/InputMethodService.java
//Synthetic comment -- index 5499bba..f4fbaad 100644

//Synthetic comment -- @@ -1981,15 +1981,19 @@
ei.inputType != InputType.TYPE_NULL);
if (hasAction) {
mExtractAccessories.setVisibility(View.VISIBLE);
            if (mExtractAction != null) {
                if (ei.actionLabel != null) {
                    mExtractAction.setText(ei.actionLabel);
                } else {
                    mExtractAction.setText(getTextForImeAction(ei.imeOptions));
                }
                mExtractAction.setOnClickListener(mActionClickListener);
}
} else {
mExtractAccessories.setVisibility(View.GONE);
            if (mExtractAction != null) {
                mExtractAction.setOnClickListener(null);
            }
}
}








