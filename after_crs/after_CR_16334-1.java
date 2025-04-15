/*Fix compile errors in telephony tests

Simple fix of package names for two test cases, currently
they do not compile in an IDE.

Change-Id:Ia7cd4df70ff37875a02563cfddbe8dd4a16a5b01*/




//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/android/telephony/PhoneNumberUtilsTest.java b/telephony/tests/telephonytests/src/android/telephony/PhoneNumberUtilsTest.java
//Synthetic comment -- index dd2cb08..2165023 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package android.telephony;

import android.test.suitebuilder.annotation.SmallTest;
import android.text.SpannableStringBuilder;








//Synthetic comment -- diff --git a/telephony/tests/telephonytests/src/android/telephony/PhoneNumberWatcherTest.java b/telephony/tests/telephonytests/src/android/telephony/PhoneNumberWatcherTest.java
//Synthetic comment -- index 88eaecd..4c7ebca 100644

//Synthetic comment -- @@ -14,7 +14,7 @@
* limitations under the License.
*/

package android.telephony;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.test.suitebuilder.annotation.SmallTest;







