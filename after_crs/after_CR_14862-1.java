/*Replaced deprecated String Constructor

Change-Id:I91874093d69e230850c695b92da5f91391030d44*/




//Synthetic comment -- diff --git a/services/java/com/android/server/ProcessStats.java b/services/java/com/android/server/ProcessStats.java
//Synthetic comment -- index ac3b723..cbd3d8b8 100644

//Synthetic comment -- @@ -687,7 +687,7 @@
break;
}
}
                return new String(mBuffer, 0, i);
}
} catch (java.io.FileNotFoundException e) {
} catch (java.io.IOException e) {







