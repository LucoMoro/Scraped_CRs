/*Constants refactoring.

This changeset moves most constants into the SdkConstants
class, and gets rid of AndroidConstants and LintConstants.
It also migrates all non-ADT specific constants from
AdtConstants into SdkConstants. It furthermore moves various
other constants (such as those in XmlUtils and ValuesDescriptors)
into the constants class. It also fixes the modifier order
to be the canonical modifier order (JLS 8.x).

Finally, it removes redundancy and combines various constant
aliases such that we don't have both NAME_ATTR and ATTR_NAME
pointing to "name", etc.

Change-Id:Ifd1755016f62ce2dd80e5c76130d6de4b0e32161*/
//Synthetic comment -- diff --git a/common/src/com/android/AndroidConstants.java b/common/src/com/android/AndroidConstants.java
deleted file mode 100644
//Synthetic comment -- index dfe137a..0000000

//Synthetic comment -- @@ -1,50 +0,0 @@
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

package com.android;

/**
 * Generic Android Constants.
 */
public class AndroidConstants {

    /** Default anim resource folder name, i.e. "anim" */
    public final static String FD_RES_ANIM = "anim"; //$NON-NLS-1$
    /** Default animator resource folder name, i.e. "animator" */
    public final static String FD_RES_ANIMATOR = "animator"; //$NON-NLS-1$
    /** Default color resource folder name, i.e. "color" */
    public final static String FD_RES_COLOR = "color"; //$NON-NLS-1$
    /** Default drawable resource folder name, i.e. "drawable" */
    public final static String FD_RES_DRAWABLE = "drawable"; //$NON-NLS-1$
    /** Default interpolator resource folder name, i.e. "interpolator" */
    public final static String FD_RES_INTERPOLATOR = "interpolator"; //$NON-NLS-1$
    /** Default layout resource folder name, i.e. "layout" */
    public final static String FD_RES_LAYOUT = "layout"; //$NON-NLS-1$
    /** Default menu resource folder name, i.e. "menu" */
    public final static String FD_RES_MENU = "menu"; //$NON-NLS-1$
    /** Default menu resource folder name, i.e. "mipmap" */
    public final static String FD_RES_MIPMAP = "mipmap"; //$NON-NLS-1$
    /** Default values resource folder name, i.e. "values" */
    public final static String FD_RES_VALUES = "values"; //$NON-NLS-1$
    /** Default xml resource folder name, i.e. "xml" */
    public final static String FD_RES_XML = "xml"; //$NON-NLS-1$
    /** Default raw resource folder name, i.e. "raw" */
    public final static String FD_RES_RAW = "raw"; //$NON-NLS-1$

    /** Separator between the resource folder qualifier. */
    public final static String RES_QUALIFIER_SEP = "-"; //$NON-NLS-1$

}








//Synthetic comment -- diff --git a/common/src/com/android/SdkConstants.java b/common/src/com/android/SdkConstants.java
//Synthetic comment -- index 6f47fa9..d2f1421 100644

//Synthetic comment -- @@ -30,31 +30,31 @@
*
*/
public final class SdkConstants {
    public final static int PLATFORM_UNKNOWN = 0;
    public final static int PLATFORM_LINUX = 1;
    public final static int PLATFORM_WINDOWS = 2;
    public final static int PLATFORM_DARWIN = 3;

/**
* Returns current platform, one of {@link #PLATFORM_WINDOWS}, {@link #PLATFORM_DARWIN},
* {@link #PLATFORM_LINUX} or {@link #PLATFORM_UNKNOWN}.
*/
    public final static int CURRENT_PLATFORM = currentPlatform();

/**
* Charset for the ini file handled by the SDK.
*/
    public final static String INI_CHARSET = "UTF-8";                                 //$NON-NLS-1$

/** An SDK Project's AndroidManifest.xml file */
public static final String FN_ANDROID_MANIFEST_XML= "AndroidManifest.xml";        //$NON-NLS-1$
/** pre-dex jar filename. i.e. "classes.jar" */
    public final static String FN_CLASSES_JAR = "classes.jar";                        //$NON-NLS-1$
/** Dex filename inside the APK. i.e. "classes.dex" */
    public final static String FN_APK_CLASSES_DEX = "classes.dex";                    //$NON-NLS-1$

/** An SDK Project's build.xml file */
    public final static String FN_BUILD_XML = "build.xml";                            //$NON-NLS-1$

/** Name of the framework library, i.e. "android.jar" */
public static final String FN_FRAMEWORK_LIBRARY = "android.jar";                  //$NON-NLS-1$
//Synthetic comment -- @@ -89,164 +89,164 @@
public static final String FN_ANNOTATIONS_JAR = "annotations.jar";                //$NON-NLS-1$

/** platform build property file */
    public final static String FN_BUILD_PROP = "build.prop";                          //$NON-NLS-1$
/** plugin properties file */
    public final static String FN_PLUGIN_PROP = "plugin.prop";                        //$NON-NLS-1$
/** add-on manifest file */
    public final static String FN_MANIFEST_INI = "manifest.ini";                      //$NON-NLS-1$
/** add-on layout device XML file. */
    public final static String FN_DEVICES_XML = "devices.xml";                        //$NON-NLS-1$
/** hardware properties definition file */
    public final static String FN_HARDWARE_INI = "hardware-properties.ini";           //$NON-NLS-1$

/** project property file */
    public final static String FN_PROJECT_PROPERTIES = "project.properties";          //$NON-NLS-1$

/** project local property file */
    public final static String FN_LOCAL_PROPERTIES = "local.properties";              //$NON-NLS-1$

/** project ant property file */
    public final static String FN_ANT_PROPERTIES = "ant.properties";                  //$NON-NLS-1$

/** Skin layout file */
    public final static String FN_SKIN_LAYOUT = "layout";                             //$NON-NLS-1$

/** dx.jar file */
public static final String FN_DX_JAR = "dx.jar";                                  //$NON-NLS-1$

/** dx executable (with extension for the current OS) */
    public final static String FN_DX =
"dx" + ext(".bat", "");                           //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** aapt executable (with extension for the current OS) */
    public final static String FN_AAPT =
"aapt" + ext(".exe", "");                         //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** aidl executable (with extension for the current OS) */
    public final static String FN_AIDL =
"aidl" + ext(".exe", "");                         //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** renderscript executable (with extension for the current OS) */
    public final static String FN_RENDERSCRIPT =
"llvm-rs-cc" + ext(".exe", "");                   //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** adb executable (with extension for the current OS) */
    public final static String FN_ADB =
"adb" + ext(".exe", "");                          //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** emulator executable for the current OS */
    public final static String FN_EMULATOR =
"emulator" + ext(".exe", "");                     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** zipalign executable (with extension for the current OS) */
    public final static String FN_ZIPALIGN =
"zipalign" + ext(".exe", "");                     //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** dexdump executable (with extension for the current OS) */
    public final static String FN_DEXDUMP =
"dexdump" + ext(".exe", "");                      //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** proguard executable (with extension for the current OS) */
    public final static String FN_PROGUARD =
"proguard" + ext(".bat", ".sh");                  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** find_lock for Windows (with extension for the current OS) */
    public final static String FN_FIND_LOCK =
"find_lock" + ext(".exe", "");                    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

/** properties file for SDK Updater packages */
    public final static String FN_SOURCE_PROP = "source.properties";                  //$NON-NLS-1$
/** properties file for content hash of installed packages */
    public final static String FN_CONTENT_HASH_PROP = "content_hash.properties";      //$NON-NLS-1$
/** properties file for the SDK */
    public final static String FN_SDK_PROP = "sdk.properties";                        //$NON-NLS-1$

/**
* filename for gdbserver.
*/
    public final static String FN_GDBSERVER = "gdbserver";              //$NON-NLS-1$

/** global Android proguard config file */
    public final static String FN_ANDROID_PROGUARD_FILE = "proguard-android.txt";   //$NON-NLS-1$
/** global Android proguard config file with optimization enabled */
    public final static String FN_ANDROID_OPT_PROGUARD_FILE = "proguard-android-optimize.txt";  //$NON-NLS-1$
/** default proguard config file with new file extension (for project specific stuff) */
    public final static String FN_PROJECT_PROGUARD_FILE = "proguard-project.txt";   //$NON-NLS-1$

/* Folder Names for Android Projects . */

/** Resources folder name, i.e. "res". */
    public final static String FD_RESOURCES = "res";                    //$NON-NLS-1$
/** Assets folder name, i.e. "assets" */
    public final static String FD_ASSETS = "assets";                    //$NON-NLS-1$
/** Default source folder name in an SDK project, i.e. "src".
* <p/>
* Note: this is not the same as {@link #FD_PKG_SOURCES}
* which is an SDK sources folder for packages. */
    public final static String FD_SOURCES = "src";                      //$NON-NLS-1$
/** Default generated source folder name, i.e. "gen" */
    public final static String FD_GEN_SOURCES = "gen";                  //$NON-NLS-1$
/** Default native library folder name inside the project, i.e. "libs"
* While the folder inside the .apk is "lib", we call that one libs because
* that's what we use in ant for both .jar and .so and we need to make the 2 development ways
* compatible. */
    public final static String FD_NATIVE_LIBS = "libs";                 //$NON-NLS-1$
/** Native lib folder inside the APK: "lib" */
    public final static String FD_APK_NATIVE_LIBS = "lib";              //$NON-NLS-1$
/** Default output folder name, i.e. "bin" */
    public final static String FD_OUTPUT = "bin";                       //$NON-NLS-1$
/** Classes output folder name, i.e. "classes" */
    public final static String FD_CLASSES_OUTPUT = "classes";           //$NON-NLS-1$
/** proguard output folder for mapping, etc.. files */
    public final static String FD_PROGUARD = "proguard";                //$NON-NLS-1$
/** aidl output folder for copied aidl files */
    public final static String FD_AIDL = "aidl";                        //$NON-NLS-1$

/* Folder Names for the Android SDK */

/** Name of the SDK platforms folder. */
    public final static String FD_PLATFORMS = "platforms";              //$NON-NLS-1$
/** Name of the SDK addons folder. */
    public final static String FD_ADDONS = "add-ons";                   //$NON-NLS-1$
/** Name of the SDK system-images folder. */
    public final static String FD_SYSTEM_IMAGES = "system-images";      //$NON-NLS-1$
/** Name of the SDK sources folder where source packages are installed.
* <p/>
* Note this is not the same as {@link #FD_SOURCES} which is the folder name where sources
* are installed inside a project. */
    public final static String FD_PKG_SOURCES = "sources";              //$NON-NLS-1$
/** Name of the SDK tools folder. */
    public final static String FD_TOOLS = "tools";                      //$NON-NLS-1$
/** Name of the SDK tools/support folder. */
    public final static String FD_SUPPORT = "support";                  //$NON-NLS-1$
/** Name of the SDK platform tools folder. */
    public final static String FD_PLATFORM_TOOLS = "platform-tools";    //$NON-NLS-1$
/** Name of the SDK tools/lib folder. */
    public final static String FD_LIB = "lib";                          //$NON-NLS-1$
/** Name of the SDK docs folder. */
    public final static String FD_DOCS = "docs";                        //$NON-NLS-1$
/** Name of the doc folder containing API reference doc (javadoc) */
public static final String FD_DOCS_REFERENCE = "reference";         //$NON-NLS-1$
/** Name of the SDK images folder. */
    public final static String FD_IMAGES = "images";                    //$NON-NLS-1$
/** Name of the ABI to support. */
    public final static String ABI_ARMEABI = "armeabi";                 //$NON-NLS-1$
    public final static String ABI_ARMEABI_V7A = "armeabi-v7a";         //$NON-NLS-1$
    public final static String ABI_INTEL_ATOM = "x86";                  //$NON-NLS-1$
    public final static String ABI_MIPS = "mips";                       //$NON-NLS-1$
/** Name of the CPU arch to support. */
    public final static String CPU_ARCH_ARM = "arm";                    //$NON-NLS-1$
    public final static String CPU_ARCH_INTEL_ATOM = "x86";             //$NON-NLS-1$
    public final static String CPU_ARCH_MIPS = "mips";                  //$NON-NLS-1$
/** Name of the CPU model to support. */
    public final static String CPU_MODEL_CORTEX_A8 = "cortex-a8";       //$NON-NLS-1$

/** Name of the SDK skins folder. */
    public final static String FD_SKINS = "skins";                      //$NON-NLS-1$
/** Name of the SDK samples folder. */
    public final static String FD_SAMPLES = "samples";                  //$NON-NLS-1$
/** Name of the SDK extras folder. */
    public final static String FD_EXTRAS = "extras";                    //$NON-NLS-1$
/**
* Name of an extra's sample folder.
* Ideally extras should have one {@link #FD_SAMPLES} folder containing
//Synthetic comment -- @@ -255,32 +255,32 @@
* in it. When possible we should encourage extras' owners to move to the
* multi-samples format.
*/
    public final static String FD_SAMPLE = "sample";                    //$NON-NLS-1$
/** Name of the SDK templates folder, i.e. "templates" */
    public final static String FD_TEMPLATES = "templates";              //$NON-NLS-1$
/** Name of the SDK Ant folder, i.e. "ant" */
    public final static String FD_ANT = "ant";                          //$NON-NLS-1$
/** Name of the SDK data folder, i.e. "data" */
    public final static String FD_DATA = "data";                        //$NON-NLS-1$
/** Name of the SDK renderscript folder, i.e. "rs" */
    public final static String FD_RENDERSCRIPT = "rs";                  //$NON-NLS-1$
/** Name of the SDK resources folder, i.e. "res" */
    public final static String FD_RES = "res";                          //$NON-NLS-1$
/** Name of the SDK font folder, i.e. "fonts" */
    public final static String FD_FONTS = "fonts";                      //$NON-NLS-1$
/** Name of the android sources directory */
public static final String FD_ANDROID_SOURCES = "sources";          //$NON-NLS-1$
/** Name of the addon libs folder. */
    public final static String FD_ADDON_LIBS = "libs";                  //$NON-NLS-1$

/** Name of the cache folder in the $HOME/.android. */
    public final static String FD_CACHE = "cache";                      //$NON-NLS-1$

/** API codename of a release (non preview) system image or platform. **/
    public final static String CODENAME_RELEASE = "REL";                //$NON-NLS-1$

/** Namespace for the resource XML, i.e. "http://schemas.android.com/apk/res/android" */
    public final static String NS_RESOURCES =
"http://schemas.android.com/apk/res/android";                   //$NON-NLS-1$

/** Namespace for the device schema, i.e. "http://schemas.android.com/sdk/devices/1" */
//Synthetic comment -- @@ -289,42 +289,42 @@


/** The name of the uses-library that provides "android.test.runner" */
    public final static String ANDROID_TEST_RUNNER_LIB =
"android.test.runner";                                          //$NON-NLS-1$

/* Folder path relative to the SDK root */
/** Path of the documentation directory relative to the sdk folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_DOCS_FOLDER = FD_DOCS + File.separator;

/** Path of the tools directory relative to the sdk folder, or to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_TOOLS_FOLDER = FD_TOOLS + File.separator;

/** Path of the lib directory relative to the sdk folder, or to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_TOOLS_LIB_FOLDER =
OS_SDK_TOOLS_FOLDER + FD_LIB + File.separator;

/**
* Path of the lib directory relative to the sdk folder, or to a platform
* folder. This is an OS path, ending with a separator.
*/
    public final static String OS_SDK_TOOLS_LIB_EMULATOR_FOLDER = OS_SDK_TOOLS_LIB_FOLDER
+ "emulator" + File.separator;                              //$NON-NLS-1$

/** Path of the platform tools directory relative to the sdk folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLATFORM_TOOLS_FOLDER = FD_PLATFORM_TOOLS + File.separator;

/** Path of the Platform tools Lib directory relative to the sdk folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_PLATFORM_TOOLS_LIB_FOLDER =
OS_SDK_PLATFORM_TOOLS_FOLDER + FD_LIB + File.separator;

/** Path of the bin folder of proguard folder relative to the sdk folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SDK_TOOLS_PROGUARD_BIN_FOLDER =
SdkConstants.OS_SDK_TOOLS_FOLDER +
"proguard" + File.separator +                                   //$NON-NLS-1$
"bin" + File.separator;                                         //$NON-NLS-1$
//Synthetic comment -- @@ -333,120 +333,118 @@

/** Path of the images directory relative to a platform or addon folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_IMAGES_FOLDER = FD_IMAGES + File.separator;

/** Path of the skin directory relative to a platform or addon folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_SKINS_FOLDER = FD_SKINS + File.separator;

/* Folder paths relative to a Platform folder */

/** Path of the data directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_DATA_FOLDER = FD_DATA + File.separator;

/** Path of the renderscript directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_RENDERSCRIPT_FOLDER = FD_RENDERSCRIPT + File.separator;


/** Path of the samples directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_SAMPLES_FOLDER = FD_SAMPLES + File.separator;

/** Path of the resources directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_RESOURCES_FOLDER =
OS_PLATFORM_DATA_FOLDER + FD_RES + File.separator;

/** Path of the fonts directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_FONTS_FOLDER =
OS_PLATFORM_DATA_FOLDER + FD_FONTS + File.separator;

/** Path of the android source directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_SOURCES_FOLDER = FD_ANDROID_SOURCES + File.separator;

/** Path of the android templates directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_TEMPLATES_FOLDER = FD_TEMPLATES + File.separator;

/** Path of the Ant build rules directory relative to a platform folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_PLATFORM_ANT_FOLDER = FD_ANT + File.separator;

/** Path of the attrs.xml file relative to a platform folder. */
    public final static String OS_PLATFORM_ATTRS_XML =
            OS_PLATFORM_RESOURCES_FOLDER + AndroidConstants.FD_RES_VALUES + File.separator +
FN_ATTRS_XML;

/** Path of the attrs_manifest.xml file relative to a platform folder. */
    public final static String OS_PLATFORM_ATTRS_MANIFEST_XML =
            OS_PLATFORM_RESOURCES_FOLDER + AndroidConstants.FD_RES_VALUES + File.separator +
FN_ATTRS_MANIFEST_XML;

/** Path of the layoutlib.jar file relative to a platform folder. */
    public final static String OS_PLATFORM_LAYOUTLIB_JAR =
OS_PLATFORM_DATA_FOLDER + FN_LAYOUTLIB_JAR;

/** Path of the renderscript include folder relative to a platform folder. */
    public final static String OS_FRAMEWORK_RS =
FN_FRAMEWORK_RENDERSCRIPT + File.separator + FN_FRAMEWORK_INCLUDE;
/** Path of the renderscript (clang) include folder relative to a platform folder. */
    public final static String OS_FRAMEWORK_RS_CLANG =
FN_FRAMEWORK_RENDERSCRIPT + File.separator + FN_FRAMEWORK_INCLUDE_CLANG;

/* Folder paths relative to a addon folder */
/** Path of the images directory relative to a folder folder.
*  This is an OS path, ending with a separator. */
    public final static String OS_ADDON_LIBS_FOLDER = FD_ADDON_LIBS + File.separator;

/** Skin default **/
    public final static String SKIN_DEFAULT = "default";                    //$NON-NLS-1$

/** SDK property: ant templates revision */
    public final static String PROP_SDK_ANT_TEMPLATES_REVISION =
"sdk.ant.templates.revision";                                       //$NON-NLS-1$

/** SDK property: default skin */
    public final static String PROP_SDK_DEFAULT_SKIN = "sdk.skin.default"; //$NON-NLS-1$

/* Android Class Constants */
    public final static String CLASS_ACTIVITY = "android.app.Activity"; //$NON-NLS-1$
    public final static String CLASS_APPLICATION = "android.app.Application"; //$NON-NLS-1$
    public final static String CLASS_SERVICE = "android.app.Service"; //$NON-NLS-1$
    public final static String CLASS_BROADCASTRECEIVER = "android.content.BroadcastReceiver"; //$NON-NLS-1$
    public final static String CLASS_CONTENTPROVIDER = "android.content.ContentProvider"; //$NON-NLS-1$
    public final static String CLASS_INSTRUMENTATION = "android.app.Instrumentation"; //$NON-NLS-1$
    public final static String CLASS_INSTRUMENTATION_RUNNER =
"android.test.InstrumentationTestRunner"; //$NON-NLS-1$
    public final static String CLASS_BUNDLE = "android.os.Bundle"; //$NON-NLS-1$
    public final static String CLASS_R = "android.R"; //$NON-NLS-1$
    public final static String CLASS_MANIFEST_PERMISSION = "android.Manifest$permission"; //$NON-NLS-1$
    public final static String CLASS_INTENT = "android.content.Intent"; //$NON-NLS-1$
    public final static String CLASS_CONTEXT = "android.content.Context"; //$NON-NLS-1$
    public final static String CLASS_VIEW = "android.view.View"; //$NON-NLS-1$
    public final static String CLASS_VIEWGROUP = "android.view.ViewGroup"; //$NON-NLS-1$
    public final static String CLASS_NAME_LAYOUTPARAMS = "LayoutParams"; //$NON-NLS-1$
    public final static String CLASS_VIEWGROUP_LAYOUTPARAMS =
CLASS_VIEWGROUP + "$" + CLASS_NAME_LAYOUTPARAMS; //$NON-NLS-1$
    public final static String CLASS_NAME_FRAMELAYOUT = "FrameLayout"; //$NON-NLS-1$
    public final static String CLASS_FRAMELAYOUT =
"android.widget." + CLASS_NAME_FRAMELAYOUT; //$NON-NLS-1$
    public final static String CLASS_PREFERENCE = "android.preference.Preference"; //$NON-NLS-1$
    public final static String CLASS_NAME_PREFERENCE_SCREEN = "PreferenceScreen"; //$NON-NLS-1$
    public final static String CLASS_PREFERENCES =
"android.preference." + CLASS_NAME_PREFERENCE_SCREEN; //$NON-NLS-1$
    public final static String CLASS_PREFERENCEGROUP = "android.preference.PreferenceGroup"; //$NON-NLS-1$
    public final static String CLASS_PARCELABLE = "android.os.Parcelable"; //$NON-NLS-1$
public static final String CLASS_FRAGMENT = "android.app.Fragment"; //$NON-NLS-1$
public static final String CLASS_V4_FRAGMENT = "android.support.v4.app.Fragment"; //$NON-NLS-1$
/** MockView is part of the layoutlib bridge and used to display classes that have
* no rendering in the graphical layout editor. */
    public final static String CLASS_MOCK_VIEW = "com.android.layoutlib.bridge.MockView"; //$NON-NLS-1$



/** Returns the appropriate name for the 'android' command, which is 'android.exe' for
* Windows and 'android' for all other platforms. */
//Synthetic comment -- @@ -514,4 +512,613 @@
return nonWindowsExtension;
}
}
}








//Synthetic comment -- diff --git a/common/src/com/android/utils/XmlUtils.java b/common/src/com/android/utils/XmlUtils.java
//Synthetic comment -- index 7b51bc0..73e1d11 100644

//Synthetic comment -- @@ -15,6 +15,18 @@
*/
package com.android.utils;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

//Synthetic comment -- @@ -27,39 +39,6 @@

/** XML Utilities */
public class XmlUtils {
    /** Namespace used in XML files for Android attributes */
    public static final String ANDROID_URI =
            "http://schemas.android.com/apk/res/android";                    //$NON-NLS-1$
    /** Namespace used in XML files for Android Tooling attributes */
    public static final String TOOLS_URI =
            "http://schemas.android.com/tools";                              //$NON-NLS-1$
    /** URI of the reserved "xmlns"  prefix */
    public final static String XMLNS_URI = "http://www.w3.org/2000/xmlns/";  //$NON-NLS-1$
    /** The "xmlns" attribute name */
    public static final String XMLNS = "xmlns";                              //$NON-NLS-1$
    /** The default prefix used for the {@link #XMLNS_URI} */
    public static final String XMLNS_PREFIX = "xmlns:";                      //$NON-NLS-1$
    /** Qualified name of the xmlns android declaration element */
    public static final String XMLNS_ANDROID = "xmlns:android";              //$NON-NLS-1$
    /** The default prefix used for the {@link #ANDROID_URI} name space */
    public static final String ANDROID_NS_NAME = "android";                  //$NON-NLS-1$
    /** The default prefix used for the {@link #ANDROID_URI} name space including the colon  */
    public static final String ANDROID_NS_NAME_PREFIX = "android:";          //$NON-NLS-1$
    /** The default prefix used for the app */
    private static final String APP_PREFIX = "app";                          //$NON-NLS-1$
    /** The "xmlns:" attribute prefix used for namespace declarations */
    public static final String XMLNS_COLON = "xmlns:";                       //$NON-NLS-1$
    /** The entity for the ampersand character */
    public static final String AMP_ENTITY = "&amp;";                         //$NON-NLS-1$
    /** The entity for the quote character */
    public static final String QUOT_ENTITY = "&quot;";                       //$NON-NLS-1$
    /** The entity for the apostrophe character */
    public static final String APOS_ENTITY = "&apos;";                       //$NON-NLS-1$
    /** The entity for the less than character */
    public static final String LT_ENTITY = "&lt;";                           //$NON-NLS-1$
    /** The entity for the greater than character */
    public static final String GT_ENTITY = "&gt;";                           //$NON-NLS-1$

/**
* Returns the namespace prefix matching the requested namespace URI.
* If no such declaration is found, returns the default "android" prefix for
//Synthetic comment -- @@ -67,7 +46,7 @@
*
* @param node The current node. Must not be null.
* @param nsUri The namespace URI of which the prefix is to be found,
     *              e.g. SdkConstants.NS_RESOURCES
* @return The first prefix declared or the default "android" prefix
*              (or "app" for non-Android URIs)
*/
//Synthetic comment -- @@ -83,7 +62,7 @@
*
* @param node The current node. Must not be null.
* @param nsUri The namespace URI of which the prefix is to be found,
     *              e.g. SdkConstants.NS_RESOURCES
* @param defaultPrefix The default prefix (root) to use if the namespace
*              is not found. If null, do not create a new namespace
*              if this URI is not defined for the document.
//Synthetic comment -- @@ -94,7 +73,7 @@
@Nullable Node node, @Nullable String nsUri, @Nullable String defaultPrefix) {
// Note: Node.lookupPrefix is not implemented in wst/xml/core NodeImpl.java
// The following code emulates this simple call:
        //   String prefix = node.lookupPrefix(SdkConstants.NS_RESOURCES);

// if the requested URI is null, it denotes an attribute with no namespace.
if (nsUri == null) {








//Synthetic comment -- diff --git a/common/tests/src/com/android/utils/XmlUtilsTest.java b/common/tests/src/com/android/utils/XmlUtilsTest.java
//Synthetic comment -- index a4758a5..6c28451 100644

//Synthetic comment -- @@ -15,6 +15,7 @@
*/
package com.android.utils;

import com.android.utils.XmlUtils;

import org.w3c.dom.Attr;
//Synthetic comment -- @@ -36,9 +37,9 @@
DocumentBuilder builder = factory.newDocumentBuilder();
Document document = builder.newDocument();
Element rootElement = document.createElement("root");
        Attr attr = document.createAttributeNS(XmlUtils.XMLNS_URI,
"xmlns:customPrefix");
        attr.setValue(XmlUtils.ANDROID_URI);
rootElement.getAttributes().setNamedItemNS(attr);
document.appendChild(rootElement);
Element root = document.getDocumentElement();
//Synthetic comment -- @@ -51,7 +52,7 @@
Element baz = document.createElement("baz");
root.appendChild(baz);

        String prefix = XmlUtils.lookupNamespacePrefix(baz, XmlUtils.ANDROID_URI);
assertEquals("customPrefix", prefix);

prefix = XmlUtils.lookupNamespacePrefix(baz,








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/AbsoluteLayoutRule.java
//Synthetic comment -- index 0ed2b63..3ec3b5f 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_X;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_Y;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseLayoutRule.java
//Synthetic comment -- index 8fc1ed6..df2c8f4 100644

//Synthetic comment -- @@ -16,42 +16,42 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_X;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_Y;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/BaseViewRule.java
//Synthetic comment -- index a53859d..a7dd7d4 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HINT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_STYLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.DOT_LAYOUT_PARAMS;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -638,7 +638,7 @@
if (!definedBy.endsWith(DOT_LAYOUT_PARAMS)) {
continue;
}
                } else if (!id.startsWith(ATTR_LAYOUT_PREFIX)) {
continue;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/CalendarViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/CalendarViewRule.java
//Synthetic comment -- index 18be1b2..91684e2 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/DialerFilterRule.java
//Synthetic comment -- index 6736849..606bbd8 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EDIT_TEXT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/EditTextRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/EditTextRule.java
//Synthetic comment -- index e80fd27..03a5bc0 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_EMS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.REQUEST_FOCUS;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FragmentRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FragmentRule.java
//Synthetic comment -- index 74fe2b7..e809d00 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/FrameLayoutRule.java
//Synthetic comment -- index ab5ad15..0f90962 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GravityHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GravityHelper.java
//Synthetic comment -- index db9ab9f..4b8154b 100644

//Synthetic comment -- @@ -15,18 +15,18 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_CENTER;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_TOP;
import static com.android.utils.XmlUtils.ANDROID_URI;

import org.w3c.dom.Element;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridLayoutRule.java
//Synthetic comment -- index 40063f2..a197e23 100644

//Synthetic comment -- @@ -16,20 +16,20 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/GridViewRule.java
//Synthetic comment -- index bd29ea6..b82f391 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/HorizontalScrollViewRule.java
//Synthetic comment -- index a288933..7229490 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageButtonRule.java
//Synthetic comment -- index 78a0b32..9907950 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ImageViewRule.java
//Synthetic comment -- index 4246aa6..bc0184c 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IncludeRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/IncludeRule.java
//Synthetic comment -- index 978455a..fcb1a6d 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LayoutConstants.java
deleted file mode 100644
//Synthetic comment -- index ec9c435..0000000

//Synthetic comment -- @@ -1,283 +0,0 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.common.layout;

import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

/**
 * A bunch of constants that map to either:
 * <ul>
 * <li>Android Layouts XML element names (Linear, Relative, Absolute, etc.)
 * <li>Attributes for layout XML elements.
 * <li>Values for attributes.
 * </ul>
 */
public class LayoutConstants {
    /** The element name in a {@code <view class="...">} element. */
    public static final String VIEW = "view";                           //$NON-NLS-1$

    /** The attribute name in a {@code <view class="...">} element. */
    public static final String ATTR_CLASS = "class";                    //$NON-NLS-1$
    public static final String ATTR_ON_CLICK = "onClick";               //$NON-NLS-1$
    public static final String ATTR_TAG = "tag";                        //$NON-NLS-1$
    public static final String ATTR_NUM_COLUMNS = "numColumns";         //$NON-NLS-1$
    public static final String ATTR_PADDING = "padding";                //$NON-NLS-1$

    // Some common layout element names
    public static final String RELATIVE_LAYOUT = "RelativeLayout";      //$NON-NLS-1$
    public static final String LINEAR_LAYOUT   = "LinearLayout";        //$NON-NLS-1$
    public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";      //$NON-NLS-1$
    public static final String TABLE_LAYOUT = "TableLayout";            //$NON-NLS-1$
    public static final String TABLE_ROW = "TableRow";                  //$NON-NLS-1$
    public static final String CALENDAR_VIEW = "CalendarView";          //$NON-NLS-1$
    public static final String LIST_VIEW = "ListView";                  //$NON-NLS-1$
    public static final String EDIT_TEXT = "EditText";                  //$NON-NLS-1$
    public static final String GALLERY = "Gallery";                     //$NON-NLS-1$
    public static final String GRID_LAYOUT = "GridLayout";              //$NON-NLS-1$
    public static final String GRID_VIEW = "GridView";                  //$NON-NLS-1$
    public static final String SPINNER = "Spinner";                     //$NON-NLS-1$
    public static final String SCROLL_VIEW = "ScrollView";              //$NON-NLS-1$
    public static final String RADIO_BUTTON = "RadioButton";            //$NON-NLS-1$
    public static final String RADIO_GROUP = "RadioGroup";              //$NON-NLS-1$
    public static final String SPACE = "Space";                         //$NON-NLS-1$
    public static final String EXPANDABLE_LIST_VIEW = "ExpandableListView";//$NON-NLS-1$
    public static final String GESTURE_OVERLAY_VIEW = "GestureOverlayView";//$NON-NLS-1$
    public static final String HORIZONTAL_SCROLL_VIEW = "HorizontalScrollView"; //$NON-NLS-1$

    public static final String ATTR_CONTENT_DESCRIPTION = "contentDescription"; //$NON-NLS-1$
    public static final String ATTR_TEXT = "text";                      //$NON-NLS-1$
    public static final String ATTR_HINT = "hint";                      //$NON-NLS-1$
    public static final String ATTR_ID = "id";                          //$NON-NLS-1$
    public static final String ATTR_INPUT_TYPE = "inputType";           //$NON-NLS-1$
    public static final String ATTR_STYLE = "style";                    //$NON-NLS-1$
    public static final String ATTR_HANDLE = "handle";                  //$NON-NLS-1$
    public static final String ATTR_CONTENT = "content";                //$NON-NLS-1$
    public static final String ATTR_CHECKED = "checked";                //$NON-NLS-1$
    public static final String ATTR_BACKGROUND = "background";          //$NON-NLS-1$

    public static final String ATTR_LAYOUT_PREFIX = "layout_";          //$NON-NLS-1$
    public static final String ATTR_LAYOUT_HEIGHT = "layout_height";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_WIDTH = "layout_width";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_GRAVITY = "layout_gravity";  //$NON-NLS-1$
    public static final String ATTR_LAYOUT_WEIGHT = "layout_weight";    //$NON-NLS-1$

    public static final String ATTR_LAYOUT_MARGIN = "layout_margin";               //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_LEFT = "layout_marginLeft";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_RIGHT = "layout_marginRight";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_TOP = "layout_marginTop";        //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_BOTTOM = "layout_marginBottom";  //$NON-NLS-1$

    // TextView
    public static final String ATTR_DRAWABLE_RIGHT = "drawableRight";              //$NON-NLS-1$
    public static final String ATTR_DRAWABLE_LEFT = "drawableLeft";                //$NON-NLS-1$
    public static final String ATTR_DRAWABLE_BOTTOM = "drawableBottom";            //$NON-NLS-1$
    public static final String ATTR_DRAWABLE_TOP = "drawableTop";                  //$NON-NLS-1$
    public static final String ATTR_DRAWABLE_PADDING = "drawablePadding";          //$NON-NLS-1$

    // RelativeLayout layout params:
    public static final String ATTR_LAYOUT_ALIGN_LEFT = "layout_alignLeft";        //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_RIGHT = "layout_alignRight";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_TOP = "layout_alignTop";          //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_BOTTOM = "layout_alignBottom";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_TOP = "layout_alignParentTop"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_BOTTOM = "layout_alignParentBottom"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_LEFT = "layout_alignParentLeft";//$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_RIGHT = "layout_alignParentRight";   //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING = "layout_alignWithParentIfMissing"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_BASELINE = "layout_alignBaseline"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_IN_PARENT = "layout_centerInParent"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_VERTICAL = "layout_centerVertical"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_HORIZONTAL = "layout_centerHorizontal"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_TO_RIGHT_OF = "layout_toRightOf";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_TO_LEFT_OF = "layout_toLeftOf";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_BELOW = "layout_below";              //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ABOVE = "layout_above";              //$NON-NLS-1$

    // GridLayout
    public static final String ATTR_ROW_COUNT = "rowCount";                         //$NON-NLS-1$
    public static final String ATTR_COLUMN_COUNT = "columnCount";                   //$NON-NLS-1$
    public static final String ATTR_USE_DEFAULT_MARGINS = "useDefaultMargins";      //$NON-NLS-1$
    public static final String ATTR_MARGINS_INCLUDED_IN_ALIGNMENT = "marginsIncludedInAlignment"; //$NON-NLS-1$

    // GridLayout layout params
    public static final String ATTR_LAYOUT_ROW = "layout_row";                      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ROW_SPAN = "layout_rowSpan";             //$NON-NLS-1$
    public static final String ATTR_LAYOUT_COLUMN = "layout_column";                //$NON-NLS-1$
    public static final String ATTR_LAYOUT_COLUMN_SPAN = "layout_columnSpan";       //$NON-NLS-1$

    public static final String ATTR_LAYOUT_Y = "layout_y";                      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_X = "layout_x";                      //$NON-NLS-1$
    public static final String ATTR_NAME = "name";                              //$NON-NLS-1$

    public static final String VALUE_WRAP_CONTENT = "wrap_content";             //$NON-NLS-1$
    public static final String VALUE_FILL_PARENT = "fill_parent";               //$NON-NLS-1$
    public static final String VALUE_TRUE = "true";                             //$NON-NLS-1$
    public static final String VALUE_FALSE= "false";                            //$NON-NLS-1$
    public static final String VALUE_N_DP = "%ddp";                             //$NON-NLS-1$
    public static final String VALUE_ZERO_DP = "0dp";                           //$NON-NLS-1$
    public static final String VALUE_ONE_DP = "1dp";                            //$NON-NLS-1$
    public static final String VALUE_TOP = "top";                               //$NON-NLS-1$
    public static final String VALUE_LEFT = "left";                             //$NON-NLS-1$
    public static final String VALUE_RIGHT = "right";                           //$NON-NLS-1$
    public static final String VALUE_BOTTOM = "bottom";                         //$NON-NLS-1$
    public static final String VALUE_CENTER_VERTICAL = "center_vertical";       //$NON-NLS-1$
    public static final String VALUE_CENTER_HORIZONTAL = "center_horizontal";   //$NON-NLS-1$
    public static final String VALUE_FILL_HORIZONTAL = "fill_horizontal";       //$NON-NLS-1$
    public static final String VALUE_FILL_VERTICAL = "fill_vertical";           //$NON-NLS-1$
    public static final String VALUE_0 = "0";                                   //$NON-NLS-1$
    public static final String VALUE_1 = "1";                                   //$NON-NLS-1$

    // Gravity values. These have the GRAVITY_ prefix in front of value because we already
    // have VALUE_CENTER_HORIZONTAL defined for layouts, and its definition conflicts
    // (centerHorizontal versus center_horizontal)
    public static final String GRAVITY_VALUE_ = "center";                             //$NON-NLS-1$
    public static final String GRAVITY_VALUE_CENTER = "center";                       //$NON-NLS-1$
    public static final String GRAVITY_VALUE_RIGHT = "right";                         //$NON-NLS-1$
    public static final String GRAVITY_VALUE_LEFT = "left";                           //$NON-NLS-1$
    public static final String GRAVITY_VALUE_BOTTOM = "bottom";                       //$NON-NLS-1$
    public static final String GRAVITY_VALUE_TOP = "top";                             //$NON-NLS-1$
    public static final String GRAVITY_VALUE_FILL_HORIZONTAL = "fill_horizontal";     //$NON-NLS-1$
    public static final String GRAVITY_VALUE_FILL_VERTICAL = "fill_vertical";         //$NON-NLS-1$
    public static final String GRAVITY_VALUE_CENTER_HORIZONTAL = "center_horizontal"; //$NON-NLS-1$
    public static final String GRAVITY_VALUE_CENTER_VERTICAL = "center_vertical";     //$NON-NLS-1$
    public static final String GRAVITY_VALUE_FILL = "fill";                           //$NON-NLS-1$

    /**
     * The top level android package as a prefix, "android.".
     */
    public static final String ANDROID_PKG_PREFIX = ANDROID_PKG + '.';
    public static final String ANDROID_SUPPORT_PKG_PREFIX = ANDROID_PKG_PREFIX + "support."; //$NON-NLS-1$

    /** The android.view. package prefix */
    public static final String ANDROID_VIEW_PKG = ANDROID_PKG_PREFIX + "view."; //$NON-NLS-1$

    /** The android.widget. package prefix */
    public static final String ANDROID_WIDGET_PREFIX = ANDROID_PKG_PREFIX + "widget."; //$NON-NLS-1$

    /** The android.webkit. package prefix */
    public static final String ANDROID_WEBKIT_PKG = ANDROID_PKG_PREFIX + "webkit."; //$NON-NLS-1$

    /** The LayoutParams inner-class name suffix, .LayoutParams */
    public static final String DOT_LAYOUT_PARAMS = ".LayoutParams"; //$NON-NLS-1$

    /** The fully qualified class name of an EditText view */
    public static final String FQCN_EDIT_TEXT = "android.widget.EditText"; //$NON-NLS-1$

    /** The fully qualified class name of a LinearLayout view */
    public static final String FQCN_LINEAR_LAYOUT = "android.widget.LinearLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a RelativeLayout view */
    public static final String FQCN_RELATIVE_LAYOUT = "android.widget.RelativeLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a RelativeLayout view */
    public static final String FQCN_GRID_LAYOUT = "android.widget.GridLayout"; //$NON-NLS-1$
    public static final String FQCN_GRID_LAYOUT_V7 = "android.support.v7.widget.GridLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a FrameLayout view */
    public static final String FQCN_FRAME_LAYOUT = "android.widget.FrameLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a TableRow view */
    public static final String FQCN_TABLE_ROW = "android.widget.TableRow"; //$NON-NLS-1$

    /** The fully qualified class name of a TableLayout view */
    public static final String FQCN_TABLE_LAYOUT = "android.widget.TableLayout"; //$NON-NLS-1$

    /** The fully qualified class name of a GridView view */
    public static final String FQCN_GRID_VIEW = "android.widget.GridView"; //$NON-NLS-1$

    /** The fully qualified class name of a TabWidget view */
    public static final String FQCN_TAB_WIDGET = "android.widget.TabWidget"; //$NON-NLS-1$

    /** The fully qualified class name of a Button view */
    public static final String FQCN_BUTTON = "android.widget.Button"; //$NON-NLS-1$

    /** The fully qualified class name of a RadioButton view */
    public static final String FQCN_RADIO_BUTTON = "android.widget.RadioButton"; //$NON-NLS-1$

    /** The fully qualified class name of a ToggleButton view */
    public static final String FQCN_TOGGLE_BUTTON = "android.widget.ToggleButton"; //$NON-NLS-1$

    /** The fully qualified class name of a Spinner view */
    public static final String FQCN_SPINNER = "android.widget.Spinner"; //$NON-NLS-1$

    /** The fully qualified class name of an AdapterView */
    public static final String FQCN_ADAPTER_VIEW = "android.widget.AdapterView"; //$NON-NLS-1$

    /** The fully qualified class name of a ListView */
    public static final String FQCN_LIST_VIEW = "android.widget.ListView"; //$NON-NLS-1$

    /** The fully qualified class name of an ExpandableListView */
    public static final String FQCN_EXPANDABLE_LIST_VIEW = "android.widget.ExpandableListView"; //$NON-NLS-1$

    /** The fully qualified class name of a GestureOverlayView */
    public static final String FQCN_GESTURE_OVERLAY_VIEW = "android.gesture.GestureOverlayView"; //$NON-NLS-1$

    /** The fully qualified class name of a DatePicker */
    public static final String FQCN_DATE_PICKER = "android.widget.DatePicker"; //$NON-NLS-1$

    /** The fully qualified class name of a TimePicker */
    public static final String FQCN_TIME_PICKER = "android.widget.TimePicker"; //$NON-NLS-1$

    /** The fully qualified class name of a RadioGroup */
    public static final String FQCN_RADIO_GROUP = "android.widgets.RadioGroup";  //$NON-NLS-1$

    /** The fully qualified class name of a Space */
    public static final String FQCN_SPACE = "android.widget.Space"; //$NON-NLS-1$
    public static final String FQCN_SPACE_V7 = "android.support.v7.widget.Space"; //$NON-NLS-1$

    /** The fully qualified class name of a TextView view */
    public static final String FQCN_TEXT_VIEW = "android.widget.TextView"; //$NON-NLS-1$

    /** The fully qualified class name of an ImageView view */
    public static final String FQCN_IMAGE_VIEW = "android.widget.ImageView"; //$NON-NLS-1$

    public static final String ATTR_SRC = "src"; //$NON-NLS-1$

    // like fill_parent for API 8
    public static final String VALUE_MATCH_PARENT = "match_parent"; //$NON-NLS-1$

    public static final String ATTR_GRAVITY = "gravity"; //$NON-NLS-1$
    public static final String ATTR_WEIGHT_SUM = "weightSum"; //$NON-NLS-1$
    public static final String ATTR_BASELINE_ALIGNED = "baselineAligned"; //$NON-NLS-1$
    public static String ATTR_ORIENTATION = "orientation"; //$NON-NLS-1$
    public static String ATTR_EMS = "ems"; //$NON-NLS-1$

    public static String VALUE_HORIZONTAL = "horizontal"; //$NON-NLS-1$

    public static String VALUE_VERTICAL = "vertical"; //$NON-NLS-1$

    /** The prefix for new id attribute values, @+id/ */
    public static String NEW_ID_PREFIX = "@+id/"; //$NON-NLS-1$

    /** The prefix for existing id attribute values, @id/ */
    public static String ID_PREFIX = "@id/"; //$NON-NLS-1$

    /** Prefix for resources that reference layouts */
    public static String LAYOUT_PREFIX = "@layout/"; //$NON-NLS-1$

    /** Prefix for resources that reference drawables */
    public static String DRAWABLE_PREFIX = "@drawable/"; //$NON-NLS-1$

    /** Prefix for resources that reference strings */
    public static String STRING_PREFIX = "@string/"; //$NON-NLS-1$

    /** Prefix for resources that reference Android strings */
    public static String ANDROID_STRING_PREFIX = "@android:string/"; //$NON-NLS-1$

    /** Prefix for resources that reference Android layouts */
    public static String ANDROID_LAYOUT_PREFIX = "@android:layout/"; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/LinearLayoutRule.java
//Synthetic comment -- index 48a7e83..e6f4a4e 100644

//Synthetic comment -- @@ -16,20 +16,20 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_BASELINE_ALIGNED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_WEIGHT_SUM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_1;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ZERO_DP;
import static com.android.ide.eclipse.adt.AdtUtils.formatFloatAttribute;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ListViewRule.java
//Synthetic comment -- index 31d6259..70728c8 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/MapViewRule.java
//Synthetic comment -- index 6106b05..006661e 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.layout;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RadioGroupRule.java
//Synthetic comment -- index 30e6bfc..c9aa207 100644

//Synthetic comment -- @@ -15,11 +15,13 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CHECKED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -37,7 +39,7 @@

if (insertType.isCreate()) {
for (int i = 0; i < 3; i++) {
                INode handle = node.appendChild(LayoutConstants.FQCN_RADIO_BUTTON);
handle.setAttribute(ANDROID_URI, ATTR_ID, String.format("@+id/radio%d", i));
if (i == 0) {
handle.setAttribute(ANDROID_URI, ATTR_CHECKED, VALUE_TRUE);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/RelativeLayoutRule.java
//Synthetic comment -- index 850d017..b4bc869 100644

//Synthetic comment -- @@ -16,27 +16,27 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -118,8 +118,8 @@
if (a != null && a.length() > 0) {
// Display the layout parameters without the leading layout_ prefix
// and id references without the @+id/ prefix
            if (propertyName.startsWith(ATTR_LAYOUT_PREFIX)) {
                propertyName = propertyName.substring(ATTR_LAYOUT_PREFIX.length());
}
a = stripIdPrefix(a);
String s = propertyName + ": " + a;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ResizeState.java
//Synthetic comment -- index e95957a..42b9083 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -128,4 +128,4 @@
return String.format(VALUE_N_DP, mRule.mRulesEngine.pxToDp(bounds.h));
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ScrollViewRule.java
//Synthetic comment -- index 8ab5682..1dafe53 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SeekBarRule.java
//Synthetic comment -- index abafb53..b88f8ab 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/SlidingDrawerRule.java
//Synthetic comment -- index fd5bcf9..e4267bb 100644

//Synthetic comment -- @@ -15,14 +15,16 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HANDLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
//Synthetic comment -- @@ -52,12 +54,12 @@
node.setAttribute(ANDROID_URI, ATTR_CONTENT, contentId);

// Handle
            INode handle = node.appendChild(LayoutConstants.FQCN_BUTTON);
handle.setAttribute(ANDROID_URI, ATTR_TEXT, "Handle");
handle.setAttribute(ANDROID_URI, ATTR_ID, handleId);

// Content
            INode content = node.appendChild(LayoutConstants.FQCN_LINEAR_LAYOUT);
content.setAttribute(ANDROID_URI, ATTR_ID, contentId);
content.setAttribute(ANDROID_URI, ATTR_LAYOUT_WIDTH, matchParent);
content.setAttribute(ANDROID_URI, ATTR_LAYOUT_HEIGHT, matchParent);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TabHostRule.java
//Synthetic comment -- index 21c368f..cb2153b 100644

//Synthetic comment -- @@ -16,16 +16,16 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_FRAME_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TAB_WIDGET;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableLayoutRule.java
//Synthetic comment -- index b2cb1e4..b6aeeb4 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_ROW;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/TableRowRule.java
//Synthetic comment -- index af6f7a0..6e3f202 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_LAYOUT;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/WebViewRule.java
//Synthetic comment -- index 95d532e..42b06e6 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/ZoomButtonRule.java
//Synthetic comment -- index af38913..66cbd45 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridDropHandler.java
//Synthetic comment -- index e01e045..8a6fdef 100644

//Synthetic comment -- @@ -20,12 +20,12 @@
import static com.android.ide.common.layout.GridLayoutRule.MARGIN_SIZE;
import static com.android.ide.common.layout.GridLayoutRule.MAX_CELL_DIFFERENCE;
import static com.android.ide.common.layout.GridLayoutRule.SHORT_GAP_DP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.grid.GridModel.UNDEFINED;
import static java.lang.Math.abs;

//Synthetic comment -- @@ -753,4 +753,4 @@
public GridModel getGrid() {
return mGrid;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/grid/GridModel.java
//Synthetic comment -- index fe38c42..fa9a11f 100644

//Synthetic comment -- @@ -15,34 +15,33 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.GravityHelper.GRAVITY_BOTTOM;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_HORIZ;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_CENTER_VERT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ROW_COUNT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.SPACE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.VALUE_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TOP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ConstraintType.java
//Synthetic comment -- index bb32086..ed4ac1b 100644

//Synthetic comment -- @@ -23,22 +23,22 @@
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.api.SegmentType.UNKNOWN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -238,4 +238,4 @@

return null;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DeletionHandler.java
//Synthetic comment -- index 30f12c0..3eac510 100644

//Synthetic comment -- @@ -15,21 +15,21 @@
*/
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.BaseViewRule.stripIdPrefix;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.relative.ConstraintType.LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.relative.ConstraintType.LAYOUT_CENTER_VERTICAL;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.INode.IAttribute;
import com.android.ide.common.layout.LayoutConstants;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

//Synthetic comment -- @@ -42,7 +42,7 @@
* deleted nodes
* <p>
* TODO: Consider adding the
 * {@link LayoutConstants#ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING} attribute to a
* node if it's pointing to a node which is deleted and which has no transitive
* reference to another node.
*/
//Synthetic comment -- @@ -96,7 +96,7 @@

@Nullable
private static String getId(@NonNull IAttribute attribute) {
        if (attribute.getName().startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attribute.getUri())
&& !attribute.getName().startsWith(ATTR_LAYOUT_MARGIN)) {
String id = attribute.getValue();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DependencyGraph.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/DependencyGraph.java
//Synthetic comment -- index 9a2eeaa..43d52d1 100644

//Synthetic comment -- @@ -15,11 +15,13 @@
*/
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.INode;
//Synthetic comment -- @@ -314,8 +316,8 @@
}

private static String stripLayoutAttributePrefix(String name) {
            if (name.startsWith(ATTR_LAYOUT_PREFIX)) {
                return name.substring(ATTR_LAYOUT_PREFIX.length());
}

return name;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelineHandler.java
//Synthetic comment -- index 135cabe..db08b18 100644

//Synthetic comment -- @@ -26,33 +26,35 @@
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.BaseLayoutRule.getMaxMatchDistance;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.relative.ConstraintType.ALIGN_BASELINE;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelinePainter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/GuidelinePainter.java
//Synthetic comment -- index 46038ee..2fe7476 100644

//Synthetic comment -- @@ -15,13 +15,13 @@
*/
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;

import com.android.annotations.NonNull;
import com.android.ide.common.api.DrawingStyle;
//Synthetic comment -- @@ -153,8 +153,8 @@
// usually not a prefix of the value (for example, 'layout_alignBottom=@+id/foo').
String constraint = m.getConstraint(false /* generateId */);
String description = constraint.replace(NEW_ID_PREFIX, "").replace(ID_PREFIX, "");
        if (description.startsWith(ATTR_LAYOUT_PREFIX)) {
            description = description.substring(ATTR_LAYOUT_PREFIX.length());
}
if (margin > 0) {
int dp = state.getRulesEngine().pxToDp(margin);
//Synthetic comment -- @@ -205,4 +205,4 @@
gc.drawLine(points.get(i-1), points.get(i));
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/Match.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/Match.java
//Synthetic comment -- index 9c998f2..6f3f0d0 100644

//Synthetic comment -- @@ -15,10 +15,12 @@
*/
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.Segment;

/** A match is a potential pairing of two segments with a given {@link ConstraintType}. */








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/MoveHandler.java
//Synthetic comment -- index cc1953a..0fa915d 100644

//Synthetic comment -- @@ -23,10 +23,12 @@
import static com.android.ide.common.api.SegmentType.LEFT;
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.IDragElement;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/layout/relative/ResizeHandler.java
//Synthetic comment -- index 0e44724..a5e071d 100644

//Synthetic comment -- @@ -23,10 +23,12 @@
import static com.android.ide.common.api.SegmentType.LEFT;
import static com.android.ide.common.api.SegmentType.RIGHT;
import static com.android.ide.common.api.SegmentType.TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static java.lang.Math.abs;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/common/resources/platform/AttrsXmlParser.java
//Synthetic comment -- index 6549361..3c1fa97 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.resources.platform;

import static com.android.ide.common.layout.LayoutConstants.DOT_LAYOUT_PARAMS;
import static com.android.ide.eclipse.adt.AdtConstants.DOC_HIDE;

import com.android.ide.common.api.IAttributeInfo.Format;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtConstants.java
//Synthetic comment -- index 2e97d93..e9cee47 100644

//Synthetic comment -- @@ -16,7 +16,11 @@

package com.android.ide.eclipse.adt;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
import com.android.ide.eclipse.adt.internal.build.builders.PreCompilerBuilder;
//Synthetic comment -- @@ -68,87 +72,12 @@
/** Separator character for workspace path, i.e. '/'. */
public final static char WS_SEP_CHAR = '/';

    /** Extension of the Application package Files, i.e. "apk". */
    public final static String EXT_ANDROID_PACKAGE = "apk"; //$NON-NLS-1$
    /** Extension of java files, i.e. "java" */
    public final static String EXT_JAVA = "java"; //$NON-NLS-1$
    /** Extension of compiled java files, i.e. "class" */
    public final static String EXT_CLASS = "class"; //$NON-NLS-1$
    /** Extension of xml files, i.e. "xml" */
    public final static String EXT_XML = "xml"; //$NON-NLS-1$
    /** Extension of jar files, i.e. "jar" */
    public final static String EXT_JAR = "jar"; //$NON-NLS-1$
    /** Extension of aidl files, i.e. "aidl" */
    public final static String EXT_AIDL = "aidl"; //$NON-NLS-1$
    /** Extension of Renderscript files, i.e. "rs" */
    public final static String EXT_RS = "rs"; //$NON-NLS-1$
    /** Extension of dependency files, i.e. "d" */
    public final static String EXT_DEP = "d"; //$NON-NLS-1$
    /** Extension of native libraries, i.e. "so" */
    public final static String EXT_NATIVE_LIB = "so"; //$NON-NLS-1$
    /** Extension of dex files, i.e. "dex" */
    public final static String EXT_DEX = "dex"; //$NON-NLS-1$
    /** Extension for temporary resource files, ie "ap_ */
    public final static String EXT_RES = "ap_"; //$NON-NLS-1$
    /** Extension for pre-processable images. Right now pngs */
    public final static String EXT_PNG = "png"; //$NON-NLS-1$

    private final static String DOT = "."; //$NON-NLS-1$

    /** Dot-Extension of the Application package Files, i.e. ".apk". */
    public final static String DOT_ANDROID_PACKAGE = DOT + EXT_ANDROID_PACKAGE;
    /** Dot-Extension of java files, i.e. ".java" */
    public final static String DOT_JAVA = DOT + EXT_JAVA;
    /** Dot-Extension of compiled java files, i.e. ".class" */
    public final static String DOT_CLASS = DOT + EXT_CLASS;
    /** Dot-Extension of xml files, i.e. ".xml" */
    public final static String DOT_XML = DOT + EXT_XML;
    /** Dot-Extension of jar files, i.e. ".jar" */
    public final static String DOT_JAR = DOT + EXT_JAR;
    /** Dot-Extension of aidl files, i.e. ".aidl" */
    public final static String DOT_AIDL = DOT + EXT_AIDL;
    /** Dot-Extension of renderscript files, i.e. ".rs" */
    public final static String DOT_RS = DOT + EXT_RS;
    /** Dot-Extension of dependency files, i.e. ".d" */
    public final static String DOT_DEP = DOT + EXT_DEP;
    /** Dot-Extension of dex files, i.e. ".dex" */
    public final static String DOT_DEX = DOT + EXT_DEX;
    /** Dot-Extension for temporary resource files, ie "ap_ */
    public final static String DOT_RES = DOT + EXT_RES;
    /** Dot-Extension for PNG files, i.e. ".png" */
    public static final String DOT_PNG = ".png"; //$NON-NLS-1$
    /** Dot-Extension for 9-patch files, i.e. ".9.png" */
    public static final String DOT_9PNG = ".9.png"; //$NON-NLS-1$
    /** Dot-Extension for GIF files, i.e. ".gif" */
    public static final String DOT_GIF = ".gif"; //$NON-NLS-1$
    /** Dot-Extension for JPEG files, i.e. ".jpg" */
    public static final String DOT_JPG = ".jpg"; //$NON-NLS-1$
    /** Dot-Extension for BMP files, i.e. ".bmp" */
    public static final String DOT_BMP = ".bmp"; //$NON-NLS-1$
    /** Dot-Extension for SVG files, i.e. ".svg" */
    public static final String DOT_SVG = ".svg"; //$NON-NLS-1$
    /** Dot-Extension for template files */
    public static final String DOT_FTL = ".ftl"; //$NON-NLS-1$
    /** Dot-Extension of text files, i.e. ".txt" */
    public final static String DOT_TXT = ".txt"; //$NON-NLS-1$

    /** Name of the android sources directory */
    public static final String FD_ANDROID_SOURCES = "sources"; //$NON-NLS-1$

    /** Resource base name for java files and classes */
    public final static String FN_RESOURCE_BASE = "R"; //$NON-NLS-1$
    /** Resource java class  filename, i.e. "R.java" */
    public final static String FN_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_JAVA;
    /** Resource class file  filename, i.e. "R.class" */
    public final static String FN_COMPILED_RESOURCE_CLASS = FN_RESOURCE_BASE + DOT_CLASS;
    /** Manifest java class filename, i.e. "Manifest.java" */
    public final static String FN_MANIFEST_CLASS = "Manifest.java"; //$NON-NLS-1$
    /** Temporary packaged resources file name, i.e. "resources.ap_" */
    public final static String FN_RESOURCES_AP_ = "resources.ap_"; //$NON-NLS-1$

/** aapt's proguard output */
public final static String FN_AAPT_PROGUARD = "proguard.txt"; //$NON-NLS-1$

public final static String FN_TRACEVIEW =
(SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_WINDOWS) ?
"traceview.bat" : "traceview"; //$NON-NLS-1$ //$NON-NLS-2$
//Synthetic comment -- @@ -171,7 +100,7 @@
public final static String WS_ASSETS = WS_SEP + SdkConstants.FD_ASSETS;

/** Absolute path of the layout folder, e.g. "/res/layout".<br> This is a workspace path. */
    public final static String WS_LAYOUTS = WS_RESOURCES + WS_SEP + AndroidConstants.FD_RES_LAYOUT;

/** Leaf of the javaDoc folder. Does not start with a separator. */
public final static String WS_JAVADOC_FOLDER_LEAF = SdkConstants.FD_DOCS + "/" + //$NON-NLS-1$
//Synthetic comment -- @@ -197,8 +126,8 @@
* <p/>
* This string contains a %s. It must be combined with the desired Java package, e.g.:
* <pre>
     *    String.format(AndroidConstants.NS_CUSTOM_RESOURCES, "android");
     *    String.format(AndroidConstants.NS_CUSTOM_RESOURCES, "com.test.mycustomapp");
* </pre>
*
* Note: if you need an URI specifically for the "android" namespace, consider using
//Synthetic comment -- @@ -208,9 +137,6 @@
// another CL.
public final static String NS_CUSTOM_RESOURCES = "http://schemas.android.com/apk/res/%1$s"; //$NON-NLS-1$

    /** The package "android" as used in resource urls etc */
    public static final String ANDROID_PKG = "android"; //$NON-NLS-1$

/** The old common plug-in ID. Please do not use for new features. */
private static final String LEGACY_PLUGIN_ID = "com.android.ide.eclipse.common"; //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index de043aa..692adc7 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import static com.android.SdkConstants.PLATFORM_LINUX;
import static com.android.SdkConstants.PLATFORM_WINDOWS;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -1583,7 +1582,7 @@
@Override
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
int kind, @Nullable String extension, int flags) {
                if (flags == IResourceDelta.MARKERS || !AdtConstants.EXT_XML.equals(extension)) {
// ONLY the markers changed, or not XML file: not relevant to this listener
return;
}
//Synthetic comment -- @@ -1603,7 +1602,7 @@
// we are inside a res/ folder, get the ResourceFolderType of the
// parent folder.
String[] folderSegments = file.getParent().getName().split(
                                AndroidConstants.RES_QUALIFIER_SEP);

// get the enum for the resource type.
ResourceFolderType type = ResourceFolderType.getTypeByName(








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtUtils.java
//Synthetic comment -- index eb80984..f5f7770 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt;

import static com.android.tools.lint.detector.api.LintConstants.HIGHEST_KNOWN_API;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -950,7 +950,7 @@
* @return the highest known API number
*/
public static int getHighestKnownApiLevel() {
        return HIGHEST_KNOWN_API;
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java
//Synthetic comment -- index 5ef7d7e..85f6992 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFix.java
//Synthetic comment -- index 5586c81..defaca6 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.build;

import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS_ANDROID;
import static com.android.utils.XmlUtils.XMLNS_URI;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/AidlProcessor.java
//Synthetic comment -- index 7f5ca7a..52e887a 100644

//Synthetic comment -- @@ -84,7 +84,7 @@

@Override
protected String getExtension() {
        return AdtConstants.EXT_AIDL;
}

@Override
//Synthetic comment -- @@ -390,7 +390,7 @@
String javaName;
if (replaceExt) {
javaName = sourceFile.getName().replaceAll(
                        AdtConstants.RE_AIDL_EXT, AdtConstants.DOT_JAVA);
} else {
javaName = sourceFile.getName();
}
//Synthetic comment -- @@ -441,7 +441,7 @@
// TODO: properly parse aidl file to determine type and generate dependency graphs.
//
//        String className = file.getName().substring(0,
//                file.getName().length() - AndroidConstants.DOT_AIDL.length());
//
//        InputStream input = file.getContents(true /* force*/);
//        try {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/BuildHelper.java
//Synthetic comment -- index 7e756bc..fe0863c 100644

//Synthetic comment -- @@ -961,7 +961,7 @@
// if it's a project we should just ignore it because it's going to be added
// later when we add all the referenced projects.

        } else if (AdtConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
// case of a jar file (which could be relative to the workspace or a full path)
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/RenderScriptProcessor.java
//Synthetic comment -- index aff02a3..5b58c4f 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.adt.internal.build;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -71,7 +70,7 @@
boolean r = super.handleGeneratedFile(file, kind);
if (r == false &&
kind == IResourceDelta.REMOVED &&
                    AdtConstants.EXT_DEP.equalsIgnoreCase(file.getFileExtension())) {
// This looks to be a dependency file.
// For future-proofness let's make sure this dependency file was generated by
// this processor even if it's the only processor using them for now.
//Synthetic comment -- @@ -87,7 +86,7 @@
relative = relative.removeLastSegments(1);
// add the file name of a Renderscript file.
relative = relative.append(file.getName().replaceAll(AdtConstants.RE_DEP_EXT,
                        AdtConstants.DOT_RS));

// now look for a match in the source folders.
List<IPath> sourceFolders = BaseProjectHelper.getSourceClasspaths(
//Synthetic comment -- @@ -123,7 +122,7 @@

@Override
protected String getExtension() {
        return AdtConstants.EXT_RS;
}

@Override
//Synthetic comment -- @@ -143,7 +142,7 @@
IFolder genFolder = getGenFolder();

IFolder rawFolder = project.getFolder(
                new Path(SdkConstants.FD_RES).append(AndroidConstants.FD_RES_RAW));

int depIndex;

//Synthetic comment -- @@ -440,7 +439,7 @@
private IFile getDependencyFileFor(IFile sourceFile) {
IFolder depFolder = getDependencyFolder(sourceFile);
return depFolder.getFile(sourceFile.getName().replaceAll(AdtConstants.RE_RS_EXT,
                AdtConstants.DOT_DEP));
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ChangedFileSetHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/ChangedFileSetHelper.java
//Synthetic comment -- index e8e55d5..67c7e8a 100644

//Synthetic comment -- @@ -120,7 +120,7 @@
String path = getRelativeJavaCOut(project);

ChangedFileSet set = new ChangedFileSet("compiledCode",                   //$NON-NLS-1$
                path + "/**/*" + AdtConstants.DOT_CLASS);                         //$NON-NLS-1$

return set;
}
//Synthetic comment -- @@ -154,8 +154,8 @@
String path = getRelativeJavaCOut(project);

ChangedFileSet set = new ChangedFileSet("classAndJars",                    //$NON-NLS-1$
                path + "/**/*" + AdtConstants.DOT_CLASS,                           //$NON-NLS-1$
                SdkConstants.FD_NATIVE_LIBS + "/*" + AdtConstants.DOT_JAR);        //$NON-NLS-1$

// output file is based on the project's android output folder
path = getRelativeAndroidOut(project);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 5911d79..5373002 100644

//Synthetic comment -- @@ -356,7 +356,7 @@
if (isLibrary) {
// check the jar output file is present, if not create it.
IFile jarIFile = androidOutputFolder.getFile(
                        project.getName().toLowerCase() + AdtConstants.DOT_JAR);
if (mConvertToDex == false && jarIFile.exists() == false) {
mConvertToDex = true;
}
//Synthetic comment -- @@ -746,7 +746,7 @@

public void addFile(IFile file, IFolder rootFolder) throws ApkCreationException {
// we only package class file from the output folder
            if (AdtConstants.EXT_CLASS.equals(file.getFileExtension()) == false) {
return;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerBuilder.java
//Synthetic comment -- index d5b1b9a..d4c8525 100644

//Synthetic comment -- @@ -995,7 +995,7 @@
// We actually need to delete the manifest.java as it may become empty and
// in this case aapt doesn't generate an empty one, but instead doesn't
// touch it.
        IFile manifestJavaFile = packageFolder.getFile(AdtConstants.FN_MANIFEST_CLASS);
manifestJavaFile.getLocation().toFile().delete();

// launch aapt: create the command line








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PreCompilerDeltaVisitor.java
//Synthetic comment -- index 2c4c13c..f868224 100644

//Synthetic comment -- @@ -275,8 +275,8 @@
String fileName = resource.getName();

// Special case of R.java/Manifest.java.
                if (AdtConstants.FN_RESOURCE_CLASS.equals(fileName) ||
                        AdtConstants.FN_MANIFEST_CLASS.equals(fileName)) {
// if it was removed, there's a possibility that it was removed due to a
// package change, or an aidl that was removed, but the only thing
// that will happen is that we'll have an extra build. Not much of a problem.
//Synthetic comment -- @@ -341,12 +341,12 @@
case IResourceDelta.ADDED:
// display verbose message
message = String.format(Messages.Added_s_s_Needs_Updating, p,
                            AdtConstants.FN_RESOURCE_CLASS);
break;
case IResourceDelta.REMOVED:
// display verbose message
message = String.format(Messages.s_Removed_s_Needs_Updating, p,
                            AdtConstants.FN_RESOURCE_CLASS);
break;
}
if (message != null) {
//Synthetic comment -- @@ -358,7 +358,7 @@
handler.handleResourceFile((IFile)resource, kind);
}
// If it's an XML resource, check the syntax
            if (AdtConstants.EXT_XML.equalsIgnoreCase(ext) && kind != IResourceDelta.REMOVED) {
// check xml Validity
mBuilder.checkXML(resource, this);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index af8de68..2ea70fc 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor.ATTRIBUTE_ICON_FILENAME;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_PX;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_SP;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
//Synthetic comment -- @@ -742,9 +742,9 @@
}
}

        if (!matches && word.startsWith(ATTR_LAYOUT_PREFIX, wordStart)
                && !prefix.startsWith(ATTR_LAYOUT_PREFIX, prefixStart)) {
            wordStart += ATTR_LAYOUT_PREFIX.length();

if (wordLength - wordStart < prefixLength - prefixStart) {
return false;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/Hyperlinks.java
//Synthetic comment -- index 8c15965..18135aa 100644

//Synthetic comment -- @@ -16,29 +16,28 @@

package com.android.ide.eclipse.adt.internal.editors;

import static com.android.SdkConstants.FD_DOCS;
import static com.android.SdkConstants.FD_DOCS_REFERENCE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ON_CLICK;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VIEW;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.FN_RESOURCE_BASE;
import static com.android.ide.eclipse.adt.AdtConstants.FN_RESOURCE_CLASS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.ROOT_ELEMENT;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.STYLE_ELEMENT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_STYLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.STYLE_RESOURCE_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.xml.AndroidManifest.NODE_ACTIVITY;
//Synthetic comment -- @@ -57,7 +56,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -215,7 +213,7 @@
}

String value = attribute.getValue();
        if (value.startsWith(NEW_ID_RESOURCE_PREFIX)) {
// It's a value -declaration-, nowhere else to jump
// (though we could consider jumping to the R-file; would that
// be helpful?)
//Synthetic comment -- @@ -329,7 +327,7 @@
/** Returns true if this represents a style attribute */
private static boolean isStyleAttribute(XmlContext context) {
String tag = context.getElement().getTagName();
        return STYLE_ELEMENT.equals(tag);
}

/**
//Synthetic comment -- @@ -508,7 +506,7 @@
public static String getTagName(ResourceType type) {
if (type == ResourceType.ID) {
// Ids are recorded in <item> tags instead of <id> tags
            return ValuesDescriptors.ITEM_TAG;
}

return type.getName();
//Synthetic comment -- @@ -629,7 +627,7 @@
IType activityType = null;
IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
if (javaProject != null) {
                activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
if (activityType != null) {
scope = SearchEngine.createHierarchyScope(activityType);
}
//Synthetic comment -- @@ -897,14 +895,14 @@
ResourceType type, String name, IFile file, Document document) {
String targetTag = getTagName(type);
Element root = document.getDocumentElement();
        if (root.getTagName().equals(ROOT_ELEMENT)) {
NodeList children = root.getChildNodes();
for (int i = 0, n = children.getLength(); i < n; i++) {
Node child = children.item(i);
if (child.getNodeType() == Node.ELEMENT_NODE) {
Element element = (Element)child;
if (element.getTagName().equals(targetTag)) {
                        String elementName = element.getAttribute(NAME_ATTR);
if (elementName.equals(name)) {
IRegion region = null;
if (element instanceof IndexedRegion) {
//Synthetic comment -- @@ -1050,7 +1048,7 @@
if (child.getNodeType() == Node.ELEMENT_NODE) {
Element element = (Element) child;
if (element.getTagName().equals(targetTag)) {
                    String elementName = element.getAttribute(NAME_ATTR);
if (elementName.equals(name)) {
return Pair.of(file, parser.getOffset(element));
}
//Synthetic comment -- @@ -1080,10 +1078,10 @@
Pair<ResourceType,String> resource = parseResource(url);
if (resource == null) {
String androidStyle = ANDROID_STYLE_RESOURCE_PREFIX;
            if (url.startsWith(PREFIX_ANDROID_RESOURCE_REF)) {
                url = androidStyle + url.substring(PREFIX_ANDROID_RESOURCE_REF.length());
            } else if (url.startsWith(PREFIX_ANDROID_THEME_REF)) {
                url = androidStyle + url.substring(PREFIX_ANDROID_THEME_REF.length());
} else if (url.startsWith(ANDROID_PKG + ':')) {
url = androidStyle + url.substring(ANDROID_PKG.length() + 1);
} else {
//Synthetic comment -- @@ -1130,8 +1128,8 @@
ResourceType type = resource.getFirst();
String name = resource.getSecond();

        boolean isFramework = url.startsWith(PREFIX_ANDROID_RESOURCE_REF)
                || url.startsWith(PREFIX_ANDROID_THEME_REF);
if (project == null) {
// Local reference *within* a framework
isFramework = true;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/OutlineLabelProvider.java
//Synthetic comment -- index f38c554..1d27e33 100644

//Synthetic comment -- @@ -13,26 +13,30 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.android.ide.eclipse.adt.internal.editors;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.DRAWABLE_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeLabelProvider;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/** Label provider for the XML outlines and quick outlines: Use our own icons,
 * when available, and and include the most important attribute (id, name, or text) */
@SuppressWarnings("restriction") // XML UI API
class OutlineLabelProvider extends JFaceNodeLabelProvider {
@Override
public Image getImage(Object element) {
//Synthetic comment -- @@ -70,10 +74,10 @@
id = id.substring(DRAWABLE_PREFIX.length());
}
} else {
                                id = e.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
if (id != null && id.length() > 0) {
                                    if (id.startsWith(LAYOUT_PREFIX)) {
                                        id = id.substring(LAYOUT_PREFIX.length());
}
}
}
//Synthetic comment -- @@ -81,7 +85,6 @@
}
}
}

if (id != null && id.length() > 0) {
return text + ": " + id; //$NON-NLS-1$
}
//Synthetic comment -- @@ -92,8 +95,8 @@
/**
* Wrapper around {@link Element#getAttributeNS(String, String)}.
* <p/>
     * The implementation used in Eclipse's XML editor sometimes internally throws
     * an NPE instead of politely returning null.
*
* @see Element#getAttributeNS(String, String)
*/








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimDescriptors.java
//Synthetic comment -- index 978b202..2489cf5 100644

//Synthetic comment -- @@ -15,9 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.animator;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
//Synthetic comment -- @@ -71,8 +69,8 @@
return;
}

        XmlnsAttributeDescriptor xmlns = new XmlnsAttributeDescriptor(ANDROID_NS_NAME,
                ANDROID_URI);

List<ElementDescriptor> descriptors = new ArrayList<ElementDescriptor>();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimationContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimationContentAssist.java
//Synthetic comment -- index bd9114e..777cf1d 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.animator;

import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.IAttributeInfo.Format;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimatorDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/animator/AnimatorDescriptors.java
//Synthetic comment -- index efef5e7..713f6d9 100644

//Synthetic comment -- @@ -15,10 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.animator;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
//Synthetic comment -- @@ -56,7 +55,7 @@
return mRootDescriptors;
}

    public ElementDescriptor getElementDescriptor(String mRootTag) {
if (nameToDescriptor == null) {
nameToDescriptor = new HashMap<String, ElementDescriptor>();
for (ElementDescriptor descriptor : getRootElementDescriptors()) {
//Synthetic comment -- @@ -64,7 +63,7 @@
}
}

        ElementDescriptor descriptor = nameToDescriptor.get(mRootTag);
if (descriptor == null) {
descriptor = getDescriptor();
}
//Synthetic comment -- @@ -153,7 +152,7 @@

DescriptorsUtils.appendAttributes(descs,
null,   // elementName
                SdkConstants.NS_RESOURCES,
style.getAttributes(),
null,   // requiredAttributes
null);  // overrides
//Synthetic comment -- @@ -164,7 +163,7 @@
if (style != null) {
DescriptorsUtils.appendAttributes(descs,
null,   // elementName
                        SdkConstants.NS_RESOURCES,
style.getAttributes(),
null,   // requiredAttributes
null);  // overrides








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/color/ColorDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/color/ColorDescriptors.java
//Synthetic comment -- index fd6e7a4..16add3e 100644

//Synthetic comment -- @@ -15,10 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.color;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
//Synthetic comment -- @@ -77,7 +76,7 @@
SDK_URL,
new ReferenceAttributeDescriptor(
ResourceType.COLOR, ATTR_COLOR,
                    SdkConstants.NS_RESOURCES,
new AttributeInfo(ATTR_COLOR, Format.COLOR_SET)).setTooltip(
"Hexadeximal color. Required. The color is specified with an RGB value and "
+ "optional alpha channel.\n"








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/DescriptorsUtils.java
//Synthetic comment -- index e6f296a..ff555cc 100644

//Synthetic comment -- @@ -16,30 +16,30 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.EDIT_TEXT;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_ADAPTER_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GALLERY;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.SPACE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.REQUEST_FOCUS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.GT_ENTITY;
import static com.android.utils.XmlUtils.LT_ENTITY;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
//Synthetic comment -- @@ -715,12 +715,12 @@
node.getUiParent() instanceof UiDocumentNode;
node.setAttributeValue(
ATTR_LAYOUT_WIDTH,
                    SdkConstants.NS_RESOURCES,
fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);
node.setAttributeValue(
ATTR_LAYOUT_HEIGHT,
                    SdkConstants.NS_RESOURCES,
fill ? VALUE_FILL_PARENT : VALUE_WRAP_CONTENT,
false /* override */);
}
//Synthetic comment -- @@ -730,7 +730,7 @@
if (freeId != null) {
node.setAttributeValue(
ATTR_ID,
                        SdkConstants.NS_RESOURCES,
freeId,
false /* override */);
}
//Synthetic comment -- @@ -744,7 +744,7 @@
String type = getBasename(descriptor.getUiName());
node.setAttributeValue(
ATTR_TEXT,
                SdkConstants.NS_RESOURCES,
type,
false /*override*/);
}
//Synthetic comment -- @@ -761,7 +761,7 @@
id = id.replace("@+", "@");                     //$NON-NLS-1$ //$NON-NLS-2$
node.setAttributeValue(
ATTR_LAYOUT_BELOW,
                                SdkConstants.NS_RESOURCES,
id,
false /* override */);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/ElementDescriptor.java
//Synthetic comment -- index fb09b61..0d62ec0 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/XmlnsAttributeDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/descriptors/XmlnsAttributeDescriptor.java
//Synthetic comment -- index 22b09d4..39bb0f5 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.descriptors;

import static com.android.utils.XmlUtils.XMLNS;
import static com.android.utils.XmlUtils.XMLNS_URI;

import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/drawable/DrawableDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/drawable/DrawableDescriptors.java
//Synthetic comment -- index f269b79..4858ac7 100644

//Synthetic comment -- @@ -15,10 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.drawable;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
//Synthetic comment -- @@ -212,7 +211,7 @@
+ "its attributes. Must be a child of a <selector> element.",
SDK_URL_BASE + "drawable-resource.html#StateList", //$NON-NLS-1$
new ReferenceAttributeDescriptor(
                    ResourceType.DRAWABLE, "drawable", SdkConstants.NS_RESOURCES, //$NON-NLS-1$
new AttributeInfo("drawable", Format.REFERENCE_SET))
.setTooltip("Reference to a drawable resource."),
null, /* This is wrong -- we can now embed any above drawable








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/AndroidXmlFormattingStrategy.java
//Synthetic comment -- index 59073f6..9c29077 100644

//Synthetic comment -- @@ -18,7 +18,6 @@
import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findLineStart;
import static com.android.ide.eclipse.adt.internal.editors.AndroidXmlAutoEditStrategy.findTextStart;
import static com.android.ide.eclipse.adt.internal.editors.color.ColorDescriptors.SELECTOR_TAG;

import static org.eclipse.jface.text.formatter.FormattingContextProperties.CONTEXT_MEDIUM;
import static org.eclipse.jface.text.formatter.FormattingContextProperties.CONTEXT_PARTITION;
import static org.eclipse.jface.text.formatter.FormattingContextProperties.CONTEXT_REGION;
//Synthetic comment -- @@ -31,7 +30,6 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.resources.ResourceType;

//Synthetic comment -- @@ -512,7 +510,7 @@
// integer-arrays, string-arrays, and typed-arrays
Element rootElement = domDocument.getDocumentElement();
if (rootElement != null
                && ValuesDescriptors.ROOT_ELEMENT.equals(rootElement.getTagName())) {
style = XmlFormatStyle.RESOURCE;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/formatting/XmlPrettyPrinter.java
//Synthetic comment -- index 57c10e3..6f15d83 100644

//Synthetic comment -- @@ -15,12 +15,12 @@
*/
package com.android.ide.eclipse.adt.internal.editors.formatting;

import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.COLOR_ELEMENT;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.DIMEN_ELEMENT;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.ITEM_TAG;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.STRING_ELEMENT;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.STYLE_ELEMENT;
import static com.android.utils.XmlUtils.XMLNS;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -708,7 +708,7 @@
// element
if (mPrefs.spaceBeforeClose && (mStyle != XmlFormatStyle.RESOURCE || isClosed)
// in <selector> files etc still treat the <item> entries as in resource files
                && !ITEM_TAG.equals(element.getTagName())
&& (isClosed || element.getAttributes().getLength() > 0)) {
mOut.append(' ');
}
//Synthetic comment -- @@ -777,7 +777,7 @@
Node curr = element.getPreviousSibling();

// <style> elements are traditionally separated unless it follows a comment
            if (STYLE_ELEMENT.equals(element.getTagName())) {
if (curr == null
|| curr.getNodeType() == Node.ELEMENT_NODE
|| (curr.getNodeType() == Node.TEXT_NODE
//Synthetic comment -- @@ -908,7 +908,7 @@

Node curr = element.getParentNode();
while (curr != null) {
            if (STRING_ELEMENT.equals(curr.getNodeName())) {
return true;
}

//Synthetic comment -- @@ -928,10 +928,10 @@
private boolean isSingleLineTag(Element element) {
String tag = element.getTagName();

        return (tag.equals(ITEM_TAG) && mStyle == XmlFormatStyle.RESOURCE)
                || tag.equals(STRING_ELEMENT)
                || tag.equals(DIMEN_ELEMENT)
                || tag.equals(COLOR_ELEMENT);
}

private boolean keepElementAsSingleLine(int depth, Element element) {
//Synthetic comment -- @@ -973,4 +973,4 @@
}
return isClosed;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ContextPullParser.java
//Synthetic comment -- index 26f5223..0eee47a 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;

import com.android.SdkConstants;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/LayoutReloadMonitor.java
//Synthetic comment -- index e51f880..d9e798e 100644

//Synthetic comment -- @@ -177,8 +177,8 @@
public void fileChanged(@NonNull IFile file, @NonNull IMarkerDelta[] markerDeltas,
int kind, @Nullable String extension, int flags) {
// This listener only cares about .class files and AndroidManifest.xml files
            if (!(AdtConstants.EXT_CLASS.equals(extension)
                    || AdtConstants.EXT_XML.equals(extension)
&& SdkConstants.FN_ANDROID_MANIFEST_XML.equals(file.getName()))) {
return;
}
//Synthetic comment -- @@ -233,7 +233,7 @@

// here we only care about code change (so change for .class files).
// Resource changes is handled by the IResourceListener.
            if (AdtConstants.EXT_CLASS.equals(extension)) {
if (file.getName().matches("R[\\$\\.](.*)")) {
// this is a R change!
if (changeFlags == null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 7a4b5ba..98f5317 100644

//Synthetic comment -- @@ -16,15 +16,15 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.CALENDAR_VIEW;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPINNER;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;

import com.android.SdkConstants;
import com.android.ide.common.rendering.LayoutLibrary;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/UiElementPullParser.java
//Synthetic comment -- index 46079d7..53f1e6b 100644

//Synthetic comment -- @@ -16,18 +16,17 @@

package com.android.ide.eclipse.adt.internal.editors.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_PADDING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.ATTR_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
//Synthetic comment -- @@ -166,7 +165,7 @@
ViewElementDescriptor descriptor = mDescriptors.findDescriptorByTag(xml);
if (descriptor != null) {
NamedNodeMap attributes = node.getXmlNode().getAttributes();
                Node padding = attributes.getNamedItemNS(SdkConstants.NS_RESOURCES, ATTR_PADDING);
if (padding == null) {
// we'll return an extra padding
mZeroAttributeIsPadding = true;
//Synthetic comment -- @@ -271,7 +270,7 @@
public String getAttributeNamespace(int i) {
if (mZeroAttributeIsPadding) {
if (i == 0) {
                return SdkConstants.NS_RESOURCES;
} else {
i--;
}
//Synthetic comment -- @@ -294,7 +293,7 @@
if (i == 0) {
// figure out the prefix associated with the android namespace.
Document doc = mRoot.getXmlDocument();
                return doc.lookupPrefix(SdkConstants.NS_RESOURCES);
} else {
i--;
}
//Synthetic comment -- @@ -325,7 +324,7 @@
if (attribute != null) {
String value = attribute.getNodeValue();
if (mIncreaseExistingPadding && ATTR_PADDING.equals(attribute.getLocalName()) &&
                    SdkConstants.NS_RESOURCES.equals(attribute.getNamespaceURI())) {
// add the padding and return the value
return addPaddingToValue(value);
}
//Synthetic comment -- @@ -341,7 +340,7 @@
@Override
public String getAttributeValue(String namespace, String localName) {
if (mExplodeNodes != null && ATTR_PADDING.equals(localName) &&
                SdkConstants.NS_RESOURCES.equals(namespace)) {
UiElementNode node = getCurrentNode();
if (node != null && mExplodeNodes.contains(node)) {
return FIXED_PADDING_VALUE;
//Synthetic comment -- @@ -349,7 +348,7 @@
}

if (mZeroAttributeIsPadding && ATTR_PADDING.equals(localName) &&
                SdkConstants.NS_RESOURCES.equals(namespace)) {
return DEFAULT_PADDING_VALUE;
}

//Synthetic comment -- @@ -380,7 +379,7 @@
if (attribute != null) {
String value = attribute.getNodeValue();
if (mIncreaseExistingPadding && ATTR_PADDING.equals(localName) &&
                        SdkConstants.NS_RESOURCES.equals(namespace)) {
// add the padding and return the value
return addPaddingToValue(value);
}
//Synthetic comment -- @@ -390,7 +389,7 @@
if (VALUE_MATCH_PARENT.equals(value) &&
(ATTR_LAYOUT_WIDTH.equals(localName) ||
ATTR_LAYOUT_HEIGHT.equals(localName)) &&
                        SdkConstants.NS_RESOURCES.equals(namespace)) {
return VALUE_FILL_PARENT;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 4324b10..1dec5cd 100644

//Synthetic comment -- @@ -16,15 +16,14 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.AndroidConstants.RES_QUALIFIER_SEP;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;

import com.android.AndroidConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -336,9 +335,9 @@
// can be mistaken for {@link #SEP}. Instead use {@link #MARKER_FRAMEWORK}.
if (theme != null) {
String themeName = ResourceHelper.styleToTheme(theme);
                    if (theme.startsWith(PREFIX_STYLE)) {
sb.append(MARKER_PROJECT);
                    } else if (theme.startsWith(PREFIX_ANDROID_STYLE)) {
sb.append(MARKER_FRAMEWORK);
}
sb.append(themeName);
//Synthetic comment -- @@ -396,10 +395,11 @@
// Decode the theme name: See {@link #getData}
theme = values[3];
if (theme.startsWith(MARKER_FRAMEWORK)) {
                                theme = PREFIX_ANDROID_STYLE
+ theme.substring(MARKER_FRAMEWORK.length());
} else if (theme.startsWith(MARKER_PROJECT)) {
                                theme = PREFIX_STYLE + theme.substring(MARKER_PROJECT.length());
}

uiMode = UiMode.getEnum(values[4]);
//Synthetic comment -- @@ -1102,7 +1102,8 @@
}

void selectTheme(String theme) {
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : theme;
mThemeCombo.setData(theme);
if (theme != null) {
mThemeCombo.setText(getThemeLabel(theme, true));
//Synthetic comment -- @@ -1686,7 +1687,7 @@
//String current = fileConfig.toDisplayString();
//String current = fileConfig.getFolderName(ResourceFolderType.LAYOUT);
String current = mEditedFile.getParent().getName();
        if (current.equals(AndroidConstants.FD_RES_LAYOUT)) {
current = "default";
}

//Synthetic comment -- @@ -2396,7 +2397,7 @@

for (String theme : themes) {
if (!theme.startsWith(PREFIX_RESOURCE_REF)) {
                                theme = PREFIX_STYLE + theme;
}
mThemeList.add(theme);
}
//Synthetic comment -- @@ -2428,7 +2429,7 @@

for (String theme : themes) {
if (!theme.startsWith(PREFIX_RESOURCE_REF)) {
                            theme = PREFIX_ANDROID_STYLE + theme;
}
mThemeList.add(theme);
}
//Synthetic comment -- @@ -2442,8 +2443,8 @@
// or a framework style. For now we need to migrate. Search through the
// theme list until we have a match
if (!mState.theme.startsWith(PREFIX_RESOURCE_REF)) {
                String projectStyle = PREFIX_STYLE + mState.theme;
                String frameworkStyle = PREFIX_ANDROID_STYLE + mState.theme;
for (String theme : mThemeList) {
if (theme.equals(projectStyle)) {
mState.theme = projectStyle;
//Synthetic comment -- @@ -2656,7 +2657,8 @@
public boolean isProjectTheme() {
String theme = getSelectedTheme();
if (theme != null) {
            assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE);

return ResourceHelper.isProjectStyle(theme);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/SelectThemeAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/SelectThemeAction.java
//Synthetic comment -- index 8d92d3c..a8f6504 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
//Synthetic comment -- @@ -33,7 +33,8 @@
public SelectThemeAction(ConfigurationComposite configuration, String title, String theme,
boolean selected) {
super(title, IAction.AS_RADIO_BUTTON);
        assert theme.startsWith(PREFIX_STYLE) || theme.startsWith(PREFIX_ANDROID_STYLE) : theme;
mConfiguration = configuration;
mTheme = theme;
if (selected) {
//Synthetic comment -- @@ -46,4 +47,4 @@
mConfiguration.selectTheme(mTheme);
mConfiguration.onThemeChange();
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ThemeMenuAction.java
//Synthetic comment -- index 0836709..7d8c487 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;

import com.android.ide.eclipse.adt.internal.editors.Hyperlinks;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.SubmenuAction;
//Synthetic comment -- @@ -49,17 +49,17 @@
*/
class ThemeMenuAction extends SubmenuAction {
private static final String DEVICE_LIGHT_PREFIX =
            PREFIX_ANDROID_STYLE + "Theme.DeviceDefault.Light";  //$NON-NLS-1$
private static final String HOLO_LIGHT_PREFIX =
            PREFIX_ANDROID_STYLE + "Theme.Holo.Light";           //$NON-NLS-1$
private static final String DEVICE_PREFIX =
            PREFIX_ANDROID_STYLE + "Theme.DeviceDefault";        //$NON-NLS-1$
private static final String HOLO_PREFIX =
            PREFIX_ANDROID_STYLE + "Theme.Holo";                 //$NON-NLS-1$
private static final String LIGHT_PREFIX =
            PREFIX_ANDROID_STYLE +"Theme.Light";                 //$NON-NLS-1$
private static final String THEME_PREFIX =
            PREFIX_ANDROID_STYLE +"Theme";                       //$NON-NLS-1$

// Constants used to indicate what type of menu is being shown, such that
// the submenus can lazily construct their contents








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/CustomViewDescriptorService.java
//Synthetic comment -- index 330c7d4..ca229a4 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.SdkConstants.CLASS_VIEWGROUP;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/LayoutDescriptors.java
//Synthetic comment -- index fb8cc25..15dc356 100644

//Synthetic comment -- @@ -16,11 +16,16 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TAG;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.api.IAttributeInfo.Format;
//Synthetic comment -- @@ -50,54 +55,6 @@
* Complete description of the layout structure.
*/
public final class LayoutDescriptors implements IDescriptorProvider {

    /**
     * The XML name of the special {@code <include>} layout tag.
     * A synthetic element with that name is created as part of the view descriptors list
     * returned by {@link #getViewDescriptors()}.
     */
    public static final String VIEW_INCLUDE = "include";      //$NON-NLS-1$

    /**
     * The XML name of the special {@code <merge>} layout tag.
     * A synthetic element with that name is created as part of the view descriptors list
     * returned by {@link #getViewDescriptors()}.
     */
    public static final String VIEW_MERGE = "merge";          //$NON-NLS-1$

    /**
     * The XML name of the special {@code <fragment>} layout tag.
     * A synthetic element with that name is created as part of the view descriptors list
     * returned by {@link #getViewDescriptors()}.
     */
    public static final String VIEW_FRAGMENT = "fragment";    //$NON-NLS-1$

    /**
     * The XML name of the special {@code <view>} layout tag. This is used to add generic
     * views with a class attribute to specify the view.
     * <p>
     * TODO: We should add a synthetic descriptor for this, similar to our descriptors for
     * include, merge and requestFocus.
     */
    public static final String VIEW_VIEWTAG = "view";           //$NON-NLS-1$

    /**
     * The XML name of the special {@code <requestFocus>} layout tag.
     * A synthetic element with that name is created as part of the view descriptors list
     * returned by {@link #getViewDescriptors()}.
     */
    public static final String REQUEST_FOCUS = "requestFocus";//$NON-NLS-1$

    /**
     * The attribute name of the include tag's url naming the resource to be inserted
     * <p>
     * <b>NOTE</b>: The layout attribute is NOT in the Android namespace!
     */
    public static final String ATTR_LAYOUT = "layout"; //$NON-NLS-1$

    // Public attributes names, attributes descriptors and elements descriptors
    public static final String ID_ATTR = "id"; //$NON-NLS-1$

/** The document descriptor. Contains all layouts and views linked together. */
private DocumentDescriptor mRootDescriptor =
new DocumentDescriptor("layout_doc", null); //$NON-NLS-1$
//Synthetic comment -- @@ -288,7 +245,7 @@
// Process all View attributes
DescriptorsUtils.appendAttributes(attributes,
null, // elementName
                SdkConstants.NS_RESOURCES,
info.getAttributes(),
null, // requiredAttributes
null /* overrides */);
//Synthetic comment -- @@ -306,7 +263,7 @@
attributeSources.add(link.getFullClassName());
DescriptorsUtils.appendAttributes(attributes,
null, // elementName
                        SdkConstants.NS_RESOURCES,
attrList,
null, // requiredAttributes
null /* overrides */);
//Synthetic comment -- @@ -320,12 +277,12 @@
for(; layoutParams != null; layoutParams = layoutParams.getSuperClass()) {
for (AttributeInfo attrInfo : layoutParams.getAttributes()) {
if (DescriptorsUtils.containsAttribute(layoutAttributes,
                        SdkConstants.NS_RESOURCES, attrInfo)) {
continue;
}
DescriptorsUtils.appendAttribute(layoutAttributes,
null, // elementName
                        SdkConstants.NS_RESOURCES,
attrInfo,
false, // required
null /* overrides */);
//Synthetic comment -- @@ -371,7 +328,7 @@

DescriptorsUtils.appendAttribute(attributes,
null, //elementXmlName
                SdkConstants.NS_RESOURCES, //nsUri
new AttributeInfo(
"id",           //$NON-NLS-1$
Format.REFERENCE_SET ),
//Synthetic comment -- @@ -455,7 +412,7 @@
descs.add(classAttribute);
DescriptorsUtils.appendAttributes(descs,
null,   // elementName
                    SdkConstants.NS_RESOURCES,
style.getAttributes(),
null,   // requiredAttributes
null);  // overrides








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/descriptors/ViewElementDescriptor.java
//Synthetic comment -- index fdfe191..466720a 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.descriptors;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_VIEW_PKG;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_WEBKIT_PKG;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;

import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CanvasViewInfo.java
//Synthetic comment -- index a443a9d..0298fbb 100644

//Synthetic comment -- @@ -16,11 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.common.layout.LayoutConstants.GESTURE_OVERLAY_VIEW;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Margins;
//Synthetic comment -- @@ -31,7 +32,6 @@
import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.UiElementPullParser;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -566,14 +566,14 @@
if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
String nodeName = node.getNodeName();
if (node.getNamespaceURI() == null
                            && LayoutDescriptors.VIEW_INCLUDE.equals(nodeName)) {
// Note: the layout attribute is NOT in the Android namespace
Element element = (Element) node;
                        String url = element.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
if (url.length() > 0) {
return url;
}
                    } else if (LayoutDescriptors.VIEW_FRAGMENT.equals(nodeName)) {
String url = FragmentMenu.getFragmentLayout(node);
if (url != null) {
return url;
//Synthetic comment -- @@ -910,7 +910,7 @@
UiViewElementNode uiViewNode = view.getUiViewNode();
String containerName = uiViewNode != null
? uiViewNode.getDescriptor().getXmlLocalName() : ""; //$NON-NLS-1$
                if (containerName.equals(LayoutDescriptors.VIEW_INCLUDE)) {
// This is expected -- we don't WANT to get node keys for the content
// of an include since it's in a different file and should be treated
// as a single unit that cannot be edited (hence, no CanvasViewInfo








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ClipboardSupport.java
//Synthetic comment -- index 22666a2..3f9c13b 100644

//Synthetic comment -- @@ -15,7 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import com.android.SdkConstants;
import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.IDragElement.IDragAttribute;
import com.android.ide.common.api.INode;
//Synthetic comment -- @@ -28,7 +31,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.utils.XmlUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.StyledText;
//Synthetic comment -- @@ -338,10 +340,7 @@
UiElementNode uiNew = uiDoc.appendNewUiChild(viewDesc);

// A root node requires the Android XMLNS
                uiNew.setAttributeValue(
                        XmlUtils.ANDROID_NS_NAME,
                        XmlUtils.XMLNS_URI,
                        SdkConstants.NS_RESOURCES,
true /*override*/);

// Copy all the attributes from the pasted element








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilities.java
//Synthetic comment -- index bb4fbda..1625195 100644

//Synthetic comment -- @@ -15,14 +15,16 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML.ContentTypeID_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DynamicContextMenu.java
//Synthetic comment -- index 27c98d0..fc71272 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_IMAGE_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TEXT_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.SPINNER;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExportScreenshotAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ExportScreenshotAction.java
//Synthetic comment -- index 8b3dec1..6829c40 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;

import com.android.ide.eclipse.adt.AdtPlugin;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/FragmentMenu.java
//Synthetic comment -- index 28a477b..0dbd152 100644

//Synthetic comment -- @@ -15,13 +15,15 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_FRAGMENT_LAYOUT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.ResourceRepository;
//Synthetic comment -- @@ -58,7 +60,7 @@
* Fragment context menu allowing a layout to be chosen for previewing in the fragment frame.
*/
public class FragmentMenu extends SubmenuAction {
    private static final String R_LAYOUT_PREFIX = "R.layout."; //$NON-NLS-1$
private static final String ANDROID_R_PREFIX = "android.R.layout"; //$NON-NLS-1$

/** Associated canvas */
//Synthetic comment -- @@ -94,8 +96,8 @@

String selected = getSelectedLayout();
if (selected != null) {
            if (selected.startsWith(ANDROID_LAYOUT_PREFIX)) {
                selected = selected.substring(ANDROID_LAYOUT_PREFIX.length());
}
}

//Synthetic comment -- @@ -129,11 +131,11 @@

int index = 0;
while (true) {
                    index = source.indexOf(R_LAYOUT_PREFIX, index);
if (index == -1) {
break;
} else {
                        index += R_LAYOUT_PREFIX.length();
int end = index;
while (end < source.length()) {
char c = source.charAt(end);
//Synthetic comment -- @@ -148,9 +150,9 @@
// Is this R.layout part of an android.R.layout?
int len = ANDROID_R_PREFIX.length() + 1; // prefix length to check
if (index > len && source.startsWith(ANDROID_R_PREFIX, index - len)) {
                                layout = ANDROID_LAYOUT_PREFIX + title;
} else {
                                layout = LAYOUT_PREFIX + title;
}
if (!self.equals(title)) {
layouts.add(Pair.of(title, layout));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index ded2f76..0f1b373 100644

//Synthetic comment -- @@ -17,25 +17,26 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.SdkConstants.FD_GEN_SOURCES;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_STRING_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor.viewNeedsPackage;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_EAST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.DOCK_WEST;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_COLLAPSED;
import static org.eclipse.wb.core.controls.flyout.IFlyoutPreferences.STATE_OPEN;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.ide.common.api.Rect;
import com.android.ide.common.layout.BaseLayoutRule;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageUtils.java
//Synthetic comment -- index aae1f99..0178173 100644

//Synthetic comment -- @@ -15,11 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_9PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_BMP;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_GIF;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JPG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index ca345b6..0b8f784 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_LAYOUTS;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.resources.ResourceType.LAYOUT;

import static org.eclipse.core.resources.IResourceDelta.ADDED;
import static org.eclipse.core.resources.IResourceDelta.CHANGED;
import static org.eclipse.core.resources.IResourceDelta.CONTENT;
import static org.eclipse.core.resources.IResourceDelta.REMOVED;

import com.android.SdkConstants;
import com.android.annotations.VisibleForTesting;
import com.android.ide.common.resources.ResourceFile;
import com.android.ide.common.resources.ResourceFolder;
import com.android.ide.common.resources.ResourceItem;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -529,12 +529,12 @@

/** Searches the given DOM document and returns the list of includes, if any */
private List<String> findIncludesInDocument(Document document) {
        NodeList includes = document.getElementsByTagName(LayoutDescriptors.VIEW_INCLUDE);
if (includes.getLength() > 0) {
List<String> urls = new ArrayList<String>();
for (int i = 0; i < includes.getLength(); i++) {
Element element = (Element) includes.item(i);
                String url = element.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
if (url.length() > 0) {
String resourceName = urlToLocalResource(url);
if (resourceName != null) {
//Synthetic comment -- @@ -921,7 +921,7 @@
reference = FD_RES_LAYOUT + WS_SEP + reference;
}

            String projectPath = SdkConstants.FD_RESOURCES + WS_SEP + reference + '.' + EXT_XML;
IResource member = mProject.findMember(projectPath);
if (member instanceof IFile) {
return (IFile) member;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutActionBar.java
//Synthetic comment -- index 37f44bf..285cba2 100644

//Synthetic comment -- @@ -15,9 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.RuleAction;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutCanvas.java
//Synthetic comment -- index 78c8cba..6eb5f27 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.lint.LintEditAction;
import com.android.resources.Density;
import com.android.utils.XmlUtils;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
//Synthetic comment -- @@ -1507,8 +1506,8 @@

// A root node requires the Android XMLNS
uiNew.setAttributeValue(
                        XmlUtils.ANDROID_NS_NAME,
                        XmlUtils.XMLNS_URI,
SdkConstants.NS_RESOURCES,
true /*override*/);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadata.java
//Synthetic comment -- index 7b669bd..a164e3d 100644

//Synthetic comment -- @@ -15,14 +15,16 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NUM_COLUMNS;
import static com.android.ide.common.layout.LayoutConstants.EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.GRID_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.AdapterBinding;
//Synthetic comment -- @@ -359,10 +361,10 @@

/** Strips out @layout/ or @android:layout/ from the given layout reference */
private static String stripLayoutPrefix(String layout) {
        if (layout.startsWith(ANDROID_LAYOUT_PREFIX)) {
            layout = layout.substring(ANDROID_LAYOUT_PREFIX.length());
        } else if (layout.startsWith(LAYOUT_PREFIX)) {
            layout = layout.substring(LAYOUT_PREFIX.length());
}

return layout;
//Synthetic comment -- @@ -401,23 +403,23 @@
AdapterBinding binding = new AdapterBinding(count);

if (header != null) {
                boolean isFramework = header.startsWith(ANDROID_LAYOUT_PREFIX);
binding.addHeader(new ResourceReference(stripLayoutPrefix(header),
isFramework));
}

if (footer != null) {
                boolean isFramework = footer.startsWith(ANDROID_LAYOUT_PREFIX);
binding.addFooter(new ResourceReference(stripLayoutPrefix(footer),
isFramework));
}

if (layout != null) {
                boolean isFramework = layout.startsWith(ANDROID_LAYOUT_PREFIX);
if (isFramework) {
                    layout = layout.substring(ANDROID_LAYOUT_PREFIX.length());
                } else if (layout.startsWith(LAYOUT_PREFIX)) {
                    layout = layout.substring(LAYOUT_PREFIX.length());
}

binding.addItem(new DataBindingItem(layout, isFramework, 1));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LintTooltip.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LintTooltip.java
//Synthetic comment -- index bc2f390..cedd436 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;

import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ListViewTypeMenu.java
//Synthetic comment -- index d840c30..076b11a 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_LAYOUT_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_LV_FOOTER;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_LV_HEADER;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.LayoutMetadata.KEY_LV_ITEM;
//Synthetic comment -- @@ -80,8 +80,8 @@

String selected = getSelectedLayout();
if (selected != null) {
                if (selected.startsWith(ANDROID_LAYOUT_PREFIX)) {
                    selected = selected.substring(ANDROID_LAYOUT_PREFIX.length());
}
}

//Synthetic comment -- @@ -151,7 +151,7 @@
@Override
public void run() {
if (isChecked()) {
                setNewType(KEY_LV_ITEM, ANDROID_LAYOUT_PREFIX + mLayout);
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index f9a3e02..8178c68 100644

//Synthetic comment -- @@ -16,25 +16,25 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ROW_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.DRAWABLE_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import static org.eclipse.jface.viewers.StyledString.COUNTER_STYLER;
import static org.eclipse.jface.viewers.StyledString.QUALIFIER_STYLER;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.InsertType;
//Synthetic comment -- @@ -45,7 +45,6 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.NodeProxy;
import com.android.ide.eclipse.adt.internal.editors.layout.properties.PropertySheetPage;
//Synthetic comment -- @@ -918,14 +917,14 @@
styledString.append(LABEL_SEPARATOR, QUALIFIER_STYLER);
styledString.append(truncate(src, styledString), QUALIFIER_STYLER);
}
                    } else if (e.getTagName().equals(LayoutDescriptors.VIEW_INCLUDE)) {
// Show the include reference.

// Note: the layout attribute is NOT in the Android namespace
                        String src = e.getAttribute(LayoutDescriptors.ATTR_LAYOUT);
if (src != null && src.length() > 0) {
                            if (src.startsWith(LAYOUT_PREFIX)) {
                                src = src.substring(LAYOUT_PREFIX.length());
}
styledString.append(LABEL_SEPARATOR, QUALIFIER_STYLER);
styledString.append(truncate(src, styledString), QUALIFIER_STYLER);
//Synthetic comment -- @@ -938,7 +937,7 @@
if (includedWithin != null) {
styledString = new StyledString();
styledString.append(includedWithin.getDisplayName(), QUALIFIER_STYLER);
                    image = IconFactory.getInstance().getIcon(LayoutDescriptors.VIEW_INCLUDE);
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index dcb2d96..a3be5cc 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS_ANDROID;
import static com.android.utils.XmlUtils.XMLNS_URI;

import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.Rect;
//Synthetic comment -- @@ -925,10 +925,8 @@
attr.setValue(ANDROID_URI);
element.getAttributes().setNamedItemNS(attr);

            element.setAttributeNS(ANDROID_URI,
                    ATTR_LAYOUT_WIDTH, VALUE_WRAP_CONTENT);
            element.setAttributeNS(ANDROID_URI,
                    ATTR_LAYOUT_HEIGHT, VALUE_WRAP_CONTENT);

// This doesn't apply to all, but doesn't seem to cause harm and makes for a
// better experience with text-oriented views like buttons and texts








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PlayAnimationMenu.java
//Synthetic comment -- index c531848..629a42f 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.AndroidConstants.FD_RES_ANIMATOR;
import static com.android.SdkConstants.FD_RESOURCES;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;

import com.android.ide.common.rendering.api.Capability;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 345cfe2..e68c47c 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.FQCN_DATE_PICKER;
import static com.android.ide.common.layout.LayoutConstants.FQCN_EXPANDABLE_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LIST_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TIME_PICKER;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;

import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.Capability;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index b4b4433..8f6eb56 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.LAYOUT_PREFIX;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
//Synthetic comment -- @@ -303,7 +303,7 @@

// Find the layout file.
ResourceValue contextLayout = mResourceResolver.findResValue(
                    LAYOUT_PREFIX + contextLayoutName, false  /* forceFrameworkOnly*/);
if (contextLayout != null) {
File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionManager.java
//Synthetic comment -- index 6f6259c..57b7871 100644

//Synthetic comment -- @@ -15,15 +15,16 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_MARGIN;
import static com.android.ide.eclipse.adt.internal.editors.layout.gle2.SelectionHandle.PIXEL_RADIUS;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index ef016fb..01dd21e 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ClientRulesEngine.java
//Synthetic comment -- index 83479ce..2fb16ff 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.SdkConstants.CLASS_FRAGMENT;
import static com.android.SdkConstants.CLASS_V4_FRAGMENT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeFactory.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/NodeProxy.java
old mode 100755
new mode 100644









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/PaletteMetadataDescriptor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/PaletteMetadataDescriptor.java
//Synthetic comment -- index abd4486..884cb07 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
old mode 100755
new mode 100644
//Synthetic comment -- index 616d4ab..f7eac4434

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadataRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/ViewMetadataRepository.java
//Synthetic comment -- index c448695..586da12 100644

//Synthetic comment -- @@ -16,15 +16,15 @@

package com.android.ide.eclipse.adt.internal.editors.layout.gre;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.FQCN_BUTTON;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPINNER;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TOGGLE_BUTTON;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.VisibleForTesting;
import com.android.ide.common.api.IViewMetadata.FillPreference;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/BooleanXmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/BooleanXmlPropertyEditor.java
//Synthetic comment -- index 32b1192..d6ff4d5 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyFactory.java
//Synthetic comment -- index 68a4c07..59754af 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;

import com.android.annotations.Nullable;
import com.android.ide.common.api.IAttributeInfo;
//Synthetic comment -- @@ -276,7 +276,7 @@
}

String firstName = descriptor.getXmlLocalName();
            if (firstName.startsWith(ATTR_LAYOUT_PREFIX)) {
if (firstName.startsWith(ATTR_LAYOUT_MARGIN)) {
if (marginProperties == null) {
marginProperties = Lists.newArrayListWithExpectedSize(5);
//Synthetic comment -- @@ -443,7 +443,7 @@
}

String firstName = descriptor.getXmlLocalName();
            if (firstName.startsWith(ATTR_LAYOUT_PREFIX)) {
if (firstName.startsWith(ATTR_LAYOUT_MARGIN)) {
if (marginProperties == null) {
marginProperties = Lists.newArrayListWithExpectedSize(5);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyMetadata.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/PropertyMetadata.java
//Synthetic comment -- index cdf7664..b230aa9 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HINT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ResourceValueCompleter.java
//Synthetic comment -- index a5e3f64..f6b80d7 100644

//Synthetic comment -- @@ -15,12 +15,12 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.resources.ResourceItem;
import com.android.ide.common.resources.ResourceRepository;
//Synthetic comment -- @@ -93,16 +93,16 @@
// System matches: only do this if the value already matches at least @a,
// and doesn't start with something that can't possibly be @android
if (prefix.startsWith("@a") && //$NON-NLS-1$
                prefix.regionMatches(true /* ignoreCase */, 0, PREFIX_ANDROID_RESOURCE_REF, 0,
                        Math.min(prefix.length() - 1, PREFIX_ANDROID_RESOURCE_REF.length()))) {
AndroidTargetData data = editor.getTargetData();
if (data != null) {
ResourceRepository repository = data.getFrameworkResources();
addMatches(repository, prefix, true /* isSystem */, results);
}
} else if (prefix.startsWith("?") && //$NON-NLS-1$
                prefix.regionMatches(true /* ignoreCase */, 0, PREFIX_ANDROID_THEME_REF, 0,
                        Math.min(prefix.length() - 1, PREFIX_ANDROID_THEME_REF.length()))) {
AndroidTargetData data = editor.getTargetData();
if (data != null) {
ResourceRepository repository = data.getFrameworkResources();
//Synthetic comment -- @@ -141,7 +141,7 @@
private static void addMatches(ResourceRepository repository, String prefix, boolean isSystem,
List<String> results) {
int typeStart = isSystem
                ? PREFIX_ANDROID_RESOURCE_REF.length() : PREFIX_RESOURCE_REF.length();

for (ResourceType type : repository.getAvailableResourceTypes()) {
if (prefix.regionMatches(typeStart, type.getName(), 0,
//Synthetic comment -- @@ -179,4 +179,4 @@
}
}
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleter.java
//Synthetic comment -- index 944b889..132855d 100644

//Synthetic comment -- @@ -15,6 +15,13 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.api.IAttributeInfo.Format.BOOLEAN;
import static com.android.ide.common.api.IAttributeInfo.Format.DIMENSION;
import static com.android.ide.common.api.IAttributeInfo.Format.ENUM;
//Synthetic comment -- @@ -23,13 +30,6 @@
import static com.android.ide.common.api.IAttributeInfo.Format.INTEGER;
import static com.android.ide.common.api.IAttributeInfo.Format.REFERENCE;
import static com.android.ide.common.api.IAttributeInfo.Format.STRING;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT_SIZE;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_SP;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -183,4 +183,4 @@

return proposals.toArray(new IContentProposal[proposals.size()]);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlProperty.java
//Synthetic comment -- index 2a756c9..a320b68 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -91,12 +91,12 @@
String name = mDescriptor.getXmlLocalName();
int nameLength = name.length();

        if (name.startsWith(ATTR_LAYOUT_PREFIX)) {
if (name.startsWith(ATTR_LAYOUT_MARGIN)
&& nameLength > ATTR_LAYOUT_MARGIN.length()) {
name = name.substring(ATTR_LAYOUT_MARGIN.length());
} else {
                name = name.substring(ATTR_LAYOUT_PREFIX.length());
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/properties/XmlPropertyEditor.java
//Synthetic comment -- index 7cb3f66..72577a5 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_PNG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.ide.common.api.IAttributeInfo;
//Synthetic comment -- @@ -127,8 +127,8 @@
GraphicalEditorPart graphicalEditor = xmlProperty.getGraphicalEditor();
if (graphicalEditor != null) {
ResourceResolver resolver = graphicalEditor.getResourceResolver();
                    boolean isFramework = text.startsWith(PREFIX_ANDROID_RESOURCE_REF)
                            || text.startsWith(PREFIX_ANDROID_THEME_REF);
resValue = resolver.findResValue(text, isFramework);
while (resValue != null && resValue.getValue() != null) {
String value = resValue.getValue();
//Synthetic comment -- @@ -136,8 +136,8 @@
|| value.startsWith(PREFIX_THEME_REF)) {
// TODO: do I have to strip off the @ too?
isFramework = isFramework
                                    || value.startsWith(PREFIX_ANDROID_RESOURCE_REF)
                                    || value.startsWith(PREFIX_ANDROID_THEME_REF);;
ResourceValue v = resolver.findResValue(text, isFramework);
if (v != null && !value.equals(v.getValue())) {
resValue = v;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoring.java
//Synthetic comment -- index ceb3b76..b01b4b1 100644

//Synthetic comment -- @@ -15,30 +15,30 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BASELINE_ALIGNED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_TABLE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.TABLE_ROW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -309,10 +309,10 @@
String value = nameValue[1];
String prefix = null;
String namespaceUri = null;
                if (attribute.startsWith(ANDROID_NS_NAME_PREFIX)) {
prefix = namespace;
namespaceUri = ANDROID_URI;
                    attribute = attribute.substring(ANDROID_NS_NAME_PREFIX.length());
}
setAttribute(rootEdit, layout, namespaceUri,
prefix, attribute, value);
//Synthetic comment -- @@ -568,7 +568,7 @@
Node attributeNode = attributeMap.item(i);

String name = attributeNode.getLocalName();
            if (!name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attributeNode.getNamespaceURI())) {
if (!defined.contains(name)) {
// Remove it








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutWizard.java
//Synthetic comment -- index 004e66e..f558271 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_MERGE;

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoring.java
//Synthetic comment -- index a9a78e9..73f5eb1 100644

//Synthetic comment -- @@ -15,13 +15,13 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -255,7 +255,7 @@
Node attributeNode = attributes.item(i);

String name = attributeNode.getLocalName();
            if (!name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attributeNode.getNamespaceURI())) {
result.add((Attr) attributeNode);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewWizard.java
//Synthetic comment -- index 775a7b5..0ac7106 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.REQUEST_FOCUS;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_FRAGMENT;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoring.java
//Synthetic comment -- index 7cc0b1e..65edd54 100644

//Synthetic comment -- @@ -15,35 +15,33 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.SdkConstants.FD_RES;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.resources.ResourceType.LAYOUT;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS;
import static com.android.utils.XmlUtils.XMLNS_COLON;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CanvasViewInfo;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.DomUtilities;
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
//Synthetic comment -- @@ -161,7 +159,7 @@
UiViewElementNode uiNode = infos.get(0).getUiViewNode();
if (uiNode != null) {
Node xmlNode = uiNode.getXmlNode();
                        if (xmlNode.getLocalName().equals(LayoutDescriptors.VIEW_INCLUDE)) {
status.addWarning("No point in refactoring a single include tag");
}
}
//Synthetic comment -- @@ -327,7 +325,7 @@
IPath parentPath = parent.getProjectRelativePath();
final IFile file = project.getFile(new Path(parentPath + WS_SEP + newFileName));
TextFileChange addFile = new TextFileChange("Create new separate layout", file);
        addFile.setTextType(AdtConstants.EXT_XML);
changes.add(addFile);

String newFile = sb.toString();
//Synthetic comment -- @@ -401,10 +399,10 @@
private List<IFile> getOtherLayouts(IFile sourceFile) {
List<IFile> layouts = new ArrayList<IFile>(100);
IPath sourcePath = sourceFile.getProjectRelativePath();
        IFolder resources = mProject.getFolder(SdkConstants.FD_RESOURCES);
try {
for (IResource folder : resources.members()) {
                if (folder.getName().startsWith(AndroidConstants.FD_RES_LAYOUT) &&
folder instanceof IFolder) {
IFolder layoutFolder = (IFolder) folder;
for (IResource file : layoutFolder.members()) {
//Synthetic comment -- @@ -494,8 +492,8 @@
String value = attributeNode.getNodeValue();
if (value.equals(ANDROID_URI)) {
androidNsPrefix = name;
                    if (androidNsPrefix.startsWith(XMLNS_COLON)) {
                        androidNsPrefix = androidNsPrefix.substring(XMLNS_COLON.length());
}
}
sb.append(XmlUtils.toXmlAttributeValue(value));
//Synthetic comment -- @@ -511,7 +509,7 @@
if (namespaceDeclarations.length() == 0) {
sb.setLength(0);
sb.append(' ');
            sb.append(XMLNS_COLON);
sb.append(androidNsPrefix);
sb.append('=').append('"');
sb.append(ANDROID_URI);
//Synthetic comment -- @@ -606,7 +604,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Node attr = attributes.item(i);
String name = attr.getLocalName();
                if (name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attr.getNamespaceURI())) {
if (name.equals(ATTR_LAYOUT_WIDTH) || name.equals(ATTR_LAYOUT_HEIGHT)) {
// Already handled








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java
//Synthetic comment -- index e35dc1b..ffe6892 100644

//Synthetic comment -- @@ -15,26 +15,29 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.SdkConstants.FD_RESOURCES;
import static com.android.ide.common.layout.LayoutConstants.ATTR_HINT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ON_CLICK;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.ide.common.layout.LayoutConstants.ATTR_STYLE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_TEXT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.ITEM_TAG;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.PARENT_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.ROOT_ELEMENT;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS_COLON;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -309,7 +312,7 @@
return !(name == null
|| name.equals(ATTR_ID)
|| name.startsWith(ATTR_STYLE)
                || (name.startsWith(ATTR_LAYOUT_PREFIX) &&
!name.startsWith(ATTR_LAYOUT_MARGIN))
|| name.equals(ATTR_TEXT)
|| name.equals(ATTR_HINT)
//Synthetic comment -- @@ -363,8 +366,7 @@
// Set the style attribute?
if (mApplyStyle) {
for (Element element : getElements()) {
                String value = ResourceResolver.PREFIX_RESOURCE_REF +
                   ResourceResolver.REFERENCE_STYLE + mStyleName;
setAttribute(rootEdit, element, null, null, ATTR_STYLE, value);
}
}
//Synthetic comment -- @@ -395,8 +397,8 @@
StringBuilder sb = new StringBuilder();
if (createFile) {
sb.append(NewXmlFileWizard.XML_HEADER_LINE);
            sb.append('<').append(ROOT_ELEMENT).append(' ');
            sb.append(XMLNS_COLON).append(ANDROID_NS_NAME).append('=').append('"');
sb.append(ANDROID_URI);
sb.append('"').append('>').append('\n');
}
//Synthetic comment -- @@ -413,11 +415,11 @@
}
sb.append(initialIndent);
String styleTag = "style"; //$NON-NLS-1$ // TODO - use constant in parallel changeset
        sb.append('<').append(styleTag).append(' ').append(NAME_ATTR).append('=').append('"');
sb.append(mStyleName);
sb.append('"');
if (mParent != null) {
            sb.append(' ').append(PARENT_ATTR).append('=').append('"');
sb.append(mParent);
sb.append('"');
}
//Synthetic comment -- @@ -425,7 +427,7 @@

for (Attr attribute : mChosenAttributes) {
sb.append(initialIndent).append(indent);
            sb.append('<').append(ITEM_TAG).append(' ').append(NAME_ATTR).append('=').append('"');
// We've already enforced that regardless of prefix, only attributes with
// an Android namespace can be in the set of chosen attributes. Rewrite the
// prefix to android here.
//Synthetic comment -- @@ -435,12 +437,12 @@
sb.append(attribute.getLocalName());
sb.append('"').append('>');
sb.append(attribute.getValue());
            sb.append('<').append('/').append(ITEM_TAG).append('>').append('\n');
}
sb.append(initialIndent).append('<').append('/').append(styleTag).append('>').append('\n');

if (createFile) {
            sb.append('<').append('/').append(ROOT_ELEMENT).append('>').append('\n');
}
String styleString = sb.toString();
return styleString;
//Synthetic comment -- @@ -494,7 +496,7 @@

if (insertAtIndex == -1) {
String contents = AdtPlugin.readFile(file);
            insertAtIndex = contents.indexOf("</" + ROOT_ELEMENT + ">"); //$NON-NLS-1$
if (insertAtIndex == -1) {
insertAtIndex = contents.length();
}
//Synthetic comment -- @@ -564,7 +566,7 @@
String name = resolvedValue.getName();
if (name != null) {
if (resolvedValue.isFramework()) {
                        return ResourceResolver.PREFIX_ANDROID + name;
} else {
return name;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/GridLayoutConverter.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/GridLayoutConverter.java
//Synthetic comment -- index 0ec664d..868d790 100644

//Synthetic comment -- @@ -17,42 +17,44 @@

import static com.android.ide.common.layout.GravityHelper.GRAVITY_HORIZ_MASK;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_VERT_MASK;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BACKGROUND;
import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.GRAVITY_VALUE_FILL_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.RADIO_GROUP;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.SPACE;
import static com.android.ide.common.layout.LayoutConstants.TABLE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.TABLE_ROW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.IViewMetadata.FillPreference;
import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.common.layout.GravityHelper;
//Synthetic comment -- @@ -879,7 +881,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attr = (Attr) attributes.item(i);
String name = attr.getLocalName();
                            if (name.startsWith(ATTR_LAYOUT_PREFIX)) {
boolean alignVertical =
name.equals(ATTR_LAYOUT_ALIGN_TOP) ||
name.equals(ATTR_LAYOUT_ALIGN_BOTTOM) ||








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RelativeLayoutConversionHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RelativeLayoutConversionHelper.java
//Synthetic comment -- index d4b008d..7f9cc71 100644

//Synthetic comment -- @@ -24,42 +24,44 @@
import static com.android.ide.common.layout.GravityHelper.GRAVITY_RIGHT;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_TOP;
import static com.android.ide.common.layout.GravityHelper.GRAVITY_VERT_MASK;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BACKGROUND;
import static com.android.ide.common.layout.LayoutConstants.ATTR_BASELINE_ALIGNED;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ABOVE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_BELOW;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.RELATIVE_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;
import static com.android.ide.common.layout.LayoutConstants.VALUE_TRUE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.layout.GravityHelper;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
//Synthetic comment -- @@ -695,7 +697,7 @@
if (name.equals(ATTR_LAYOUT_WIDTH)
|| name.equals(ATTR_LAYOUT_HEIGHT)) {
// Ignore these for now
                    } else if (name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attribute.getNamespaceURI())) {
// Determine if the reference is to a known edge
String id = getIdBasename(value);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UnwrapRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UnwrapRefactoring.java
//Synthetic comment -- index bfdac27..1dcc1b7 100644

//Synthetic comment -- @@ -15,10 +15,10 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UseCompoundDrawableRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/UseCompoundDrawableRefactoring.java
//Synthetic comment -- index 427f187..8f678c1 100644

//Synthetic comment -- @@ -15,29 +15,29 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ATTR_DRAWABLE_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_DRAWABLE_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_DRAWABLE_PADDING;
import static com.android.ide.common.layout.LayoutConstants.ATTR_DRAWABLE_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_DRAWABLE_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_GRAVITY;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_SRC;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -254,7 +254,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attribute = (Attr) attributes.item(i);
String name = attribute.getLocalName();
            if (name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attribute.getNamespaceURI())
&& !(name.equals(ATTR_LAYOUT_WIDTH) || name.equals(ATTR_LAYOUT_HEIGHT))) {
// Ignore layout params: the parent layout is going away








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/VisualRefactoring.java
//Synthetic comment -- index 64212b5..5d8d700 100644

//Synthetic comment -- @@ -15,17 +15,17 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS;
import static com.android.utils.XmlUtils.XMLNS_COLON;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -361,7 +361,7 @@
return Collections.emptyList();
}

        String namePrefix = androidNamePrefix + ':' + ATTR_LAYOUT_PREFIX;
List<TextEdit> edits = new ArrayList<TextEdit>();

IStructuredDocumentRegion region = doc.getFirstStructuredDocumentRegion();
//Synthetic comment -- @@ -425,9 +425,9 @@
String value = attributeNode.getNodeValue();
if (value.equals(ANDROID_URI)) {
mAndroidNamespacePrefix = name;
                        if (mAndroidNamespacePrefix.startsWith(XMLNS_COLON)) {
mAndroidNamespacePrefix =
                                mAndroidNamespacePrefix.substring(XMLNS_COLON.length());
}
}
}
//Synthetic comment -- @@ -451,9 +451,9 @@
String value = attributeNode.getNodeValue();
if (value.equals(ANDROID_URI)) {
nsPrefix = name;
                    if (nsPrefix.startsWith(XMLNS_COLON)) {
nsPrefix =
                            nsPrefix.substring(XMLNS_COLON.length());
}
}
}
//Synthetic comment -- @@ -502,7 +502,7 @@
Node attributeNode = attributes.item(i);

String name = attributeNode.getLocalName();
            if (name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attributeNode.getNamespaceURI())) {
result.add((Attr) attributeNode);
}
//Synthetic comment -- @@ -537,7 +537,7 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Node attr = attributes.item(i);
String name = attr.getLocalName();
                if (name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attr.getNamespaceURI())) {
if (name.equals(ATTR_LAYOUT_WIDTH) || name.equals(ATTR_LAYOUT_HEIGHT)) {
// These are special and are left in








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoring.java
//Synthetic comment -- index fbc09ad..ff2e9bd 100644

//Synthetic comment -- @@ -15,16 +15,16 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInWizard.java
//Synthetic comment -- index 893703f..2e06a3b 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_RADIO_BUTTON;
import static com.android.ide.common.layout.LayoutConstants.GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.RADIO_GROUP;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_INCLUDE;

import com.android.ide.eclipse.adt.internal.editors.layout.LayoutEditorDelegate;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.ViewElementDescriptor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/uimodel/UiViewElementNode.java
//Synthetic comment -- index f4a026c..7050be4 100644

//Synthetic comment -- @@ -16,13 +16,14 @@

package com.android.ide.eclipse.adt.internal.editors.layout.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.FQCN_FRAME_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors.VIEW_VIEWTAG;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -38,7 +39,6 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.utils.XmlUtils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
//Synthetic comment -- @@ -125,9 +125,7 @@
mCachedAttributeDescriptors, direct_attrs.length,
layout_attrs.length);
if (need_xmlns) {
            AttributeDescriptor desc = new XmlnsAttributeDescriptor(
                    XmlUtils.ANDROID_NS_NAME,
                    XmlUtils.ANDROID_URI);
mCachedAttributeDescriptors[direct_attrs.length + layout_attrs.length] = desc;
}

//Synthetic comment -- @@ -144,12 +142,11 @@
IconFactory icons = IconFactory.getInstance();
if (uiName.equals(LINEAR_LAYOUT)) {
Element e = (Element) getXmlNode();
                if (VALUE_VERTICAL.equals(e.getAttributeNS(ANDROID_URI,
                        ATTR_ORIENTATION))) {
IconFactory factory = icons;
img = factory.getIcon("VerticalLinearLayout"); //$NON-NLS-1$
}
            } else if (uiName.equals(VIEW_VIEWTAG)) {
Node xmlNode = getXmlNode();
if (xmlNode instanceof Element) {
String className = ((Element) xmlNode).getAttribute(ATTR_CLASS);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestEditor.java
//Synthetic comment -- index 3e815f5..69a6b84 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors.USES_PERMISSION;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index aee75e8..ed93b73 100644

//Synthetic comment -- @@ -16,8 +16,9 @@

package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.SdkConstants.NS_RESOURCES;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.xml.AndroidManifest.ATTRIBUTE_ICON;
import static com.android.xml.AndroidManifest.ATTRIBUTE_LABEL;
import static com.android.xml.AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION;
//Synthetic comment -- @@ -27,10 +28,8 @@
import static com.android.xml.AndroidManifest.ATTRIBUTE_THEME;
import static com.android.xml.AndroidManifest.NODE_ACTIVITY;
import static com.android.xml.AndroidManifest.NODE_USES_SDK;

import static org.eclipse.jdt.core.search.IJavaSearchConstants.REFERENCES;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -358,9 +357,9 @@
// For now this theme works only on XLARGE screens. When it works for all sizes,
// add that new apiLevel to this check.
if (apiLevel >= 11 && screenSize == ScreenSize.XLARGE || apiLevel >= 14) {
            return PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
} else {
            return PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -562,7 +561,7 @@
final IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
if (javaProject != null) {
IType[] activityTypes = new IType[0];
                IType activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
if (activityType != null) {
ITypeHierarchy hierarchy =
activityType.newTypeHierarchy(javaProject, new NullProgressMonitor());
//Synthetic comment -- @@ -669,7 +668,7 @@
typeFqcn = mPackage + '.' + typeFqcn;
}

                    IType activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
if (activityType != null) {
IMethod method = activityType.getMethod(
"setContentView", new String[] {"I"}); //$NON-NLS-1$ //$NON-NLS-2$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/descriptors/AndroidManifestDescriptors.java
//Synthetic comment -- index cafaf8b..ba7894b 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.utils.XmlUtils;

import org.eclipse.core.runtime.IStatus;

//Synthetic comment -- @@ -233,7 +232,7 @@
insertAttribute(MANIFEST_ELEMENT, PACKAGE_ATTR_DESC);

XmlnsAttributeDescriptor xmlns = new XmlnsAttributeDescriptor(
                XmlUtils.ANDROID_NS_NAME, XmlUtils.ANDROID_URI);
insertAttribute(MANIFEST_ELEMENT, xmlns);

assert sanityCheck(manifestMap, MANIFEST_ELEMENT);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/menu/descriptors/MenuDescriptors.java
//Synthetic comment -- index 4fc5309..b7bab1b 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.menu.descriptors;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.resources.platform.DeclareStyleableInfo;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -31,14 +31,11 @@
import java.util.ArrayList;
import java.util.Map;


/**
* Complete description of the menu structure.
*/
public final class MenuDescriptors implements IDescriptorProvider {

    public static final String MENU_ROOT_ELEMENT = "menu"; //$NON-NLS-1$

/** The root element descriptor. */
private ElementDescriptor mDescriptor = null;

//Synthetic comment -- @@ -74,7 +71,7 @@

if (mDescriptor == null) {
mDescriptor = createElement(styleMap,
                MENU_ROOT_ELEMENT, // xmlName
"Menu", // uiName,
null, // TODO SDK URL
null, // extraAttribute
//Synthetic comment -- @@ -101,7 +98,7 @@
false /* mandatory */);

ElementDescriptor sub_menu = createElement(styleMap,
                MENU_ROOT_ELEMENT, // xmlName
"Sub-Menu", // uiName,
null, // TODO SDK URL
null, // extraAttribute
//Synthetic comment -- @@ -165,7 +162,7 @@
if (style != null) {
DescriptorsUtils.appendAttributes(descs,
null,   // elementName
                    SdkConstants.NS_RESOURCES,
style.getAttributes(),
null,   // requiredAttributes
null);  // overrides








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/otherxml/descriptors/OtherXmlDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/otherxml/descriptors/OtherXmlDescriptors.java
//Synthetic comment -- index 4678936..7f3ed09 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.otherxml.descriptors;

import static com.android.utils.XmlUtils.ANDROID_NS_NAME;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.SdkConstants;
import com.android.ide.common.resources.platform.AttributeInfo;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiAttributeNode.java
//Synthetic comment -- index 17a28c8..71cb35d 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_NAME;
import static com.android.ide.common.layout.LayoutConstants.ATTR_STYLE;
import static com.android.ide.eclipse.adt.internal.editors.color.ColorDescriptors.ATTR_COLOR;
import static com.google.common.base.Strings.nullToEmpty;

//Synthetic comment -- @@ -259,7 +259,7 @@
return 20;
}

        if (name.startsWith(ATTR_LAYOUT_PREFIX)) {
// Width and height are special cased because we (a) want width and height
// before the other layout attributes, and (b) we want width to sort before height
// even though it comes after it alphabetically.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiElementNode.java
//Synthetic comment -- index a6f9a02..b521d78 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ANDROID_SUPPORT_PKG_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_CLASS;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;

import com.android.SdkConstants;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -37,11 +37,9 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.XmlnsAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.CustomViewDescriptorService;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.manifest.descriptors.AndroidManifestDescriptors;
import com.android.ide.eclipse.adt.internal.editors.otherxml.descriptors.OtherXmlDescriptors;
import com.android.ide.eclipse.adt.internal.editors.uimodel.IUiUpdateListener.UiUpdateState;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.utils.XmlUtils;
//Synthetic comment -- @@ -252,7 +250,7 @@
attr = _Element_getAttributeNS(elem,
SdkConstants.NS_RESOURCES,
AndroidManifestDescriptors.ANDROID_LABEL_ATTR);
            } else if (mXmlNode.getNodeName().equals(LayoutDescriptors.VIEW_FRAGMENT)) {
attr = attr.substring(attr.lastIndexOf('.') + 1);
}
if (attr == null || attr.length() == 0) {
//Synthetic comment -- @@ -263,12 +261,12 @@
if (attr == null || attr.length() == 0) {
attr = _Element_getAttributeNS(elem,
null, // no namespace
                                ValuesDescriptors.NAME_ATTR);
}
if (attr == null || attr.length() == 0) {
attr = _Element_getAttributeNS(elem,
SdkConstants.NS_RESOURCES,
                                LayoutDescriptors.ID_ATTR);

if (attr != null && attr.length() > 0) {
for (String prefix : ID_PREFIXES) {
//Synthetic comment -- @@ -299,7 +297,7 @@
// Special case: for <view>, show the class attribute value instead.
// This is done here rather than in the descriptor since this depends on
// node instance data.
        if (LayoutDescriptors.VIEW_VIEWTAG.equals(uiName) && mXmlNode instanceof Element) {
Element element = (Element) mXmlNode;
String cls = element.getAttribute(ATTR_CLASS);
if (cls != null) {
//Synthetic comment -- @@ -1061,7 +1059,7 @@
for (AttributeDescriptor attrDesc : getAttributeDescriptors()) {
if (attrDesc instanceof XmlnsAttributeDescriptor) {
XmlnsAttributeDescriptor desc = (XmlnsAttributeDescriptor) attrDesc;
                Attr attr = doc.createAttributeNS(XmlUtils.XMLNS_URI,
desc.getXmlNsName());
attr.setValue(desc.getValue());
attr.setPrefix(desc.getXmlNsPrefix());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiListAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiListAttributeNode.java
//Synthetic comment -- index d8a3e9d..0fd317c 100644

//Synthetic comment -- @@ -25,7 +25,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.utils.XmlUtils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -141,9 +140,9 @@
// FrameworkResourceManager expects a specific prefix for the attribute.
String nsPrefix = "";
if (SdkConstants.NS_RESOURCES.equals(descriptor.getNamespaceUri())) {
            nsPrefix = XmlUtils.ANDROID_NS_NAME + ':';
        } else if (XmlUtils.XMLNS_URI.equals(descriptor.getNamespaceUri())) {
            nsPrefix = XmlUtils.XMLNS_COLON;
}
attr_name = nsPrefix + attr_name;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/uimodel/UiResourceAttributeNode.java
//Synthetic comment -- index ac713d0..4addbf0 100644

//Synthetic comment -- @@ -16,13 +16,14 @@

package com.android.ide.eclipse.adt.internal.editors.uimodel;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_THEME_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_THEME_REF;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;

import com.android.ide.common.api.IAttributeInfo;
import com.android.ide.common.api.IAttributeInfo.Format;
//Synthetic comment -- @@ -32,7 +33,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DescriptorsUtils;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.layout.descriptors.LayoutDescriptors;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -254,7 +254,7 @@
// or does not have the resource type in it. Simply offer the list of potential
// resource types.
if (prefix != null && prefix.startsWith(PREFIX_THEME_REF)) {
                results.add(PREFIX_ANDROID_THEME_REF + ResourceType.ATTR.getName() + '/');
if (resTypes.contains(ResourceType.ATTR)
|| resTypes.contains(ResourceType.STYLE)) {
results.add(PREFIX_THEME_REF + ResourceType.ATTR.getName() + '/');
//Synthetic comment -- @@ -264,7 +264,7 @@

for (ResourceType resType : resTypes) {
if (isSystem) {
                    results.add(PREFIX_ANDROID_RESOURCE_REF + resType.getName() + '/');
} else {
results.add('@' + resType.getName() + '/');
}
//Synthetic comment -- @@ -278,7 +278,7 @@
// "@an" we offer to complete it.
if (prefix == null ||
ANDROID_PKG.regionMatches(0, prefix, 1, prefix.length() - 1)) {
                results.add(PREFIX_ANDROID_RESOURCE_REF);
}
} else if (repository != null) {
// We have a style name and a repository. Find all resources that match this
//Synthetic comment -- @@ -351,7 +351,7 @@

/** Compute a suitable sorting score for the given  */
private static final int score(IAttributeInfo attributeInfo, String value) {
        if (value.equals(PREFIX_ANDROID_RESOURCE_REF)) {
return -1;
}

//Synthetic comment -- @@ -383,7 +383,7 @@
return -2;
}

                    if (value.startsWith(PREFIX_ANDROID_RESOURCE_REF + type + '/')) {
return -2;
}
}
//Synthetic comment -- @@ -392,7 +392,7 @@
return -2;
}

                    if (value.startsWith(PREFIX_ANDROID_THEME_REF + type + '/')) {
return -2;
}
}
//Synthetic comment -- @@ -407,7 +407,7 @@
type = "id"; //$NON-NLS-1$
} else if (attribute.equals(ATTR_STYLE)) {
type = "style"; //$NON-NLS-1$
        } else if (attribute.equals(LayoutDescriptors.ATTR_LAYOUT)) {
type = "layout"; //$NON-NLS-1$
} else if (attribute.equals("drawable")) { //$NON-NLS-1$
type = "drawable"; //$NON-NLS-1$
//Synthetic comment -- @@ -422,7 +422,7 @@
return -2;
}

                if (value.startsWith(PREFIX_ANDROID_RESOURCE_REF + type + '/')) {
return -2;
}
}
//Synthetic comment -- @@ -431,7 +431,7 @@
return -2;
}

                if (value.startsWith(PREFIX_ANDROID_THEME_REF + type + '/')) {
return -2;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/ValuesContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/ValuesContentAssist.java
//Synthetic comment -- index 9712876..bd6c079 100644

//Synthetic comment -- @@ -16,15 +16,15 @@

package com.android.ide.eclipse.adt.internal.editors.values;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor.ATTRIBUTE_ICON_FILENAME;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.ITEM_TAG;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.NAME_ATTR;
import static com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors.STYLE_ELEMENT;
import static com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData.DESCRIPTOR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TYPE;
import static com.android.utils.XmlUtils.ANDROID_NS_NAME_PREFIX;

import com.android.annotations.VisibleForTesting;
import com.android.ide.eclipse.adt.internal.editors.AndroidContentAssist;
//Synthetic comment -- @@ -76,7 +76,7 @@
super.computeAttributeValues(proposals, offset, parentTagName, attributeName, node,
wordPrefix, skipEndTag, replaceLength);

        if (parentTagName.equals(ITEM_TAG) && NAME_ATTR.equals(attributeName)) {

// Special case: the user is code completing inside
//    <style><item name="^"/></style>
//Synthetic comment -- @@ -142,9 +142,9 @@
super.computeTextValues(proposals, offset, parentNode, currentNode, uiParent,
prefix);

        if (parentNode.getNodeName().equals(ITEM_TAG) &&
parentNode.getParentNode() != null &&
            STYLE_ELEMENT.equals(parentNode.getParentNode().getNodeName())) {

// Special case: the user is code completing inside
//    <style><item name="android:foo"/>|</style>
//Synthetic comment -- @@ -158,7 +158,7 @@
if (descriptorProvider != null) {

Element element = (Element) parentNode;
                    String attrName = element.getAttribute(NAME_ATTR);
int pos = attrName.indexOf(':');
if (pos >= 0) {
attrName = attrName.substring(pos + 1);
//Synthetic comment -- @@ -203,7 +203,7 @@
}
}

        if (parentNode.getNodeName().equals(ITEM_TAG)) {
// Completing text content inside an <item> tag: offer @resource completion.
if (prefix.startsWith(PREFIX_RESOURCE_REF) || prefix.trim().length() == 0) {
String[] choices = UiResourceAttributeNode.computeResourceStringMatches(
//Synthetic comment -- @@ -218,7 +218,7 @@
String value = typeNode.getNodeValue();
List<String> filtered = new ArrayList<String>();
for (String s : choices) {
                        if (s.startsWith(PREFIX_ANDROID_RESOURCE_REF) ||
s.startsWith(PREFIX_RESOURCE_REF+ value)) {
filtered.add(s);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/descriptors/ValuesDescriptors.java
//Synthetic comment -- index 8f4d5c8..724e019 100644

//Synthetic comment -- @@ -16,6 +16,19 @@

package com.android.ide.eclipse.adt.internal.editors.values.descriptors;

import com.android.ide.common.api.IAttributeInfo.Format;
import com.android.ide.common.resources.platform.AttributeInfo;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
//Synthetic comment -- @@ -35,24 +48,6 @@
* Complete description of the structure for resources XML files (under res/values/)
*/
public final class ValuesDescriptors implements IDescriptorProvider {

    // Public attributes names, attributes descriptors and elements descriptors

    public static final String ROOT_ELEMENT = "resources"; //$NON-NLS-1$
    public static final String STRING_ELEMENT = "string";  //$NON-NLS-1$
    public static final String STYLE_ELEMENT = "style";    //$NON-NLS-1$
    public static final String COLOR_ELEMENT = "color";    //$NON-NLS-1$
    public static final String DIMEN_ELEMENT = "dimen";    //$NON-NLS-1$
    public static final String DRAWABLE_ELEMENT = "drawable"; //$NON-NLS-1$
    public static final String INTEGER_ARRAY_ELEMENT = "integer-array"; //$NON-NLS-1$
    public static final String STRING_ARRAY_ELEMENT = "string-array";   //$NON-NLS-1$
    public static final String PLURALS_ELEMENT = "plurals";             //$NON-NLS-1$

    public static final String ITEM_TAG = "item";  //$NON-NLS-1$
    public static final String NAME_ATTR = "name"; //$NON-NLS-1$
    public static final String TYPE_ATTR = "type"; //$NON-NLS-1$
    public static final String PARENT_ATTR = "parent"; //$NON-NLS-1$

private static final ValuesDescriptors sThis = new ValuesDescriptors();

/** The {@link ElementDescriptor} for the root Resources element. */
//Synthetic comment -- @@ -85,15 +80,15 @@

// Elements

        AttributeInfo nameAttrInfo = new AttributeInfo(NAME_ATTR, Format.STRING_SET);

ElementDescriptor color_element = new ElementDescriptor(
                COLOR_ELEMENT,
"Color",
"A @color@ value specifies an RGB value with an alpha channel, which can be used in various places such as specifying a solid color for a Drawable or the color to use for text.  It always begins with a # character and then is followed by the alpha-red-green-blue information in one of the following formats: #RGB, #ARGB, #RRGGBB or #AARRGGBB.",
"http://code.google.com/android/reference/available-resources.html#colorvals",  //$NON-NLS-1$
new AttributeDescriptor[] {
                        new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo),
new ColorValueDescriptor(
//Synthetic comment -- @@ -105,12 +100,12 @@
false /* not mandatory */);

ElementDescriptor string_element = new ElementDescriptor(
                STRING_ELEMENT,
"String",
"@Strings@, with optional simple formatting, can be stored and retrieved as resources. You can add formatting to your string by using three standard HTML tags: b, i, and u. If you use an apostrophe or a quote in your string, you must either escape it or enclose the whole string in the other kind of enclosing quotes.",
"http://code.google.com/android/reference/available-resources.html#stringresources",  //$NON-NLS-1$
new AttributeDescriptor[] {
                        new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this string."),
//Synthetic comment -- @@ -122,18 +117,18 @@
false /* not mandatory */);

ElementDescriptor item_element = new ItemElementDescriptor(
                 ITEM_TAG,
"Item",
null,  // TODO find javadoc
null,  // TODO find link to javadoc
new AttributeDescriptor[] {
                         new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this resource."),
                         new ListAttributeDescriptor(TYPE_ATTR,
null /* nsUri */,
                                 new AttributeInfo(TYPE_ATTR,
EnumSet.of(Format.STRING, Format.ENUM)
).setEnumValues(ResourceType.getNames())
).setTooltip("The mandatory type of this resource."),
//Synthetic comment -- @@ -144,7 +139,7 @@
).setFlagValues(
new String[] {
"boolean",     //$NON-NLS-1$
                                         COLOR_ELEMENT,
"dimension",   //$NON-NLS-1$
"float",       //$NON-NLS-1$
"fraction",    //$NON-NLS-1$
//Synthetic comment -- @@ -161,12 +156,12 @@
false /* not mandatory */);

ElementDescriptor drawable_element = new ElementDescriptor(
                DRAWABLE_ELEMENT,
"Drawable",
"A @drawable@ defines a rectangle of color. Android accepts color values written in various web-style formats -- a hexadecimal constant in any of the following forms: #RGB, #ARGB, #RRGGBB, #AARRGGBB. Zero in the alpha channel means transparent. The default value is opaque.",
"http://code.google.com/android/reference/available-resources.html#colordrawableresources",  //$NON-NLS-1$
new AttributeDescriptor[] {
                        new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this drawable."),
//Synthetic comment -- @@ -178,12 +173,12 @@
false /* not mandatory */);

ElementDescriptor dimen_element = new ElementDescriptor(
                DIMEN_ELEMENT,
"Dimension",
"You can create common dimensions to use for various screen elements by defining @dimension@ values in XML. A dimension resource is a number followed by a unit of measurement. Supported units are px (pixels), in (inches), mm (millimeters), pt (points at 72 DPI), dp (density-independent pixels) and sp (scale-independent pixels)",
"http://code.google.com/android/reference/available-resources.html#dimension",  //$NON-NLS-1$
new AttributeDescriptor[] {
                        new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this dimension."),
//Synthetic comment -- @@ -195,12 +190,12 @@
false /* not mandatory */);

ElementDescriptor style_element = new ElementDescriptor(
                STYLE_ELEMENT,
"Style/Theme",
"Both @styles and themes@ are defined in a style block containing one or more string or numerical values (typically color values), or references to other resources (drawables and so on).",
"http://code.google.com/android/reference/available-resources.html#stylesandthemes",  //$NON-NLS-1$
new AttributeDescriptor[] {
                        new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this theme."),
//Synthetic comment -- @@ -212,12 +207,12 @@
},
new ElementDescriptor[] {
new ElementDescriptor(
                        ITEM_TAG,
"Item",
"A value to use in this @theme@. It can be a standard string, a hex color value, or a reference to any other resource type.",
"http://code.google.com/android/reference/available-resources.html#stylesandthemes",  //$NON-NLS-1$
new AttributeDescriptor[] {
                            new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this item."),
//Synthetic comment -- @@ -231,19 +226,19 @@
false /* not mandatory */);

ElementDescriptor string_array_element = new ElementDescriptor(
                 STRING_ARRAY_ELEMENT,
"String Array",
"An array of strings. Strings are added as underlying item elements to the array.",
null, // tooltips
new AttributeDescriptor[] {
                         new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this string array."),
},
new ElementDescriptor[] {
new ElementDescriptor(
                         ITEM_TAG,
"Item",
"A string value to use in this string array.",
null, // tooltip
//Synthetic comment -- @@ -258,19 +253,19 @@
false /* not mandatory */);

ElementDescriptor plurals_element = new ElementDescriptor(
                 PLURALS_ELEMENT,
"Quantity Strings (Plurals)",
"A quantity string",
null, // tooltips
new AttributeDescriptor[] {
                         new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("A name for the pair of strings. This name will be used as the resource ID."),
},
new ElementDescriptor[] {
new ElementDescriptor(
                         ITEM_TAG,
"Item",
"A plural or singular string",
null, // tooltip
//Synthetic comment -- @@ -294,19 +289,19 @@
false /* not mandatory */);

ElementDescriptor integer_array_element = new ElementDescriptor(
                 INTEGER_ARRAY_ELEMENT,
"Integer Array",
"An array of integers. Integers are added as underlying item elements to the array.",
null, // tooltips
new AttributeDescriptor[] {
                         new TextAttributeDescriptor(NAME_ATTR,
null /* nsUri */,
nameAttrInfo)
.setTooltip("The mandatory name used in referring to this integer array.")
},
new ElementDescriptor[] {
new ElementDescriptor(
                         ITEM_TAG,
"Item",
"An integer value to use in this integer array.",
null, // tooltip
//Synthetic comment -- @@ -321,7 +316,7 @@
false /* not mandatory */);

mResourcesElement = new ElementDescriptor(
                        ROOT_ELEMENT,
"Resources",
null,
"http://code.google.com/android/reference/available-resources.html",  //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/uimodel/UiItemElementNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/values/uimodel/UiItemElementNode.java
//Synthetic comment -- index 138605f..88ac3e1 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.editors.values.uimodel;

import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ItemElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -45,8 +45,8 @@
if (xmlNode != null && xmlNode instanceof Element && xmlNode.hasAttributes()) {

Element elem = (Element) xmlNode;
            String type = elem.getAttribute(ValuesDescriptors.TYPE_ATTR);
            String name = elem.getAttribute(ValuesDescriptors.NAME_ATTR);
if (type != null && name != null && type.length() > 0 && name.length() > 0) {
type = AdtUtils.capitalize(type);
return String.format("%1$s (%2$s %3$s)", name, type, getDescriptor().getUiName());








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java
//Synthetic comment -- index 69f4f55..b494f62 100644

//Synthetic comment -- @@ -80,7 +80,7 @@
}

/**
     * Helper method to determine if given manifest has a <code>AndroidConstants.LIBRARY_TEST_RUNNER
* </code> library reference
*
* @param manifestParser the {@link ManifestData} to search








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddPrefixFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddPrefixFix.java
//Synthetic comment -- index cc8ed75..d8ce657 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.utils.XmlUtils;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAnnotation.java
//Synthetic comment -- index df196de..80dac65 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.lint;

import static com.android.tools.lint.detector.api.LintConstants.FQCN_SUPPRESS_LINT;
import static com.android.tools.lint.detector.api.LintConstants.FQCN_TARGET_API;
import static com.android.tools.lint.detector.api.LintConstants.SUPPRESS_LINT;
import static com.android.tools.lint.detector.api.LintConstants.TARGET_API;
import static org.eclipse.jdt.core.dom.ArrayInitializer.EXPRESSIONS_PROPERTY;
import static org.eclipse.jdt.core.dom.SingleMemberAnnotation.VALUE_PROPERTY;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/AddSuppressAttribute.java
//Synthetic comment -- index e7037ff..23943d5 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.lint;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_IGNORE;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/ConvertToDpFix.java
//Synthetic comment -- index 4a351a0..0ca6aa2 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.common.layout.LayoutConstants.VALUE_N_DP;

import com.android.ide.eclipse.adt.AdtPlugin;

//Synthetic comment -- @@ -109,4 +109,4 @@

return null;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintClient.java
//Synthetic comment -- index 16daae3..1937a94 100644

//Synthetic comment -- @@ -16,8 +16,8 @@
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.SdkConstants.FD_NATIVE_LIBS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAR;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.MARKER_LINT;
import static com.android.ide.eclipse.adt.AdtUtils.workspacePathToFile;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LinearLayoutWeightFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LinearLayoutWeightFix.java
//Synthetic comment -- index dee581f..af3fc74 100644

//Synthetic comment -- @@ -15,12 +15,12 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_ZERO_DP;
import static com.android.utils.XmlUtils.ANDROID_URI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintDeltaProcessor.java
//Synthetic comment -- index 6b94232..df8d9af 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_CLASS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_JAVA;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintFixGenerator.java
//Synthetic comment -- index 97cedc7..47aac0c 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintJob.java
//Synthetic comment -- index 1d62564..0442f18 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_CLASS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
//Synthetic comment -- @@ -186,4 +186,4 @@
IJobManager jobManager = Job.getJobManager();
return jobManager.find(LintJob.FAMILY_RUN_LINT);
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/LintViewPart.java
//Synthetic comment -- index 188d2cf..90b956e 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java
//Synthetic comment -- index a0f1262..a0d414e 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetAttributeFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetAttributeFix.java
//Synthetic comment -- index a860c69..4ed5e05 100644

//Synthetic comment -- @@ -15,17 +15,18 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.common.layout.LayoutConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.ide.common.layout.LayoutConstants.ATTR_INPUT_TYPE;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FALSE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TRANSLATABLE;

import com.android.tools.lint.checks.AccessibilityDetector;
import com.android.tools.lint.checks.InefficientWeightDetector;
import com.android.tools.lint.checks.SecurityDetector;
import com.android.tools.lint.checks.TextFieldDetector;
import com.android.tools.lint.checks.TranslationDetector;
import com.android.tools.lint.detector.api.LintConstants;

import org.eclipse.core.resources.IMarker;

//Synthetic comment -- @@ -40,9 +41,9 @@
if (mId.equals(AccessibilityDetector.ISSUE.getId())) {
return ATTR_CONTENT_DESCRIPTION;
} else if (mId.equals(InefficientWeightDetector.BASELINE_WEIGHTS.getId())) {
            return LintConstants.ATTR_BASELINE_ALIGNED;
} else if (mId.equals(SecurityDetector.EXPORTED_SERVICE.getId())) {
            return LintConstants.ATTR_PERMISSION;
} else if (mId.equals(TextFieldDetector.ISSUE.getId())) {
return ATTR_INPUT_TYPE;
} else if (mId.equals(TranslationDetector.MISSING.getId())) {
//Synthetic comment -- @@ -117,4 +118,4 @@
return super.getProposal();
}

}
\ No newline at end of file








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetPropertyFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetPropertyFix.java
//Synthetic comment -- index e07ee11..ee049ca 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetScrollViewSizeFix.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/SetScrollViewSizeFix.java
//Synthetic comment -- index 4355950..52860cf 100644

//Synthetic comment -- @@ -15,11 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.lint;

import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.VALUE_WRAP_CONTENT;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.utils.XmlUtils.ANDROID_URI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AttributeSortOrder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AttributeSortOrder.java
//Synthetic comment -- index 5df1a5e..b743014 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.preferences;

import static com.android.utils.XmlUtils.XMLNS;

import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ExportHelper.java
//Synthetic comment -- index c448f9b..fb8742d 100644

//Synthetic comment -- @@ -136,7 +136,7 @@

// tmp file for the packaged resource file. To not disturb the incremental builders
// output, all intermediary files are created in tmp files.
            File resourceFile = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_RES);
resourceFile.deleteOnExit();

// Make sure the PNG crunch cache is up to date
//Synthetic comment -- @@ -160,7 +160,7 @@
// Step 2. Convert the byte code to Dalvik bytecode

// tmp file for the packaged resource file.
            File dexFile = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_DEX);
dexFile.deleteOnExit();

ProjectState state = Sdk.getProjectState(project);
//Synthetic comment -- @@ -219,7 +219,7 @@

// create a jar file containing all the project output (as proguard cannot
// process folders of .class files).
                File inputJar = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_JAR);
inputJar.deleteOnExit();
JarOutputStream jos = new JarOutputStream(new FileOutputStream(inputJar));

//Synthetic comment -- @@ -237,7 +237,7 @@
jos.close();

// destination file for proguard
                File obfuscatedJar = File.createTempFile(TEMP_PREFIX, AdtConstants.DOT_JAR);
obfuscatedJar.deleteOnExit();

// run proguard
//Synthetic comment -- @@ -327,7 +327,7 @@
Shell shell = Display.getCurrent().getActiveShell();

// create a default file name for the apk.
        String fileName = project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;

// Pop up the file save window to get the file location
FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/LibraryClasspathContainerInitializer.java
//Synthetic comment -- index ff4ac05..aa0e736 100644

//Synthetic comment -- @@ -207,7 +207,7 @@

if (outputFolder != null) { // can happen when closing/deleting a library)
IFile jarIFile = outputFolder.getFile(libProject.getName().toLowerCase() +
                        AdtConstants.DOT_JAR);

// get the source folder for the library project
List<IPath> srcs = BaseProjectHelper.getSourceClasspaths(libProject);
//Synthetic comment -- @@ -383,7 +383,7 @@
IResource[] members = libsFolder.members();
for (IResource member : members) {
if (member.getType() == IResource.FILE &&
                            AdtConstants.EXT_JAR.equalsIgnoreCase(member.getFileExtension())) {
jarFiles.add(member.getLocation().toFile());
}
}
//Synthetic comment -- @@ -536,7 +536,7 @@

IResource resource = wsRoot.findMember(path);

        if (AdtConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
// case of a jar file (which could be relative to the workspace or a full path)
if (resource != null && resource.exists() &&
resource.getType() == IResource.FILE) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 2146b88..cb58b02 100644

//Synthetic comment -- @@ -813,10 +813,10 @@
*/
public static String getApkFilename(IProject project, String config) {
if (config != null) {
            return project.getName() + "-" + config + AdtConstants.DOT_ANDROID_PACKAGE; //$NON-NLS-1$
}

        return project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;
}

/**
//Synthetic comment -- @@ -872,7 +872,7 @@


// get the package path
        String packageName = project.getName() + AdtConstants.DOT_ANDROID_PACKAGE;
IResource r = outputLocation.findMember(packageName);

// check the package is present








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/SupportLibraryHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/SupportLibraryHelper.java
//Synthetic comment -- index 35bc2a9..e1819b2 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.project;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT_V7;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE;
import static com.android.ide.common.layout.LayoutConstants.FQCN_SPACE_V7;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChange.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/changes/AndroidLayoutChange.java
//Synthetic comment -- index c9d6b34..834a57c 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.changes;

import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.internal.refactoring.core.RefactoringUtil;

import org.eclipse.core.filebuffers.ITextFileBufferManager;
//Synthetic comment -- @@ -135,8 +135,8 @@
MultiTextEdit multiEdit = new MultiTextEdit();
for (AndroidLayoutChangeDescription change : mChanges) {
if (!change.isStandalone()) {
                TextEdit edit = createTextEdit(LayoutConstants.VIEW,
                        LayoutConstants.ATTR_CLASS,
change.getClassName(),
change.getNewName());
if (edit != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidPackageRenameParticipant.java
//Synthetic comment -- index 6a17b1b..52b49a7 100644

//Synthetic comment -- @@ -16,9 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -298,7 +296,7 @@
IResource resource = layoutMembers[j];
if (resource instanceof IFolder
&& resource.exists()
                        && resource.getName().startsWith(AndroidConstants.FD_RES_LAYOUT)) {
IFolder layoutFolder = (IFolder) resource;
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {
//Synthetic comment -- @@ -355,13 +353,13 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc.getElementsByTagName(LayoutConstants.VIEW);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
Node attributeNode = attributes
                                    .getNamedItem(LayoutConstants.ATTR_CLASS);
if (attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeMoveParticipant.java
//Synthetic comment -- index 039f83f..25ca533 100644

//Synthetic comment -- @@ -16,9 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -233,7 +231,7 @@
private void addLayoutChanges(IProject project, String className) {
try {
IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);
            IFolder layoutFolder = resFolder.getFolder(AndroidConstants.FD_RES_LAYOUT);
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {
IResource member = members[i];
//Synthetic comment -- @@ -281,13 +279,13 @@
if (model != null) {
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
                    NodeList nodes = xmlDoc.getElementsByTagName(LayoutConstants.VIEW);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
Node attributeNode =
                                attributes.getNamedItem(LayoutConstants.ATTR_CLASS);
if (attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactoring/core/AndroidTypeRenameParticipant.java
//Synthetic comment -- index d3fa5c4..d62cc23 100644

//Synthetic comment -- @@ -16,9 +16,7 @@

package com.android.ide.eclipse.adt.internal.refactoring.core;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
//Synthetic comment -- @@ -194,7 +192,7 @@
private void addLayoutChanges(IProject project, String className) {
try {
IFolder resFolder = project.getFolder(SdkConstants.FD_RESOURCES);
            IFolder layoutFolder = resFolder.getFolder(AndroidConstants.FD_RES_LAYOUT);
IResource[] members = layoutFolder.members();
for (int i = 0; i < members.length; i++) {
IResource member = members[i];
//Synthetic comment -- @@ -243,13 +241,13 @@
IDOMModel xmlModel = (IDOMModel) model;
IDOMDocument xmlDoc = xmlModel.getDocument();
NodeList nodes = xmlDoc
                            .getElementsByTagName(LayoutConstants.VIEW);
for (int i = 0; i < nodes.getLength(); i++) {
Node node = nodes.item(i);
NamedNodeMap attributes = node.getAttributes();
if (attributes != null) {
Node attributeNode =
                                attributes.getNamedItem(LayoutConstants.ATTR_CLASS);
if (attributeNode instanceof Attr) {
Attr attribute = (Attr) attributeNode;
String value = attribute.getValue();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringInputPage.java
//Synthetic comment -- index d3304a4..5ac5f5c 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.ide.eclipse.adt.internal.refactorings.extractstring;


import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -582,7 +581,7 @@
wsFolderPath = wsFolderPath.substring(0, pos);
}

                String[] folderSegments = wsFolderPath.split(AndroidConstants.RES_QUALIFIER_SEP);

if (folderSegments.length > 0) {
String folderName = folderSegments[0];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index abdbd66..e9d386e 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.utils.XmlUtils.AMP_ENTITY;
import static com.android.utils.XmlUtils.LT_ENTITY;
import static com.android.utils.XmlUtils.QUOT_ENTITY;

import com.android.SdkConstants;
import com.android.ide.common.xml.ManifestData;
//Synthetic comment -- @@ -29,7 +29,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.ReferenceAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiAttributeNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
//Synthetic comment -- @@ -427,7 +426,7 @@
}

// Check this a Layout XML file and get the selection and its context.
            if (mFile != null && AdtConstants.EXT_XML.equals(mFile.getFileExtension())) {

// Currently we only support Android resource XML files, so they must have a path
// similar to
//Synthetic comment -- @@ -1069,7 +1068,7 @@
if (res.exists() && !res.isDerived() && res instanceof IFile) {
IFile file = (IFile) res;
// Must have an XML extension
                                if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {
IPath p = file.getFullPath();
// And not be either paths we want to filter out
if ((mFilterPath1 != null && mFilterPath1.equals(p)) ||
//Synthetic comment -- @@ -1117,7 +1116,7 @@
SubMonitor monitor) {

TextFileChange xmlChange = new TextFileChange(getName(), targetXml);
        xmlChange.setTextType(AdtConstants.EXT_XML);

String error = "";                  //$NON-NLS-1$
TextEdit edit = null;
//Synthetic comment -- @@ -1186,9 +1185,9 @@

IModelManager modelMan = StructuredModelManager.getModelManager();

        final String NODE_RESOURCES = ValuesDescriptors.ROOT_ELEMENT;
        final String NODE_STRING = "string";    //$NON-NLS-1$ //TODO find or create constant
        final String ATTR_NAME = "name";        //$NON-NLS-1$ //TODO find or create constant


// Scan the source to find the best insertion point.
//Synthetic comment -- @@ -1570,7 +1569,7 @@
HashSet<IFile> files = new HashSet<IFile>();
files.add(sourceFile);

        if (allConfigurations && AdtConstants.EXT_XML.equals(sourceFile.getFileExtension())) {
IPath path = sourceFile.getFullPath();
if (path.segmentCount() == 4 && path.segment(1).equals(SdkConstants.FD_RESOURCES)) {
IProject project = sourceFile.getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/XmlStringFileHelper.java
//Synthetic comment -- index 9a40464..01e814e 100644

//Synthetic comment -- @@ -16,18 +16,12 @@

package com.android.ide.eclipse.adt.internal.refactorings.extractstring;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
//Synthetic comment -- @@ -124,15 +118,15 @@
//    <string name="ID">something</string>
// </resources>

                    Node root = findChild(doc, null, ValuesDescriptors.ROOT_ELEMENT);
if (root != null) {
for (Node strNode = findChild(root, null,
                                                      ValuesDescriptors.STRING_ELEMENT);
strNode != null;
strNode = findChild(null, strNode,
                                                 ValuesDescriptors.STRING_ELEMENT)) {
NamedNodeMap attrs = strNode.getAttributes();
                            Node nameAttr = attrs.getNamedItem(ValuesDescriptors.NAME_ATTR);
if (nameAttr != null) {
String id = nameAttr.getNodeValue();









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/renamepackage/ApplicationPackageNameRefactoring.java
//Synthetic comment -- index 92f6853..7005d82 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.ide.eclipse.adt.internal.refactorings.renamepackage;

import static com.android.utils.XmlUtils.ANDROID_URI;
import static com.android.utils.XmlUtils.XMLNS_COLON;

import com.android.SdkConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -151,7 +148,7 @@

ImportRewrite irw = ImportRewrite.create(cu, true);
irw.addImport(mNewPackageName.getFullyQualifiedName() + '.'
                    + AdtConstants.FN_RESOURCE_BASE);

try {
rewrittenImports.addChild( irw.rewriteImports(null) );
//Synthetic comment -- @@ -208,7 +205,7 @@
}

TextFileChange xmlChange = new TextFileChange("XML resource file edit", file);
        xmlChange.setTextType(AdtConstants.EXT_XML);

MultiTextEdit multiEdit = new MultiTextEdit();
ArrayList<TextEditGroup> editGroups = new ArrayList<TextEditGroup>();
//Synthetic comment -- @@ -241,7 +238,7 @@
// Check this is the attribute and the original string

if (lastAttrName != null &&
                            lastAttrName.startsWith(XMLNS_COLON)) {

String lastAttrValue = region.getText(subRegion);
if (oldAppNamespaceString.equals(stripQuotes(lastAttrValue))) {
//Synthetic comment -- @@ -299,7 +296,7 @@
}

TextFileChange xmlChange = new TextFileChange("Make Manifest edits", file);
        xmlChange.setTextType(AdtConstants.EXT_XML);

MultiTextEdit multiEdit = new MultiTextEdit();
ArrayList<TextEditGroup> editGroups = new ArrayList<TextEditGroup>();
//Synthetic comment -- @@ -336,12 +333,12 @@

String lastAttrValue = region.getText(subRegion);
if (lastAttrName != null &&
                            lastAttrName.startsWith(XMLNS_COLON)) {

// Resolves the android namespace prefix for this file
                        if (ANDROID_URI.equals(stripQuotes(lastAttrValue))) {
String android_namespace_prefix = lastAttrName
                                .substring(XMLNS_COLON.length());
android_name_attribute = android_namespace_prefix + ':'
+ AndroidManifest.ATTRIBUTE_NAME;
}
//Synthetic comment -- @@ -416,7 +413,7 @@
public boolean visit(IResource resource) throws CoreException {
if (resource instanceof IFile) {
IFile file = (IFile) resource;
                if (AdtConstants.EXT_JAVA.equals(file.getFileExtension())) {

ICompilationUnit icu = JavaCore.createCompilationUnitFrom(file);

//Synthetic comment -- @@ -429,7 +426,7 @@
edit.addChild(text_edit);

TextFileChange text_file_change = new TextFileChange(file.getName(), file);
                        text_file_change.setTextType(AdtConstants.EXT_JAVA);
text_file_change.setEdit(edit);
mChanges.add(text_file_change);
}
//Synthetic comment -- @@ -437,7 +434,7 @@
// XXX Partially taken from ExtractStringRefactoring.java
// Check this a Layout XML file and get the selection and
// its context.
                } else if (AdtConstants.EXT_XML.equals(file.getFileExtension())) {

if (SdkConstants.FN_ANDROID_MANIFEST_XML.equals(file.getName())) {

//Synthetic comment -- @@ -509,7 +506,7 @@
QualifiedName qualifiedImportName = (QualifiedName) importName;

if (qualifiedImportName.getName().getIdentifier()
                        .equals(AdtConstants.FN_RESOURCE_BASE)) {
mRewriter.replace(qualifiedImportName.getQualifier(), mNewPackageName,
null);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index e3ccc0b..6b09b34 100644

//Synthetic comment -- @@ -16,16 +16,21 @@

package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.SdkConstants.FD_RESOURCES;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceDeltaKind;
//Synthetic comment -- @@ -58,7 +63,6 @@
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.ImageUtils;
import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.VisualRefactoring;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.wizards.newxmlfile.NewXmlFileWizard;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -113,9 +117,6 @@
@SuppressWarnings("restriction") // XML model
public class ResourceHelper {

    private static final String TAG_ITEM = "item"; //$NON-NLS-1$
    private static final String ATTR_COLOR = "color";  //$NON-NLS-1$

private final static Map<Class<?>, Image> sIconMap = new HashMap<Class<?>, Image>(
FolderConfiguration.getQualifierCount());

//Synthetic comment -- @@ -183,7 +184,7 @@
* @return a pair of the resource type and the resource name
*/
public static Pair<ResourceType,String> parseResource(String url) {
        if (!url.startsWith("@")) { //$NON-NLS-1$
return null;
}
int typeEnd = url.indexOf('/', 1);
//Synthetic comment -- @@ -274,7 +275,7 @@
*/
public static boolean canCreateResource(String resource) {
// Cannot create framework resources
        if (resource.startsWith('@' + ANDROID_PKG + ':')) {
return false;
}

//Synthetic comment -- @@ -411,9 +412,9 @@
elementImpl.setEmptyTag(true);
}
}
                        element.setAttribute(ValuesDescriptors.NAME_ATTR, name);
if (!tagName.equals(typeName)) {
                            element.setAttribute(ValuesDescriptors.TYPE_ATTR, typeName);
}
root.insertBefore(element, nextChild);
IRegion region = null;
//Synthetic comment -- @@ -450,7 +451,7 @@
String prolog = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"; //$NON-NLS-1$
StringBuilder sb = new StringBuilder(prolog);

            String root = ValuesDescriptors.ROOT_ELEMENT;
sb.append('<').append(root).append('>').append('\n');
sb.append("    "); //$NON-NLS-1$
sb.append('<');
//Synthetic comment -- @@ -508,10 +509,10 @@
* @return the user visible theme name
*/
public static String styleToTheme(String style) {
        if (style.startsWith(PREFIX_STYLE)) {
            style = style.substring(PREFIX_STYLE.length());
        } else if (style.startsWith(PREFIX_ANDROID_STYLE)) {
            style = style.substring(PREFIX_ANDROID_STYLE.length());
} else if (style.startsWith(PREFIX_RESOURCE_REF)) {
// @package:style/foo
int index = style.indexOf('/');
//Synthetic comment -- @@ -530,9 +531,10 @@
*         to a framework theme
*/
public static boolean isProjectStyle(String style) {
        assert style.startsWith(PREFIX_STYLE) || style.startsWith(PREFIX_ANDROID_STYLE) : style;

        return style.startsWith(PREFIX_STYLE);
}

/**
//Synthetic comment -- @@ -577,7 +579,7 @@
}
return null;
}
            if (value.startsWith("@")) { //$NON-NLS-1$
boolean isFramework = color.isFramework();
color = resources.findResValue(value, isFramework);
if (color != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceNameValidator.java
//Synthetic comment -- index be11cf7..5ea1edc 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java
//Synthetic comment -- index ac70547..6554cc2 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.resources.IntArrayWrapper;
//Synthetic comment -- @@ -91,7 +92,7 @@

IProject project = file.getProject();

        if (file.getName().equals(AdtConstants.FN_COMPILED_RESOURCE_CLASS)) {
// create the classname
String className = getRClassName(project);
if (className == null) {
//Synthetic comment -- @@ -118,7 +119,7 @@
*/
private boolean packagePathMatches(String path, String packageName) {
// First strip the ".class" off the end of the path
        String pathWithoutExtension = path.substring(0, path.indexOf(AdtConstants.DOT_CLASS));

// then split the components of each path by their separators
String [] pathArray = pathWithoutExtension.split(Pattern.quote(File.separator));








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectClassLoader.java
//Synthetic comment -- index e118ff7..f993d89 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.BuildHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
//Synthetic comment -- @@ -304,7 +304,7 @@
IPath path = e.getPath();

// check the name ends with .jar
        if (AdtConstants.EXT_JAR.equalsIgnoreCase(path.getFileExtension())) {
boolean local = false;
IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
if (resource != null && resource.exists() &&








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidJarLoader.java
//Synthetic comment -- index e2f8a57..161d567 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.AdtConstants;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
//Synthetic comment -- @@ -162,7 +162,7 @@
// get the name of the entry.
String entryPath = entry.getName();

            if (!entryPath.endsWith(AdtConstants.DOT_CLASS)) {
// only accept class files
continue;
}
//Synthetic comment -- @@ -227,7 +227,7 @@
while ((entry = zis.getNextEntry()) != null) {
// get the name of the entry and convert to a class binary name
String entryPath = entry.getName();
            if (!entryPath.endsWith(AdtConstants.DOT_CLASS)) {
// only accept class files
continue;
}
//Synthetic comment -- @@ -349,7 +349,7 @@

// The name is a binary name. Something like "android.R", or "android.R$id".
// Make a path out of it.
        String entryName = className.replaceAll("\\.", "/") + AdtConstants.DOT_CLASS; //$NON-NLS-1$ //$NON-NLS-2$

// create streams to read the intermediary archive
FileInputStream fis = new FileInputStream(mOsFrameworkLocation);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 92f7311..59a1236 100644

//Synthetic comment -- @@ -66,7 +66,7 @@
* mAttributeValues is a map { key => list [ values ] }.
* The key for the map is "(element-xml-name,attribute-namespace:attribute-xml-local-name)".
* The attribute namespace prefix must be:
     * - "android" for AndroidConstants.NS_RESOURCES
* - "xmlns" for the XMLNS URI.
*
* This is used for attributes that do not have a unique name, but still need to be populated








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/LayoutParamsParser.java
//Synthetic comment -- index 5e38c64..d05c12a 100644

//Synthetic comment -- @@ -262,7 +262,7 @@
IClassDescriptor superClass = groupClass.getSuperclass();

// Assertion: at this point, we should have
        //   superClass != mTopViewClass || fqcn.equals(AndroidConstants.CLASS_VIEWGROUP);

if (superClass != null && superClass.equals(mTopViewClass) == false) {
ExtViewClassInfo superClassInfo = addGroup(superClass);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/Sdk.java
//Synthetic comment -- index 29ad9fa..b1b057b 100644

//Synthetic comment -- @@ -17,8 +17,8 @@
package com.android.ide.eclipse.adt.internal.sdk;

import static com.android.SdkConstants.FD_RES;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_JAR;

import com.android.SdkConstants;
import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ConfigurationSelector.java
//Synthetic comment -- index 5173a996..ce36457 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.eclipse.adt.internal.ui;

import com.android.AndroidConstants;
import com.android.ide.common.resources.configuration.CountryCodeQualifier;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.FolderConfiguration;
//Synthetic comment -- @@ -525,7 +525,7 @@
*/
public boolean setConfiguration(String folderName) {
// split the name of the folder in segments.
        String[] folderSegments = folderName.split(AndroidConstants.RES_QUALIFIER_SEP);

return setConfiguration(folderSegments);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceChooser.java
//Synthetic comment -- index 6be5084..1291af8 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_RESOURCE_REF;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_RESOURCE_REF;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -155,7 +155,7 @@
PREFIX_RESOURCE_REF + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$

mSystemResourcePattern = Pattern.compile(
                PREFIX_ANDROID_RESOURCE_REF + mResourceType.getName() + "/(.+)"); //$NON-NLS-1$

setTitle("Resource Chooser");
setMessage(String.format("Choose a %1$s resource",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourceExplorerView.java
//Synthetic comment -- index 746e727..f48423c 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
public class ResourceExplorerView extends ViewPart implements ISelectionListener,
IResourceEventListener {

    // Note: keep using the obsolete AndroidConstants.EDITORS_NAMESPACE (which used
// to be the Editors Plugin ID) to keep existing preferences functional.
private final static String PREFS_COLUMN_RES =
AdtConstants.EDITORS_NAMESPACE + "ResourceExplorer.Col1"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/ui/ResourcePreviewHelper.java
//Synthetic comment -- index 94bafb5..d97d176 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.ui;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_9PNG;
import static com.android.ide.eclipse.adt.AdtUtils.endsWithIgnoreCase;

import com.android.ide.common.rendering.api.ResourceValue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ApplicationInfoPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ApplicationInfoPage.java
//Synthetic comment -- index 79107d3..c832534 100644

//Synthetic comment -- @@ -568,7 +568,7 @@
} else if (osTarget.indexOf('.') == 0) {
osTarget = mValues.packageName + osTarget;
}
        osTarget = osTarget.replace('.', File.separatorChar) + AdtConstants.DOT_JAVA;

File projectDir = mValues.projectLocation;
File[] allDirs = projectDir.listFiles(new FileFilter() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreator.java
//Synthetic comment -- index b8f7c50..2f7ad7a 100644

//Synthetic comment -- @@ -20,11 +20,9 @@
import static com.android.sdklib.internal.project.ProjectProperties.PROPERTY_LIBRARY;
import static org.eclipse.core.resources.IResource.DEPTH_ZERO;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.common.xml.ManifestData;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
//Synthetic comment -- @@ -147,23 +145,23 @@
private static final String ASSETS_DIRECTORY =
SdkConstants.FD_ASSETS + AdtConstants.WS_SEP;
private static final String DRAWABLE_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + AdtConstants.WS_SEP;
private static final String DRAWABLE_XHDPI_DIRECTORY =
            AndroidConstants.FD_RES_DRAWABLE + '-' + Density.XHIGH.getResourceValue() +
AdtConstants.WS_SEP;
private static final String DRAWABLE_HDPI_DIRECTORY =
            AndroidConstants.FD_RES_DRAWABLE + '-' + Density.HIGH.getResourceValue() +
AdtConstants.WS_SEP;
private static final String DRAWABLE_MDPI_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + '-' + Density.MEDIUM.getResourceValue() +
AdtConstants.WS_SEP;
private static final String DRAWABLE_LDPI_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + '-' + Density.LOW.getResourceValue() +
AdtConstants.WS_SEP;
private static final String LAYOUT_DIRECTORY =
        AndroidConstants.FD_RES_LAYOUT + AdtConstants.WS_SEP;
private static final String VALUES_DIRECTORY =
        AndroidConstants.FD_RES_VALUES + AdtConstants.WS_SEP;
private static final String GEN_SRC_DIRECTORY =
SdkConstants.FD_GEN_SOURCES + AdtConstants.WS_SEP;

//Synthetic comment -- @@ -195,7 +193,7 @@

private static final String STRINGS_FILE = "strings.xml";       //$NON-NLS-1$

    private static final String STRING_RSRC_PREFIX = LayoutConstants.STRING_PREFIX;
private static final String STRING_APP_NAME = "app_name";       //$NON-NLS-1$
private static final String STRING_HELLO_WORLD = "hello";       //$NON-NLS-1$

//Synthetic comment -- @@ -1202,7 +1200,7 @@

// Resource class
if (lastDotIndex > 0) {
                    resourcePackageClass = packageName + "." + AdtConstants.FN_RESOURCE_BASE; //$NON-NLS-1$
}

// Package name
//Synthetic comment -- @@ -1235,7 +1233,7 @@

if (activityName != null) {
// create the main activity Java file
            String activityJava = activityName + AdtConstants.DOT_JAVA;
IFile file = pkgFolder.getFile(activityJava);
if (!file.exists()) {
copyFile(JAVA_ACTIVITY_TEMPLATE, file, java_activity_parameters, monitor, false);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/AddTranslationDialog.java
//Synthetic comment -- index b4ce446..b1a0299 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.AndroidConstants.RES_QUALIFIER_SEP;
import static com.android.SdkConstants.FD_RES;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceItem;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ChooseConfigurationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ChooseConfigurationPage.java
//Synthetic comment -- index 29b524c..aec6b92 100644

//Synthetic comment -- @@ -15,7 +15,6 @@
*/
package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.eclipse.adt.AdtConstants;
//Synthetic comment -- @@ -161,7 +160,7 @@
wsFolderPath = wsFolderPath.substring(0, pos);
}

            String[] folderSegments = wsFolderPath.split(AndroidConstants.RES_QUALIFIER_SEP);

if (folderSegments.length > 0) {
String folderName = folderSegments[0];








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileCreationPage.java
//Synthetic comment -- index ab76a7b..e2d061b 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import static com.android.AndroidConstants.RES_QUALIFIER_SEP;
import static com.android.ide.common.layout.LayoutConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.VALUE_FILL_PARENT;
import static com.android.ide.common.layout.LayoutConstants.VALUE_MATCH_PARENT;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP_CHAR;
import static com.android.ide.eclipse.adt.internal.wizards.newxmlfile.ChooseConfigurationPage.RES_FOLDER_ABS;

//Synthetic comment -- @@ -37,8 +37,6 @@
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.IDescriptorProvider;
import com.android.ide.eclipse.adt.internal.editors.menu.descriptors.MenuDescriptors;
import com.android.ide.eclipse.adt.internal.editors.values.descriptors.ValuesDescriptors;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.ProjectCombo;
import com.android.ide.eclipse.adt.internal.resources.ResourceNameValidator;
//Synthetic comment -- @@ -313,7 +311,7 @@
new TypeInfo("Values",                                              // UI name
"An XML file with simple values: colors, strings, dimensions, etc.", // tooltip
ResourceFolderType.VALUES,                                  // folder type
                ValuesDescriptors.ROOT_ELEMENT,                          // root seed
null,                                                       // default root
null,                                                       // xmlns
null,                                                       // default attributes
//Synthetic comment -- @@ -331,7 +329,7 @@
new TypeInfo("Menu",                                                // UI name
"An XML file that describes an menu.",                      // tooltip
ResourceFolderType.MENU,                                    // folder type
                MenuDescriptors.MENU_ROOT_ELEMENT,                          // root seed
null,                                                       // default root
SdkConstants.NS_RESOURCES,                                  // xmlns
null,                                                       // default attributes








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/NewXmlFileWizard.java
//Synthetic comment -- index 373bf0c..22e2325 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.ide.eclipse.adt.internal.wizards.newxmlfile;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.GRID_LAYOUT;

import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;
//Synthetic comment -- @@ -389,7 +389,7 @@
} else {
fileName = name.trim();
if (fileName.length() > 0 && fileName.indexOf('.') == -1) {
                    fileName = fileName + AdtConstants.DOT_XML;
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 1c07518..2a6e784 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;


import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.eclipse.adt.AdtUtils.extractClassName;
import static com.android.ide.eclipse.adt.internal.wizards.templates.NewTemplatePage.WIZARD_PAGE_WIDTH;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/TemplateHandler.java
//Synthetic comment -- index 5bb5565..36e09f9 100644

//Synthetic comment -- @@ -16,14 +16,14 @@
package com.android.ide.eclipse.adt.internal.wizards.templates;

import static com.android.SdkConstants.FD_NATIVE_LIBS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_AIDL;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_FTL;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_JAVA;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_RS;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_SVG;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_TXT;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.internal.wizards.templates.InstallDependencyPage.SUPPORT_LIBRARY_NAME;
import static com.android.ide.eclipse.adt.internal.wizards.templates.TemplateManager.getTemplateRootFolder;

//Synthetic comment -- @@ -702,7 +702,7 @@
MultiTextEdit rootEdit = new MultiTextEdit();
rootEdit.addChild(new ReplaceEdit(0, currentXml.length(), contents));
change.setEdit(rootEdit);
            change.setTextType(AdtConstants.EXT_XML);
mMergeChanges.add(change);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/build/AaptQuickFixTest.java
//Synthetic comment -- index be201f0..cc3504f 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.ide.eclipse.adt.internal.build;

import static com.android.AndroidConstants.FD_RES_COLOR;
import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.SdkConstants.FD_RES;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java
//Synthetic comment -- index 4832682..e2f7049 100644

//Synthetic comment -- @@ -17,11 +17,11 @@

package com.android.ide.eclipse.adt.internal.editors;

import static com.android.AndroidConstants.FD_RES_ANIM;
import static com.android.AndroidConstants.FD_RES_ANIMATOR;
import static com.android.AndroidConstants.FD_RES_COLOR;
import static com.android.AndroidConstants.FD_RES_DRAWABLE;
import static com.android.SdkConstants.FD_RES;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.animator.AnimationContentAssist;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/LayoutMetadataTest.java
//Synthetic comment -- index 5ef2de6..a75fc96 100644

//Synthetic comment -- @@ -15,11 +15,11 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ID_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.NEW_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;

import com.android.ide.common.layout.BaseLayoutRule;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java
//Synthetic comment -- index a118869..3d0c3f5 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.SdkConstants.FD_RES;

import com.android.SdkConstants;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeLayoutRefactoringTest.java
//Synthetic comment -- index baa7265..4c55323 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GRID_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;
import static com.android.ide.common.layout.LayoutConstants.FQCN_RELATIVE_LAYOUT;

import com.android.ide.eclipse.adt.internal.editors.layout.gle2.CanvasViewInfo;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ChangeViewRefactoringTest.java
//Synthetic comment -- index 7ed43e7..582ed0f 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.FQCN_RADIO_BUTTON;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractIncludeRefactoringTest.java
//Synthetic comment -- index 94a0ecc..1667649 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringAssistantTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringAssistantTest.java
//Synthetic comment -- index b88a100..48dc6ae 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.SdkConstants.FD_RES;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidXmlEditor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/RefactoringTest.java
//Synthetic comment -- index ea8427e..86fc8a7 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.DOT_XML;

import com.android.ide.common.rendering.api.ViewInfo;
import com.android.ide.eclipse.adt.AdtPlugin;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoringTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/WrapInRefactoringTest.java
//Synthetic comment -- index 219dabe..2dc6744 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.refactoring;

import static com.android.ide.common.layout.LayoutConstants.FQCN_GESTURE_OVERLAY_VIEW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_LINEAR_LAYOUT;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 0e1fd8a..5cb5647 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.ide.eclipse.tests.functests.layoutRendering;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.AdapterBinding;
//Synthetic comment -- @@ -207,7 +206,7 @@
}

// look for the layout folder
        File layoutFolder = new File(resFolder, AndroidConstants.FD_RES_LAYOUT);
if (layoutFolder.isDirectory() == false) {
fail("Sample project has no layout folder!");
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/AbsoluteLayoutRuleTest.java
//Synthetic comment -- index 6f55ec3..1a14bdd 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.layout;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/BaseLayoutRuleTest.java
//Synthetic comment -- index 661a804..d176fe7 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.IDragElement;
import com.android.ide.common.api.INode;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LayoutTestBase.java
//Synthetic comment -- index a1699c9..f006ba9 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/LinearLayoutRuleTest.java
//Synthetic comment -- index bc655ca..9c4c934 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_WIDTH;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ORIENTATION;
import static com.android.ide.common.layout.LayoutConstants.VALUE_HORIZONTAL;
import static com.android.ide.common.layout.LayoutConstants.VALUE_VERTICAL;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.DropFeedback;
import com.android.ide.common.api.IAttributeInfo.Format;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/RelativeLayoutRuleTest.java
//Synthetic comment -- index 12a8ca1..f9747f3 100644

//Synthetic comment -- @@ -16,7 +16,8 @@

package com.android.ide.common.layout;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Point;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestDragElement.java
//Synthetic comment -- index b4aac0f..b96de60 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/TestNode.java
//Synthetic comment -- index 372e329..f5c9d4d 100644

//Synthetic comment -- @@ -15,9 +15,9 @@
*/
package com.android.ide.common.layout;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_WIDGET_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/grid/GridModelTest.java
//Synthetic comment -- index 03c921b..c4a58c6 100644

//Synthetic comment -- @@ -15,12 +15,12 @@
*/
package com.android.ide.common.layout.grid;

import static com.android.ide.common.layout.LayoutConstants.ATTR_COLUMN_COUNT;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.ide.common.layout.LayoutConstants.ATTR_LAYOUT_ROW;
import static com.android.ide.common.layout.LayoutConstants.FQCN_BUTTON;
import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/common/layout/relative/DeletionHandlerTest.java
//Synthetic comment -- index f5ec1ba..d629a38 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.common.layout.relative;

import static com.android.ide.common.layout.LayoutConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.ide.common.api.INode;
import com.android.ide.common.layout.BaseViewRule;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilitiesTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/gle2/DomUtilitiesTest.java
//Synthetic comment -- index 710725e..6576bea 100644

//Synthetic comment -- @@ -15,8 +15,8 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleterTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/layout/properties/ValueCompleterTest.java
//Synthetic comment -- index bbe82e3..0014fe5 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/
package com.android.ide.eclipse.adt.internal.editors.layout.properties;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiElementNodeTest.java
//Synthetic comment -- index eb4dcb2..d19f933 100644

//Synthetic comment -- @@ -16,8 +16,7 @@

package com.android.ide.eclipse.adt.internal.editors.manifest.model;

import static com.android.utils.XmlUtils.ANDROID_URI;

import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor.Mandatory;
import com.android.ide.eclipse.adt.internal.editors.mock.MockXmlNode;
//Synthetic comment -- @@ -279,7 +278,7 @@
Element baz = document.createElement("baz");
root.appendChild(baz);

        String prefix = XmlUtils.lookupNamespacePrefix(baz, ANDROID_URI);
assertEquals("android", prefix);
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/FrameworkResources.java b/ide_common/src/com/android/ide/common/resources/FrameworkResources.java
//Synthetic comment -- index 2115cdc..fbf5926 100755

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ide.common.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.io.IAbstractFile;
//Synthetic comment -- @@ -103,7 +103,7 @@
* @param logger a logger to report issues to
*/
public void loadPublicResources(@NonNull IAbstractFolder resFolder, @Nullable ILogger logger) {
        IAbstractFolder valueFolder = resFolder.getFolder(FD_RES_VALUES);
if (valueFolder.exists() == false) {
return;
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 6fb8457..ac0614d 100755

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.resources;

import com.android.AndroidConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -309,7 +309,7 @@
@Nullable
public ResourceFolder processFolder(@NonNull IAbstractFolder folder) {
// split the name of the folder in segments.
        String[] folderSegments = folder.getName().split(AndroidConstants.RES_QUALIFIER_SEP);

// get the enum for the resource type.
ResourceFolderType type = ResourceFolderType.getTypeByName(folderSegments[0]);








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 772d75e..d742c4a 100644

//Synthetic comment -- @@ -16,6 +16,13 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderResources;
import com.android.ide.common.rendering.api.ResourceValue;
//Synthetic comment -- @@ -28,24 +35,6 @@

public class ResourceResolver extends RenderResources {

    /** The constant {@code style/} */
    public final static String REFERENCE_STYLE = ResourceType.STYLE.getName() + "/";
    /** The constant {@code @android:} */
    public final static String PREFIX_ANDROID_RESOURCE_REF = "@android:";
    /** The constant {@code @} */
    public final static String PREFIX_RESOURCE_REF = "@";
    /** The constant {@code ?android:} */
    public final static String PREFIX_ANDROID_THEME_REF = "?android:";
    /** The constant {@code ?} */
    public final static String PREFIX_THEME_REF = "?";
    /** The constant {@code android:} */
    public final static String PREFIX_ANDROID = "android:";
    /** The constant {@code @style/} */
    public static final String PREFIX_STYLE = PREFIX_RESOURCE_REF + REFERENCE_STYLE;
    /** The constant {@code @android:style/} */
    public static final String PREFIX_ANDROID_STYLE = PREFIX_ANDROID_RESOURCE_REF
            + REFERENCE_STYLE;

private final Map<ResourceType, Map<String, ResourceValue>> mProjectResources;
private final Map<ResourceType, Map<String, ResourceValue>> mFrameworkResources;

//Synthetic comment -- @@ -209,9 +198,9 @@
boolean frameworkOnly = false;

// eliminate the prefix from the string
            if (reference.startsWith(PREFIX_ANDROID_THEME_REF)) {
frameworkOnly = true;
                reference = reference.substring(PREFIX_ANDROID_THEME_REF.length());
} else {
reference = reference.substring(PREFIX_THEME_REF.length());
}
//Synthetic comment -- @@ -265,10 +254,9 @@
}

// Eliminate the prefix from the string.
            if (reference.startsWith(PREFIX_ANDROID_RESOURCE_REF)) {
frameworkOnly = true;
                reference = reference.substring(
                        PREFIX_ANDROID_RESOURCE_REF.length());
} else {
reference = reference.substring(PREFIX_RESOURCE_REF.length());
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java b/ide_common/src/com/android/ide/common/resources/configuration/FolderConfiguration.java
//Synthetic comment -- index f12ed8f..e2fe767 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.ide.common.resources.configuration;

import com.android.AndroidConstants;
import com.android.resources.Density;
import com.android.resources.ResourceFolderType;
import com.android.resources.ScreenOrientation;
//Synthetic comment -- @@ -566,7 +566,7 @@
if (qualifier != null) {
String segment = qualifier.getFolderSegment();
if (segment != null && segment.length() > 0) {
                    result.append(AndroidConstants.RES_QUALIFIER_SEP);
result.append(segment);
}
}








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/HtmlReporter.java b/lint/cli/src/com/android/tools/lint/HtmlReporter.java
//Synthetic comment -- index 8fddece..19f1028 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint;

import static com.android.tools.lint.detector.api.LintConstants.DOT_JPG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_PNG;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.tools.lint.checks.BuiltinIssueRegistry;








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Main.java b/lint/cli/src/com/android/tools/lint/Main.java
//Synthetic comment -- index 541ad4e..a33890d 100644

//Synthetic comment -- @@ -18,7 +18,7 @@

import static com.android.tools.lint.client.api.IssueRegistry.LINT_ERROR;
import static com.android.tools.lint.client.api.IssueRegistry.PARSER_ERROR;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/cli/src/com/android/tools/lint/Reporter.java b/lint/cli/src/com/android/tools/lint/Reporter.java
//Synthetic comment -- index 2c86635..fc3aaae 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint;

import static com.android.tools.lint.detector.api.LintConstants.DOT_9PNG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_PNG;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.google.common.annotations.Beta;
//Synthetic comment -- @@ -256,4 +256,4 @@
}
return null;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/DefaultSdkInfo.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/DefaultSdkInfo.java
//Synthetic comment -- index a32a884..1e53834 100644

//Synthetic comment -- @@ -16,48 +16,48 @@

package com.android.tools.lint.client.api;

import static com.android.tools.lint.detector.api.LintConstants.ABSOLUTE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ABS_LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.ABS_SEEK_BAR;
import static com.android.tools.lint.detector.api.LintConstants.ABS_SPINNER;
import static com.android.tools.lint.detector.api.LintConstants.ADAPTER_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.CHECKED_TEXT_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.CHECK_BOX;
import static com.android.tools.lint.detector.api.LintConstants.COMPOUND_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.EDIT_TEXT;
import static com.android.tools.lint.detector.api.LintConstants.EXPANDABLE_LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.FRAME_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.GALLERY;
import static com.android.tools.lint.detector.api.LintConstants.GRID_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.PROGRESS_BAR;
import static com.android.tools.lint.detector.api.LintConstants.RADIO_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.RADIO_GROUP;
import static com.android.tools.lint.detector.api.LintConstants.RELATIVE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.SEEK_BAR;
import static com.android.tools.lint.detector.api.LintConstants.SPINNER;
import static com.android.tools.lint.detector.api.LintConstants.SURFACE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.SWITCH;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;
import static com.android.tools.lint.detector.api.LintConstants.TAB_HOST;
import static com.android.tools.lint.detector.api.LintConstants.TAB_WIDGET;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.TOGGLE_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.VIEW;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_ANIMATOR;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_GROUP;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_PKG_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_STUB;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_SWITCHER;
import static com.android.tools.lint.detector.api.LintConstants.WEB_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.WIDGET_PKG_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/JavaVisitor.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/JavaVisitor.java
//Synthetic comment -- index b9f7ca6..b1d8832 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.client.api;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_PKG;
import static com.android.tools.lint.detector.api.LintConstants.R_CLASS;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index 363b9cd..c15b284 100644

//Synthetic comment -- @@ -16,12 +16,13 @@

package com.android.tools.lint.client.api;

import static com.android.tools.lint.detector.api.LintConstants.CLASS_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAR;
import static com.android.tools.lint.detector.api.LintConstants.GEN_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.LIBS_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.SRC_FOLDER;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.IAndroidTarget;
//Synthetic comment -- @@ -29,7 +30,6 @@
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintConstants;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Project;
//Synthetic comment -- @@ -586,7 +586,7 @@
* @return the highest known API level
*/
public int getHighestKnownApiLevel() {
        int max = LintConstants.HIGHEST_KNOWN_API;

for (IAndroidTarget target : getTargets()) {
if (target.isPlatform()) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java b/lint/libs/lint_api/src/com/android/tools/lint/client/api/LintDriver.java
//Synthetic comment -- index 068bf2a..a833dae 100644

//Synthetic comment -- @@ -16,18 +16,18 @@

package com.android.tools.lint.client.api;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_IGNORE;
import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAR;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.OLD_PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_FILE;
import static com.android.tools.lint.detector.api.LintConstants.RES_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.SUPPRESS_ALL;
import static com.android.tools.lint.detector.api.LintConstants.SUPPRESS_LINT;
import static com.android.tools.lint.detector.api.LintConstants.TOOLS_URI;
import static org.objectweb.asm.Opcodes.ASM4;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -300,8 +300,6 @@
mScope.add(Scope.MANIFEST);
} else if (name.endsWith(DOT_XML)) {
mScope.add(Scope.RESOURCE_FILE);
                        } else if (name.equals(PROGUARD_FILE) || name.equals(OLD_PROGUARD_FILE)) {
                            mScope.add(Scope.PROGUARD_FILE);
} else if (name.equals(RES_FOLDER)
|| file.getParent().equals(RES_FOLDER)) {
mScope.add(Scope.ALL_RESOURCE_FILES);
//Synthetic comment -- @@ -310,7 +308,8 @@
mScope.add(Scope.JAVA_FILE);
} else if (name.endsWith(DOT_CLASS)) {
mScope.add(Scope.CLASS_FILE);
                        } else if (name.equals(OLD_PROGUARD_FILE) || name.equals(PROGUARD_FILE)) {
mScope.add(Scope.PROGUARD_FILE);
}
}
//Synthetic comment -- @@ -889,7 +888,7 @@
if (file.exists()) {
files.add(file);
}
                file = new File(project.getDir(), PROGUARD_FILE);
if (file.exists()) {
files.add(file);
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/ClassContext.java
//Synthetic comment -- index 4c5f298..68c25d9 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.DOT_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.Location.SearchDirection.BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.EOL_BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LayoutDetector.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LayoutDetector.java
//Synthetic comment -- index c61d7c2..b24c1a9 100644

//Synthetic comment -- @@ -16,16 +16,16 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WIDTH;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PADDING;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PADDING_BOTTOM;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PADDING_LEFT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PADDING_RIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PADDING_TOP;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_FILL_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_MATCH_PARENT;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintConstants.java
deleted file mode 100644
//Synthetic comment -- index e4975ae..0000000

//Synthetic comment -- @@ -1,364 +0,0 @@
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

package com.android.tools.lint.detector.api;

import com.google.common.annotations.Beta;

import java.io.File;

/**
 * Constants used by the various detectors, defined in one place
 * <p>
 * <b>NOTE: This is not a public or final API; if you rely on this be prepared
 * to adjust your code for the next tools release.</b>
*/
@Beta
@SuppressWarnings("javadoc") // Not documenting each and every obvious constant
public class LintConstants {
    /** Namespace prefix used for all resources */
    public static final String URI_PREFIX =
            "http://schemas.android.com/apk/res/";                     //$NON-NLS-1$
    /** Namespace used in XML files for Android attributes */
    public static final String ANDROID_URI =
            "http://schemas.android.com/apk/res/android";              //$NON-NLS-1$
    /** Namespace used in XML files for Android Tooling attributes */
    public static final String TOOLS_URI =
            "http://schemas.android.com/tools";                        //$NON-NLS-1$
    /** Namespace used for auto-adjusting namespaces */
    public final static String AUTO_URI =
            "http://schemas.android.com/apk/res-auto";                 //$NON-NLS-1$
    /** Default prefix used for tools attributes */
    public static final String TOOLS_PREFIX = "tools";                 //$NON-NLS-1$
    public static final String XMLNS_PREFIX = "xmlns:";                //$NON-NLS-1$
    public static final String R_CLASS = "R";                          //$NON-NLS-1$
    public static final String ANDROID_PKG = "android";                //$NON-NLS-1$

    // Tags: Manifest
    public static final String TAG_SERVICE = "service";                //$NON-NLS-1$
    public static final String TAG_USES_PERMISSION = "uses-permission";//$NON-NLS-1$
    public static final String TAG_USES_LIBRARY = "uses-library";      //$NON-NLS-1$
    public static final String TAG_APPLICATION = "application";        //$NON-NLS-1$
    public static final String TAG_INTENT_FILTER = "intent-filter";    //$NON-NLS-1$
    public static final String TAG_USES_SDK = "uses-sdk";              //$NON-NLS-1$
    public static final String TAG_ACTIVITY = "activity";              //$NON-NLS-1$
    public static final String TAG_RECEIVER = "receiver";              //$NON-NLS-1$
    public static final String TAG_PROVIDER = "provider";              //$NON-NLS-1$
    public static final String TAG_GRANT_PERMISSION = "grant-uri-permission"; //$NON-NLS-1$
    public static final String TAG_PATH_PERMISSION = "path-permission"; //$NON-NLS-1$

    // Tags: Resources
    public static final String TAG_RESOURCES = "resources";            //$NON-NLS-1$
    public static final String TAG_STRING = "string";                  //$NON-NLS-1$
    public static final String TAG_ARRAY = "array";                    //$NON-NLS-1$
    public static final String TAG_STYLE = "style";                    //$NON-NLS-1$
    public static final String TAG_ITEM = "item";                      //$NON-NLS-1$
    public static final String TAG_STRING_ARRAY = "string-array";      //$NON-NLS-1$
    public static final String TAG_PLURALS = "plurals";                //$NON-NLS-1$
    public static final String TAG_INTEGER_ARRAY = "integer-array";    //$NON-NLS-1$

    // Tags: Layouts
    public static final String VIEW_TAG = "view";                      //$NON-NLS-1$
    public static final String INCLUDE = "include";                    //$NON-NLS-1$
    public static final String MERGE = "merge";                        //$NON-NLS-1$
    public static final String REQUEST_FOCUS = "requestFocus";         //$NON-NLS-1$

    public static final String VIEW = "View";                          //$NON-NLS-1$
    public static final String VIEW_GROUP = "ViewGroup";               //$NON-NLS-1$
    public static final String FRAME_LAYOUT = "FrameLayout";           //$NON-NLS-1$
    public static final String LINEAR_LAYOUT = "LinearLayout";         //$NON-NLS-1$
    public static final String RELATIVE_LAYOUT = "RelativeLayout";     //$NON-NLS-1$
    public static final String GRID_LAYOUT = "GridLayout";             //$NON-NLS-1$
    public static final String SCROLL_VIEW = "ScrollView";             //$NON-NLS-1$
    public static final String BUTTON = "Button";                      //$NON-NLS-1$
    public static final String COMPOUND_BUTTON = "CompoundButton";     //$NON-NLS-1$
    public static final String ADAPTER_VIEW = "AdapterView";           //$NON-NLS-1$
    public static final String GALLERY = "Gallery";                    //$NON-NLS-1$
    public static final String GRID_VIEW = "GridView";                 //$NON-NLS-1$
    public static final String TAB_HOST = "TabHost";                   //$NON-NLS-1$
    public static final String RADIO_GROUP = "RadioGroup";             //$NON-NLS-1$
    public static final String RADIO_BUTTON = "RadioButton";           //$NON-NLS-1$
    public static final String SWITCH = "Switch";                      //$NON-NLS-1$
    public static final String EDIT_TEXT = "EditText";                 //$NON-NLS-1$
    public static final String LIST_VIEW = "ListView";                 //$NON-NLS-1$
    public static final String TEXT_VIEW = "TextView";                 //$NON-NLS-1$
    public static final String CHECKED_TEXT_VIEW = "CheckedTextView";  //$NON-NLS-1$
    public static final String IMAGE_VIEW = "ImageView";               //$NON-NLS-1$
    public static final String SURFACE_VIEW = "SurfaceView";           //$NON-NLS-1$
    public static final String ABSOLUTE_LAYOUT = "AbsoluteLayout";     //$NON-NLS-1$
    public static final String TABLE_LAYOUT = "TableLayout";           //$NON-NLS-1$
    public static final String TABLE_ROW = "TableRow";                 //$NON-NLS-1$
    public static final String TAB_WIDGET = "TabWidget";               //$NON-NLS-1$
    public static final String IMAGE_BUTTON = "ImageButton";           //$NON-NLS-1$
    public static final String SEEK_BAR = "SeekBar";                   //$NON-NLS-1$
    public static final String VIEW_STUB = "ViewStub";                 //$NON-NLS-1$
    public static final String SPINNER = "Spinner";                    //$NON-NLS-1$
    public static final String WEB_VIEW = "WebView";                   //$NON-NLS-1$
    public static final String TOGGLE_BUTTON = "ToggleButton";         //$NON-NLS-1$
    public static final String CHECK_BOX = "CheckBox";                 //$NON-NLS-1$
    public static final String ABS_LIST_VIEW = "AbsListView";          //$NON-NLS-1$
    public static final String PROGRESS_BAR = "ProgressBar";           //$NON-NLS-1$
    public static final String ABS_SPINNER = "AbsSpinner";             //$NON-NLS-1$
    public static final String ABS_SEEK_BAR = "AbsSeekBar";            //$NON-NLS-1$
    public static final String VIEW_ANIMATOR = "ViewAnimator";         //$NON-NLS-1$
    public static final String VIEW_SWITCHER = "ViewSwitcher";         //$NON-NLS-1$
    public static final String EXPANDABLE_LIST_VIEW = "ExpandableListView";    //$NON-NLS-1$
    public static final String HORIZONTAL_SCROLL_VIEW = "HorizontalScrollView"; //$NON-NLS-1$

    // Tags: Drawables
    public static final String TAG_BITMAP = "bitmap";                  //$NON-NLS-1$

    // Attributes: Manifest
    public static final String ATTR_EXPORTED = "exported";             //$NON-NLS-1$
    public static final String ATTR_PERMISSION = "permission";         //$NON-NLS-1$
    public static final String ATTR_MIN_SDK_VERSION = "minSdkVersion"; //$NON-NLS-1$
    public static final String ATTR_TARGET_SDK_VERSION = "targetSdkVersion"; //$NON-NLS-1$
    public static final String ATTR_ICON = "icon";                     //$NON-NLS-1$
    public final static String ATTR_PACKAGE = "package";               //$NON-NLS-1$
    public static final String ATTR_THEME = "theme";                   //$NON-NLS-1$
    public static final String ATTR_PATH = "path";                     //$NON-NLS-1$
    public static final String ATTR_PATH_PREFIX = "pathPrefix";        //$NON-NLS-1$
    public static final String ATTR_PATH_PATTERN = "pathPattern";      //$NON-NLS-1$
    public final static String ATTR_DEBUGGABLE = "debuggable";         //$NON-NLS-1$
    public final static String ATTR_READ_PERMISSION = "readPermission"; //$NON_NLS-1$
    public final static String ATTR_WRITE_PERMISSION = "writePermission"; //$NON_NLS-1$

    // Attributes: Resources
    public static final String ATTR_NAME = "name";                     //$NON-NLS-1$
    public static final String ATTR_TYPE = "type";                     //$NON-NLS-1$
    public static final String ATTR_PARENT = "parent";                 //$NON-NLS-1$
    public static final String ATTR_TRANSLATABLE = "translatable";     //$NON-NLS-1$

    // Attributes: Layout
    public static final String ATTR_LAYOUT_PREFIX = "layout_";         //$NON-NLS-1$
    public static final String ATTR_CLASS = "class";                   //$NON-NLS-1$
    public static final String ATTR_STYLE = "style";                   //$NON-NLS-1$

    public static final String ATTR_ID = "id";                         //$NON-NLS-1$
    public static final String ATTR_TEXT = "text";                     //$NON-NLS-1$
    public static final String ATTR_TEXT_SIZE = "textSize";            //$NON-NLS-1$
    public static final String ATTR_LABEL = "label";                   //$NON-NLS-1$
    public static final String ATTR_HINT = "hint";                     //$NON-NLS-1$
    public static final String ATTR_PROMPT = "prompt";                 //$NON-NLS-1$
    public static final String ATTR_ON_CLICK = "onClick";              //$NON-NLS-1$
    public static final String ATTR_INPUT_TYPE = "inputType";          //$NON-NLS-1$
    public static final String ATTR_INPUT_METHOD = "inputMethod";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_GRAVITY = "layout_gravity"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_WIDTH = "layout_width";     //$NON-NLS-1$
    public static final String ATTR_LAYOUT_HEIGHT = "layout_height";   //$NON-NLS-1$
    public static final String ATTR_LAYOUT_WEIGHT = "layout_weight";   //$NON-NLS-1$
    public static final String ATTR_PADDING = "padding";               //$NON-NLS-1$
    public static final String ATTR_PADDING_BOTTOM = "paddingBottom";  //$NON-NLS-1$
    public static final String ATTR_PADDING_TOP = "paddingTop";        //$NON-NLS-1$
    public static final String ATTR_PADDING_RIGHT = "paddingRight";    //$NON-NLS-1$
    public static final String ATTR_PADDING_LEFT = "paddingLeft";      //$NON-NLS-1$
    public static final String ATTR_FOREGROUND = "foreground";         //$NON-NLS-1$
    public static final String ATTR_BACKGROUND = "background";         //$NON-NLS-1$
    public static final String ATTR_ORIENTATION = "orientation";       //$NON-NLS-1$
    public static final String ATTR_LAYOUT = "layout";                 //$NON-NLS-1$
    public static final String ATTR_ROW_COUNT = "rowCount";            //$NON-NLS-1$
    public static final String ATTR_COLUMN_COUNT = "columnCount";      //$NON-NLS-1$
    public static final String ATTR_BASELINE_ALIGNED = "baselineAligned";       //$NON-NLS-1$
    public static final String ATTR_CONTENT_DESCRIPTION = "contentDescription"; //$NON-NLS-1$
    public static final String ATTR_IME_ACTION_LABEL = "imeActionLabel";        //$NON-NLS-1$
    public static final String ATTR_PRIVATE_IME_OPTIONS = "privateImeOptions";  //$NON-NLS-1$
    public static final String VALUE_NONE = "none";                    //$NON-NLS-1$
    public static final String VALUE_NO = "no";                        //$NON-NLS-1$
    public static final String ATTR_NUMERIC = "numeric";               //$NON-NLS-1$
    public static final String ATTR_IME_ACTION_ID = "imeActionId";     //$NON-NLS-1$
    public static final String ATTR_IME_OPTIONS = "imeOptions";        //$NON-NLS-1$
    public static final String ATTR_FREEZES_TEXT = "freezesText";      //$NON-NLS-1$
    public static final String ATTR_EDITOR_EXTRAS = "editorExtras";    //$NON-NLS-1$
    public static final String ATTR_EDITABLE = "editable";             //$NON-NLS-1$
    public static final String ATTR_DIGITS = "digits";                 //$NON-NLS-1$
    public static final String ATTR_CURSOR_VISIBLE = "cursorVisible";  //$NON-NLS-1$
    public static final String ATTR_CAPITALIZE = "capitalize";         //$NON-NLS-1$
    public static final String ATTR_PHONE_NUMBER = "phoneNumber";      //$NON-NLS-1$
    public static final String ATTR_PASSWORD = "password";             //$NON-NLS-1$
    public static final String ATTR_BUFFER_TYPE = "bufferType";        //$NON-NLS-1$
    public static final String ATTR_AUTO_TEXT = "autoText";            //$NON-NLS-1$
    public static final String ATTR_ENABLED = "enabled";               //$NON-NLS-1$
    public static final String ATTR_SINGLE_LINE = "singleLine";        //$NON-NLS-1$
    public static final String ATTR_SCALE_TYPE = "scaleType";          //$NON-NLS-1$
    public static final String ATTR_IMPORTANT_FOR_ACCESSIBILITY =
            "importantForAccessibility";                               //$NON-NLS-1$

    // AbsoluteLayout layout params
    public static final String ATTR_LAYOUT_Y = "layout_y";             //$NON-NLS-1$
    public static final String ATTR_LAYOUT_X = "layout_x";             //$NON-NLS-1$

    // GridLayout layout params
    public static final String ATTR_LAYOUT_ROW = "layout_row";         //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ROW_SPAN = "layout_rowSpan";//$NON-NLS-1$
    public static final String ATTR_LAYOUT_COLUMN = "layout_column";   //$NON-NLS-1$
    public static final String ATTR_LAYOUT_COLUMN_SPAN = "layout_columnSpan";       //$NON-NLS-1$

    // TableRow
    public static final String ATTR_LAYOUT_SPAN = "layout_span";       //$NON-NLS-1$

    // RelativeLayout layout params:
    public static final String ATTR_LAYOUT_ALIGN_LEFT = "layout_alignLeft";        //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_RIGHT = "layout_alignRight";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_TOP = "layout_alignTop";          //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_BOTTOM = "layout_alignBottom";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_TOP = "layout_alignParentTop"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_BOTTOM = "layout_alignParentBottom"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_LEFT = "layout_alignParentLeft";//$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_PARENT_RIGHT = "layout_alignParentRight";   //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING = "layout_alignWithParentIfMissing"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ALIGN_BASELINE = "layout_alignBaseline"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_IN_PARENT = "layout_centerInParent"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_VERTICAL = "layout_centerVertical"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_CENTER_HORIZONTAL = "layout_centerHorizontal"; //$NON-NLS-1$
    public static final String ATTR_LAYOUT_TO_RIGHT_OF = "layout_toRightOf";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_TO_LEFT_OF = "layout_toLeftOf";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_BELOW = "layout_below";              //$NON-NLS-1$
    public static final String ATTR_LAYOUT_ABOVE = "layout_above";              //$NON-NLS-1$

    // Margins
    public static final String ATTR_LAYOUT_MARGIN = "layout_margin";               //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_LEFT = "layout_marginLeft";      //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_RIGHT = "layout_marginRight";    //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_TOP = "layout_marginTop";        //$NON-NLS-1$
    public static final String ATTR_LAYOUT_MARGIN_BOTTOM = "layout_marginBottom";  //$NON-NLS-1$

    // Attributes: Drawables
    public static final String ATTR_TILE_MODE = "tileMode";            //$NON-NLS-1$

    // Values: Layouts
    public static final String VALUE_FILL_PARENT = "fill_parent";       //$NON-NLS-1$
    public static final String VALUE_MATCH_PARENT = "match_parent";     //$NON-NLS-1$
    public static final String VALUE_VERTICAL = "vertical";             //$NON-NLS-1$
    public static final String VALUE_TRUE = "true";                     //$NON-NLS-1$
    public static final String VALUE_EDITABLE = "editable";             //$NON-NLS-1$


    // Values: Resources
    public static final String VALUE_ID = "id";                        //$NON-NLS-1$

    // Values: Drawables
    public static final String VALUE_DISABLED = "disabled";            //$NON-NLS-1$
    public static final String VALUE_CLAMP = "clamp";                  //$NON-NLS-1$

    // Menus
    public static final String ATTR_SHOW_AS_ACTION = "showAsAction";   //$NON-NLS-1$
    public static final String VALUE_IF_ROOM = "ifRoom";               //$NON-NLS-1$
    public static final String VALUE_ALWAYS = "always";                //$NON-NLS-1$

    // Units
    public static final String UNIT_DP = "dp";                         //$NON-NLS-1$
    public static final String UNIT_DIP = "dip";                       //$NON-NLS-1$
    public static final String UNIT_SP = "sp";                         //$NON-NLS-1$
    public static final String UNIT_PX = "px";                         //$NON-NLS-1$

    // Filenames and folder names
    public static final String ANDROID_MANIFEST_XML = "AndroidManifest.xml"; //$NON-NLS-1$
    public static final String OLD_PROGUARD_FILE = "proguard.cfg";     //$NON-NLS-1$
    public static final String PROGUARD_FILE = "proguard-project.txt"; //$NON-NLS-1$
    public static final String CLASS_FOLDER =
            "bin" + File.separator + "classes";                        //$NON-NLS-1$ //$NON-NLS-2$
    public static final String GEN_FOLDER = "gen";                     //$NON-NLS-1$
    public static final String SRC_FOLDER = "src";                     //$NON-NLS-1$
    public static final String LIBS_FOLDER = "libs";                   //$NON-NLS-1$
    public static final String BIN_FOLDER = "bin";                     //$NON-NLS-1$

    public static final String RES_FOLDER = "res";                     //$NON-NLS-1$
    public static final String DOT_XML = ".xml";                       //$NON-NLS-1$
    public static final String DOT_GIF = ".gif";                       //$NON-NLS-1$
    public static final String DOT_JPG = ".jpg";                       //$NON-NLS-1$
    public static final String DOT_PNG = ".png";                       //$NON-NLS-1$
    public static final String DOT_9PNG = ".9.png";                    //$NON-NLS-1$
    public static final String DOT_JAVA = ".java";                     //$NON-NLS-1$
    public static final String DOT_CLASS = ".class";                   //$NON-NLS-1$
    public static final String DOT_JAR = ".jar";                       //$NON-NLS-1$
    public static final String DRAWABLE_FOLDER = "drawable";           //$NON-NLS-1$
    public static final String DRAWABLE_XHDPI = "drawable-xhdpi";      //$NON-NLS-1$
    public static final String DRAWABLE_HDPI = "drawable-hdpi";        //$NON-NLS-1$
    public static final String DRAWABLE_MDPI = "drawable-mdpi";        //$NON-NLS-1$
    public static final String DRAWABLE_LDPI = "drawable-ldpi";        //$NON-NLS-1$

    // Resources
    public static final String ANDROID_RESOURCE_PREFIX = "@android:";   //$NON-NLS-1$
    public static final String ANDROID_THEME_PREFIX = "?android:";      //$NON-NLS-1$
    public static final String ID_RESOURCE_PREFIX = "@id/";             //$NON-NLS-1$
    public static final String NEW_ID_RESOURCE_PREFIX = "@+id/";        //$NON-NLS-1$
    public static final String DRAWABLE_RESOURCE_PREFIX = "@drawable/"; //$NON-NLS-1$
    public static final String LAYOUT_RESOURCE_PREFIX = "@layout/";     //$NON-NLS-1$
    public static final String STYLE_RESOURCE_PREFIX = "@style/";       //$NON-NLS-1$
    public static final String STRING_RESOURCE_PREFIX = "@string/";     //$NON-NLS-1$
    public static final String RESOURCE_CLZ_ID = "id";                  //$NON-NLS-1$
    public static final String RESOURCE_CLZ_COLOR = "color";            //$NON-NLS-1$
    public static final String RESOURCE_CLZ_ARRAY = "array";            //$NON-NLS-1$
    public static final String RESOURCE_CLZ_ATTR = "attr";              //$NON-NLS-1$
    public static final String RESOURCE_CLR_STYLEABLE = "styleable";    //$NON-NLS-1$
    public static final String NULL_RESOURCE = "@null";                 //$NON-NLS-1$
    public static final String TRANSPARENT_COLOR = "@android:color/transparent";      //$NON-NLS-1$
    public static final String ANDROID_STYLE_RESOURCE_PREFIX = "@android:style/";     //$NON-NLS-1$
    public static final String ANDROID_STRING_RESOURCE_PREFIX = "@android:string/";   //$NON-NLS-1$

    // Packages
    public static final String ANDROID_PKG_PREFIX = "android.";         //$NON-NLS-1$
    public static final String WIDGET_PKG_PREFIX = "android.widget.";   //$NON-NLS-1$
    public static final String VIEW_PKG_PREFIX = "android.view.";       //$NON-NLS-1$

    // Project properties
    public static final String ANDROID_LIBRARY = "android.library";     //$NON-NLS-1$
    public static final String PROGUARD_CONFIG = "proguard.config";     //$NON-NLS-1$
    public static final String ANDROID_LIBRARY_REFERENCE_FORMAT = "android.library.reference.%1$d";//$NON-NLS-1$
    public static final String PROJECT_PROPERTIES = "project.properties";//$NON-NLS-1$

    // Java References
    public static final String ATTR_REF_PREFIX = "?attr/";               //$NON-NLS-1$
    public static final String R_PREFIX = "R.";                          //$NON-NLS-1$
    public static final String R_ID_PREFIX = "R.id.";                    //$NON-NLS-1$
    public static final String R_LAYOUT_PREFIX = "R.layout.";            //$NON-NLS-1$
    public static final String R_DRAWABLE_PREFIX = "R.drawable.";        //$NON-NLS-1$
    public static final String R_ATTR_PREFIX = "R.attr.";                //$NON-NLS-1$

    // Attributes related to tools
    public final static String ATTR_IGNORE = "ignore";                   //$NON-NLS-1$

    // SuppressLint
    public static final String SUPPRESS_ALL = "all";                     //$NON-NLS-1$
    public static final String SUPPRESS_LINT = "SuppressLint";           //$NON-NLS-1$
    public static final String TARGET_API = "TargetApi";                 //$NON-NLS-1$
    public static final String FQCN_SUPPRESS_LINT = "android.annotation." + SUPPRESS_LINT; //$NON-NLS-1$
    public static final String FQCN_TARGET_API = "android.annotation." + TARGET_API; //$NON-NLS-1$

    // Class Names
    public static final String CONSTRUCTOR_NAME = "<init>";                          //$NON-NLS-1$
    public static final String FRAGMENT = "android/app/Fragment";                    //$NON-NLS-1$
    public static final String FRAGMENT_V4 = "android/support/v4/app/Fragment";      //$NON-NLS-1$
    public static final String ANDROID_APP_ACTIVITY = "android/app/Activity";        //$NON-NLS-1$
    public static final String ANDROID_APP_SERVICE = "android/app/Service";          //$NON-NLS-1$
    public static final String ANDROID_CONTENT_CONTENT_PROVIDER =
            "android/content/ContentProvider";                                       //$NON-NLS-1$
    public static final String ANDROID_CONTENT_BROADCAST_RECEIVER =
            "android/content/BroadcastReceiver";                                     //$NON-NLS-1$

    // Method Names
    public static final String FORMAT_METHOD = "format";                             //$NON-NLS-1$
    public static final String GET_STRING_METHOD = "getString";                      //$NON-NLS-1$

    /**
     * The highest known API level. Note that the tools may also look at the
     * installed platforms to see if they can find more recently released
     * platforms, e.g. when the tools have not yet been updated for a new
     * release. This number is used as a baseline and any more recent platforms
     * found can be used to increase the highest known number.
     */
    public static final int HIGHEST_KNOWN_API = 16;
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index fe659cd..60c9e97 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.BIN_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -218,10 +218,10 @@
public static String stripIdPrefix(@Nullable String id) {
if (id == null) {
return "";
        } else if (id.startsWith(NEW_ID_RESOURCE_PREFIX)) {
            return id.substring(NEW_ID_RESOURCE_PREFIX.length());
        } else if (id.startsWith(ID_RESOURCE_PREFIX)) {
            return id.substring(ID_RESOURCE_PREFIX.length());
}

return id;
//Synthetic comment -- @@ -236,28 +236,28 @@
* @return true if the two id references refer to the same id
*/
public static boolean idReferencesMatch(String id1, String id2) {
        if (id1.startsWith(NEW_ID_RESOURCE_PREFIX)) {
            if (id2.startsWith(NEW_ID_RESOURCE_PREFIX)) {
return id1.equals(id2);
} else {
                assert id2.startsWith(ID_RESOURCE_PREFIX);
return ((id1.length() - id2.length())
                            == (NEW_ID_RESOURCE_PREFIX.length() - ID_RESOURCE_PREFIX.length()))
                        && id1.regionMatches(NEW_ID_RESOURCE_PREFIX.length(), id2,
                                ID_RESOURCE_PREFIX.length(),
                                id2.length() - ID_RESOURCE_PREFIX.length());
}
} else {
            assert id1.startsWith(ID_RESOURCE_PREFIX);
            if (id2.startsWith(ID_RESOURCE_PREFIX)) {
return id1.equals(id2);
} else {
                assert id2.startsWith(NEW_ID_RESOURCE_PREFIX);
return (id2.length() - id1.length()
                            == (NEW_ID_RESOURCE_PREFIX.length() - ID_RESOURCE_PREFIX.length()))
                        && id2.regionMatches(NEW_ID_RESOURCE_PREFIX.length(), id1,
                                ID_RESOURCE_PREFIX.length(),
                                id1.length() - ID_RESOURCE_PREFIX.length());
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index acd1d06..b584020 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_LIBRARY;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_LIBRARY_REFERENCE_FORMAT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_MIN_SDK_VERSION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TARGET_SDK_VERSION;
import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_CONFIG;
import static com.android.tools.lint.detector.api.LintConstants.PROJECT_PROPERTIES;
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_SDK;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_TRUE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AccessibilityDetector.java
//Synthetic comment -- index af5c805..6162c38 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_IMPORTANT_FOR_ACCESSIBILITY;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_NO;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AlwaysShowActionDetector.java
//Synthetic comment -- index 42d48bc..6cdb42f 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_SHOW_AS_ACTION;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_ALWAYS;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_IF_ROOM;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/AnnotationDetector.java
//Synthetic comment -- index 0cb145e..7543def 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.FQCN_SUPPRESS_LINT;
import static com.android.tools.lint.detector.api.LintConstants.SUPPRESS_LINT;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.IssueRegistry;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiClass.java
//Synthetic comment -- index a0aa591..b534b2f 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.utils.Pair;
import com.google.common.collect.Lists;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index a434359..083916b 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_THEME_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.TARGET_API;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;
import static com.android.tools.lint.detector.api.LintUtils.getNextInstruction;
import static com.android.tools.lint.detector.api.Location.SearchDirection.BACKWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;
//Synthetic comment -- @@ -148,8 +148,8 @@
String value = attribute.getValue();

String prefix = null;
        if (value.startsWith(ANDROID_RESOURCE_PREFIX)) {
            prefix = ANDROID_RESOURCE_PREFIX;
} else if (value.startsWith(ANDROID_THEME_PREFIX)) {
prefix = ANDROID_THEME_PREFIX;
} else {
//Synthetic comment -- @@ -194,13 +194,13 @@
Node textNode = childNodes.item(i);
if (textNode.getNodeType() == Node.TEXT_NODE) {
String text = textNode.getNodeValue();
                    if (text.indexOf(ANDROID_RESOURCE_PREFIX) != -1) {
text = text.trim();
// Convert @android:type/foo into android/R$type and "foo"
                        int index = text.indexOf('/', ANDROID_RESOURCE_PREFIX.length());
if (index != -1) {
String owner = "android/R$"    //$NON-NLS-1$
                                    + text.substring(ANDROID_RESOURCE_PREFIX.length(), index);
String name = text.substring(index + 1);
int api = mApiDatabase.getFieldVersion(owner, name);
int minSdk = getMinSdk(context);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ApiLookup.java
//Synthetic comment -- index 5cf2603..9d661a8 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 3fc95a5..771c247 100644

//Synthetic comment -- @@ -17,10 +17,10 @@
package com.android.tools.lint.checks;


import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_INTEGER_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index 46fb4bf..4b5bc84 100644

//Synthetic comment -- @@ -16,26 +16,26 @@

package com.android.tools.lint.checks;

import static com.android.AndroidConstants.FD_RES_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_STRING_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ORIENTATION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT;
import static com.android.tools.lint.detector.api.LintConstants.BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.RELATIVE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.STRING_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_TRUE;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_VERTICAL;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -85,10 +85,10 @@

/** Layout text attribute reference to {@code @android:string/ok} */
private static final String ANDROID_OK_RESOURCE =
            ANDROID_STRING_RESOURCE_PREFIX + "ok"; //$NON-NLS-1$
/** Layout text attribute reference to {@code @android:string/cancel} */
private static final String ANDROID_CANCEL_RESOURCE =
            ANDROID_STRING_RESOURCE_PREFIX + "cancel"; //$NON-NLS-1$

/** The main issue discovered by this detector */
public static final Issue ORDER = Issue.create(
//Synthetic comment -- @@ -288,8 +288,8 @@
if (context.getDriver().getPhase() == 2) {
if (mApplicableResources.contains(text)) {
String key = text;
                    if (key.startsWith(STRING_RESOURCE_PREFIX)) {
                        key = key.substring(STRING_RESOURCE_PREFIX.length());
}
String label = mKeyToLabel.get(key);
boolean isCancel = CANCEL_LABEL.equalsIgnoreCase(label);
//Synthetic comment -- @@ -370,7 +370,7 @@
mApplicableResources = new HashSet<String>();
}

        mApplicableResources.add(STRING_RESOURCE_PREFIX + name);

// ALSO record all the other string resources in this file to pick up other
// labels. If you define "OK" in one resource file and "Cancel" in another
//Synthetic comment -- @@ -431,7 +431,7 @@
if (resFolders != null) {
for (File folder : resFolders) {
String folderName = folder.getName();
                        if (folderName.startsWith(FD_RES_LAYOUT)
&& folderName.contains("-v14")) { //$NON-NLS-1$
File layout = new File(folder, fileName);
if (layout.exists()) {
//Synthetic comment -- @@ -549,15 +549,15 @@

private String getLabel(String key) {
String label = null;
        if (key.startsWith(ANDROID_STRING_RESOURCE_PREFIX)) {
if (key.equals(ANDROID_OK_RESOURCE)) {
label = OK_LABEL;
} else if (key.equals(ANDROID_CANCEL_RESOURCE)) {
label = CANCEL_LABEL;
}
} else if (mKeyToLabel != null) {
            if (key.startsWith(STRING_RESOURCE_PREFIX)) {
                label = mKeyToLabel.get(key.substring(STRING_RESOURCE_PREFIX.length()));
}
}

//Synthetic comment -- @@ -679,4 +679,4 @@
}
return false;
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ChildCountDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ChildCountDetector.java
//Synthetic comment -- index 2f4503b..d4aa1f0 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.GRID_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.REQUEST_FOCUS;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ColorUsageDetector.java
//Synthetic comment -- index 2c7a520..c659ab1 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.RESOURCE_CLZ_COLOR;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DeprecationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DeprecationDetector.java
//Synthetic comment -- index 952224c..00953d8 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ABSOLUTE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_AUTO_TEXT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CAPITALIZE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_EDITABLE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ENABLED;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_METHOD;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NUMERIC;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PASSWORD;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PHONE_NUMBER;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_SINGLE_LINE;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DetectMissingPrefix.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DetectMissingPrefix.java
//Synthetic comment -- index 8a9c0a9..e6234f3 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_PKG_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;
import static com.android.tools.lint.detector.api.LintConstants.XMLNS_PREFIX;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index f27005c..720845d 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.INCLUDE;
import static com.android.tools.lint.detector.api.LintConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -120,7 +120,7 @@

@Override
public Collection<String> getApplicableElements() {
        return Collections.singletonList(INCLUDE);
}

@Override
//Synthetic comment -- @@ -259,9 +259,9 @@
context.report(WITHIN_LAYOUT, attribute, location,
String.format("Duplicate id %1$s, already defined earlier in this layout",
id), null);
            } else if (id.startsWith(NEW_ID_RESOURCE_PREFIX)) {
// Skip id's on include tags
                if (attribute.getOwnerElement().getTagName().equals(INCLUDE)) {
return;
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/FragmentDetector.java
//Synthetic comment -- index 06162c4..954872d 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.FRAGMENT;
import static com.android.tools.lint.detector.api.LintConstants.FRAGMENT_V4;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.LintDriver;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/GridLayoutDetector.java
//Synthetic comment -- index f32905a..34cc089 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_COLUMN_COUNT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_COLUMN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ROW;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ROW_COUNT;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedDebugModeDetector.java
//Synthetic comment -- index b684e6f..3bd913c 100644

//Synthetic comment -- @@ -16,9 +16,9 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_DEBUGGABLE;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/HardcodedValuesDetector.java
//Synthetic comment -- index 3665303..b7f60df 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_HINT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LABEL;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PROMPT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 9dd42b9..e149cc0 100644

//Synthetic comment -- @@ -16,22 +16,22 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ICON;
import static com.android.tools.lint.detector.api.LintConstants.DOT_9PNG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_GIF;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JPG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_PNG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_HDPI;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_LDPI;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_MDPI;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_XHDPI;
import static com.android.tools.lint.detector.api.LintConstants.RES_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -1221,8 +1221,8 @@
public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
assert element.getTagName().equals(TAG_APPLICATION);
mApplicationIcon = element.getAttributeNS(ANDROID_URI, ATTR_ICON);
        if (mApplicationIcon.startsWith(DRAWABLE_RESOURCE_PREFIX)) {
            mApplicationIcon = mApplicationIcon.substring(DRAWABLE_RESOURCE_PREFIX.length());
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/InefficientWeightDetector.java
//Synthetic comment -- index c00b57e..98d1301 100644

//Synthetic comment -- @@ -16,14 +16,14 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BASELINE_ALIGNED;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WIDTH;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ORIENTATION;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_VERTICAL;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/LocaleDetector.java
//Synthetic comment -- index 29b7b48..43e9ea0 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.FORMAT_METHOD;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ManifestOrderDetector.java
//Synthetic comment -- index 3d71a85..237d8a0 100644

//Synthetic comment -- @@ -16,20 +16,20 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_MIN_SDK_VERSION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TARGET_SDK_VERSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_LIBRARY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_USES_SDK;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index 5b3fe99..8769893 100644

//Synthetic comment -- @@ -16,16 +16,16 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BACKGROUND;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_FOREGROUND;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.FRAME_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.INCLUDE;
import static com.android.tools.lint.detector.api.LintConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.R_LAYOUT_PREFIX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -143,13 +143,13 @@

@Override
public Collection<String> getApplicableElements() {
        return Arrays.asList(INCLUDE, FRAME_LAYOUT);
}

@Override
public void visitElement(@NonNull XmlContext context, @NonNull Element element) {
String tag = element.getTagName();
        if (tag.equals(INCLUDE)) {
String layout = element.getAttribute(ATTR_LAYOUT); // NOTE: Not in android: namespace
if (layout.startsWith(LAYOUT_RESOURCE_PREFIX)) { // Ignore @android:layout/ layouts
layout = layout.substring(LAYOUT_RESOURCE_PREFIX.length());
//Synthetic comment -- @@ -199,8 +199,8 @@
Expression argument = argumentList.first();
if (argument instanceof Select) {
String expression = argument.toString();
                if (expression.startsWith(R_LAYOUT_PREFIX)) {
                    whiteListLayout(expression.substring(R_LAYOUT_PREFIX.length()));
}
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 8c80b68..dac3b82 100644

//Synthetic comment -- @@ -16,16 +16,16 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_PKG_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NamespaceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NamespaceDetector.java
//Synthetic comment -- index e760a9c..a4fc2f6 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_PKG_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import static com.android.tools.lint.detector.api.LintConstants.URI_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.XMLNS_PREFIX;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NestedScrollingWidgetDetector.java
//Synthetic comment -- index 3f0fef3..f32b1f4 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.GALLERY;
import static com.android.tools.lint.detector.api.LintConstants.GRID_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.LIST_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index c0fc698..08d53a3 100644

//Synthetic comment -- @@ -16,52 +16,52 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ABSOLUTE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ABOVE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_BASELINE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_BOTTOM;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_LEFT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_BOTTOM;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_LEFT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_RIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_PARENT_TOP;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_RIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_TOP;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ALIGN_WITH_PARENT_MISSING;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_BELOW;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_CENTER_HORIZONTAL;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_CENTER_IN_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_CENTER_VERTICAL;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_COLUMN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_COLUMN_SPAN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_MARGIN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ROW;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_ROW_SPAN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_SPAN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_TO_LEFT_OF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_TO_RIGHT_OF;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WIDTH;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_X;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_Y;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.GRID_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.INCLUDE;
import static com.android.tools.lint.detector.api.LintConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.MERGE;
import static com.android.tools.lint.detector.api.LintConstants.RELATIVE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.IDomParser;
//Synthetic comment -- @@ -225,7 +225,7 @@

@Override
public Collection<String> getApplicableElements() {
        return Collections.singletonList(INCLUDE);
}

@Override
//Synthetic comment -- @@ -236,7 +236,7 @@
@Override
public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
String name = attribute.getLocalName();
        if (name != null && name.startsWith(ATTR_LAYOUT_PREFIX)
&& ANDROID_URI.equals(attribute.getNamespaceURI())) {
if (VALID.contains(name)) {
return;
//Synthetic comment -- @@ -263,7 +263,7 @@
}

String parentTag = ((Element) layoutNode).getTagName();
                if (parentTag.equals(MERGE)) {
// This is a merge which means we need to check the including contexts,
// wherever they are. This has to be done after all the files have been
// scanned since we are not processing the files in any particular order.
//Synthetic comment -- @@ -305,7 +305,7 @@
Node parent = element.getParentNode();
if (parent.getNodeType() == Node.ELEMENT_NODE) {
String tag = parent.getNodeName();
                if (tag.indexOf('.') == -1 && !tag.equals(MERGE)) {
if (mIncludes == null) {
mIncludes = new HashMap<String, List<Pair<File, String>>>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 4283b7b..04fdd9d 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_ON_CLICK;

import com.android.annotations.NonNull;
import com.android.tools.lint.client.api.LintDriver;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index 0e39aff..2c3999d 100644

//Synthetic comment -- @@ -16,23 +16,23 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BACKGROUND;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_THEME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TILE_MODE;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.DRAWABLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NULL_RESOURCE;
import static com.android.tools.lint.detector.api.LintConstants.STYLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_BITMAP;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.TRANSPARENT_COLOR;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_DISABLED;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -360,7 +360,7 @@
if (endsWith(resource, DOT_XML)) {
resource = resource.substring(0, resource.length() - DOT_XML.length());
}
        return DRAWABLE_RESOURCE_PREFIX + resource;
}

private void scanBitmap(Context context, Element element) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ProguardDetector.java
//Synthetic comment -- index 29db23f..5e57849 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.PROGUARD_CONFIG;
import static com.android.tools.lint.detector.api.LintConstants.PROJECT_PROPERTIES;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PxUsageDetector.java
//Synthetic comment -- index c7d688f..ef2768d 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TEXT_SIZE;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ITEM;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DIP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_DP;
import static com.android.tools.lint.detector.api.LintConstants.UNIT_PX;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index 5f4aff4..e87505e 100644

//Synthetic comment -- @@ -16,17 +16,17 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_SERVICE;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_CONTENT_BROADCAST_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_CONTENT_CONTENT_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PACKAGE;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ScrollViewChildDetector.java
//Synthetic comment -- index e7796ea..c4db539 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WIDTH;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_FILL_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_MATCH_PARENT;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SecurityDetector.java
//Synthetic comment -- index 8c79a2c..8c3bc84 100644

//Synthetic comment -- @@ -16,24 +16,24 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_MANIFEST_XML;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_EXPORTED;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PATH;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PATH_PATTERN;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PATH_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_READ_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_WRITE_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ACTIVITY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_GRANT_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_INTENT_FILTER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PATH_PERMISSION;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PROVIDER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_RECEIVER;
import static com.android.tools.lint.detector.api.LintConstants.TAG_SERVICE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StateListDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StateListDetector.java
//Synthetic comment -- index c08b0c6..6161e04 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index ae1bf52..f6f0f7e 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.FORMAT_METHOD;
import static com.android.tools.lint.detector.api.LintConstants.GET_STRING_METHOD;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StyleCycleDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StyleCycleDetector.java
//Synthetic comment -- index 82001d0..61290e1 100644

//Synthetic comment -- @@ -16,10 +16,10 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PARENT;
import static com.android.tools.lint.detector.api.LintConstants.STYLE_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextFieldDetector.java
//Synthetic comment -- index 518bf2e..74efb32 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_HINT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_METHOD;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.EDIT_TEXT;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TextViewDetector.java
//Synthetic comment -- index db9155a..1714198 100644

//Synthetic comment -- @@ -16,33 +16,33 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_AUTO_TEXT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BUFFER_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CAPITALIZE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_CURSOR_VISIBLE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_DIGITS;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_EDITABLE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_EDITOR_EXTRAS;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_IME_ACTION_ID;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_IME_ACTION_LABEL;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_IME_OPTIONS;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_METHOD;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_INPUT_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NUMERIC;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PASSWORD;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PHONE_NUMBER;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_PRIVATE_IME_OPTIONS;
import static com.android.tools.lint.detector.api.LintConstants.BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.CHECKED_TEXT_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.CHECK_BOX;
import static com.android.tools.lint.detector.api.LintConstants.RADIO_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.SWITCH;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.TOGGLE_BUTTON;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_EDITABLE;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_NONE;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_TRUE;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TranslationDetector.java
//Synthetic comment -- index 02fe6ae..7c409fc 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TRANSLATABLE;
import static com.android.tools.lint.detector.api.LintConstants.STRING_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ITEM;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;
//Synthetic comment -- @@ -533,8 +533,8 @@
Node valueNode = itemChildren.item(j);
if (valueNode.getNodeType() == Node.TEXT_NODE) {
String value = valueNode.getNodeValue().trim();
                        if (!value.startsWith(ANDROID_RESOURCE_PREFIX)
                                && !value.startsWith(STRING_RESOURCE_PREFIX)) {
return false;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoDetector.java
//Synthetic comment -- index 0eb9866..6f85505 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
package com.android.tools.lint.checks;

import static com.android.tools.lint.checks.TypoLookup.isLetter;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.google.common.base.Objects.equal;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoLookup.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypoLookup.java
//Synthetic comment -- index 4a4591d..2dcd6c3 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintUtils.assertionsEnabled;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/TypographyDetector.java
//Synthetic comment -- index 5ddc75b..66d6889 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;

import com.android.annotations.NonNull;
import com.android.annotations.VisibleForTesting;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UnusedResourceDetector.java
//Synthetic comment -- index 3217a09..1b4163e 100644

//Synthetic comment -- @@ -16,27 +16,27 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_REF_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.DOT_GIF;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JPG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_PNG;
import static com.android.tools.lint.detector.api.LintConstants.DOT_XML;
import static com.android.tools.lint.detector.api.LintConstants.RESOURCE_CLR_STYLEABLE;
import static com.android.tools.lint.detector.api.LintConstants.RESOURCE_CLZ_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.RESOURCE_CLZ_ID;
import static com.android.tools.lint.detector.api.LintConstants.RES_FOLDER;
import static com.android.tools.lint.detector.api.LintConstants.R_ATTR_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.R_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.R_ID_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.R_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ITEM;
import static com.android.tools.lint.detector.api.LintConstants.TAG_PLURALS;
import static com.android.tools.lint.detector.api.LintConstants.TAG_RESOURCES;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STRING_ARRAY;
import static com.android.tools.lint.detector.api.LintConstants.TAG_STYLE;
import static com.android.tools.lint.detector.api.LintUtils.endsWith;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UseCompoundDrawableDetector.java
//Synthetic comment -- index e2923b0..289806f 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BACKGROUND;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_WEIGHT;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_SCALE_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.IMAGE_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TEXT_VIEW;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/UselessViewDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/UselessViewDetector.java
//Synthetic comment -- index b913588..c75cb93 100644

//Synthetic comment -- @@ -16,22 +16,22 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ABSOLUTE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_BACKGROUND;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_STYLE;
import static com.android.tools.lint.detector.api.LintConstants.FRAME_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.GRID_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.GRID_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.HORIZONTAL_SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.LINEAR_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.MERGE;
import static com.android.tools.lint.detector.api.LintConstants.RADIO_GROUP;
import static com.android.tools.lint.detector.api.LintConstants.RELATIVE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.SCROLL_VIEW;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TABLE_ROW;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -158,7 +158,7 @@
Element parent = (Element) parentNode;
String parentTag = parent.getTagName();
if (parentTag.equals(SCROLL_VIEW) || parentTag.equals(HORIZONTAL_SCROLL_VIEW) ||
                parentTag.equals(MERGE)) {
// Can't remove if the parent is a scroll view or a merge
return;
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewConstructorDetector.java
//Synthetic comment -- index a532ced..1c276ed 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.CONSTRUCTOR_NAME;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewTypeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ViewTypeDetector.java
//Synthetic comment -- index 30def17..61498f4 100644

//Synthetic comment -- @@ -16,12 +16,12 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ATTR_CLASS;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.DOT_JAVA;
import static com.android.tools.lint.detector.api.LintConstants.ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.VIEW_TAG;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -106,10 +106,10 @@
String view = attribute.getOwnerElement().getTagName();
String value = attribute.getValue();
String id = null;
        if (value.startsWith(ID_RESOURCE_PREFIX)) {
            id = value.substring(ID_RESOURCE_PREFIX.length());
        } else if (value.startsWith(NEW_ID_RESOURCE_PREFIX)) {
            id = value.substring(NEW_ID_RESOURCE_PREFIX.length());
} // else: could be @android id

if (id != null) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index 066575a..ccf8d73 100644

//Synthetic comment -- @@ -15,7 +15,7 @@
*/

package com.android.tools.lint.checks;
import static com.android.tools.lint.detector.api.LintConstants.ANDROID_APP_ACTIVITY;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index 8003843..3f8afb6 100644

//Synthetic comment -- @@ -16,16 +16,16 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.ANDROID_URI;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_ID;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_LAYOUT_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_NAME;
import static com.android.tools.lint.detector.api.LintConstants.ATTR_TYPE;
import static com.android.tools.lint.detector.api.LintConstants.ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.NEW_ID_RESOURCE_PREFIX;
import static com.android.tools.lint.detector.api.LintConstants.RELATIVE_LAYOUT;
import static com.android.tools.lint.detector.api.LintConstants.TAG_ITEM;
import static com.android.tools.lint.detector.api.LintConstants.VALUE_ID;
import static com.android.tools.lint.detector.api.LintUtils.stripIdPrefix;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -168,10 +168,10 @@
for (int i = 0, n = attributes.getLength(); i < n; i++) {
Attr attr = (Attr) attributes.item(i);
String value = attr.getValue();
                        if ((value.startsWith(NEW_ID_RESOURCE_PREFIX) ||
                                value.startsWith(ID_RESOURCE_PREFIX))
&& ANDROID_URI.equals(attr.getNamespaceURI())
                                && attr.getLocalName().startsWith(ATTR_LAYOUT_PREFIX)) {
if (!idDefined(mFileIds, value)) {
// Stash a reference to this id and location such that
// we can check after the *whole* layout has been processed,
//Synthetic comment -- @@ -273,7 +273,7 @@
if (mDeclaredIds == null) {
mDeclaredIds = Sets.newHashSet();
}
                    mDeclaredIds.add(ID_RESOURCE_PREFIX + name);
}
}
}
//Synthetic comment -- @@ -293,12 +293,12 @@
}
boolean definedLocally = ids.contains(id);
if (!definedLocally) {
            if (id.startsWith(NEW_ID_RESOURCE_PREFIX)) {
                definedLocally = ids.contains(ID_RESOURCE_PREFIX +
                        id.substring(NEW_ID_RESOURCE_PREFIX.length()));
            } else if (id.startsWith(ID_RESOURCE_PREFIX)) {
                definedLocally = ids.contains(NEW_ID_RESOURCE_PREFIX +
                        id.substring(ID_RESOURCE_PREFIX.length()));
}
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongLocationDetector.java
//Synthetic comment -- index 39ffe1f..09011cc 100644

//Synthetic comment -- @@ -16,7 +16,7 @@

package com.android.tools.lint.checks;

import static com.android.tools.lint.detector.api.LintConstants.TAG_RESOURCES;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/IssueTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/IssueTest.java
//Synthetic comment -- index 740e921..eba5fd3 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
package com.android.tools.lint.detector.api;

import static com.android.tools.lint.detector.api.Issue.convertMarkup;
import static com.android.tools.lint.detector.api.LintConstants.AUTO_URI;
import junit.framework.TestCase;

@SuppressWarnings("javadoc")








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 8bc5d91..fee9472 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.sdklib.internal.project;

import com.android.AndroidConstants;
import com.android.SdkConstants;
import com.android.io.FileWrapper;
import com.android.io.FolderWrapper;
//Synthetic comment -- @@ -338,11 +337,11 @@

if (isTestProject == false) {
/* Make res files only for non test projects */
                File valueFolder = createDirs(resourceFolder, AndroidConstants.FD_RES_VALUES);
installTargetTemplate("strings.template", new File(valueFolder, "strings.xml"),
keywords, target);

                File layoutFolder = createDirs(resourceFolder, AndroidConstants.FD_RES_LAYOUT);
installTargetTemplate("layout.template", new File(layoutFolder, "main.xml"),
keywords, target);








