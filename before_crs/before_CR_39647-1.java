/*Updated DeviceConfig to generate button information

Change-Id:I7c7536ad871135dd39672dc4164a4f5970028f38Conflicts:

	apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java*/
//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index e8d6333..bc2ca7f 100644

//Synthetic comment -- @@ -488,10 +488,15 @@
ram.setAttribute(ATTR_UNIT, unit);
ram.appendChild(doc.createTextNode(Long.toString(ramAmount)));

            // Can't actually get whether we're using software buttons
Element buttons = doc.createElement(PREFIX + NODE_BUTTONS);
hardware.appendChild(buttons);
            buttons.appendChild(doc.createTextNode(" "));

Element internalStorage = doc.createElement(PREFIX + NODE_INTERNAL_STORAGE);
hardware.appendChild(internalStorage);
//Synthetic comment -- @@ -647,6 +652,17 @@
return cList;
}

private void error(String err, Throwable e) {
Toast.makeText(mCtx, "Error Generating Configuration", Toast.LENGTH_SHORT).show();
Log.e(TAG, err);







