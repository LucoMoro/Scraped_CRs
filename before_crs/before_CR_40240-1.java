/*SDK Manager: allow user URLs to point to sys-img.xml too.

The "user defined sites" code originally only dealt with add-on XML sources.
Now we'd like it to deal with system-image sources too, but we
don't know which kind of object it is (at least not without
trying to fetch it.) As a temporary workaround, just take a
guess based on the leaf URI name.

However ideally what we should simply do is add a checkbox "is
system-image XML" in the user dialog and pass this info down
here. Another alternative is to make a "dynamic" source object
that tries to guess its type once the URI has been fetched.

Change-Id:I019d1b0dee56a587bc917c9141ca4bd052e55876*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/sources/SdkSources.java
//Synthetic comment -- index b1354c3..619d7b2 100755

//Synthetic comment -- @@ -287,7 +287,21 @@
for (int i = 0; i < count; i++) {
String url = props.getProperty(String.format("%s%02d", KEY_SRC, i));  //$NON-NLS-1$
if (url != null) {
                            SdkSource s = new SdkAddonSource(url, null/*uiName*/);
if (!hasSourceUrl(s)) {
add(SdkSourceCategory.USER_ADDONS, s);
}







