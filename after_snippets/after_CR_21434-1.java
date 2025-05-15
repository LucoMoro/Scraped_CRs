
//<Beginning of snippet n. 0>


assertNull(filter.getResult());
// 12-key support
if (mNumeric) {
            // "numeric" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("6688633777444222");
            Thread.sleep(100);
            assertEquals("numeric", filter.getResult());
} else {
mInstrumentation.sendStringSync(STRING_TEST);
            // give some time for UI to settle
            Thread.sleep(100);
            assertEquals(STRING_TEST, filter.getResult());
}
}

@TestTargets({

//<End of snippet n. 0>








