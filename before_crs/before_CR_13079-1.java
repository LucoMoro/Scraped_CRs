/*Fix digits direction in RTL text*/
//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f0a5ffd..8dc2943 100644

//Synthetic comment -- @@ -375,7 +375,7 @@
cur = d;

if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER)
                        chdirs[j] = cur;
}

// dump(chdirs, n, "W7");
//Synthetic comment -- @@ -390,7 +390,7 @@
cur = d;
} else if (d == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
d == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                        cur = Character.DIRECTIONALITY_RIGHT_TO_LEFT;
} else {
byte dd = SOR;
int k;
//Synthetic comment -- @@ -404,7 +404,7 @@
}
if (dd == Character.DIRECTIONALITY_EUROPEAN_NUMBER ||
dd == Character.DIRECTIONALITY_ARABIC_NUMBER) {
                                dd = Character.DIRECTIONALITY_RIGHT_TO_LEFT;
break;
}
}







