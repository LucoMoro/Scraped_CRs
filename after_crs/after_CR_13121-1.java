/*This patch fixed the order of digits in RTL text,
Originally digits that were in RTL text were displayed in RTL order. (you can see that digits direction was set to the direction of the text before it)
It is now always set to LRT, which is the way it should be.*/




//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f0a5ffd..c93795c 100644

//Synthetic comment -- @@ -375,7 +375,7 @@
cur = d;

if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                        chdirs[j] = DIRECTIONALITY_LEFT_TO_RIGHT;
}

// dump(chdirs, n, "W7");
//Synthetic comment -- @@ -390,7 +390,7 @@
cur = d;
} else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
d == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                        cur = Character.DIRECTIONALITY_LEFT_TO_RIGHT;
} else {
byte dd = SOR;
int k;
//Synthetic comment -- @@ -404,7 +404,7 @@
}
if (dd == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
dd == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                                dd = Character.DIRECTIONALITY_LEFT_TO_RIGHT;
break;
}
}







