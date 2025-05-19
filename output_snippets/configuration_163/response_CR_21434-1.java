//<Beginning of snippet n. 0>

assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
    String numericString = "8888866600022330008337777833";
    mInstrumentation.sendStringSync(numericString);
    mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
    mInstrumentation.sendStringSync("3");
} else {
    String sanitizedStringTest = STRING_TEST.replaceAll("\\s+", "");
    mInstrumentation.sendStringSync(sanitizedStringTest);
    assertEquals(sanitizedStringTest, filter.getResult());
}
// Use a more reliable synchronization method
UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).waitForIdle();

//<End of snippet n. 0>