/*MountService: unlink death notification when unregistering listeners

Change-Id:I09045cfe67f7da84bc68a50fc5440ea2c6b754e5*/




//Synthetic comment -- diff --git a/services/java/com/android/server/MountService.java b/services/java/com/android/server/MountService.java
//Synthetic comment -- index 5425813..540dda4 100644

//Synthetic comment -- @@ -1218,6 +1218,7 @@
for(MountServiceBinderListener bl : mListeners) {
if (bl.mListener == listener) {
mListeners.remove(mListeners.indexOf(bl));
                    listener.asBinder().unlinkToDeath(bl, 0);
return;
}
}







