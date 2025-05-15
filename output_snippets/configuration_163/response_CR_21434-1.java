//<Beginning of snippet n. 0>

assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
            // Sending the valid numeric string without spaces
            mInstrumentation.sendStringSync("8888866600022330008337777833");
            mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
            // Sending "3" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("3");
} else {
mInstrumentation.sendStringSync("STRING_TEST");
}
// give some time for UI to settle
Thread.sleep(100);
assertEquals("STRING_TEST", filter.getResult());
}

//@TestTargets({

//<End of snippet n. 0>