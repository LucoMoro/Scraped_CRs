/*Add missing features of JELLY_BEAN and JELLY_BEAN_MR1 to feature summary list

Change-Id:I53f01b14c7247c8f68f6cd4e6cdd4bf64d32ea75Signed-off-by: Leo Liao <leo_liao@asus.com>*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index 3ea9451..ca3c2a9 100644

//Synthetic comment -- @@ -168,6 +168,14 @@
new Feature(PackageManager.FEATURE_WIFI_DIRECT, false),
};

    public static final Feature[] ALL_JELLY_BEAN_FEATURES = {
            new Feature(PackageManager.FEATURE_TELEVISION, false),
    };

    public static final Feature[] ALL_JELLY_BEAN_MR1_FEATURES = {
            new Feature(PackageManager.FEATURE_CAMERA_ANY, false),
    };

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -198,6 +206,12 @@

// add features from latest to last so that the latest requirements are put in the set first
int apiVersion = Build.VERSION.SDK_INT;
        if (apiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Collections.addAll(features, ALL_JELLY_BEAN_MR1_FEATURES);
        }
        if (apiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            Collections.addAll(features, ALL_JELLY_BEAN_FEATURES);
        }
if (apiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
Collections.addAll(features, ALL_ICE_CREAM_SANDWICH_FEATURES);
}







