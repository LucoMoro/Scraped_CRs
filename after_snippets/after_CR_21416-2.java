
//<Beginning of snippet n. 0>



viewGroup.addView(editText);
editText.requestFocus();

        new DelayedCheck(TIMEOUT_DELTA) {
            @Override
            protected boolean check() {
                return editText.isFocused();
            }
        }.run();

imm.showSoftInput(editText, 0);
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());

//<End of snippet n. 0>








