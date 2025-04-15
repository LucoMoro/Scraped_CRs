/*Updated DeviceConfig to generate button information

Change-Id:I7c7536ad871135dd39672dc4164a4f5970028f38*/
//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index d5573c4..22d9ee4 100644

//Synthetic comment -- @@ -51,10 +51,12 @@
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class ConfigGenerator {
//Synthetic comment -- @@ -486,10 +488,15 @@
ram.setAttribute(ATTR_UNIT, unit);
ram.appendChild(doc.createTextNode(Long.toString(ramAmount)));

            // Can't actually get whether we're using software buttons
Element buttons = doc.createElement(PREFIX + NODE_BUTTONS);
hardware.appendChild(buttons);
            buttons.appendChild(doc.createTextNode(" "));

Element internalStorage = doc.createElement(PREFIX + NODE_INTERNAL_STORAGE);
hardware.appendChild(internalStorage);
//Synthetic comment -- @@ -645,6 +652,17 @@
return cList;
}

private void error(String err, Throwable e) {
Toast.makeText(mCtx, "Error Generating Configuration", Toast.LENGTH_SHORT).show();
Log.e(TAG, err);







