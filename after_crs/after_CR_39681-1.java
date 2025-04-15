/*Fix flakey test_SSLSocket_setSoTimeout_basic by using a full second timeout

Change-Id:I5b7d57f012dcef0047cf292f311ee3fa49700414*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java b/luni/src/test/java/libcore/javax/net/ssl/SSLSocketTest.java
//Synthetic comment -- index 90cdeb9..8b9cb11 100644

//Synthetic comment -- @@ -1054,9 +1054,10 @@
assertEquals(0, wrapping.getSoTimeout());

// setting wrapper sets underlying and ...
        int expectedTimeoutMillis = 1000;  // Using a small value such as 10 was affected by rounding
        wrapping.setSoTimeout(expectedTimeoutMillis);
        assertEquals(expectedTimeoutMillis, wrapping.getSoTimeout());
        assertEquals(expectedTimeoutMillis, underlying.getSoTimeout());

// ... getting wrapper inspects underlying
underlying.setSoTimeout(0);







