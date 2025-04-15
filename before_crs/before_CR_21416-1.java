/*Adding DelayedCheck() in testInputConnection.

After executing requestFocus(), there are some cases where test case is failed since showSoftInput() is called before EditText gets focus.

To avoid this, I changed the code to add wait until EditText has focus.*/
//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/ViewTest.java b/tests/tests/view/src/android/view/cts/ViewTest.java
//Synthetic comment -- index 77624e8..791e4f17 100644

//Synthetic comment -- @@ -4438,6 +4438,14 @@

viewGroup.addView(editText);
editText.requestFocus();
imm.showSoftInput(editText, 0);
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());







