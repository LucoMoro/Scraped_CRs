/*Fix off-by-one error when filtering application UIDs

A filtering check in writeLPr() mistakenly includes the first application UID
(10000) with the result that the package with UID 10000 is missing from
packages.list. This patch fix the error.

Change-Id:I3651beb346290db8e09317391b95a77aed1946b6Signed-off-by: Magnus Eriksson <eriksson.mag@gmail.com>*/
//Synthetic comment -- diff --git a/services/java/com/android/server/pm/Settings.java b/services/java/com/android/server/pm/Settings.java
//Synthetic comment -- index 36442a0..f0f5414 100644

//Synthetic comment -- @@ -971,7 +971,7 @@

// Avoid any application that has a space in its path
// or that is handled by the system.
                    if (dataPath.indexOf(" ") >= 0 || ai.uid <= Process.FIRST_APPLICATION_UID)
continue;

// we store on each line the following information for now:
//Synthetic comment -- @@ -2261,4 +2261,4 @@
pw.println("Settings parse messages:");
pw.print(mReadMessages.toString());
}
}
\ No newline at end of file







