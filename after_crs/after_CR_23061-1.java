/*Improvements in ActivityManagerNative.onTransact().

- When onTransact() is called for non-user transaction, that
  huge switch-case lookup is skipped entirely.
- Repetitive first line from each switch-case branch is moved
  before switch.

Change-Id:I27a23040d15b87e04171cf08d24f79403a1128c9*/




//Synthetic comment -- diff --git a/core/java/android/app/ActivityManagerNative.java b/core/java/android/app/ActivityManagerNative.java
//Synthetic comment -- index d6231ea..e6455ed 100644

//Synthetic comment -- @@ -124,1188 +124,1074 @@

public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
throws RemoteException {
        if (code <= LAST_CALL_TRANSACTION) {
data.enforceInterface(IActivityManager.descriptor);
            switch (code) {
                case START_ACTIVITY_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    Uri[] grantedUriPermissions = data.createTypedArray(Uri.CREATOR);
                    int grantedMode = data.readInt();
                    IBinder resultTo = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    boolean onlyIfNeeded = data.readInt() != 0;
                    boolean debug = data.readInt() != 0;
                    int result = startActivity(app, intent, resolvedType,
                            grantedUriPermissions, grantedMode, resultTo, resultWho,
                            requestCode, onlyIfNeeded, debug);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }

                case START_ACTIVITY_AND_WAIT_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    Uri[] grantedUriPermissions = data.createTypedArray(Uri.CREATOR);
                    int grantedMode = data.readInt();
                    IBinder resultTo = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    boolean onlyIfNeeded = data.readInt() != 0;
                    boolean debug = data.readInt() != 0;
                    WaitResult result = startActivityAndWait(app, intent, resolvedType,
                            grantedUriPermissions, grantedMode, resultTo, resultWho,
                            requestCode, onlyIfNeeded, debug);
                    reply.writeNoException();
                    result.writeToParcel(reply, 0);
                    return true;
                }

                case START_ACTIVITY_WITH_CONFIG_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    Uri[] grantedUriPermissions = data.createTypedArray(Uri.CREATOR);
                    int grantedMode = data.readInt();
                    IBinder resultTo = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    boolean onlyIfNeeded = data.readInt() != 0;
                    boolean debug = data.readInt() != 0;
                    Configuration config = Configuration.CREATOR.createFromParcel(data);
                    int result = startActivityWithConfig(app, intent, resolvedType,
                            grantedUriPermissions, grantedMode, resultTo, resultWho,
                            requestCode, onlyIfNeeded, debug, config);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }

                case START_ACTIVITY_INTENT_SENDER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    IntentSender intent = IntentSender.CREATOR.createFromParcel(data);
                    Intent fillInIntent = null;
                    if (data.readInt() != 0) {
                        fillInIntent = Intent.CREATOR.createFromParcel(data);
                    }
                    String resolvedType = data.readString();
                    IBinder resultTo = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int flagsMask = data.readInt();
                    int flagsValues = data.readInt();
                    int result = startActivityIntentSender(app, intent,
                            fillInIntent, resolvedType, resultTo, resultWho,
                            requestCode, flagsMask, flagsValues);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }

                case START_NEXT_MATCHING_ACTIVITY_TRANSACTION: {
                    IBinder callingActivity = data.readStrongBinder();
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    boolean result = startNextMatchingActivity(callingActivity, intent);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }

                case FINISH_ACTIVITY_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Intent resultData = null;
                    int resultCode = data.readInt();
                    if (data.readInt() != 0) {
                        resultData = Intent.CREATOR.createFromParcel(data);
                    }
                    boolean res = finishActivity(token, resultCode, resultData);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case FINISH_SUB_ACTIVITY_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    finishSubActivity(token, resultWho, requestCode);
                    reply.writeNoException();
                    return true;
                }

                case WILL_ACTIVITY_BE_VISIBLE_TRANSACTION: {
                    data.enforceInterface(IActivityManager.descriptor);
                    IBinder token = data.readStrongBinder();
                    boolean res = willActivityBeVisible(token);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case REGISTER_RECEIVER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app =
                            b != null ? ApplicationThreadNative.asInterface(b) : null;
                    b = data.readStrongBinder();
                    IIntentReceiver rec
                            = b != null ? IIntentReceiver.Stub.asInterface(b) : null;
                    IntentFilter filter = IntentFilter.CREATOR.createFromParcel(data);
                    String perm = data.readString();
                    Intent intent = registerReceiver(app, rec, filter, perm);
                    reply.writeNoException();
                    if (intent != null) {
                        reply.writeInt(1);
                        intent.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }

                case UNREGISTER_RECEIVER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    if (b == null) {
                        return true;
                    }
                    IIntentReceiver rec = IIntentReceiver.Stub.asInterface(b);
                    unregisterReceiver(rec);
                    reply.writeNoException();
                    return true;
                }

                case BROADCAST_INTENT_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app =
                            b != null ? ApplicationThreadNative.asInterface(b) : null;
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    b = data.readStrongBinder();
                    IIntentReceiver resultTo =
                            b != null ? IIntentReceiver.Stub.asInterface(b) : null;
                    int resultCode = data.readInt();
                    String resultData = data.readString();
                    Bundle resultExtras = data.readBundle();
                    String perm = data.readString();
                    boolean serialized = data.readInt() != 0;
                    boolean sticky = data.readInt() != 0;
                    int res = broadcastIntent(app, intent, resolvedType, resultTo,
                            resultCode, resultData, resultExtras, perm,
                            serialized, sticky);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case UNBROADCAST_INTENT_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = b != null ? ApplicationThreadNative.asInterface(b) : null;
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    unbroadcastIntent(app, intent);
                    reply.writeNoException();
                    return true;
                }

                case FINISH_RECEIVER_TRANSACTION: {
                    IBinder who = data.readStrongBinder();
                    int resultCode = data.readInt();
                    String resultData = data.readString();
                    Bundle resultExtras = data.readBundle();
                    boolean resultAbort = data.readInt() != 0;
                    if (who != null) {
                        finishReceiver(who, resultCode, resultData, resultExtras, resultAbort);
                    }
                    reply.writeNoException();
                    return true;
                }

                case ATTACH_APPLICATION_TRANSACTION: {
                    IApplicationThread app = ApplicationThreadNative.asInterface(
                            data.readStrongBinder());
                    if (app != null) {
                        attachApplication(app);
                    }
                    reply.writeNoException();
                    return true;
                }

                case ACTIVITY_IDLE_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Configuration config = null;
                    if (data.readInt() != 0) {
                        config = Configuration.CREATOR.createFromParcel(data);
                    }
                    if (token != null) {
                        activityIdle(token, config);
                    }
                    reply.writeNoException();
                    return true;
                }

                case ACTIVITY_PAUSED_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Bundle map = data.readBundle();
                    activityPaused(token, map);
                    reply.writeNoException();
                    return true;
                }

                case ACTIVITY_STOPPED_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Bitmap thumbnail = data.readInt() != 0
                            ? Bitmap.CREATOR.createFromParcel(data) : null;
                    CharSequence description = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    activityStopped(token, thumbnail, description);
                    reply.writeNoException();
                    return true;
                }

                case ACTIVITY_DESTROYED_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    activityDestroyed(token);
                    reply.writeNoException();
                    return true;
                }

                case GET_CALLING_PACKAGE_TRANSACTION: {
                    data.enforceInterface(IActivityManager.descriptor);
                    IBinder token = data.readStrongBinder();
                    String res = token != null ? getCallingPackage(token) : null;
                    reply.writeNoException();
                    reply.writeString(res);
                    return true;
                }

                case GET_CALLING_ACTIVITY_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    ComponentName cn = getCallingActivity(token);
                    reply.writeNoException();
                    ComponentName.writeToParcel(cn, reply);
                    return true;
                }

                case GET_TASKS_TRANSACTION: {
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    IBinder receiverBinder = data.readStrongBinder();
                    IThumbnailReceiver receiver = receiverBinder != null
                            ? IThumbnailReceiver.Stub.asInterface(receiverBinder)
                            : null;
                    List list = getTasks(maxNum, fl, receiver);
                    reply.writeNoException();
                    int N = list != null ? list.size() : -1;
                    reply.writeInt(N);
                    int i;
                    for (i = 0; i < N; i++) {
                        ActivityManager.RunningTaskInfo info =
                                (ActivityManager.RunningTaskInfo) list.get(i);
                        info.writeToParcel(reply, 0);
                    }
                    return true;
                }

                case GET_RECENT_TASKS_TRANSACTION: {
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    List<ActivityManager.RecentTaskInfo> list = getRecentTasks(maxNum,
                            fl);
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }

                case GET_SERVICES_TRANSACTION: {
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    List list = getServices(maxNum, fl);
                    reply.writeNoException();
                    int N = list != null ? list.size() : -1;
                    reply.writeInt(N);
                    int i;
                    for (i = 0; i < N; i++) {
                        ActivityManager.RunningServiceInfo info =
                                (ActivityManager.RunningServiceInfo) list.get(i);
                        info.writeToParcel(reply, 0);
                    }
                    return true;
                }

                case GET_PROCESSES_IN_ERROR_STATE_TRANSACTION: {
                    List<ActivityManager.ProcessErrorStateInfo> list = getProcessesInErrorState();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }

                case GET_RUNNING_APP_PROCESSES_TRANSACTION: {
                    List<ActivityManager.RunningAppProcessInfo> list = getRunningAppProcesses();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }

                case GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION: {
                    List<ApplicationInfo> list = getRunningExternalApplications();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }

                case MOVE_TASK_TO_FRONT_TRANSACTION: {
                    int task = data.readInt();
                    moveTaskToFront(task);
                    reply.writeNoException();
                    return true;
                }

                case MOVE_TASK_TO_BACK_TRANSACTION: {
                    int task = data.readInt();
                    moveTaskToBack(task);
                    reply.writeNoException();
                    return true;
                }

                case MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    boolean nonRoot = data.readInt() != 0;
                    boolean res = moveActivityTaskToBack(token, nonRoot);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case MOVE_TASK_BACKWARDS_TRANSACTION: {
                    int task = data.readInt();
                    moveTaskBackwards(task);
                    reply.writeNoException();
                    return true;
                }

                case GET_TASK_FOR_ACTIVITY_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    boolean onlyRoot = data.readInt() != 0;
                    int res = token != null
                            ? getTaskForActivity(token, onlyRoot) : -1;
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case FINISH_OTHER_INSTANCES_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    ComponentName className = ComponentName.readFromParcel(data);
                    finishOtherInstances(token, className);
                    reply.writeNoException();
                    return true;
                }

                case REPORT_THUMBNAIL_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Bitmap thumbnail = data.readInt() != 0
                            ? Bitmap.CREATOR.createFromParcel(data) : null;
                    CharSequence description = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    reportThumbnail(token, thumbnail, description);
                    reply.writeNoException();
                    return true;
                }

                case GET_CONTENT_PROVIDER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    String name = data.readString();
                    ContentProviderHolder cph = getContentProvider(app, name);
                    reply.writeNoException();
                    if (cph != null) {
                        reply.writeInt(1);
                        cph.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }

                case PUBLISH_CONTENT_PROVIDERS_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    ArrayList<ContentProviderHolder> providers =
                            data.createTypedArrayList(ContentProviderHolder.CREATOR);
                    publishContentProviders(app, providers);
                    reply.writeNoException();
                    return true;
                }

                case REMOVE_CONTENT_PROVIDER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    String name = data.readString();
                    removeContentProvider(app, name);
                    reply.writeNoException();
                    return true;
                }

                case GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION: {
                    ComponentName comp = ComponentName.CREATOR.createFromParcel(data);
                    PendingIntent pi = getRunningServiceControlPanel(comp);
                    reply.writeNoException();
                    PendingIntent.writePendingIntentOrNullToParcel(pi, reply);
                    return true;
                }

                case START_SERVICE_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Intent service = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    ComponentName cn = startService(app, service, resolvedType);
                    reply.writeNoException();
                    ComponentName.writeToParcel(cn, reply);
                    return true;
                }

                case STOP_SERVICE_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Intent service = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    int res = stopService(app, service, resolvedType);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case STOP_SERVICE_TOKEN_TRANSACTION: {
                    ComponentName className = ComponentName.readFromParcel(data);
                    IBinder token = data.readStrongBinder();
                    int startId = data.readInt();
                    boolean res = stopServiceToken(className, token, startId);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case SET_SERVICE_FOREGROUND_TRANSACTION: {
                    ComponentName className = ComponentName.readFromParcel(data);
                    IBinder token = data.readStrongBinder();
                    int id = data.readInt();
                    Notification notification = null;
                    if (data.readInt() != 0) {
                        notification = Notification.CREATOR.createFromParcel(data);
                    }
                    boolean removeNotification = data.readInt() != 0;
                    setServiceForeground(className, token, id, notification, removeNotification);
                    reply.writeNoException();
                    return true;
                }

                case BIND_SERVICE_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    IBinder token = data.readStrongBinder();
                    Intent service = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    b = data.readStrongBinder();
                    int fl = data.readInt();
                    IServiceConnection conn = IServiceConnection.Stub.asInterface(b);
                    int res = bindService(app, token, service, resolvedType, conn, fl);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case UNBIND_SERVICE_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IServiceConnection conn = IServiceConnection.Stub.asInterface(b);
                    boolean res = unbindService(conn);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case PUBLISH_SERVICE_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    IBinder service = data.readStrongBinder();
                    publishService(token, intent, service);
                    reply.writeNoException();
                    return true;
                }

                case UNBIND_FINISHED_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    boolean doRebind = data.readInt() != 0;
                    unbindFinished(token, intent, doRebind);
                    reply.writeNoException();
                    return true;
                }

                case SERVICE_DONE_EXECUTING_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    int type = data.readInt();
                    int startId = data.readInt();
                    int res = data.readInt();
                    serviceDoneExecuting(token, type, startId, res);
                    reply.writeNoException();
                    return true;
                }

                case START_INSTRUMENTATION_TRANSACTION: {
                    ComponentName className = ComponentName.readFromParcel(data);
                    String profileFile = data.readString();
                    int fl = data.readInt();
                    Bundle arguments = data.readBundle();
                    IBinder b = data.readStrongBinder();
                    IInstrumentationWatcher w = IInstrumentationWatcher.Stub.asInterface(b);
                    boolean res = startInstrumentation(className, profileFile, fl, arguments, w);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }


                case FINISH_INSTRUMENTATION_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    int resultCode = data.readInt();
                    Bundle results = data.readBundle();
                    finishInstrumentation(app, resultCode, results);
                    reply.writeNoException();
                    return true;
                }

                case GET_CONFIGURATION_TRANSACTION: {
                    Configuration config = getConfiguration();
                    reply.writeNoException();
                    config.writeToParcel(reply, 0);
                    return true;
                }

                case UPDATE_CONFIGURATION_TRANSACTION: {
                    Configuration config = Configuration.CREATOR.createFromParcel(data);
                    updateConfiguration(config);
                    reply.writeNoException();
                    return true;
                }

                case SET_REQUESTED_ORIENTATION_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    int requestedOrientation = data.readInt();
                    setRequestedOrientation(token, requestedOrientation);
                    reply.writeNoException();
                    return true;
                }

                case GET_REQUESTED_ORIENTATION_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    int req = getRequestedOrientation(token);
                    reply.writeNoException();
                    reply.writeInt(req);
                    return true;
                }

                case GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    ComponentName cn = getActivityClassForToken(token);
                    reply.writeNoException();
                    ComponentName.writeToParcel(cn, reply);
                    return true;
                }

                case GET_PACKAGE_FOR_TOKEN_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    reply.writeNoException();
                    reply.writeString(getPackageForToken(token));
                    return true;
                }

                case GET_INTENT_SENDER_TRANSACTION: {
                    int type = data.readInt();
                    String packageName = data.readString();
                    IBinder token = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    Intent requestIntent = data.readInt() != 0
                            ? Intent.CREATOR.createFromParcel(data) : null;
                    String requestResolvedType = data.readString();
                    int fl = data.readInt();
                    IIntentSender res = getIntentSender(type, packageName, token,
                            resultWho, requestCode, requestIntent,
                            requestResolvedType, fl);
                    reply.writeNoException();
                    reply.writeStrongBinder(res != null ? res.asBinder() : null);
                    return true;
                }

                case CANCEL_INTENT_SENDER_TRANSACTION: {
                    IIntentSender r = IIntentSender.Stub.asInterface(
                            data.readStrongBinder());
                    cancelIntentSender(r);
                    reply.writeNoException();
                    return true;
                }

                case GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION: {
                    IIntentSender r = IIntentSender.Stub.asInterface(
                            data.readStrongBinder());
                    String res = getPackageForIntentSender(r);
                    reply.writeNoException();
                    reply.writeString(res);
                    return true;
                }

                case SET_PROCESS_LIMIT_TRANSACTION: {
                    int max = data.readInt();
                    setProcessLimit(max);
                    reply.writeNoException();
                    return true;
                }

                case GET_PROCESS_LIMIT_TRANSACTION: {
                    int limit = getProcessLimit();
                    reply.writeNoException();
                    reply.writeInt(limit);
                    return true;
                }

                case SET_PROCESS_FOREGROUND_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    int pid = data.readInt();
                    boolean isForeground = data.readInt() != 0;
                    setProcessForeground(token, pid, isForeground);
                    reply.writeNoException();
                    return true;
                }

                case CHECK_PERMISSION_TRANSACTION: {
                    String perm = data.readString();
                    int pid = data.readInt();
                    int uid = data.readInt();
                    int res = checkPermission(perm, pid, uid);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case CHECK_URI_PERMISSION_TRANSACTION: {
                    Uri uri = Uri.CREATOR.createFromParcel(data);
                    int pid = data.readInt();
                    int uid = data.readInt();
                    int mode = data.readInt();
                    int res = checkUriPermission(uri, pid, uid, mode);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }

                case CLEAR_APP_DATA_TRANSACTION: {
                    String packageName = data.readString();
                    IPackageDataObserver observer = IPackageDataObserver.Stub.asInterface(
                            data.readStrongBinder());
                    boolean res = clearApplicationUserData(packageName, observer);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case GRANT_URI_PERMISSION_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    String targetPkg = data.readString();
                    Uri uri = Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    grantUriPermission(app, targetPkg, uri, mode);
                    reply.writeNoException();
                    return true;
                }

                case REVOKE_URI_PERMISSION_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    Uri uri = Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    revokeUriPermission(app, uri, mode);
                    reply.writeNoException();
                    return true;
                }

                case SHOW_WAITING_FOR_DEBUGGER_TRANSACTION: {
                    IBinder b = data.readStrongBinder();
                    IApplicationThread app = ApplicationThreadNative.asInterface(b);
                    boolean waiting = data.readInt() != 0;
                    showWaitingForDebugger(app, waiting);
                    reply.writeNoException();
                    return true;
                }

                case GET_MEMORY_INFO_TRANSACTION: {
                    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                    getMemoryInfo(mi);
                    reply.writeNoException();
                    mi.writeToParcel(reply, 0);
                    return true;
                }

                case UNHANDLED_BACK_TRANSACTION: {
                    unhandledBack();
                    reply.writeNoException();
                    return true;
                }

                case OPEN_CONTENT_URI_TRANSACTION: {
                    Uri uri = Uri.parse(data.readString());
                    ParcelFileDescriptor pfd = openContentUri(uri);
                    reply.writeNoException();
                    if (pfd != null) {
                        reply.writeInt(1);
                        pfd.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }

                case GOING_TO_SLEEP_TRANSACTION: {
                    goingToSleep();
                    reply.writeNoException();
                    return true;
                }

                case WAKING_UP_TRANSACTION: {
                    wakingUp();
                    reply.writeNoException();
                    return true;
                }

                case SET_DEBUG_APP_TRANSACTION: {
                    String pn = data.readString();
                    boolean wfd = data.readInt() != 0;
                    boolean per = data.readInt() != 0;
                    setDebugApp(pn, wfd, per);
                    reply.writeNoException();
                    return true;
                }

                case SET_ALWAYS_FINISH_TRANSACTION: {
                    boolean enabled = data.readInt() != 0;
                    setAlwaysFinish(enabled);
                    reply.writeNoException();
                    return true;
                }

                case SET_ACTIVITY_CONTROLLER_TRANSACTION: {
                    IActivityController watcher = IActivityController.Stub.asInterface(
                            data.readStrongBinder());
                    setActivityController(watcher);
                    return true;
                }

                case ENTER_SAFE_MODE_TRANSACTION: {
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                }

                case NOTE_WAKEUP_ALARM_TRANSACTION: {
                    IIntentSender is = IIntentSender.Stub.asInterface(
                            data.readStrongBinder());
                    noteWakeupAlarm(is);
                    reply.writeNoException();
                    return true;
                }

                case KILL_PIDS_TRANSACTION: {
                    int[] pids = data.createIntArray();
                    String reason = data.readString();
                    boolean res = killPids(pids, reason);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case START_RUNNING_TRANSACTION: {
                    String pkg = data.readString();
                    String cls = data.readString();
                    String action = data.readString();
                    String indata = data.readString();
                    startRunning(pkg, cls, action, indata);
                    reply.writeNoException();
                    return true;
                }

                case HANDLE_APPLICATION_CRASH_TRANSACTION: {
                    IBinder app = data.readStrongBinder();
                    ApplicationErrorReport.CrashInfo ci = new ApplicationErrorReport.CrashInfo(data);
                    handleApplicationCrash(app, ci);
                    reply.writeNoException();
                    return true;
                }

                case HANDLE_APPLICATION_WTF_TRANSACTION: {
                    IBinder app = data.readStrongBinder();
                    String tag = data.readString();
                    ApplicationErrorReport.CrashInfo ci = new ApplicationErrorReport.CrashInfo(data);
                    boolean res = handleApplicationWtf(app, tag, ci);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION: {
                    IBinder app = data.readStrongBinder();
                    int violationMask = data.readInt();
                    StrictMode.ViolationInfo info = new StrictMode.ViolationInfo(data);
                    handleApplicationStrictModeViolation(app, violationMask, info);
                    reply.writeNoException();
                    return true;
                }

                case SIGNAL_PERSISTENT_PROCESSES_TRANSACTION: {
                    int sig = data.readInt();
                    signalPersistentProcesses(sig);
                    reply.writeNoException();
                    return true;
                }

                case KILL_BACKGROUND_PROCESSES_TRANSACTION: {
                    String packageName = data.readString();
                    killBackgroundProcesses(packageName);
                    reply.writeNoException();
                    return true;
                }

                case FORCE_STOP_PACKAGE_TRANSACTION: {
                    String packageName = data.readString();
                    forceStopPackage(packageName);
                    reply.writeNoException();
                    return true;
                }

                case GET_DEVICE_CONFIGURATION_TRANSACTION: {
                    ConfigurationInfo config = getDeviceConfigurationInfo();
                    reply.writeNoException();
                    config.writeToParcel(reply, 0);
                    return true;
                }

                case PROFILE_CONTROL_TRANSACTION: {
                    String process = data.readString();
                    boolean start = data.readInt() != 0;
                    String path = data.readString();
                    ParcelFileDescriptor fd = data.readInt() != 0
                            ? data.readFileDescriptor() : null;
                    boolean res = profileControl(process, start, path, fd);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case SHUTDOWN_TRANSACTION: {
                    boolean res = shutdown(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }

                case STOP_APP_SWITCHES_TRANSACTION: {
                    stopAppSwitches();
                    reply.writeNoException();
                    return true;
                }

                case RESUME_APP_SWITCHES_TRANSACTION: {
                    resumeAppSwitches();
                    reply.writeNoException();
                    return true;
                }

                case PEEK_SERVICE_TRANSACTION: {
                    Intent service = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    IBinder binder = peekService(service, resolvedType);
                    reply.writeNoException();
                    reply.writeStrongBinder(binder);
                    return true;
                }

                case START_BACKUP_AGENT_TRANSACTION: {
                    ApplicationInfo info = ApplicationInfo.CREATOR.createFromParcel(data);
                    int backupRestoreMode = data.readInt();
                    boolean success = bindBackupAgent(info, backupRestoreMode);
                    reply.writeNoException();
                    reply.writeInt(success ? 1 : 0);
                    return true;
                }

                case BACKUP_AGENT_CREATED_TRANSACTION: {
                    String packageName = data.readString();
                    IBinder agent = data.readStrongBinder();
                    backupAgentCreated(packageName, agent);
                    reply.writeNoException();
                    return true;
                }

                case UNBIND_BACKUP_AGENT_TRANSACTION: {
                    ApplicationInfo info = ApplicationInfo.CREATOR.createFromParcel(data);
                    unbindBackupAgent(info);
                    reply.writeNoException();
                    return true;
                }

                case REGISTER_ACTIVITY_WATCHER_TRANSACTION: {
                    IActivityWatcher watcher = IActivityWatcher.Stub.asInterface(
                            data.readStrongBinder());
                    registerActivityWatcher(watcher);
                    return true;
                }

                case UNREGISTER_ACTIVITY_WATCHER_TRANSACTION: {
                    IActivityWatcher watcher = IActivityWatcher.Stub.asInterface(
                            data.readStrongBinder());
                    unregisterActivityWatcher(watcher);
                    return true;
                }

                case START_ACTIVITY_IN_PACKAGE_TRANSACTION: {
                    int uid = data.readInt();
                    Intent intent = Intent.CREATOR.createFromParcel(data);
                    String resolvedType = data.readString();
                    IBinder resultTo = data.readStrongBinder();
                    String resultWho = data.readString();
                    int requestCode = data.readInt();
                    boolean onlyIfNeeded = data.readInt() != 0;
                    int result = startActivityInPackage(uid, intent, resolvedType,
                            resultTo, resultWho, requestCode, onlyIfNeeded);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }

                case KILL_APPLICATION_WITH_UID_TRANSACTION: {
                    String pkg = data.readString();
                    int uid = data.readInt();
                    killApplicationWithUid(pkg, uid);
                    reply.writeNoException();
                    return true;
                }

                case CLOSE_SYSTEM_DIALOGS_TRANSACTION: {
                    String reason = data.readString();
                    closeSystemDialogs(reason);
                    reply.writeNoException();
                    return true;
                }

                case GET_PROCESS_MEMORY_INFO_TRANSACTION: {
                    int[] pids = data.createIntArray();
                    Debug.MemoryInfo[] res = getProcessMemoryInfo(pids);
                    reply.writeNoException();
                    reply.writeTypedArray(res, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                }

                case KILL_APPLICATION_PROCESS_TRANSACTION: {
                    String processName = data.readString();
                    int uid = data.readInt();
                    killApplicationProcess(processName, uid);
                    reply.writeNoException();
                    return true;
                }

                case OVERRIDE_PENDING_TRANSITION_TRANSACTION: {
                    IBinder token = data.readStrongBinder();
                    String packageName = data.readString();
                    int enterAnim = data.readInt();
                    int exitAnim = data.readInt();
                    overridePendingTransition(token, packageName, enterAnim, exitAnim);
                    reply.writeNoException();
                    return true;
                }

                case IS_USER_A_MONKEY_TRANSACTION: {
                    boolean areThey = isUserAMonkey();
                    reply.writeNoException();
                    reply.writeInt(areThey ? 1 : 0);
                    return true;
                }

                case FINISH_HEAVY_WEIGHT_APP_TRANSACTION: {
                    finishHeavyWeightApp();
                    reply.writeNoException();
                    return true;
                }

                case CRASH_APPLICATION_TRANSACTION: {
                    int uid = data.readInt();
                    int initialPid = data.readInt();
                    String packageName = data.readString();
                    String message = data.readString();
                    crashApplication(uid, initialPid, packageName, message);
                    reply.writeNoException();
                    return true;
                }

                case GET_PROVIDER_MIME_TYPE_TRANSACTION: {
                    Uri uri = Uri.CREATOR.createFromParcel(data);
                    String type = getProviderMimeType(uri);
                    reply.writeNoException();
                    reply.writeString(type);
                    return true;
                }

                case NEW_URI_PERMISSION_OWNER_TRANSACTION: {
                    String name = data.readString();
                    IBinder perm = newUriPermissionOwner(name);
                    reply.writeNoException();
                    reply.writeStrongBinder(perm);
                    return true;
                }

                case GRANT_URI_PERMISSION_FROM_OWNER_TRANSACTION: {
                    IBinder owner = data.readStrongBinder();
                    int fromUid = data.readInt();
                    String targetPkg = data.readString();
                    Uri uri = Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    grantUriPermissionFromOwner(owner, fromUid, targetPkg, uri, mode);
                    reply.writeNoException();
                    return true;
                }

                case REVOKE_URI_PERMISSION_FROM_OWNER_TRANSACTION: {
                    IBinder owner = data.readStrongBinder();
                    Uri uri = null;
                    if (data.readInt() != 0) {
                        Uri.CREATOR.createFromParcel(data);
                    }
                    int mode = data.readInt();
                    revokeUriPermissionFromOwner(owner, uri, mode);
                    reply.writeNoException();
                    return true;
                }
}
}

return super.onTransact(code, data, reply, flags);
}








