/*Fix for too many binder calls in packagemanager

The packagemanager uses a ParceledListSlice to send back its lists
of installed packages and apps. The list slice has a method append
which, in addition to adding the item to the list, also returns true
if the list has passed a size limit (about 1/4 of the total possible
IPC parcel size) to let the caller know that he should send the
slice. However, when used by the pm, it has an extra ! that makes it
send whenever it ISN'T over this limit instead (and conversely, not
send if it is under). This causes a lot more calls than needed since
it sends tiny one item slices instead of larger ones. This patch
removes the extra ! making it behave correctly.

Change-Id:I8db46d380a25406b55f3214aee1505e81949acc5*/




//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 6b61c47..938d93a 100644

//Synthetic comment -- @@ -2573,7 +2573,7 @@
}
}

                if (pi != null && list.append(pi)) {
break;
}
}
//Synthetic comment -- @@ -2620,7 +2620,7 @@
}
}

                if (ai != null && list.append(ai)) {
break;
}
}







