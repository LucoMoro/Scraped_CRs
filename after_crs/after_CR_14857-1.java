/*Replaced deprecated String Constructor

Change-Id:I452400c9cb00b60316b0270b2f4a68c15d80b698*/




//Synthetic comment -- diff --git a/src/com/android/settings/RunningServices.java b/src/com/android/settings/RunningServices.java
//Synthetic comment -- index 6c11ea0..dc370a3 100644

//Synthetic comment -- @@ -776,7 +776,7 @@
&& buffer[index] <= '9') {
index++;
}
                String str = new String(buffer, start, index-start);
return ((long)Integer.parseInt(str)) * 1024;
}
index++;







