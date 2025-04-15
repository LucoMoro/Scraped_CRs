/*Add seinfo parsing to PackageManagerService.

This patch set allows the PMS to parse the
mac_permissions.xml file which contains the
seinfo values. Each package that is installed
on the device will be assigned an seinfo value
based on policy. This value will help label the
app process and data directory.

You will need to checkout the project:

git.selinuxproject.org/~seandroid/selinux/mac-policy

There is also a dependency on:Ief91d6a717741c91c5ba8745452bb247dc8048ec(includes the mac_permissions.xml with each build)

andI0b4950a4f9e23b2f9f8c848acf0e81e44d580cca(ensures that ApplicationInfo contains seinfo variable)

Change-Id:I61ad1ea12fb6a9a6d0b108ec163bc4bf4c954b58Signed-off-by: rpcraig <rpcraig@tycho.ncsc.mil>*/




//Synthetic comment -- diff --git a/services/java/com/android/server/pm/PackageManagerService.java b/services/java/com/android/server/pm/PackageManagerService.java
//Synthetic comment -- index 3501e47..747b1a3 100644

//Synthetic comment -- @@ -321,6 +321,15 @@
// Temporary for building the final shared libraries for an .apk.
String[] mTmpSharedLibraries = null;

    // Signature based seinfo values read from policy.
    final HashMap<Signature, String> mSigSeinfo = new HashMap<Signature, String>();

    // Package name seinfo values read from policy.
    final HashMap<String, String> mPackageSeinfo = new HashMap<String, String>();

    // If mac_permissions.xml was found.
    boolean mFoundPolicyFile;

// These are the features this devices supports that were read from the
// etc/permissions.xml file.
final HashMap<String, FeatureInfo> mAvailableFeatures =
//Synthetic comment -- @@ -1042,6 +1051,13 @@
}
}

            // Find install policy
            long startPolicyTime = SystemClock.uptimeMillis();
            mFoundPolicyFile = readPolicy();
            Slog.i(TAG, "Time to scan policy: "
                   + ((SystemClock.uptimeMillis()-startPolicyTime)/1000f)
                   + " seconds");

// Find base frameworks (resource packages without code).
mFrameworkInstallObserver = new AppDirObserver(
mFrameworkDir.getPath(), OBSERVER_EVENTS, true);
//Synthetic comment -- @@ -1317,6 +1333,128 @@
mSettings.removePackageLPw(ps.name);
}

    boolean readPolicy() {

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
            Slog.d(TAG, "No mac_permissions.xml policy file. All seinfo values will be null.");
            return false;
        }

        Slog.d(TAG, "Using policy file " + policyFileLocations[i].getPath());

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
                    if (cert == null) {
                        Slog.w(TAG, "<signer> without signature at "
                               + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                    Signature signature;
                    try {
                        signature = new Signature(cert);
                    } catch (IllegalArgumentException e) {
                        Slog.w(TAG, "<signer> with bad signature at "
                               + parser.getPositionDescription(), e);
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                    String seinfo = readSeinfo(parser);
                    if (seinfo != null) {
                        mSigSeinfo.put(signature, seinfo);
                    }
                } else if ("default".equals(tagName)) {
                    String seinfo = readSeinfo(parser);
                    if (seinfo != null) {
                        // the 'null' signature is the default seinfo value
                        mSigSeinfo.put(null, seinfo);
                    }
                } else if ("package".equals(tagName)) {
                    String pkgName = parser.getAttributeValue(null, "name");
                    if (pkgName == null) {
                        Slog.w(TAG, "<package> without name at "
                               + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                        continue;
                    }
                    String seinfo = readSeinfo(parser);
                    if (seinfo != null) {
                        mPackageSeinfo.put(pkgName, seinfo);
                    }
                } else {
                    XmlUtils.skipCurrentTag(parser);
                    continue;
                }
            }
            policyFile.close();
        } catch (XmlPullParserException e) {
            Slog.w(TAG, "Got execption parsing ", e);
        } catch (IOException e) {
            Slog.w(TAG, "Got execption parsing ", e);
        }
        return true;
    }

    String readSeinfo(XmlPullParser parser) throws IOException, XmlPullParserException {

        int type;
        int outerDepth = parser.getDepth();
        String seinfo = null;
        while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
               && (type != XmlPullParser.END_TAG
                   || parser.getDepth() > outerDepth)) {
            if (type == XmlPullParser.END_TAG
                || type == XmlPullParser.TEXT) {
                continue;
            }

            String tagName = parser.getName();
            if ("seinfo".equals(tagName)) {
                String seinfoValue = parser.getAttributeValue(null, "value");
                if (seinfoValue != null) {
                    seinfo = seinfoValue;
                } else {
                    Slog.w(TAG, "<seinfo> without value at "
                           + parser.getPositionDescription());
                }
            }
            XmlUtils.skipCurrentTag(parser);
        }
        return seinfo;
    }

void readPermissions() {
// Read permissions from .../etc/permission directory.
File libraryDir = new File(Environment.getRootDirectory(), "etc/permissions");
//Synthetic comment -- @@ -3439,6 +3577,31 @@
}
}

    private void assignSeinfoValue(PackageParser.Package pkg) {

        // We just want one of the signatures to match.
        for (Signature s : pkg.mSignatures) {
            if (s == null) {
                continue;
            }

            if (mSigSeinfo.containsKey(s)) {
                pkg.applicationInfo.seinfo = mSigSeinfo.get(s);
                return;
            }
        }

        // Check for seinfo labeled by package.
        if (mPackageSeinfo.containsKey(pkg.packageName)) {
            pkg.applicationInfo.seinfo = mPackageSeinfo.get(pkg.packageName);
            return;
        }

        // If we have a default seinfo value then great, otherwise
        // we set a null object and that is what we started with.
        pkg.applicationInfo.seinfo = mSigSeinfo.get(null);
    }

private PackageParser.Package scanPackageLI(PackageParser.Package pkg,
int parseFlags, int scanMode, long currentTime) {
File scanFile = new File(pkg.mScanPath);
//Synthetic comment -- @@ -3451,6 +3614,10 @@
}
mScanningPath = scanFile;

        if (mFoundPolicyFile) {
            assignSeinfoValue(pkg);
        }

if ((parseFlags&PackageParser.PARSE_IS_SYSTEM) != 0) {
pkg.applicationInfo.flags |= ApplicationInfo.FLAG_SYSTEM;
}







