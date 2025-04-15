/*Fix javadoc here and there.

Change-Id:If02d0b97c294d9821a1c914547782a08b1d256fb*/




//Synthetic comment -- diff --git a/common/src/com/android/utils/SdkUtils.java b/common/src/com/android/utils/SdkUtils.java
//Synthetic comment -- index 9bf43dc..18d3ccd 100644

//Synthetic comment -- @@ -128,8 +128,8 @@
/**
* Returns the default line separator to use.
* <p>
     * NOTE: If you have an associated IDocument (Eclipse), it is better to call
     * TextUtilities#getDefaultLineDelimiter(IDocument) since that will
* allow (for example) editing a \r\n-delimited document on a \n-delimited
* platform and keep a consistent usage of delimiters in the file.
*








//Synthetic comment -- diff --git a/common/src/com/android/utils/StdLogger.java b/common/src/com/android/utils/StdLogger.java
//Synthetic comment -- index c0de06f..05eb456 100644

//Synthetic comment -- @@ -103,7 +103,7 @@
* <p/>
* This is displayed only if the logging {@link Level} is {@link Level#WARNING} or higher.
*
     * @param warningFormat is a string format to be used with a {@link Formatter}. Cannot be null.
* @param args provides the arguments for warningFormat.
*/
@Override








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index d81aea9..452d032 100644

//Synthetic comment -- @@ -351,7 +351,6 @@
*
* @param localPort the local port to forward
* @param remotePort the remote port.
* @throws TimeoutException in case of timeout on the connection.
* @throws AdbCommandRejectedException if adb rejects the command
* @throws IOException in case of I/O error on the connection.
//Synthetic comment -- @@ -365,7 +364,6 @@
* @param localPort the local port to forward
* @param remoteSocketName name of the unix domain socket created on the device
* @param namespace namespace in which the unix domain socket was created
* @throws TimeoutException in case of timeout on the connection.
* @throws AdbCommandRejectedException if adb rejects the command
* @throws IOException in case of I/O error on the connection.
//Synthetic comment -- @@ -379,7 +377,6 @@
*
* @param localPort the local port to forward
* @param remotePort the remote port.
* @throws TimeoutException in case of timeout on the connection.
* @throws AdbCommandRejectedException if adb rejects the command
* @throws IOException in case of I/O error on the connection.
//Synthetic comment -- @@ -393,7 +390,6 @@
* @param localPort the local port to forward
* @param remoteSocketName the remote unix domain socket name.
* @param namespace namespace in which the unix domain socket was created
* @throws TimeoutException in case of timeout on the connection.
* @throws AdbCommandRejectedException if adb rejects the command
* @throws IOException in case of I/O error on the connection.








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/NativeAllocationInfo.java b/ddms/libs/ddmlib/src/com/android/ddmlib/NativeAllocationInfo.java
//Synthetic comment -- index 385ce0d..9b104ba 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -146,7 +145,7 @@
* Returns the resolved stack call.
* @return An array of {@link NativeStackCallInfo} or <code>null</code> if the stack call
* was not resolved.
     * @see #setResolvedStackCall(List)
* @see #isStackCallResolved()
*/
public synchronized List<NativeStackCallInfo> getResolvedStackCall() {
//Synthetic comment -- @@ -263,7 +262,7 @@
* lower level of the libc, but the actual method that performed the allocation.
* @return a <code>NativeStackCallInfo</code> or <code>null</code> if the stack call has not
* been processed from the raw addresses.
     * @see #setResolvedStackCall(List)
* @see #isStackCallResolved()
*/
public synchronized NativeStackCallInfo getRelevantStackCallInfo() {








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/IRemoteAndroidTestRunner.java
//Synthetic comment -- index 7606d69..7d3d6bf 100644

//Synthetic comment -- @@ -16,8 +16,8 @@

package com.android.ddmlib.testrunner;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

//Synthetic comment -- @@ -181,7 +181,7 @@
* <p/>
* By default no timeout will be specified.
*
     * @see IDevice#executeShellCommand(String, com.android.ddmlib.IShellOutputReceiver, int)
*/
public void setMaxtimeToOutputResponse(int maxTimeToOutputResponse);









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java b/ddms/libs/ddmlib/src/com/android/ddmlib/testrunner/ITestRunListener.java
//Synthetic comment -- index a8b117d..7e20c9f 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
/**
* Receives event notifications during instrumentation test runs.
* <p/>
 * Patterned after junit.runner.TestRunListener.
* <p/>
* The sequence of calls will be:
* <ul>








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DeclareStyleableResourceValue.java
//Synthetic comment -- index f14255f..a8f269f 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
* {@link #getValue()} will return null, instead use {@link #getAttributeValues(String)} to
* get the enum/flag value associated with an attribute defined in the declare-styleable.
*
 * @deprecated This class is broken as it does not handle the namespace for each attribute.
* Thankfully, newer versions of layoutlib don't actually use it, so we just keep it as is for
* backward compatibility on older layoutlibs.
*








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/DrawableParams.java
//Synthetic comment -- index 766b3be..346d67d 100644

//Synthetic comment -- @@ -37,16 +37,7 @@
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
    * @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param minSdkVersion the minSdkVersion of the project








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ILayoutPullParser.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ILayoutPullParser.java
//Synthetic comment -- index 574f9bb..9c0e97b 100644

//Synthetic comment -- @@ -20,7 +20,7 @@

/**
* Extended version of {@link XmlPullParser} to use with
 * {@link Bridge#createSession(SessionParams)}
*/
public interface ILayoutPullParser extends XmlPullParser {









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java b/layoutlib_api/src/com/android/ide/common/rendering/api/IProjectCallback.java
//Synthetic comment -- index 8ccdd75..a88b0d3 100644

//Synthetic comment -- @@ -126,7 +126,7 @@
*     of the same type. This is only valid if the adapter view is an ExpandableListView.
*     If there is only one type of items, this is the same as <var>fullParentPosition</var>.
* @param viewRef The {@link ResourceReference} for the view we're trying to fill.
     * @param viewAttribute the attribute being queried.
* @param defaultValue the default value for this attribute. The object class matches the
*      class associated with the {@link ViewAttribute}.
* @return the item value or null if there's no value.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderParams.java
//Synthetic comment -- index 2cfe770..f89dcfe 100644

//Synthetic comment -- @@ -60,16 +60,7 @@
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
    * @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param minSdkVersion the minSdkVersion of the project








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java
//Synthetic comment -- index f9e02d6..c362224 100644

//Synthetic comment -- @@ -134,7 +134,7 @@
* @param attrName the name of the attribute to search for.
* @return the {@link ResourceValue} object or <code>null</code>
*
     * @deprecated Use {@link #findItemInStyle(StyleResourceValue, String, boolean)} since this
* method doesn't know the item namespace.
*/
@Deprecated








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index 188909e..96caa6a 100644

//Synthetic comment -- @@ -27,7 +27,7 @@
/**
* An object allowing interaction with an Android layout.
*
 * This is returned by {@link Bridge#createSession(SessionParams)}.
* and can then be used for subsequent actions on the layout.
*
* @since 5
//Synthetic comment -- @@ -94,7 +94,7 @@

/**
* Re-renders the layout as-is.
     * In case of success, this should be followed by calls to {@link #getRootViews()} and
* {@link #getImage()} to access the result of the rendering.
*
* This is equivalent to calling <code>render(SceneParams.DEFAULT_TIMEOUT)</code>
//Synthetic comment -- @@ -107,7 +107,7 @@

/**
* Re-renders the layout as-is, with a given timeout in case other renderings are being done.
     * In case of success, this should be followed by calls to {@link #getRootViews()} and
* {@link #getImage()} to access the result of the rendering.
*
* The {@link Bridge} is only able to inflate or render one layout at a time. There








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index 1af450e..a620b05 100644

//Synthetic comment -- @@ -67,16 +67,7 @@
* @param density the density factor for the screen.
* @param xdpi the screen actual dpi in X
* @param ydpi the screen actual dpi in Y
    * @param renderResources a {@link RenderResources} object providing access to the resources.
* @param projectCallback The {@link IProjectCallback} object to get information from
* the project.
* @param minSdkVersion the minSdkVersion of the project








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ViewInfo.java
//Synthetic comment -- index 2671fc0..d859e95 100644

//Synthetic comment -- @@ -88,7 +88,7 @@
/**
* Returns the cookie associated with the XML node. Can be null.
*
     * @see ILayoutPullParser#getViewCookie()
*/
public Object getCookie() {
return mCookie;
//Synthetic comment -- @@ -131,8 +131,8 @@

/**
* Returns the actual android.view.View (or child class) object. This can be used
     * to query the object properties that are not in the XML and not available through
     * {@link RenderSession#getProperty(Object, String)}.
*/
public Object getViewObject() {
return mViewObject;
//Synthetic comment -- @@ -140,8 +140,8 @@

/**
* Returns the actual  android.view.ViewGroup$LayoutParams (or child class) object.
     * This can be used to query the object properties that are not in the XML and not available
     * through {@link RenderSession#getProperty(Object, String)}.
*/
public Object getLayoutParamsObject() {
return mLayoutParamsObject;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutBridge.java
//Synthetic comment -- index 56d2e36..f849cdd 100644

//Synthetic comment -- @@ -104,7 +104,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link Bridge#createSession(com.android.ide.common.rendering.api.SessionParams)}
* @since 4
*/
@Deprecated
//Synthetic comment -- @@ -141,7 +141,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link Bridge#createSession(com.android.ide.common.rendering.api.SessionParams)}
* @since 3
*/
@Deprecated
//Synthetic comment -- @@ -174,7 +174,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link Bridge#createSession(com.android.ide.common.rendering.api.SessionParams)}
* @since 2
*/
@Deprecated
//Synthetic comment -- @@ -206,7 +206,7 @@
* the project.
* @param logger the object responsible for displaying warning/errors to the user.
* @return a new {@link ILayoutResult} object that contains the result of the layout.
     * @deprecated use {@link Bridge#createSession(com.android.ide.common.rendering.api.SessionParams)}
* @since 1
*/
@Deprecated








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java b/layoutlib_api/src/com/android/layoutlib/api/ILayoutResult.java
//Synthetic comment -- index 46255c4..6aeaf9f 100644

//Synthetic comment -- @@ -25,8 +25,9 @@
* The result of a layout computation through {@link ILayoutBridge}.
*
* @since 1
 * @deprecated use {@link RenderSession} as returned by {@link Bridge#createSession(com.android.ide.common.rendering.api.SessionParams)}
*/
@Deprecated
public interface ILayoutResult {
/**
* Success return code
//Synthetic comment -- @@ -66,6 +67,7 @@
* Layout information for a specific view.
* @deprecated
*/
    @Deprecated
public interface ILayoutViewInfo {

/**








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java b/layoutlib_api/src/com/android/layoutlib/api/IXmlPullParser.java
//Synthetic comment -- index b4d10a2..e0a98a6 100644

//Synthetic comment -- @@ -21,12 +21,14 @@
/**
* @deprecated
*/
@Deprecated
public interface IXmlPullParser extends XmlPullParser {

/**
* Returns a key for the current XML node.
     * <p/>This key will be passed back in the {@link com.android.ide.common.rendering.api.ViewInfo}
     * objects, allowing association of a particular XML node with its result from the
     * layout computation.
*/
Object getViewKey();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/util/Pair.java b/layoutlib_api/src/com/android/util/Pair.java
//Synthetic comment -- index 3818107..7e797e0 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
* @param <S> The type of the first value
* @param <T> The type of the second value
*
 * @deprecated This is used for backward compatibility with layoutlib_api. Use com.android.utils.Pair instead
*/
@Deprecated
public class Pair<S,T> {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceWriter.java
//Synthetic comment -- index feed6d4..d80f91e 100644

//Synthetic comment -- @@ -47,7 +47,7 @@

/**
* Writes the XML definition of the given {@link Collection} of {@link Device}s according to
     * {@link SdkConstants#NS_DEVICES_XSD} to the {@link OutputStream}.
* Note that it is up to the caller to close the {@link OutputStream}.
* @param out The {@link OutputStream} to write the resulting XML to.
* @param devices The {@link Device}s from which to generate the XML.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectCreator.java
//Synthetic comment -- index 8a201d2..1fdea64 100644

//Synthetic comment -- @@ -50,8 +50,6 @@

/**
* Creates the basic files needed to get an Android project up and running.
*/
public class ProjectCreator {









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IDescription.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/IDescription.java
//Synthetic comment -- index 5662a9c..7226360 100755

//Synthetic comment -- @@ -25,7 +25,7 @@
* Returns a description of the given element. Cannot be null.
* <p/>
* A description is a multi-line of text, typically much more
     * elaborate than what {@link Object#toString()} would provide.
*/
public abstract String getShortDescription();

//Synthetic comment -- @@ -33,7 +33,7 @@
* Returns a description of the given element. Cannot be null.
* <p/>
* A description is a multi-line of text, typically much more
     * elaborate than what {@link Object#toString()} would provide.
*/
public abstract String getLongDescription();








