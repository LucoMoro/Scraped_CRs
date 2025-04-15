/*Improve sending web link with Bluetooth

When sending web link from Google maps app, link cannot be used by
receiver as maps app will send extra text in addition to web link.

Improve the creation of html so that only the web link is
interpreted as a link.

Code is partly copied from MessageView class in the email app.

Change-Id:Idb093cc3d5af5fa6b85649d194fa6dc0c6fdfd1e*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java b/src/com/android/bluetooth/opp/BluetoothOppLauncherActivity.java
//Synthetic comment -- index 8f2b910..bde0030 100644

//Synthetic comment -- @@ -51,6 +51,11 @@
import android.util.Log;
import android.provider.Settings;

/**
* This class is designed to act as the entry point of handling the share intent
* via BT from other APPs. and also make "Bluetooth" available in sharing method
//Synthetic comment -- @@ -61,6 +66,10 @@
private static final boolean D = Constants.DEBUG;
private static final boolean V = Constants.VERBOSE;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//Synthetic comment -- @@ -211,11 +220,58 @@
String fileName = getString(R.string.bluetooth_share_file_name) + ".html";
context.deleteFile(fileName);

            String uri = shareContent.toString();
            String content = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;"
                + " charset=UTF-8\"/></head><body>" + "<a href=\"" + uri + "\">" + uri + "</a></p>"
                + "</body></html>";
            byte[] byteBuff = content.getBytes();

outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
if (outStream != null) {
//Synthetic comment -- @@ -244,4 +300,44 @@
}
return fileUri;
}
}







