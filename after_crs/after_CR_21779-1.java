/*Adding DelayedCheck() in testInputConnection.

After executing requestFocus(), there are some cases where test case is failed since showSoftInput() is called before EditText gets focus.

To avoid this, I changed the code to add wait until EditText has focus.*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 5c730f8..3370e2b 100644

//Synthetic comment -- @@ -4442,6 +4442,14 @@

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







