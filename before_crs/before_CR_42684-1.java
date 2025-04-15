/*Fix unreleased wakelock problem

Do not acquire the wake lock if the current thread is interrupted
when sending ping post or sleep as it leads to high power
consumption in flight mode.

Change-Id:If5b121d47dab9094a7d7c638e0333a1b7f558b31*/
//Synthetic comment -- diff --git a/exchange2/src/com/android/exchange/EasAccountService.java b/exchange2/src/com/android/exchange/EasAccountService.java
//Synthetic comment -- index 58ddaf3..04c3ca7 100644

//Synthetic comment -- @@ -509,9 +509,11 @@
try {
Thread.sleep(ms);
} catch (InterruptedException e) {
            // Doesn't matter whether we stop early; it's the thought that counts
} finally {
            if (runAsleep) {
ExchangeService.runAwake(mMailboxId);
}
}








//Synthetic comment -- diff --git a/exchange2/src/com/android/exchange/EasSyncService.java b/exchange2/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index 657bc84..b1a97c4 100644

//Synthetic comment -- @@ -1288,7 +1288,9 @@
} finally {
synchronized(getSynchronizer()) {
if (isPingCommand) {
                    ExchangeService.runAwake(mMailboxId);
} else {
ExchangeService.clearWatchdogAlarm(mMailboxId);
}







