/*Fixing infinite loop for zero duration.

Change-Id:I837478c1598f1d2f99bf773597f0d66d0fc3cf1a*/
//Synthetic comment -- diff --git a/core/java/android/util/TimeUtils.java b/core/java/android/util/TimeUtils.java
//Synthetic comment -- index 60ca384..ceecbce 100644

//Synthetic comment -- @@ -191,7 +191,7 @@
int pos = 0;
fieldLen -= 1;
while (pos < fieldLen) {
                formatStr[pos] = ' ';
}
formatStr[pos] = '0';
return pos+1;







