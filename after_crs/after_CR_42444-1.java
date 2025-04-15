/*Am: Clean mLaunchingProviders of all providers started by the client

Launching providers is removed from mLaunchingProviders when the client
process dies and works as long as the client process had requested just
one provider and not otherwise. Fix is to make sure mLaunchingProviders
is updated to remove all providers launched by the client process.

Change-Id:Ib4b8b44b6a102762a6558f65fb2e6dbf11d725ea*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..9058839 100644

//Synthetic comment -- @@ -10950,7 +10950,9 @@
restart = true;
} else {
removeDyingProviderLocked(app, cpr, true);
                    // cpr should have been removed from mLaunchingProviders
NL = mLaunchingProviders.size();
                    i--;
}
}
}







