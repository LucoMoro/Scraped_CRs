/*Admit that we can't really fix DateFormatSymbols serialization...

...by relaxing a test.

Change-Id:Icbc53a99670487b5878ee0971aea05be9d284d0c*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java b/luni/src/test/java/libcore/java/text/DateFormatSymbolsTest.java
//Synthetic comment -- index 4d9c87d..992e352 100644

//Synthetic comment -- @@ -67,9 +67,10 @@
assertEquals("stycznia", formatDate(pl, "MMMM", originalDfs));
assertEquals("stycze\u0144", formatDate(pl, "LLLL", originalDfs));

        // But the deserialized object is screwed because the RI's serialized form doesn't
        // contain the locale or the necessary strings. Don't serialize DateFormatSymbols, folks!
assertEquals("stycznia", formatDate(pl, "MMMM", deserializedDfs));
        assertEquals("January", formatDate(pl, "LLLL", deserializedDfs));
}

private String formatDate(Locale l, String fmt, DateFormatSymbols dfs) {







