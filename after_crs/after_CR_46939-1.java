/*Add a test for a Matcher.hitEnd bug fixed by icu4c 49.

Bug:http://code.google.com/p/android/issues/detail?id=19308Change-Id:I4a88cc554b7d5fe2d8706287f8dd4390217bc7ab*/




//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/regex/tests/java/util/regex/PatternTest.java b/luni/src/test/java/org/apache/harmony/regex/tests/java/util/regex/PatternTest.java
//Synthetic comment -- index ffbdf9e..a81d294 100644

//Synthetic comment -- @@ -1774,4 +1774,11 @@
}
}

    // http://code.google.com/p/android/issues/detail?id=19308
    public void test_hitEnd() {
        Pattern p = Pattern.compile("^2(2[4-9]|3\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
        Matcher m = p.matcher("224..");
        boolean isPartialMatch = !m.matches() && m.hitEnd();
        assertFalse(isPartialMatch);
    }
}







