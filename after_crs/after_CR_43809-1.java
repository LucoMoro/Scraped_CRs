/*Fix comment from a previous review.

Change-Id:I772ed35a85093c57f5e6e7b1faac3a60cd49ce3f*/




//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/AaptExecTask.java b/anttasks/src/com/android/ant/AaptExecTask.java
//Synthetic comment -- index 76a6339..2511500 100644

//Synthetic comment -- @@ -684,8 +684,8 @@
SymbolLoader symbolValues = new SymbolLoader(rFile);
symbolValues.load();

                    // we have two props which contains list of items. Both items represent
                    // 2 data of a single property.
// Don't want to use guava's splitter because it doesn't provide a list of the
// result. but we know the list starts with a ; so strip it.
if (libPkgProp.startsWith(";")) {







