/*Clear preferred activities when home process crashes

If the "default" Home application has been replaced with
a third-party app that is repeatedly crashing at start-up,
there is no way for the user to clear the preferred activities
or uninstall the bad application. If we clear the package
preferred activities when the application crashes, the user
will be prompted with the ResolverActivity at the next boot
and can try using the app again or choose to use another
Home application.

Change-Id:I8ba8e95e6752916d50515d96c117d3084fa980fd*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 9e9552a..0aa36d1 100644

//Synthetic comment -- @@ -8798,7 +8798,28 @@
sr.crashCount++;
}
}
        
mProcessCrashTimes.put(app.info.processName, app.info.uid, now);
return true;
}







