//<Beginning of snippet n. 0>
viewGroup.addView(editText);
editText.requestFocus();
if (DelayedCheck(editText)) {
    new Handler().post(() -> imm.showSoftInput(editText, 0));
}
assertTrue(editText.hasCalledOnCreateInputConnection());
assertTrue(editText.hasCalledOnCheckIsTextEditor());

private boolean DelayedCheck(EditText editText) {
    long startTime = System.currentTimeMillis();
    long timeout = 2000; // 2 seconds
    while (!editText.hasFocus()) {
        if (System.currentTimeMillis() - startTime > timeout) {
            throw new RuntimeException("EditText did not gain focus within " + timeout + " milliseconds. Current focus state: " + editText.hasFocus());
        }
        try {
            Thread.sleep(50); // Check every 50 ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while waiting for EditText to gain focus.");
        }
    }
    return true;
}
//<End of snippet n. 0>