/*39939: Cannot use 4.0" WVGA, 4.65" 720p device definitions. DO NOT MERGE

Change-Id:I03c0f02e22496dc898691a706b64f9a5c989e809*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdCreationDialog.java
//Synthetic comment -- index 5d2a264..b65b528 100644

//Synthetic comment -- @@ -468,10 +468,8 @@
List<Device> nexus = new ArrayList<Device>(deviceList.size());
List<Device> other = new ArrayList<Device>(deviceList.size());
for (Device device : deviceList) {
                if (isNexus(device)) {
                    if (!isGeneric(device)) { // Filter out repeated definitions
                        nexus.add(device);
                    }
} else {
other.add(device);
}
//Synthetic comment -- @@ -491,7 +489,7 @@
String[] labels = new String[devices.length];
for (int i = 0, n = devices.length; i < n; i++) {
Device device = devices[i];
                if (isNexus(device)) {
labels[i] = getNexusLabel(device);
} else {
labels[i] = getGenericLabel(device);







