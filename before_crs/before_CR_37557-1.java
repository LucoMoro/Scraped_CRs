/*Move base class for FindDialog to a common location.

Change-Id:Ie3b2e424a7dc9df73aca1c7914497f1c9a126901*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/AbstractBufferFindTarget.java
similarity index 90%
rename from ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/RollingBufferFindTarget.java
rename to ddms/libs/ddmuilib/src/com/android/ddmuilib/AbstractBufferFindTarget.java
//Synthetic comment -- index b353a13..13a787a 100644

//Synthetic comment -- @@ -14,14 +14,16 @@
* limitations under the License.
*/

package com.android.ddmuilib.logcat;

import java.util.regex.Pattern;

/**
 * {@link RollingBufferFindTarget} implements methods to find items inside a circular buffer.
*/
public abstract class RollingBufferFindTarget implements IFindTarget {
private int mCurrentSearchIndex;

// Single element cache of the last search regex








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/FindDialog.java
similarity index 97%
rename from ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/FindDialog.java
rename to ddms/libs/ddmuilib/src/com/android/ddmuilib/FindDialog.java
//Synthetic comment -- index cfd1ee8..6370be4 100644

//Synthetic comment -- @@ -14,7 +14,8 @@
* limitations under the License.
*/

package com.android.ddmuilib.logcat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
//Synthetic comment -- @@ -32,7 +33,7 @@

/**
* {@link FindDialog} provides a text box where users can enter text that should be
 * searched for in the list of logcat messages. The buttons "Find Previous" and "Find Next"
* allow users to search forwards/backwards. This dialog simply provides a front end for the user
* and the actual task of searching is delegated to the {@link IFindTarget}.
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/IFindTarget.java
similarity index 94%
rename from ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/IFindTarget.java
rename to ddms/libs/ddmuilib/src/com/android/ddmuilib/IFindTarget.java
//Synthetic comment -- index f27c53e..9aa6943 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package com.android.ddmuilib.logcat;

public interface IFindTarget {
boolean findAndSelect(String text, boolean isNewSearch, boolean searchForward);








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index 06952f9..e7dcec9 100644

//Synthetic comment -- @@ -21,9 +21,11 @@
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.ITableFocusListener;
import com.android.ddmuilib.ITableFocusListener.IFocusedTableActivator;
import com.android.ddmuilib.ImageLoader;
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
//Synthetic comment -- @@ -1443,7 +1445,7 @@
mAssertColor.dispose();
}

    private class LogcatFindTarget extends RollingBufferFindTarget {
@Override
public void selectAndReveal(int index) {
mTable.deselectAll();








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java b/ddms/libs/ddmuilib/tests/src/com/android/ddmuilib/logcat/RollingBufferFindTest.java
//Synthetic comment -- index 7afac24..32a36c4 100644

//Synthetic comment -- @@ -16,13 +16,15 @@

package com.android.ddmuilib.logcat;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class RollingBufferFindTest extends TestCase {
    public class FindTarget extends RollingBufferFindTarget {
private int mSelectedItem = -1;
private int mItemReadCount = 0;
private List<String> mItems = Arrays.asList(







