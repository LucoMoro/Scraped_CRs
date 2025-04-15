/*ProviderMap: remove hashmap when entire user specific entries are removed

User specific entries in  mProvidersByNamePerUser and mProvidersByClassPerUser
isn't cleared when entire user specific providers are removed. Fix is to remove
the user specific hashmap in such cases.

Change-Id:I6edf211ced873e59f3b89ffceb9eceec13ea8f3f*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ProviderMap.java b/services/java/com/android/server/am/ProviderMap.java
//Synthetic comment -- index d148ec3..e4608a2 100644

//Synthetic comment -- @@ -127,7 +127,12 @@
Slog.i(TAG,
"Removing from providersByName name=" + name + " user="
+ (optionalUserId == -1 ? Binder.getOrigCallingUser() : optionalUserId));
            getProvidersByName(optionalUserId).remove(name);
}
}

//Synthetic comment -- @@ -141,7 +146,12 @@
Slog.i(TAG,
"Removing from providersByClass name=" + name + " user="
+ (optionalUserId == -1 ? Binder.getOrigCallingUser() : optionalUserId));
            getProvidersByClass(optionalUserId).remove(name);
}
}








