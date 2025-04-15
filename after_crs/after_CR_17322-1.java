/*Fix target loading when sdk.properties is not present.

Older (obsolete) targets would fail to load if
sdk.properties was not present. Since the content
is optional anyway, it's ok to load the target.

Change-Id:I6539be1450e72096de7b651e1c9137b41060e315*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index ad9e390..32c5838 100644

//Synthetic comment -- @@ -335,8 +335,10 @@

// Ant properties
FileWrapper sdkPropFile = new FileWrapper(platformFolder, SdkConstants.FN_SDK_PROP);
                Map<String, String> antProp = null;
                if (sdkPropFile.isFile()) { // obsolete platforms don't have this.
                    antProp = ProjectProperties.parsePropertyFile(sdkPropFile, log);
                }

if (antProp != null) {
map.putAll(antProp);







