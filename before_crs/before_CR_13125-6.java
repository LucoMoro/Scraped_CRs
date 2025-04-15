/*Fix the order of digits in RTL text.

Originally digits that were in RTL text were displayed in
RTL order. (you can see that digits direction was set to
the direction of the text before it)
now it complies better withhttp://unicode.org/reports/tr9/#W7Change-Id:I325e280f6156b976570171ea56596c869c43c5e1*/
//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f0a5ffd..b4ebb6a 100644

//Synthetic comment -- @@ -374,7 +374,8 @@
d == Character.DIRECTIONALITY_RIGHT_TO_LEFT)
cur = d;

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
chdirs[j] = cur;
}








