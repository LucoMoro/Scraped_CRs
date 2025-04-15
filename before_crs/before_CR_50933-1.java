/*Refactoring: move SDK Updater core code into sdklib.

This requires the 2 following changes:
in tools/base.git:I507a2bebe348fae598bc6e6fe24af3c5bf78acf0in tools/ swt.git:I97c5874e6b5dcb5d6c0ca25ca921a291c6330fccChange-Id:I79742d366b176cee2443bbed1f96dc253e6c74bb*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 8f58612..0ef18b8 100644

//Synthetic comment -- @@ -40,9 +40,9 @@
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.repository.SdkAddonConstants;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.SdkUpdaterNoWindow;
import com.android.sdkuilib.internal.widgets.MessageBoxLog;
import com.android.sdkuilib.repository.AvdManagerWindow;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
//Synthetic comment -- @@ -1512,6 +1512,7 @@
/**
* Reads a line from the input stream, masking it as much as possible.
*/
private String promptPassword(String prompt) throws IOException {

// Setup a thread that tries to overwrite any input by







