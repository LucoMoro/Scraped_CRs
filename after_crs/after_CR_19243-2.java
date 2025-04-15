/*Fix crash by "adb shell pm list permissions -f|-s"

pm command makes the assumption that every permission should
contain android:description and android:label attributes while
they are not mandatory. If a permission does not contain these
two attributes, we get "android.content.res.
Resources$NotFoundException: String resource ID #0x0"
followed by a NPE when using -f or -s options.

With the following change, users will get "null" in output for
respective fields.

Change-Id:I4e7f407592fa071abdab1d979775f46ec27dc9d2*/




//Synthetic comment -- diff --git a/cmds/pm/src/com/android/commands/pm/Pm.java b/cmds/pm/src/com/android/commands/pm/Pm.java
//Synthetic comment -- index 9b8b0ac..46fa114 100644

//Synthetic comment -- @@ -341,9 +341,11 @@
if (nonLocalized != null) {
return nonLocalized.toString();
}
        if (res != 0) {
            Resources r = getResources(pii);
            if (r != null) {
                return r.getString(res);
            }
}
return null;
}







