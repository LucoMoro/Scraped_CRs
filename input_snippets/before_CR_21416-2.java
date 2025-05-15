
//<Beginning of snippet n. 0>



viewGroup.addView(editText);
editText.requestFocus();
imm.showSoftInput(editText, 0);
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());

//<End of snippet n. 0>








