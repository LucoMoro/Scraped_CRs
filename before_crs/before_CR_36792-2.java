/*in procedure of ConfigurationChange, the configuartion sometimes changes while Activity/Window/ViewRoot processing it. The reason is because of there is only one ActivityThread in Client process, or in System process, while ActivityManagerService
send notify via scheduleConfigurationChange through ActivityThread, it use the config, instead a copy of it to notify everyone, post many times in message queue, with many ViewRootImpl's handler and then be handled in different thread if
this occured in system_server's process. so when some one processing configuration change in it thread, while not finished, and swith to other thread, which will
update the very same config, so the original one will find the value changed when it is swith back, and then end of many error layouts.
fix:
copy the config and push then in to message queue, instead of push the original config into message queue directly
in ActivityThread*/
//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java b/core/java/android/app/ActivityThread.java
//Synthetic comment -- index 7242029..5f69841 100644

//Synthetic comment -- @@ -4680,9 +4680,9 @@
// everyone about it.
if (mPendingConfiguration == null ||
mPendingConfiguration.isOtherSeqNewer(newConfig)) {
                            mPendingConfiguration = newConfig;

                            queueOrSendMessage(H.CONFIGURATION_CHANGED, newConfig);
}
}
}








//Synthetic comment -- diff --git a/core/java/android/app/ActivityThread.java~ b/core/java/android/app/ActivityThread.java~
new file mode 100644
//Synthetic comment -- index 0000000..0c761fc

//Synthetic comment -- @@ -0,0 +1,4428 @@







