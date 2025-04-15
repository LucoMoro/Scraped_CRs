/*SDK Manager refactor: remove obsolete UpdaterPage

Change-Id:Ieffc0fd8c88705616197bdb971fc66cb9c7f536c*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterPage.java
deleted file mode 100755
//Synthetic comment -- index f136bfb..0000000

//Synthetic comment -- @@ -1,81 +0,0 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository;

import com.android.utils.ILogger;

import org.eclipse.swt.widgets.Composite;

import java.lang.reflect.Constructor;

/**
 * Base class for pages shown in the updater.
 */
public abstract class UpdaterPage extends Composite {

    public enum Purpose {
        /** A generic page with is neither of the other specific purposes. */
        GENERIC,
        /** A page that displays the about box for the SDK Manager. */
        ABOUT_BOX,
        /** A page that displays the settings for the SDK Manager. */
        SETTINGS
    }

    public UpdaterPage(Composite parent, int swtStyle) {
        super(parent, swtStyle);
    }

    /**
     * The title of the page. Default is null.
     * <p/>
     * Useful for SdkManager1 when it displays a list of pages using
     * a vertical page selector.
     * Default implement for SdkManager2 is to return null.
     */
    public String getPageTitle() {
        return null;
    }

    public static UpdaterPage newInstance(
            Class<? extends UpdaterPage> clazz,
            Composite parent,
            int swtStyle,
            ILogger log) {

        try {
            Constructor<? extends UpdaterPage> cons =
                clazz.getConstructor(new Class<?>[] { Composite.class, int.class });

            return cons.newInstance(new Object[] { parent, swtStyle });

        } catch (NoSuchMethodException e) {
            // There is no such constructor.
            log.error(e,
                    "Failed to instanciate page %1$s. Constructor args must be (Composite,int).",
                    clazz.getSimpleName());

        } catch (Exception e) {
            // Log this instead of crashing the whole app.
            log.error(e,
                    "Failed to instanciate page %1$s.",
                    clazz.getSimpleName());
        }

        return null;
    }
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerPage.java
//Synthetic comment -- index fc8e2bf..c691b77 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.widgets.AvdSelector;
import com.android.sdkuilib.internal.widgets.AvdSelector.DisplayMode;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -40,7 +39,7 @@
* thus composed of the {@link AvdManagerWindowImpl1} (the window shell itself) and this
* page displays the actually list of AVDs and various action buttons.
*/
public class AvdManagerPage extends UpdaterPage implements ISdkChangeListener, DevicesChangeListener {

private AvdSelector mAvdSelector;









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/PackagesPage.java
//Synthetic comment -- index d9d1744..566981d 100755

//Synthetic comment -- @@ -27,7 +27,6 @@
import com.android.sdklib.internal.repository.packages.Package;
import com.android.sdklib.internal.repository.sources.SdkSource;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.UpdaterPage;
import com.android.sdkuilib.internal.repository.core.PackageLoader;
import com.android.sdkuilib.internal.repository.core.PackageLoader.ISourceLoadedCallback;
import com.android.sdkuilib.internal.repository.core.PackagesDiffLogic;
//Synthetic comment -- @@ -93,7 +92,7 @@
* remote available packages. This gives an overview of what is installed
* vs what is available and allows the user to update or install packages.
*/
public class PackagesPage extends UpdaterPage implements ISdkChangeListener {

public  static final String ICON_CAT_OTHER      = "pkgcat_other_16.png";    //$NON-NLS-1$
public  static final String ICON_CAT_PLATFORM   = "pkgcat_16.png";          //$NON-NLS-1$







