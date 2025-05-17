//<Beginning of snippet n. 0>

assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
    String numericString = "8888866600022330008337777833";
    assertFalse(numericString.contains(" "));
    mInstrumentation.sendStringSync(numericString);
    mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
    mInstrumentation.sendStringSync("3");
} else {
    String initializedStringTest = STRING_TEST.trim();
    mInstrumentation.sendStringSync(initializedStringTest);
}
// Use of a more effective synchronization method instead of Thread.sleep()
UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).waitForIdle();
assertEquals(initializedStringTest, filter.getResult());
}

//@TestTargets({

 //<End of snippet n. 0>