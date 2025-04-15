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
//Synthetic comment -- index 78df780..ea198a0 100644

//Synthetic comment -- @@ -147,7 +147,8 @@
final HashMap<IBinder, Service> mServices
= new HashMap<IBinder, Service>();
AppBindData mBoundApplication;
    Configuration mConfiguration;
Configuration mResConfiguration;
Application mInitialApplication;
final ArrayList<Application> mAllApplications
//Synthetic comment -- @@ -2981,9 +2982,6 @@

applyConfigurationToResourcesLocked(config);

            if (mConfiguration == null) {
                mConfiguration = new Configuration();
            }
if (!mConfiguration.isOtherSeqNewer(config)) {
return;
}







