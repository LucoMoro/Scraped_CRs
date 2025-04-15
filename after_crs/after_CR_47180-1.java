/*Fix nullness annotation

Change-Id:Ic9317e88e94dc13f1c11f49a5300349e28f3c747*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index cd99388..eae4eea 100644

//Synthetic comment -- @@ -1564,7 +1564,7 @@
private static Map<String, String> parseIniFileImpl(
@NonNull IAbstractFile propFile,
@Nullable ILogger log,
            @Nullable Charset charset) {

BufferedReader reader = null;
try {







