/*Refactoring: move SDK Updater core code into sdklib.

This requires the 2 following changes:
in        sdk.git:I79742d366b176cee2443bbed1f96dc253e6c74bbin tools/base.git:I507a2bebe348fae598bc6e6fe24af3c5bf78acf0Change-Id:I97c5874e6b5dcb5d6c0ca25ca921a291c6330fcc*/




//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ArchiveInfo.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ArchiveInfo.java
deleted file mode 100755
//Synthetic comment -- index 2c509ca..0000000

//Synthetic comment -- @@ -1,160 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISdkUpdaterWindow.java
//Synthetic comment -- index e5f2521..ead5a78 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.repository.ISdkChangeListener;

/**
* Interface for the actual implementation of the Update Window.








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISettingsPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISettingsPage.java
deleted file mode 100755
//Synthetic comment -- index 333644f..0000000

//Synthetic comment -- @@ -1,108 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISwtUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ISwtUpdaterData.java
//Synthetic comment -- index dd2920b..32e279a 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdkuilib.internal.repository;

import com.android.sdklib.internal.repository.updater.IUpdaterData;
import com.android.sdklib.internal.repository.updater.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.swt.widgets.Shell;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/IUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/IUpdaterData.java
deleted file mode 100755
//Synthetic comment -- index 52fdb1d..0000000

//Synthetic comment -- @@ -1,44 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 695f05f..407d8be 100755

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.packages.Package.License;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.ArchiveInfo;
import com.android.sdklib.internal.repository.updater.SdkUpdaterLogic;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;









//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogic.java
deleted file mode 100755
//Synthetic comment -- index 25e6553..0000000

//Synthetic comment -- @@ -1,1477 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SdkUpdaterNoWindow.java
deleted file mode 100755
//Synthetic comment -- index 5e7143d..0000000

//Synthetic comment -- @@ -1,624 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsController.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsController.java
deleted file mode 100755
//Synthetic comment -- index 2d2352b..0000000

//Synthetic comment -- @@ -1,382 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SettingsDialog.java
//Synthetic comment -- index 822715a..af1ada7 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.updater.ISettingsPage;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.util.FormatUtils;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SwtUpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/SwtUpdaterData.java
//Synthetic comment -- index 17282b7..8934ae7 100755

//Synthetic comment -- @@ -21,6 +21,9 @@
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.updater.ArchiveInfo;
import com.android.sdklib.internal.repository.updater.SdkUpdaterLogic;
import com.android.sdklib.internal.repository.updater.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.SdkUpdaterWindowImpl2;
import com.android.utils.ILogger;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterData.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/UpdaterData.java
deleted file mode 100755
//Synthetic comment -- index 8dd200b..0000000

//Synthetic comment -- @@ -1,1090 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackageLoader.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackageLoader.java
deleted file mode 100755
//Synthetic comment -- index b659edc..0000000

//Synthetic comment -- @@ -1,499 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index 9809a4c..3bbeb54 100755

//Synthetic comment -- @@ -29,9 +29,10 @@
import com.android.sdklib.internal.repository.packages.SystemImagePackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.internal.repository.updater.PkgItem.PkgState;
import com.android.sdklib.util.SparseArray;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.ui.PackagesPageIcons;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgCategory.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgCategory.java
//Synthetic comment -- index a08b6ef..e46c8b1 100755

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.sdkuilib.internal.repository.core;


import com.android.sdklib.internal.repository.updater.PkgItem;

import java.util.ArrayList;
import java.util.List;









//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgContentProvider.java
//Synthetic comment -- index 8adf428..7f124d5 100755

//Synthetic comment -- @@ -20,6 +20,7 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdkuilib.internal.repository.ui.PackagesPage;

import org.eclipse.jface.viewers.IInputProvider;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgItem.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/PkgItem.java
deleted file mode 100755
//Synthetic comment -- index cac43e4..0000000

//Synthetic comment -- @@ -1,277 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/SwtPackageLoader.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/core/SwtPackageLoader.java
//Synthetic comment -- index 557732a..c108640 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.annotations.NonNull;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.updater.PackageLoader;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;

import org.eclipse.swt.widgets.Display;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AdtUpdateDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AdtUpdateDialog.java
//Synthetic comment -- index 932fa90..2ffa9a9 100755

//Synthetic comment -- @@ -24,11 +24,11 @@
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.PackageLoader;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.internal.repository.updater.PackageLoader.IAutoInstallTask;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.SdkLogAdapter;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.ui.GridDataBuilder;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index a18cbbd..f27cbcb 100755

//Synthetic comment -- @@ -21,10 +21,10 @@
import com.android.sdklib.devices.DeviceManager.DevicesChangedListener;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index dd869a0..b8dc9ae 100755

//Synthetic comment -- @@ -21,15 +21,15 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.DeviceManagerPage.IAvdCreatedListener;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index 5093991..041af8e 100755

//Synthetic comment -- @@ -24,12 +24,12 @@
import com.android.sdklib.devices.Storage;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.widgets.AvdCreationDialog;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.DeviceCreationDialog;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;









//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index 906fd26..7f5c6b6 100755

//Synthetic comment -- @@ -21,14 +21,14 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.internal.repository.updater.PkgItem.PkgState;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/PackagesPageImpl.java
//Synthetic comment -- index e68a852..5e6ac7f 100755

//Synthetic comment -- @@ -23,15 +23,15 @@
import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.PackageLoader;
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.internal.repository.updater.PackageLoader.ISourceLoadedCallback;
import com.android.sdklib.internal.repository.updater.PkgItem.PkgState;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgCategoryApi;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;

import org.eclipse.jface.viewers.ColumnLabelProvider;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/repository/ui/SdkUpdaterWindowImpl2.java
//Synthetic comment -- index 34346df..3b0801b 100755

//Synthetic comment -- @@ -20,11 +20,12 @@
import com.android.SdkConstants;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.sources.SdkSourceProperties;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.internal.repository.updater.SettingsController.Settings;
import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.MenuBarWrapper;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
//Synthetic comment -- @@ -35,7 +36,6 @@
import com.android.sdkuilib.internal.widgets.ImgDisabledButton;
import com.android.sdkuilib.internal.widgets.ToggleButton;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.SdkUpdaterWindow.SdkInvocationContext;
import com.android.utils.ILogger;









//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index 67d161f..0a9d303 100644

//Synthetic comment -- @@ -27,10 +27,10 @@
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.util.GrabProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.IProcessOutput;
import com.android.sdklib.util.GrabProcessOutput.Wait;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.repository.ui.AvdManagerWindowImpl1;
import com.android.sdkuilib.internal.tasks.ProgressTask;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 0ec26ff..d796276 100644

//Synthetic comment -- @@ -20,7 +20,7 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;
import com.android.utils.SdkUtils;








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/repository/ISdkChangeListener.java b/sdkuilib/src/main/java/com/android/sdkuilib/repository/ISdkChangeListener.java
deleted file mode 100755
//Synthetic comment -- index e221f98..0000000

//Synthetic comment -- @@ -1,54 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/repository/SdkUpdaterWindow.java b/sdkuilib/src/main/java/com/android/sdkuilib/repository/SdkUpdaterWindow.java
//Synthetic comment -- index 6010e48..343acc9 100755

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.sdkuilib.repository;

import com.android.sdklib.repository.ISdkChangeListener;
import com.android.sdkuilib.internal.repository.ISdkUpdaterWindow;
import com.android.sdkuilib.internal.repository.ui.SdkUpdaterWindowImpl2;
import com.android.utils.ILogger;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockDownloadCache.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockDownloadCache.java
deleted file mode 100755
//Synthetic comment -- index 04f2b04..0000000

//Synthetic comment -- @@ -1,236 +0,0 @@








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/MockSwtUpdaterData.java
//Synthetic comment -- index 9e60ee1..1f484df 100755

//Synthetic comment -- @@ -21,14 +21,17 @@
import com.android.sdklib.internal.repository.ITask;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdklib.internal.repository.ITaskMonitor;
import com.android.sdklib.internal.repository.MockDownloadCache;
import com.android.sdklib.internal.repository.MockEmptySdkManager;
import com.android.sdklib.internal.repository.NullTaskMonitor;
import com.android.sdklib.internal.repository.archives.ArchiveInstaller;
import com.android.sdklib.internal.repository.archives.ArchiveReplacement;
import com.android.sdklib.internal.repository.sources.SdkSourceCategory;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.updater.ArchiveInfo;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.sdklib.internal.repository.updater.SettingsController.Settings;
import com.android.sdklib.mock.MockLog;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.utils.ILogger;
import com.android.utils.NullLogger;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/SdkUpdaterLogicTest.java
//Synthetic comment -- index a046335..333998e 100755

//Synthetic comment -- @@ -30,6 +30,10 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.sources.SdkSources;
import com.android.sdklib.internal.repository.updater.ArchiveInfo;
import com.android.sdklib.internal.repository.updater.IUpdaterData;
import com.android.sdklib.internal.repository.updater.SdkUpdaterLogic;
import com.android.sdklib.internal.repository.updater.SettingsController;
import com.android.utils.ILogger;

import java.util.ArrayList;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/UpdaterDataTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/UpdaterDataTest.java
//Synthetic comment -- index a2d12bc..1212235 100755

//Synthetic comment -- @@ -18,6 +18,7 @@

import com.android.sdklib.internal.repository.archives.Archive;
import com.android.sdklib.internal.repository.packages.MockEmptyPackage;
import com.android.sdklib.internal.repository.updater.ArchiveInfo;

import java.util.ArrayList;
import java.util.Arrays;








//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/core/PackagesDiffLogicTest.java
//Synthetic comment -- index 7427b3b..cc8a1b9 100755

//Synthetic comment -- @@ -30,12 +30,12 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkRepoSource;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdklib.internal.repository.updater.ISettingsPage;
import com.android.sdklib.internal.repository.updater.PkgItem;
import com.android.sdklib.repository.PkgProps;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
import com.android.sdkuilib.internal.repository.core.PkgCategory;

import java.util.Properties;









//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/MockPackagesPageImpl.java
//Synthetic comment -- index 18ddd9c..fe854d8 100755

//Synthetic comment -- @@ -17,11 +17,11 @@
package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.MockDownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.updater.PackageLoader;
import com.android.sdklib.util.SparseIntArray;
import com.android.sdkuilib.internal.repository.SwtUpdaterData;
import com.android.sdkuilib.internal.repository.core.PkgCategory;
import com.android.sdkuilib.internal.repository.core.PkgContentProvider;









//Synthetic comment -- diff --git a/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java b/sdkuilib/src/test/java/com/android/sdkuilib/internal/repository/ui/SdkManagerUpgradeTest.java
//Synthetic comment -- index b3e84b1..1746eec 100755

//Synthetic comment -- @@ -18,8 +18,8 @@

import com.android.sdklib.SdkManager;
import com.android.sdklib.SdkManagerTestCase;
import com.android.sdklib.internal.repository.MockDownloadCache;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkuilib.internal.repository.MockSwtUpdaterData;

import java.util.Arrays;







