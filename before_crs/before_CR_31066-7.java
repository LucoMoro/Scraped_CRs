/*Pass additional inputs when spawning apps via the Zygote and add SELinux permission checks.

When spawning an app process, the ActivityManagerService has additional information
about the app package that may be useful in setting a SELinux security context on the
process.  Extend the Process.start() interface to allow passing such information
to the Zygote spawner.  We originally considered using the existing zygoteArgs
argument, but found that those arguments are appended after the class name and
left uninterpreted by ZygoteConnection, merely passed along to the class or wrapper.
Thus we introduce a new seInfo argument for this purpose.

Modify the ZygoteConnection to interpret the new option and convey it to
forkAndSpecialize, as well as passing the nice name as a further input.
Also modify the ZygoteConnection to apply SELinux permission checks on
privileged operations.

Change-Id:I66045ffd33ca9898b1d026882bcc1c5baf3adc17*/
//Synthetic comment -- diff --git a/core/java/android/os/Process.java b/core/java/android/os/Process.java
//Synthetic comment -- index e1bc275..50567b2 100644

//Synthetic comment -- @@ -274,6 +274,7 @@
* @param gids Additional group-ids associated with the process.
* @param debugFlags Additional flags.
* @param targetSdkVersion The target SDK version for the app.
* @param zygoteArgs Additional arguments to supply to the zygote process.
* 
* @return An object that describes the result of the attempt to start the process.
//Synthetic comment -- @@ -285,10 +286,11 @@
final String niceName,
int uid, int gid, int[] gids,
int debugFlags, int targetSdkVersion,
String[] zygoteArgs) {
try {
return startViaZygote(processClass, niceName, uid, gid, gids,
                    debugFlags, targetSdkVersion, zygoteArgs);
} catch (ZygoteStartFailedEx ex) {
Log.e(LOG_TAG,
"Starting VM process through Zygote failed");
//Synthetic comment -- @@ -451,6 +453,7 @@
* new process should setgroup() to.
* @param debugFlags Additional flags.
* @param targetSdkVersion The target SDK version for the app.
* @param extraArgs Additional arguments to supply to the zygote process.
* @return An object that describes the result of the attempt to start the process.
* @throws ZygoteStartFailedEx if process start failed for any reason
//Synthetic comment -- @@ -460,6 +463,7 @@
final int uid, final int gid,
final int[] gids,
int debugFlags, int targetSdkVersion,
String[] extraArgs)
throws ZygoteStartFailedEx {
synchronized(Process.class) {
//Synthetic comment -- @@ -510,6 +514,10 @@
argsForZygote.add("--nice-name=" + niceName);
}

argsForZygote.add(processClass);

if (extraArgs != null) {








//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ZygoteConnection.java b/core/java/com/android/internal/os/ZygoteConnection.java
//Synthetic comment -- index 9af7e96..b016e99 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import dalvik.system.PathClassLoader;
import dalvik.system.Zygote;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//Synthetic comment -- @@ -73,6 +75,7 @@
private final DataOutputStream mSocketOutStream;
private final BufferedReader mSocketReader;
private final Credentials peer;

/**
* A long-lived reference to the original command socket used to launch
//Synthetic comment -- @@ -109,6 +112,8 @@
Log.e(TAG, "Cannot read peer credentials", ex);
throw ex;
}
}

/**
//Synthetic comment -- @@ -207,10 +212,11 @@
try {
parsedArgs = new Arguments(args);

            applyUidSecurityPolicy(parsedArgs, peer);
            applyRlimitSecurityPolicy(parsedArgs, peer);
            applyCapabilitiesSecurityPolicy(parsedArgs, peer);
            applyInvokeWithSecurityPolicy(parsedArgs, peer);

applyDebuggerSystemProperty(parsedArgs);
applyInvokeWithSystemProperty(parsedArgs);
//Synthetic comment -- @@ -229,7 +235,8 @@
}

pid = Zygote.forkAndSpecialize(parsedArgs.uid, parsedArgs.gid,
                    parsedArgs.gids, parsedArgs.debugFlags, rlimits);
} catch (IOException ex) {
logAndPrintError(newStderr, "Exception creating pipe", ex);
} catch (ErrnoException ex) {
//Synthetic comment -- @@ -352,6 +359,10 @@
long permittedCapabilities;
long effectiveCapabilities;

/** from all --rlimit=r,c,m */
ArrayList<int[]> rlimits;

//Synthetic comment -- @@ -429,6 +440,13 @@
peerWait = true;
} else if (arg.equals("--runtime-init")) {
runtimeInit = true;
} else if (arg.startsWith("--capabilities=")) {
if (capabilitiesSpecified) {
throw new IllegalArgumentException(
//Synthetic comment -- @@ -591,7 +609,8 @@
* @param peer non-null; peer credentials
* @throws ZygoteSecurityException
*/
    private static void applyUidSecurityPolicy(Arguments args, Credentials peer)
throws ZygoteSecurityException {

int peerUid = peer.getUid();
//Synthetic comment -- @@ -624,6 +643,17 @@
}
}

// If not otherwise specified, uid and gid are inherited from peer
if (!args.uidSpecified) {
args.uid = peer.getUid();
//Synthetic comment -- @@ -664,7 +694,7 @@
* @throws ZygoteSecurityException
*/
private static void applyRlimitSecurityPolicy(
            Arguments args, Credentials peer)
throws ZygoteSecurityException {

int peerUid = peer.getUid();
//Synthetic comment -- @@ -676,6 +706,17 @@
"This UID may not specify rlimits.");
}
}
}

/**
//Synthetic comment -- @@ -689,7 +730,7 @@
* @throws ZygoteSecurityException
*/
private static void applyCapabilitiesSecurityPolicy(
            Arguments args, Credentials peer)
throws ZygoteSecurityException {

if (args.permittedCapabilities == 0
//Synthetic comment -- @@ -698,6 +739,15 @@
return;
}

if (peer.getUid() == 0) {
// root may specify anything
return;
//Synthetic comment -- @@ -747,7 +797,8 @@
* @param peer non-null; peer credentials
* @throws ZygoteSecurityException
*/
    private static void applyInvokeWithSecurityPolicy(Arguments args, Credentials peer)
throws ZygoteSecurityException {
int peerUid = peer.getUid();

//Synthetic comment -- @@ -755,6 +806,52 @@
throw new ZygoteSecurityException("Peer is not permitted to specify "
+ "an explicit invoke-with wrapper command");
}
}

/**








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index cffb391..5044554 100644

//Synthetic comment -- @@ -1967,7 +1967,7 @@
// the PID of the new process, or else throw a RuntimeException.
Process.ProcessStartResult startResult = Process.start("android.app.ActivityThread",
app.processName, uid, uid, gids, debugFlags,
                    app.info.targetSdkVersion, null);

BatteryStatsImpl bs = app.batteryStats.getBatteryStats();
synchronized (bs) {







