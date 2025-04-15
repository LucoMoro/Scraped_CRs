/*Pass additional inputs when spawning apps via the Zygote and add SELinux permission checks.

When spawning an app process, the ActivityManagerService has additional information
about the app package that may be useful in setting a SELinux security context on the
process.  Extend the Process.start() interface to allow passing such information
to the Zygote spawner.  We originally considered using the existing zygoteArgs
argument, but found that those arguments are appended after the class name and
left uninterpreted by ZygoteConnection, merely passed along to the class or wrapper.
Thus we introduce a new spawnerArgs argument for this purpose.  Presently
this is only used to convey a flag indicating whether the app was from the
system partition, but this may be extended in the future.

Modify the ZygoteConnection to interpret the new option and convey it to
forkAndSpecialize, as well as passing the nice name as a further input.
Also modify the ZygoteConnection to apply SELinux permission checks on
privileged operations.

Change-Id:I66045ffd33ca9898b1d026882bcc1c5baf3adc17*/




//Synthetic comment -- diff --git a/core/java/android/os/Process.java b/core/java/android/os/Process.java
//Synthetic comment -- index e1bc275..3ce36fa 100644

//Synthetic comment -- @@ -285,10 +285,11 @@
final String niceName,
int uid, int gid, int[] gids,
int debugFlags, int targetSdkVersion,
				  String[] spawnerArgs,
String[] zygoteArgs) {
try {
return startViaZygote(processClass, niceName, uid, gid, gids,
		  debugFlags, targetSdkVersion, spawnerArgs, zygoteArgs);
} catch (ZygoteStartFailedEx ex) {
Log.e(LOG_TAG,
"Starting VM process through Zygote failed");
//Synthetic comment -- @@ -460,6 +461,7 @@
final int uid, final int gid,
final int[] gids,
int debugFlags, int targetSdkVersion,
				  String[] spawnerArgs,
String[] extraArgs)
throws ZygoteStartFailedEx {
synchronized(Process.class) {
//Synthetic comment -- @@ -510,6 +512,11 @@
argsForZygote.add("--nice-name=" + niceName);
}

            if (spawnerArgs != null) {
                for (String arg : spawnerArgs) {
                    argsForZygote.add(arg);
                }
            }
argsForZygote.add(processClass);

if (extraArgs != null) {








//Synthetic comment -- diff --git a/core/java/com/android/internal/os/ZygoteConnection.java b/core/java/com/android/internal/os/ZygoteConnection.java
//Synthetic comment -- index 9af7e96..1c295bb 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import dalvik.system.PathClassLoader;
import dalvik.system.Zygote;

import gov.android.selinux.SELinuxCommon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//Synthetic comment -- @@ -73,6 +75,7 @@
private final DataOutputStream mSocketOutStream;
private final BufferedReader mSocketReader;
private final Credentials peer;
    private final String peerSecctx;

/**
* A long-lived reference to the original command socket used to launch
//Synthetic comment -- @@ -109,6 +112,13 @@
Log.e(TAG, "Cannot read peer credentials", ex);
throw ex;
}

	if (SELinuxCommon.isSELinuxEnabled()) {
	    peerSecctx = SELinuxCommon.getPeerCon(mSocket.getFileDescriptor());
	    Log.i(TAG, "Peer security context was " + peerSecctx);
	} else {
	    peerSecctx = "";
	}
}

/**
//Synthetic comment -- @@ -207,10 +217,11 @@
try {
parsedArgs = new Arguments(args);

            applyUidSecurityPolicy(parsedArgs, peer, peerSecctx);
            applyRlimitSecurityPolicy(parsedArgs, peer, peerSecctx);
            applyCapabilitiesSecurityPolicy(parsedArgs, peer, peerSecctx);
            applyInvokeWithSecurityPolicy(parsedArgs, peer, peerSecctx);
            applySEInfoSecurityPolicy(parsedArgs, peer, peerSecctx);

applyDebuggerSystemProperty(parsedArgs);
applyInvokeWithSystemProperty(parsedArgs);
//Synthetic comment -- @@ -229,7 +240,8 @@
}

pid = Zygote.forkAndSpecialize(parsedArgs.uid, parsedArgs.gid,
                    parsedArgs.gids, parsedArgs.debugFlags, rlimits,
                    parsedArgs.SEInfo, parsedArgs.niceName);
} catch (IOException ex) {
logAndPrintError(newStderr, "Exception creating pipe", ex);
} catch (ErrnoException ex) {
//Synthetic comment -- @@ -352,6 +364,10 @@
long permittedCapabilities;
long effectiveCapabilities;

        /** from --seinfo */
	boolean SEInfoSpecified;
        String SEInfo;

/** from all --rlimit=r,c,m */
ArrayList<int[]> rlimits;

//Synthetic comment -- @@ -429,6 +445,13 @@
peerWait = true;
} else if (arg.equals("--runtime-init")) {
runtimeInit = true;
                } else if (arg.startsWith("--seinfo=")) {
		    if (SEInfoSpecified) {
                        throw new IllegalArgumentException(
                                "Duplicate arg specified");
                    }
		    SEInfoSpecified = true;
		    SEInfo = arg.substring(arg.indexOf('=')+1);
} else if (arg.startsWith("--capabilities=")) {
if (capabilitiesSpecified) {
throw new IllegalArgumentException(
//Synthetic comment -- @@ -591,7 +614,7 @@
* @param peer non-null; peer credentials
* @throws ZygoteSecurityException
*/
    private static void applyUidSecurityPolicy(Arguments args, Credentials peer, String peerSecctx)
throws ZygoteSecurityException {

int peerUid = peer.getUid();
//Synthetic comment -- @@ -624,6 +647,16 @@
}
}

        if (args.uidSpecified || args.gidSpecified || args.gids != null) {
	     boolean allowed = SELinuxCommon.checkSELinuxAccess(peerSecctx,
								peerSecctx,
								"zygote",
								"specifyids");
	     if (!allowed)
		 throw new ZygoteSecurityException(
                     "Peer may not specify uid's or gid's");
         }

// If not otherwise specified, uid and gid are inherited from peer
if (!args.uidSpecified) {
args.uid = peer.getUid();
//Synthetic comment -- @@ -664,7 +697,7 @@
* @throws ZygoteSecurityException
*/
private static void applyRlimitSecurityPolicy(
            Arguments args, Credentials peer, String peerSecctx)
throws ZygoteSecurityException {

int peerUid = peer.getUid();
//Synthetic comment -- @@ -676,6 +709,16 @@
"This UID may not specify rlimits.");
}
}

        if (args.rlimits != null) {
	     boolean allowed = SELinuxCommon.checkSELinuxAccess(peerSecctx,
								peerSecctx,
								"zygote",
								"specifyrlimits");
	     if (!allowed)
		 throw new ZygoteSecurityException(
                     "Peer may not specify rlimits");
         }
}

/**
//Synthetic comment -- @@ -689,7 +732,7 @@
* @throws ZygoteSecurityException
*/
private static void applyCapabilitiesSecurityPolicy(
            Arguments args, Credentials peer, String peerSecctx)
throws ZygoteSecurityException {

if (args.permittedCapabilities == 0
//Synthetic comment -- @@ -698,6 +741,14 @@
return;
}

	boolean allowed = SELinuxCommon.checkSELinuxAccess(peerSecctx,
							   peerSecctx,
							   "zygote",
							   "specifycapabilities");
	if (!allowed)
	    throw new ZygoteSecurityException(
				      "Peer may not specify capabilities");

if (peer.getUid() == 0) {
// root may specify anything
return;
//Synthetic comment -- @@ -747,7 +798,7 @@
* @param peer non-null; peer credentials
* @throws ZygoteSecurityException
*/
    private static void applyInvokeWithSecurityPolicy(Arguments args, Credentials peer, String peerSecctx)
throws ZygoteSecurityException {
int peerUid = peer.getUid();

//Synthetic comment -- @@ -755,6 +806,50 @@
throw new ZygoteSecurityException("Peer is not permitted to specify "
+ "an explicit invoke-with wrapper command");
}

	if (args.invokeWith != null) {
	    boolean allowed = SELinuxCommon.checkSELinuxAccess(peerSecctx,
							       peerSecctx,
							       "zygote",
							       "specifyinvokewith");
	    if (!allowed)
		throw new ZygoteSecurityException("Peer is not permitted to specify "
                    + "an explicit invoke-with wrapper command");
	}
    }

    /**
     * Applies zygote security policy for SEAndroid information.
     *
     * @param args non-null; zygote spawner arguments
     * @param peer non-null; peer credentials
     * @throws ZygoteSecurityException
     */
    private static void applySEInfoSecurityPolicy(
            Arguments args, Credentials peer, String peerSecctx)
            throws ZygoteSecurityException {
        int peerUid = peer.getUid();

        if (args.SEInfo == null) {
            // nothing to check
            return;
        }

        if (!(peerUid == 0 || peerUid == Process.SYSTEM_UID)) {
            // All peers with UID other than root or SYSTEM_UID
            throw new ZygoteSecurityException(
                    "This UID may not specify SEAndroid info.");
        }

	boolean allowed = SELinuxCommon.checkSELinuxAccess(peerSecctx,
							   peerSecctx,
							   "zygote",
							   "specifyseinfo");
	if (!allowed)
	    throw new ZygoteSecurityException(
				      "Peer may not specify SEAndroid info");

	return;
}

/**








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index df58e83..15b0f9e 100644

//Synthetic comment -- @@ -1960,11 +1960,17 @@
debugFlags |= Zygote.DEBUG_ENABLE_ASSERT;
}

	    String [] spawnerArgs;
	    if ((app.info.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
		spawnerArgs = new String[] { "--seinfo=systemApp" };
	    } else
		spawnerArgs = null;

// Start the process.  It will either succeed and return a result containing
// the PID of the new process, or else throw a RuntimeException.
Process.ProcessStartResult startResult = Process.start("android.app.ActivityThread",
app.processName, uid, uid, gids, debugFlags,
                    app.info.targetSdkVersion, spawnerArgs, null);

BatteryStatsImpl bs = app.batteryStats.getBatteryStats();
synchronized (bs) {







