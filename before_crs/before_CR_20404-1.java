/*Fix MockLog to build on 1.5

Change-Id:Icbdbbae2fe105af6eb1cdb1490a2415007262980*/
//Synthetic comment -- diff --git a/sdkmanager/app/tests/com/android/sdkmanager/MockLog.java b/sdkmanager/app/tests/com/android/sdkmanager/MockLog.java
//Synthetic comment -- index a8ca90a..7925709 100644

//Synthetic comment -- @@ -28,17 +28,14 @@
mMessages.add(new Formatter().format(code + format, args).toString());
}

    @Override
public void warning(String format, Object... args) {
add("W ", format, args);
}

    @Override
public void printf(String format, Object... args) {
add("P ", format, args);
}

    @Override
public void error(Throwable t, String format, Object... args) {
if (t != null) {
add("T", "%s", t.toString());







