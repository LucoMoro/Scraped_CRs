/*Fix ADT after sdkuilib->sdklib reorg

Change-Id:I92a353806e942227af1f49df8de5dd976766f567*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/SdkManagerAction.java
//Synthetic comment -- index 4155989..71c8263 100644

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;








