/*Fix NPE during startup when battery is low

During startup the mConfiguration in the ActivityThread for the
system_server process is null until the CONFIGURATION_CHANGED
message is received. This message is sent by the
WindowManagerService.PolicyThread (and later another one is sent
by the SystemServer.ServerThread). If the PolicyThread happens to be
scheduled a bit later for some reason the LAUNCH_ACTIVITY message
for the ShutdownActivity will be received first, causing a NPE in
performLaunchActivity when executing new Configuration(mConfiguration);

Change-Id:I004b02038ba0e55ded399b3b2fe5b74ad0a76c78*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 0c761fc..6f6f640 100644

//Synthetic comment -- @@ -155,7 +155,7 @@
= new HashMap<IBinder, Service>();
AppBindData mBoundApplication;
Profiler mProfiler;
    Configuration mConfiguration;
Configuration mCompatConfiguration;
Configuration mResConfiguration;
CompatibilityInfo mResCompatibilityInfo;
//Synthetic comment -- @@ -3565,9 +3565,6 @@

applyConfigurationToResourcesLocked(config, compat);

            if (mConfiguration == null) {
                mConfiguration = new Configuration();
            }
if (!mConfiguration.isOtherSeqNewer(config) && compat == null) {
return;
}







