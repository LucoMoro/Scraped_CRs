/*Fix use case of app + 1 lib that have the same package in Ant. do not merge.

Bug: 40273

(cherry picked from commit 3ff8e8bfafeeb31b24bc9fa9a8b3c8c0c6487af3)

Change-Id:I7a0d75618d77204393c8fa67bae480df3820ede9*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecTask.java b/anttasks/src/com/android/ant/AaptExecTask.java
//Synthetic comment -- index f07cfad..257d1b9 100644

//Synthetic comment -- @@ -731,7 +731,9 @@

// simpler case of a single library
if (packages.length == 1) {
                        createRClass(fullSymbols, rFiles[0], packages[0]);
} else {

Map<String, String> libPackages = Maps.newHashMapWithExpectedSize(







