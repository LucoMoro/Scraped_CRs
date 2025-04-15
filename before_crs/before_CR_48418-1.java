/*Fix use case of app + 1 lib that have the same package in Ant.

Bug: 40273
Change-Id:I5009d3c95d677953a191145038d6d06cd8b2bf32*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecTask.java b/anttasks/src/com/android/ant/AaptExecTask.java
//Synthetic comment -- index f07cfad..257d1b9 100644

//Synthetic comment -- @@ -731,7 +731,9 @@

// simpler case of a single library
if (packages.length == 1) {
                        createRClass(fullSymbols, rFiles[0], packages[0]);
} else {

Map<String, String> libPackages = Maps.newHashMapWithExpectedSize(







