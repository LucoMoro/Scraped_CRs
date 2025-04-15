/*This patch fixed the order of digits in RTL text,
Originally digits that were in RTL text were displayed in RTL order. (you can see that digits direction was set to the direction of the text before it)
now it complies better withhttp://unicode.org/reports/tr9/#W7*/




//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f0a5ffd..fbec5bae 100644

//Synthetic comment -- @@ -374,7 +374,7 @@
d == Character.DIRECTIONALITY_RIGHT_TO_LEFT)
cur = d;

                    if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER  && cur == Character.DIRECTIONALITY_LEFT_TO_RIGHT)
chdirs[j] = cur;
}








