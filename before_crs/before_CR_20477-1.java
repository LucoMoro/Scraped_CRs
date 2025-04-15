/*Merge "Fix in SdkSources."

For User Add-ons, the counter used to save the urls was starting at one
while the counter used to load them was starting at zero.

Now they all start at zero.

Change-Id:Iff5204efe34d69ee8bcc3a4f11c8cc0de4ea7c29*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSources.java
//Synthetic comment -- index c2ad057..22678b3 100755

//Synthetic comment -- @@ -275,8 +275,8 @@

int count = 0;
for (SdkSource s : getSources(SdkSourceCategory.USER_ADDONS)) {
                count++;
props.setProperty(String.format("%s%02d", KEY_SRC, count), s.getUrl());  //$NON-NLS-1$
}
props.setProperty(KEY_COUNT, Integer.toString(count));








