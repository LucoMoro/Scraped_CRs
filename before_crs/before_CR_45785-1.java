/*Update Feature Summary CtsVerifier test for JB

Adds the new FEATURE_TELEVISION so devices with this feature
declared do not see a warning in the Feature Summary test.

Change-Id:Ie2de82b9a362364c07727ff7c4c1ea28d3bcefeaSigned-off-by: Lawrence Mok <lawrencem@gmail.com>*/
//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index 3ea9451..afbf97e 100644

//Synthetic comment -- @@ -168,6 +168,10 @@
new Feature(PackageManager.FEATURE_WIFI_DIRECT, false),
};

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -198,6 +202,9 @@

// add features from latest to last so that the latest requirements are put in the set first
int apiVersion = Build.VERSION.SDK_INT;
if (apiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
Collections.addAll(features, ALL_ICE_CREAM_SANDWICH_FEATURES);
}







