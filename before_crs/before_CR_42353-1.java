/*Add a test for ICU.getScript and ICU.addLikelySubtags.

Bug: 6156912
Change-Id:I44ec29812bbddd8034e0d568425eb0a63bb9d4f2*/
//Synthetic comment -- diff --git a/luni/src/test/java/libcore/icu/ICUTest.java b/luni/src/test/java/libcore/icu/ICUTest.java
//Synthetic comment -- index 9282167..a7e732c 100644

//Synthetic comment -- @@ -53,4 +53,12 @@
assertEquals(new Locale("", "", "POSIX"), ICU.localeFromString("__POSIX"));
assertEquals(new Locale("aa", "BB", "CC"), ICU.localeFromString("aa_BB_CC"));
}
}







