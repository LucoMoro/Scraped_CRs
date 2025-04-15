/*Remove semicolon in if statement.

if statement ends with semicolon. It looks like a typo.

Change-Id:I8c9146a0ae17dfd63aa24078f67e0902a5f39e8cSigned-off-by: You Kim <you.kim72@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index f914271..76f5741 100644

//Synthetic comment -- @@ -9455,8 +9455,8 @@
for (Entry<String, PackageSetting> entry : entries) {
entry.getValue().removeUser(userId);
}
            if (mDirtyUsers.remove(userId));
            mSettings.removeUserLPr(userId);
}
}








