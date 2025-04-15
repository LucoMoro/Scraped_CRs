/*Add support for MMAC install checks.

MMAC install check mechanism applies an install-time check
of app permissions against a MAC policy configuration. The
policy configuration can be found at

git.selinuxproject.org/~seandroid/selinux/mac-policy

The install checks run by default in permissive mode (allow
all apps to install with denials going to logcat), and can be
set to enforcing by setting 'persist.mac_enforcing_mode' to 1.
In this case, be sure to grab an updated sepolicy from

git://git.selinuxproject.org/~seandroid/selinux/sepolicy

-
Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>

Change-Id:I61d34a9fd6975f23023f70f205a510e3357d843c*/




//Synthetic comment -- diff --git a/core/java/android/content/pm/ApplicationInfo.java b/core/java/android/content/pm/ApplicationInfo.java
//Synthetic comment -- index e1434b3..a67c236 100644

//Synthetic comment -- @@ -385,6 +385,15 @@
public String[] resourceDirs;

/**
     * A string retrieved from the value attribute of the seinfo tag found
     * in MMAC policy. This value is useful in setting an SELinux security
     * context on the process and the app's data directory.
     *
     * {@hide}
     */
    public String seinfo;

    /**
* Paths to all shared libraries this application is linked against.  This
* field is only set if the {@link PackageManager#GET_SHARED_LIBRARY_FILES
* PackageManager.GET_SHARED_LIBRARY_FILES} flag was used when retrieving
//Synthetic comment -- @@ -464,6 +473,9 @@
if (resourceDirs != null) {
pw.println(prefix + "resourceDirs=" + resourceDirs);
}
        if (seinfo != null) {
            pw.println(prefix + "seinfo=" + seinfo);
        }
pw.println(prefix + "dataDir=" + dataDir);
if (sharedLibraryFiles != null) {
pw.println(prefix + "sharedLibraryFiles=" + sharedLibraryFiles);
//Synthetic comment -- @@ -531,6 +543,7 @@
publicSourceDir = orig.publicSourceDir;
nativeLibraryDir = orig.nativeLibraryDir;
resourceDirs = orig.resourceDirs;
        seinfo = orig.seinfo;
sharedLibraryFiles = orig.sharedLibraryFiles;
dataDir = orig.dataDir;
uid = orig.uid;
//Synthetic comment -- @@ -569,6 +582,7 @@
dest.writeString(publicSourceDir);
dest.writeString(nativeLibraryDir);
dest.writeStringArray(resourceDirs);
        dest.writeString(seinfo);
dest.writeStringArray(sharedLibraryFiles);
dest.writeString(dataDir);
dest.writeInt(uid);
//Synthetic comment -- @@ -607,6 +621,7 @@
publicSourceDir = source.readString();
nativeLibraryDir = source.readString();
resourceDirs = source.readStringArray();
        seinfo = source.readString();
sharedLibraryFiles = source.readStringArray();
dataDir = source.readString();
uid = source.readInt();








//Synthetic comment -- diff --git a/core/java/android/content/pm/PackageManager.java b/core/java/android/content/pm/PackageManager.java
//Synthetic comment -- index 6de69b0..82a3cc8 100644

//Synthetic comment -- @@ -619,6 +619,14 @@
public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;

/**
     * Installation failed return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the system failed to install the package because of a policy denial.
     * @hide
     */
    public static final int INSTALL_FAILED_POLICY_REJECTED_PERMISSION = -111;

    /**
* Flag parameter for {@link #deletePackage} to indicate that you don't want to delete the
* package's data directory.
*








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 60085f4..51c39c6 100644

//Synthetic comment -- @@ -2053,7 +2053,7 @@
// the PID of the new process, or else throw a RuntimeException.
Process.ProcessStartResult startResult = Process.start("android.app.ActivityThread",
app.processName, uid, uid, gids, debugFlags,
                    app.info.targetSdkVersion, app.info.seinfo, null);

BatteryStatsImpl bs = app.batteryStats.getBatteryStats();
synchronized (bs) {








//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 3501e47..6f494ef 100644

//Synthetic comment -- @@ -102,6 +102,7 @@
import android.os.UserId;
import android.provider.Settings.Secure;
import android.security.SystemKeyStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
//Synthetic comment -- @@ -139,6 +140,7 @@
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import libcore.io.ErrnoException;
import libcore.io.IoUtils;
//Synthetic comment -- @@ -162,6 +164,8 @@
private static final boolean DEBUG_PREFERRED = false;
static final boolean DEBUG_UPGRADE = false;
private static final boolean DEBUG_INSTALL = false;
    private static final boolean DEBUG_POLICY = false;
    private static final boolean DEBUG_POLICY_INSTALL = DEBUG_POLICY || false;
private static final boolean DEBUG_REMOVE = false;
private static final boolean DEBUG_SHOW_INFO = false;
private static final boolean DEBUG_PACKAGE_INFO = false;
//Synthetic comment -- @@ -186,6 +190,8 @@
// package apks to install directory.
private static final String INSTALL_PACKAGE_SUFFIX = "-";

    private static final String MMAC_DENY = "MMAC_DENIAL:";

static final int SCAN_MONITOR = 1<<0;
static final int SCAN_NO_DEX = 1<<1;
static final int SCAN_FORCE_DEX = 1<<2;
//Synthetic comment -- @@ -321,6 +327,19 @@
// Temporary for building the final shared libraries for an .apk.
String[] mTmpSharedLibraries = null;

    // Available policy read from mac_permissions.xml that deals
    // with global signature based stanzas, including any default stanza.
    private HashMap<Signature, InstallPolicy> mInstallSignaturePolicy =
            new HashMap<Signature, InstallPolicy>();

    // Available policy read from mac_permissions.xml that deals
    // with global package stanzas.
    private HashMap<String, InstallPolicy> mInstallPackagePolicy =
            new HashMap<String, InstallPolicy>();

    // Has the mac_permissions.xml been found
    private final boolean isMacPolicyEnabled;

// These are the features this devices supports that were read from the
// etc/permissions.xml file.
final HashMap<String, FeatureInfo> mAvailableFeatures =
//Synthetic comment -- @@ -1042,6 +1061,13 @@
}
}

            // Find install policy
            long startPolicyTime = SystemClock.uptimeMillis();
            isMacPolicyEnabled = scanPolicy();
            Slog.i(TAG, "Time to scan install policy: "
                   + ((SystemClock.uptimeMillis()-startPolicyTime)/1000f)
                   + " seconds");

// Find base frameworks (resource packages without code).
mFrameworkInstallObserver = new AppDirObserver(
mFrameworkDir.getPath(), OBSERVER_EVENTS, true);
//Synthetic comment -- @@ -1317,6 +1343,368 @@
mSettings.removePackageLPw(ps.name);
}

    boolean scanPolicy() {

        File dataDir = Environment.getDataDirectory();
        File rootDir = Environment.getRootDirectory();

        File[] policyFileLocations = new File[]{
            new File(dataDir, "system/mac_permissions.xml"),
            new File(rootDir, "etc/security/mac_permissions.xml"),
            null};

        FileReader policyFile = null;
        int i = 0;
        while (policyFile == null && policyFileLocations[i] != null) {
            try {
                policyFile = new FileReader(policyFileLocations[i]);
                break;
            } catch (FileNotFoundException e) {
                Slog.d(TAG,"Couldn't find permissions file " + policyFileLocations[i]);
            }
            i++;
        }

        if (policyFile == null) {
            Slog.d(TAG, "No mac_permissions.xml policy file. MMAC starting in disabled mode.");
            return false;
        }

        Slog.d(TAG, "MMAC policy starting in enabled mode.");

        boolean enforcing = SystemProperties.getBoolean("persist.mac_enforcing_mode", false);
        String mode = enforcing ? "enforcing" : "permissive";
        Slog.d(TAG, "MMAC policy starting in " + mode + " mode.");

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(policyFile);

            XmlUtils.beginDocument(parser, "policy");

            while (true) {
                XmlUtils.nextElement(parser);
                if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
                    break;
                }
                String tagName = parser.getName();
                if ("signer".equals(tagName)) {
                    String cert = parser.getAttributeValue(null, "signature");
                    if (cert != null) {
                        Signature signature = null;
                        try {
                            signature = new Signature(cert);
                        } catch (IllegalArgumentException e) {
                            Slog.w(TAG, "'signer' tag with bad signature at " +
                                   parser.getPositionDescription() + ": " + e);
                            XmlUtils.skipCurrentTag(parser);
                            continue;
                        }
                        if (signature != null) {
                            InstallPolicy type = determineInstallPolicyType(parser, true);
                            if (type != null) {
                                mInstallSignaturePolicy.put(signature, type);
                                if (DEBUG_POLICY_INSTALL)
                                    Log.i(TAG, "<signer>: (" + cert + ") " + type);
                            } else {
                                Slog.w(TAG, "Skipping policy stanza format for 'signer' at " +
                                       parser.getPositionDescription());
                            }
                        }
                    } else {
                        Slog.w(TAG, "<signer> without valid signature attribute at "
                               + parser.getPositionDescription() + ". Skipping tag.");
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                } else if ("default".equals(tagName)) {
                    InstallPolicy type = determineInstallPolicyType(parser, true);
                    if (type != null) {
                        // we use the null key in the hashset as the 'default' type
                        mInstallSignaturePolicy.put(null, type);
                        if (DEBUG_POLICY_INSTALL)
                            Log.i(TAG, "<default>: " + type);
                    } else {
                        Slog.w(TAG, "Skipping policy stanza format for 'default' at " +
                               parser.getPositionDescription());
                    }
                } else if ("package".equals(tagName)) {
                    String pkgName = parser.getAttributeValue(null, "name");
                    if (pkgName != null) {
                        InstallPolicy type = determineInstallPolicyType(parser, false);
                        if (type != null) {
                            mInstallPackagePolicy.put(pkgName, type);
                            if (DEBUG_POLICY_INSTALL)
                                Log.i(TAG, "<package>: (" + pkgName + ") " + type);
                        } else {
                            Slog.w(TAG, "Skipping policy stanza format for 'package' at " +
                                   parser.getPositionDescription());
                        }
                    } else {
                        Slog.w(TAG, "<package> without valid name attribute at "
                               + parser.getPositionDescription() + ". Skipping tag.");
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                } else {
                    Slog.w(TAG, "Skipping unknown XML element: <"+tagName+"> at " +
                           parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                    continue;
                }
            }
            policyFile.close();
        } catch (XmlPullParserException e) {
            Slog.w(TAG, "Got execption parsing mac_permissions.xml." + e);
        } catch (IOException e) {
            Slog.w(TAG, "Got execption parsing mac_permissions.xml." + e);
        }
        return true;
    }

    /**
     * Takes an install policy stanza and determines the type of policy enforced.
     * The rules are as follows:
     * - A blacklist is determined if at least one <deny-permission name="" /> tag
     *     is found. Zero or more tags are allowed.
     * - A whitelist is determined if not a blacklist and at least one
     *     <allow-permission name="" /> tag is found. Zero or more are allowed.
     * - A wildcard is assigned if no whitelist and no blacklist and at least
     *     one <allow-all /> tag is found. Zero or more are allowed.
     * - Zero or more <package name="" > sub elements are allowed. The scope
     *     of the enforcement for the package is determined by the location of
     *     the particular package tag. If found outside of a signer or default tag,
     *     then the package tag has global scope. If found within a signer or default
     *     tag then it has signatue scope. Meaning, all packages signed with the parent
     *     cert are mediated within the enclosed package policy stanza.
     * - An <seinfo value="" /> tag is allowed within a signer, default or global
     *      package tag. The value will be used as a further selector in determining
     *      the security context for the eventual process.
     * - In order for a policy stanza to be applied, at least one of the above
     *     situations must apply. If none of the above cases apply then no policy
     *     is created for this stanza.
     * - Strict enforcing of the xml stanza is not enforced in most cases.
     *     This mainly applies to duplicate tags which are allowed. In the event
     *     that a tag already exists, the original tag is replaced.
     * - There are also no checks on the validity of permission names. Although
     *     valid android permissions are expected, nothing prevents unknowns.
     * @param XmlPullParser object which points to a valid xml policy tree instance.
     * @param notInsidePackageTag a boolean representing that we have not already
     *        recursed inside a <package name=""> sub element. This is in
     *        place to prevent the <package name="" > tag appearing inside itself.
     *        at any level.
     * @return InstallPolicy an InstallPolicy class representing the type
     *          of policy that was assigned to the stanza. A null value
     *          can result which indicates no type could be determined.
     */
    InstallPolicy determineInstallPolicyType(XmlPullParser parser, boolean notInsidePackageTag)
        throws IOException, XmlPullParserException {

        final HashSet<String> allowPolicyPerms = new HashSet<String>();
        final HashSet<String> denyPolicyPerms = new HashSet<String>();

        final HashMap<String, InstallPolicy> packagePolicy =
            new HashMap<String, InstallPolicy>();

        int type;
        int outerDepth = parser.getDepth();
        boolean allowAll = false;
        String seinfo = null;
        while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
               && (type != XmlPullParser.END_TAG
                   || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG
                || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if ("allow-permission".equals(tagName)) {
                String permName = parser.getAttributeValue(null, "name");
                if (permName != null) {
                    allowPolicyPerms.add(permName);
                } else {
                    Slog.w(TAG, "<allow-permission> without valid name attribute at "
                           + parser.getPositionDescription() + ". Skipping tag.");
                }
                XmlUtils.skipCurrentTag(parser);
                continue;
            } else if ("deny-permission".equals(tagName)) {
                String permName = parser.getAttributeValue(null, "name");
                if (permName != null) {
                    denyPolicyPerms.add(permName);
                } else {
                    Slog.w(TAG, "<deny-permission> without valid name attribute at "
                           + parser.getPositionDescription() + ". Skipping tag.");
                }
                XmlUtils.skipCurrentTag(parser);
                continue;
            } else if ("allow-all".equals(tagName)) {
                allowAll = true;
            } else if (notInsidePackageTag && "seinfo".equals(tagName)) {
                String seinfoTag = parser.getAttributeValue(null, "value");
                if (seinfoTag != null) {
                    seinfo = seinfoTag;
                } else {
                    Slog.w(TAG, "<seinfo> without valid value attribute at "
                           + parser.getPositionDescription() + ". Skipping tag.");
                }
                XmlUtils.skipCurrentTag(parser);
                continue;
            } else if (notInsidePackageTag && "package".equals(tagName)) {
                String pkgName = parser.getAttributeValue(null, "name");
                if (pkgName != null) {
                    InstallPolicy packageType = determineInstallPolicyType(parser, false);
                    if (packageType != null) {
                        packagePolicy.put(pkgName, packageType);
                        if (DEBUG_POLICY_INSTALL)
                            Log.i(TAG, "<package>: (" + pkgName + ") " + packageType);
                    } else {
                        Slog.w(TAG, "Skipping policy stanza format for 'package' at " +
                               parser.getPositionDescription());
                    }
                } else {
                    Slog.w(TAG, "<package> without valid name attribute at "
                           + parser.getPositionDescription() + ". Skipping tag.");
                    XmlUtils.skipCurrentTag(parser);
                    continue;
                }
            } else {
                Slog.w(TAG, "Skipping unknown XML element: <"+tagName+"> at " +
                       parser.getPositionDescription());
                XmlUtils.skipCurrentTag(parser);
                continue;
            }
        }

        // Order is important. Provide the least amount of privelage when in doubt.
        InstallPolicy permPolicyType = null;
        if (denyPolicyPerms.size() > 0)
            permPolicyType = new BlackListPolicy(denyPolicyPerms, packagePolicy, seinfo);
        else if (allowPolicyPerms.size() > 0)
            permPolicyType = new WhiteListPolicy(allowPolicyPerms, packagePolicy, seinfo);
        else if (allowAll)
            permPolicyType = new InstallPolicy(null, packagePolicy, seinfo);

        /**
         * If we only have <package name="" > sub elements we are skipping that policy
         * stanza for now. Meaning, a signer or default tag with only package sub
         * elements is ignored.
         */

        return permPolicyType;
    }

    /**
     * Base class for all install policy classes.
     * Also doubles as the wildcard (allow everything) policy.
     */
    public class InstallPolicy {

        final HashSet<String> policyPerms;
        final HashMap<String, InstallPolicy> packagePolicy;
        final private String seinfo;

        InstallPolicy(HashSet<String> policyPerms, HashMap<String, InstallPolicy> packagePolicy,
                      String seinfo) {
            this.policyPerms = policyPerms;
            this.packagePolicy = packagePolicy;
            this.seinfo = seinfo;
        }

        public boolean passedPolicyChecks(PackageParser.Package pkg) {
            // ensure that local package policy takes precedence
            if (packagePolicy.containsKey(pkg.packageName))
                return packagePolicy.get(pkg.packageName).passedPolicyChecks(pkg);

            return true;
        }

        public String getSEinfo() {
            return seinfo;
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            out.append("[");
            if (policyPerms != null) {
                out.append(TextUtils.join(",", new TreeSet<String>(policyPerms)));
            } else {
                out.append("allow-all");
            }
            out.append("]");
            return out.toString();
        }
    }

    /**
     * Whitelist policy class. Checks that the set of requested permissions
     * is a subset of the maximal set of allowable permissions.
     */
    public class WhiteListPolicy extends InstallPolicy {

        WhiteListPolicy(HashSet<String> policyPerms, HashMap<String, InstallPolicy> packagePolicy,
                        String seinfo) {
            super(policyPerms, packagePolicy, seinfo);
        }

        @Override
        public boolean passedPolicyChecks(PackageParser.Package pkg) {
            // ensure that local package policy takes precedence
            if (packagePolicy.containsKey(pkg.packageName))
                return packagePolicy.get(pkg.packageName).passedPolicyChecks(pkg);

            if (!policyPerms.containsAll(pkg.requestedPermissions)) {
                Slog.w(TAG, MMAC_DENY + " Policy whitelist rejected package "
                       + pkg.packageName + ". The maximal set is: " + toString());
                Slog.w(TAG, MMAC_DENY + " " + pkg.requestedPermissions.toString());
                return false;
            }

            return true;
        }

        @Override
        public String toString() {
            return "allowed-permissions => " + super.toString();
        }
    }

    /**
     * Blacklist policy class. Ensures that all requested permissions
     * are not on the denied list of permissions.
     */
    public class BlackListPolicy extends InstallPolicy {

        BlackListPolicy(HashSet<String> policyPerms, HashMap<String, InstallPolicy> packagePolicy,
                        String seinfo) {
            super(policyPerms, packagePolicy, seinfo);
        }

        @Override
        public boolean passedPolicyChecks(PackageParser.Package pkg) {
            // ensure that local package policy takes precedence
            if (packagePolicy.containsKey(pkg.packageName))
                return packagePolicy.get(pkg.packageName).passedPolicyChecks(pkg);

            Iterator itr = pkg.requestedPermissions.iterator();
            while (itr.hasNext()) {
                String perm = (String)itr.next();
                if (policyPerms.contains(perm)) {
                    Slog.w(TAG, MMAC_DENY + " Policy blacklisted permission " + perm +
                           " for package " + pkg.packageName);
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "denied-permissions => " + super.toString();
        }
    }

void readPermissions() {
// Read permissions from .../etc/permission directory.
File libraryDir = new File(Environment.getRootDirectory(), "etc/permissions");
//Synthetic comment -- @@ -3439,6 +3827,64 @@
}
}

    private boolean passInstallPolicyChecks(PackageParser.Package pkg) {

        /**
         * Order of policy checks:
         *     X.509 cert policy
         *       the per-package and global checks on X.509 certs happen
         *       transparently with the call to passedPolicyChecks(pkg)
         *     global per-package check
         *     default check (null signature)
         */

        /**
         * It's possible that multiple seinfo tags are relevant for one app.
         * If this is the case, the seinfo tag that will be applied is the one
         * for which the corresponding policy stanza is used in the policy
         * decision.
         */

        // APKs can have mulitple signatures. We just want one that passes.
        for (Signature s : pkg.mSignatures) {
            if (s == null) {
                continue;
            }

            /**
             * First check for a valid policy based on a non default signature.
             * The 'default' will be checked last.
             */
            if (mInstallSignaturePolicy.containsKey(s)) {
                InstallPolicy policy = mInstallSignaturePolicy.get(s);
                if (policy.passedPolicyChecks(pkg)) {
                    pkg.applicationInfo.seinfo = policy.getSEinfo();
                    return true;
                }
            }
        }

        // note: if an app signature has been found in policy yet the signer
        // stanza check has failed, we still proceed to the next set of checks.

        // check for global per-package policy
        if (mInstallPackagePolicy.containsKey(pkg.packageName)) {
            InstallPolicy policy = mInstallPackagePolicy.get(pkg.packageName);
            pkg.applicationInfo.seinfo = policy.getSEinfo();
            return policy.passedPolicyChecks(pkg);
        }

        // Check for 'default' policy
        if (mInstallSignaturePolicy.containsKey(null)) {
            InstallPolicy policy = mInstallSignaturePolicy.get(null);
            pkg.applicationInfo.seinfo = policy.getSEinfo();
            return policy.passedPolicyChecks(pkg);
        }

        // If we get here it's because this package had no policy
        return false;
    }

private PackageParser.Package scanPackageLI(PackageParser.Package pkg,
int parseFlags, int scanMode, long currentTime) {
File scanFile = new File(pkg.mScanPath);
//Synthetic comment -- @@ -3451,6 +3897,15 @@
}
mScanningPath = scanFile;

        // Verify requested permissions are allowed by policy.
        if (isMacPolicyEnabled && !passInstallPolicyChecks(pkg) &&
                SystemProperties.getBoolean("persist.mac_enforcing_mode", false)) {
            Slog.w(TAG, "Installing application package " + pkg.packageName
                   + " failed due to policy.");
            mLastScanError = PackageManager.INSTALL_FAILED_POLICY_REJECTED_PERMISSION;
            return null;
        }

if ((parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
pkg.applicationInfo.flags |= ApplicationInfo.FLAG_SYSTEM;
}







