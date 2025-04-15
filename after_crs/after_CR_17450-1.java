/*Added noop unregisterReceiver to complement existing noop registerReceiver.

Without this addition, code that is being tested using an isolated context
can cause an exception if it unregisters a previously registered broadcast
receiver.  This is because the isolated context never actually registered
the receiver in the first place.  The fix is to make sure the isolated
context is consistent in ignoring both recevier registration and
unregistration calls.

Change-Id:Ie0ba6f4bb10f5248704327a0ffc8e37ee8b71ae2*/




//Synthetic comment -- diff --git a/test-runner/src/android/test/IsolatedContext.java b/test-runner/src/android/test/IsolatedContext.java
//Synthetic comment -- index b483b82..bc00f68 100644

//Synthetic comment -- @@ -87,6 +87,11 @@
}

@Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        // Ignore
    }

    @Override
public void sendBroadcast(Intent intent) {
mBroadcastIntents.add(intent);
}







