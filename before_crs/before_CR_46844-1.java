/*Merge "TaskHelper: properly parse Pkg.Revision property."

SDK Bug: 36987

Bug was that TaskHelper would fail when parsing a pk
revision with an RC value. A quick fix was done in CL
6d266a1bf5992d27fff4400c5d2bd30681c65c3d a while ago.

Revisit the fix by using the new FullRevision object
to parse the value and create a DeweyDecimal; should
avoid us trouble if we later change the format.

(cherry picked from commit 9093160c8aa3e3832c4db09f57f640055b964bdb)

Change-Id:I210a051650683548ca4fcd2bf755a95675673aac*/
//Synthetic comment -- diff --git a/anttasks/src/com/android/ant/TaskHelper.java b/anttasks/src/com/android/ant/TaskHelper.java
//Synthetic comment -- index fe154cf..43ea33a 100644

//Synthetic comment -- @@ -22,6 +22,8 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
//Synthetic comment -- @@ -91,19 +93,17 @@
}
}

            String value = p.getProperty("Pkg.Revision"); //$NON-NLS-1$
if (value != null) {
                value = value.trim();
                int space = value.indexOf(' ');
                if (space != -1) {
                    value = value.substring(0, space);
                }
                return new DeweyDecimal(value);
}
} catch (FileNotFoundException e) {
            // couldn't find the file? return -1 below.
} catch (IOException e) {
            // couldn't find the file? return -1 below.
}

return null;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevision.java
//Synthetic comment -- index 4c4387b..715028e 100755

//Synthetic comment -- @@ -169,6 +169,31 @@
return sb.toString();
}

@Override
public int hashCode() {
final int prime = 31;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/FullRevisionTest.java
//Synthetic comment -- index d072d05..07e8186 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;

import junit.framework.TestCase;

public class FullRevisionTest extends TestCase {
//Synthetic comment -- @@ -31,6 +33,8 @@
assertEquals(p, FullRevision.parseRevision("5"));
assertEquals("5.0.0", p.toString());
assertEquals(p, FullRevision.parseRevision("5.0.0"));

p = new FullRevision(5, 0, 0, 6);
assertEquals(5, p.getMajor());
//Synthetic comment -- @@ -42,6 +46,8 @@
assertEquals(p, FullRevision.parseRevision("5 rc6"));
assertEquals("5.0.0 rc6", p.toString());
assertEquals(p, FullRevision.parseRevision("5.0.0 rc6"));

p = new FullRevision(6, 7, 0);
assertEquals(6, p.getMajor());
//Synthetic comment -- @@ -53,6 +59,8 @@
assertEquals(p, FullRevision.parseRevision("6.7"));
assertEquals("6.7.0", p.toString());
assertEquals(p, FullRevision.parseRevision("6.7.0"));

p = new FullRevision(10, 11, 12, FullRevision.NOT_A_PREVIEW);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -63,6 +71,8 @@
assertEquals("10.11.12", p.toShortString());
assertEquals("10.11.12", p.toString());
assertEquals(p, FullRevision.parseRevision("10.11.12"));

p = new FullRevision(10, 11, 12, 13);
assertEquals(10, p.getMajor());
//Synthetic comment -- @@ -76,6 +86,8 @@
assertEquals(p, FullRevision.parseRevision("   10.11.12 rc13"));
assertEquals(p, FullRevision.parseRevision("10.11.12 rc13   "));
assertEquals(p, FullRevision.parseRevision("   10.11.12   rc13   "));
}

public final void testParseError() {







