/*Logical error. If multiple receivers are dead, only the first one will be added.
found inhttp://code.google.com/p/android/issues/detail?id=2651*/




//Synthetic comment -- diff --git a/services/java/com/android/server/LocationManagerService.java b/services/java/com/android/server/LocationManagerService.java
//Synthetic comment -- index 705ddb3..412dcf8 100644

//Synthetic comment -- @@ -836,8 +836,8 @@
} catch (PendingIntent.CanceledException e) {
if (deadReceivers == null) {
deadReceivers = new ArrayList<Receiver>();
}
                            deadReceivers.add(receiver);
}
}
} catch (RemoteException e) {







