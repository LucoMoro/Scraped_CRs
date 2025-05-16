//<Beginning of snippet n. 0>


assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
            // Numeric string for 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("8888866600022330008337777833");
            mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
            // "3" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("3");
} else {
mInstrumentation.sendStringSync("8888866600022330008337777833");
}
// give some time for UI to settle
Thread.sleep(100);
assertEquals("8888866600022330008337777833", filter.getResult());
}

@TestTargets({

//<End of snippet n. 0>