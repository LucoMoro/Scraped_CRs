/*Updated DeviceConfig to generate button information

Change-Id:I7c7536ad871135dd39672dc4164a4f5970028f38Conflicts:

	apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java*/




//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index e8d6333..bc2ca7f 100644

//Synthetic comment -- @@ -488,10 +488,15 @@
ram.setAttribute(ATTR_UNIT, unit);
ram.appendChild(doc.createTextNode(Long.toString(ramAmount)));

Element buttons = doc.createElement(PREFIX + NODE_BUTTONS);
hardware.appendChild(buttons);
            Text buttonsText;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                buttonsText = doc.createTextNode(getButtonsType());
            } else {
                buttonsText = doc.createTextNode("hard");
            }
            buttons.appendChild(buttonsText);

Element internalStorage = doc.createElement(PREFIX + NODE_INTERNAL_STORAGE);
hardware.appendChild(internalStorage);
//Synthetic comment -- @@ -647,6 +652,17 @@
return cList;
}

    @TargetApi(14)
    private String getButtonsType() {
        ViewConfiguration vConfig = ViewConfiguration.get(mCtx);

        if (vConfig.hasPermanentMenuKey()) {
            return "hard";
        } else {
            return "soft";
        }
    }

private void error(String err, Throwable e) {
Toast.makeText(mCtx, "Error Generating Configuration", Toast.LENGTH_SHORT).show();
Log.e(TAG, err);







