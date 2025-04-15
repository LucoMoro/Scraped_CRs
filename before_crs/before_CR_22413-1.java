/*Fix duplicate dependencies reported by SDK installer.

The issue was simply that when finding dependencies
we have several rules that overlap and we just need
to take the union of what we find. For example an
add-on needs to comply with both the IPlatformDependency
and the IExactApiLevelDependency, which is why it was
erroneously listed twice.

SDK Bug:http://code.google.com/p/android/issues/detail?id=16239Change-Id:Idb0895a0d2aa063d0a3a7f5d6a2c9ddd6959e0f6*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterLogic.java
//Synthetic comment -- index aa2be48..c2472d8 100755

//Synthetic comment -- @@ -40,9 +40,12 @@
import com.android.sdklib.internal.repository.Package.UpdateInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
* The logic to compute which packages to install, based on the choices
//Synthetic comment -- @@ -476,7 +479,7 @@
// - platform: *might* depends on tools of rev >= min-tools-rev
// - extra: *might* depends on platform with api >= min-api-level

        ArrayList<ArchiveInfo> list = new ArrayList<ArchiveInfo>();

if (pkg instanceof IPlatformDependency) {
ArchiveInfo ai = findPlatformDependency(
//Synthetic comment -- @@ -488,7 +491,7 @@
localArchives);

if (ai != null) {
                list.add(ai);
}
}

//Synthetic comment -- @@ -503,7 +506,7 @@
localArchives);

if (ai != null) {
                list.add(ai);
}
}

//Synthetic comment -- @@ -518,7 +521,7 @@
localArchives);

if (ai != null) {
                list.add(ai);
}
}

//Synthetic comment -- @@ -533,7 +536,7 @@
localArchives);

if (ai != null) {
                list.add(ai);
}
}

//Synthetic comment -- @@ -548,12 +551,14 @@
localArchives);

if (ai != null) {
                list.add(ai);
}
}

        if (list.size() > 0) {
            return list.toArray(new ArchiveInfo[list.size()]);
}

return null;







