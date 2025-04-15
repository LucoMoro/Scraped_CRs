/*SdkManager: suggest which platform to install to fix a broken addon.

The SDK Manager now has the notion of a "broken installed package".
The BrokenPackage can specify that:
- it requires a certain minimal platform to be installed,
and/or:
- it requires a specific exact platform to be installed.

The later constraint is expressed by IExactApiLevelDependency and
allows UpdaterLogic to find which platform would fix an addon which
is missing its base platform.

Change-Id:If429ea39f0ddc19c0cb906bf6766df310de28981*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/AddonPackage.java
//Synthetic comment -- index 8226a60..ab27561 100755

//Synthetic comment -- @@ -38,7 +38,7 @@
* Represents an add-on XML node in an SDK repository.
*/
public class AddonPackage extends Package
    implements IPackageVersion, IPlatformDependency {

private static final String PROP_NAME      = "Addon.Name";      //$NON-NLS-1$
private static final String PROP_VENDOR    = "Addon.Vendor";    //$NON-NLS-1$
//Synthetic comment -- @@ -157,15 +157,22 @@
shortDesc,
error);

        int minApiLevel = IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED;

try {
            minApiLevel = Integer.parseInt(api);
} catch(NumberFormatException e) {
// ignore
}

        return new BrokenPackage(null/*props*/, shortDesc, longDesc, minApiLevel, archiveOsPath);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/BrokenPackage.java
//Synthetic comment -- index 646172d..1629045 100755

//Synthetic comment -- @@ -27,14 +27,21 @@
* Represents an SDK repository package that is incomplete.
* It has a distinct icon and a specific error that is supposed to help the user on how to fix it.
*/
public class BrokenPackage extends Package implements IMinApiLevelDependency {

/**
     * The minimal API level required by this extra package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
private final int mMinApiLevel;

private final String mShortDescription;

/**
//Synthetic comment -- @@ -48,6 +55,7 @@
String shortDescription,
String longDescription,
int minApiLevel,
String archiveOsPath) {
super(  null,                                   //source
props,                                  //properties
//Synthetic comment -- @@ -61,6 +69,7 @@
);
mShortDescription = shortDescription;
mMinApiLevel = minApiLevel;
}

/**
//Synthetic comment -- @@ -75,13 +84,21 @@
}

/**
     * Returns the minimal API level required by this extra package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
public int getMinApiLevel() {
return mMinApiLevel;
}

/** Returns a short description for an {@link IDescription}. */
@Override
public String getShortDescription() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/ExtraPackage.java
//Synthetic comment -- index cac622d..9c94800 100755

//Synthetic comment -- @@ -113,7 +113,9 @@
ep.getPath());

BrokenPackage ba = new BrokenPackage(props, shortDesc, longDesc,
                    IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED, archiveOsPath);
return ba;
}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IExactApiLevelDependency.java
new file mode 100755
//Synthetic comment -- index 0000000..c612592

//Synthetic comment -- @@ -0,0 +1,49 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IMinApiLevelDependency.java
//Synthetic comment -- index 14e6744..e23f3b6 100755

//Synthetic comment -- @@ -34,7 +34,7 @@
public static final int MIN_API_LEVEL_NOT_SPECIFIED = 0;

/**
     * Returns the minimal API level required by this extra package, if > 0,
* or {@link #MIN_API_LEVEL_NOT_SPECIFIED} if there is no such requirement.
*/
public abstract int getMinApiLevel();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformToolPackage.java
//Synthetic comment -- index 7b0494f..c31325f 100755

//Synthetic comment -- @@ -111,7 +111,9 @@
error);

BrokenPackage ba = new BrokenPackage(props, shortDesc, longDesc,
                    IMinApiLevelDependency.MIN_API_LEVEL_NOT_SPECIFIED, archiveOsPath);
return ba;
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/BrokenPackageTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/BrokenPackageTest.java
new file mode 100755
//Synthetic comment -- index 0000000..3e9ba8d

//Synthetic comment -- @@ -0,0 +1,54 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockBrokenPackage.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/internal/repository/MockBrokenPackage.java
new file mode 100755
//Synthetic comment -- index 0000000..289305b

//Synthetic comment -- @@ -0,0 +1,43 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index f57213c..505a613 100755

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.DocPackage;
import com.android.sdklib.internal.repository.ExtraPackage;
import com.android.sdklib.internal.repository.IMinApiLevelDependency;
import com.android.sdklib.internal.repository.IMinPlatformToolsDependency;
import com.android.sdklib.internal.repository.IMinToolsDependency;
//Synthetic comment -- @@ -32,9 +33,9 @@
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.internal.repository.PlatformPackage;
import com.android.sdklib.internal.repository.PlatformToolPackage;
import com.android.sdklib.internal.repository.SdkSource;
import com.android.sdklib.internal.repository.SdkSources;
import com.android.sdklib.internal.repository.SamplePackage;
import com.android.sdklib.internal.repository.ToolPackage;
import com.android.sdklib.internal.repository.Package.UpdateInfo;

//Synthetic comment -- @@ -501,6 +502,21 @@
}
}

if (list.size() > 0) {
return list.toArray(new ArchiveInfo[list.size()]);
}
//Synthetic comment -- @@ -864,7 +880,7 @@

int api = pkg.getMinApiLevel();

        if (api == ExtraPackage.MIN_API_LEVEL_NOT_SPECIFIED) {
return null;
}

//Synthetic comment -- @@ -963,6 +979,104 @@
}

/**
* Fetch all remote packages only if really needed.
* <p/>
* This method takes a list of sources. Each source is only fetched once -- that is each








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/UpdaterLogicTest.java
//Synthetic comment -- index 94fac06..dc65e14 100755

//Synthetic comment -- @@ -22,6 +22,7 @@
import com.android.sdklib.internal.repository.Archive;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.MockAddonPackage;
import com.android.sdklib.internal.repository.MockPlatformPackage;
import com.android.sdklib.internal.repository.MockPlatformToolPackage;
import com.android.sdklib.internal.repository.MockToolPackage;
//Synthetic comment -- @@ -132,6 +133,54 @@
}

/**
* Platform packages depend on a tool package.
* This tests checks that UpdaterLogic.findToolsDependency() can find a base
* tool package for a given platform package.







