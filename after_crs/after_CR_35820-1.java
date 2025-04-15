/*FileInputStream is not closed in "UserManager.java : readUserList()"

Change-Id:I50f54c70c8b6bc808fa59ce589b0480780cc811b*/




//Synthetic comment -- diff --git a/services/java/com/android/server/pm/UserManager.java b/services/java/com/android/server/pm/UserManager.java
//Synthetic comment -- index 76fa5ab..d15e12c 100644

//Synthetic comment -- @@ -136,6 +136,7 @@
}
}
updateUserIds();
            fis.close();
} catch (IOException ioe) {
fallbackToSingleUser();
} catch (XmlPullParserException pe) {







