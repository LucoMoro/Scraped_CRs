//<Beginning of snippet n. 0>

viewGroup.addView(editText);
editText.requestFocus();
DelayedCheck(() -> {
    if (editText.hasWindowFocus()) {
        imm.showSoftInput(editText, 0);
        assertTrue(editText.hasCalledOnCreateInputConnection());
        assertTrue(editText.hasCalledOnCheckIsTextEditor());
    }
}, 2000); // 2 seconds timeout

//<End of snippet n. 0>