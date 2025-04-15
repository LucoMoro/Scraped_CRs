/*Fix AutoText setting of locale

Setting the locale in AutoTextTest does not update
the configuration properly. This problem appears
only when the default locale is not set to English
(property persist.sys.language).

The test just changes the default locale used for
initialization, but other locale fields remain not updated.
This fix also updates the configuration so that all
locale dependent fields are consistent.

Change-Id:I7d4c1d12a87e546a3320926dfd212d299263d844Signed-off-by: Irina Tirdea <irina.tirdea@intel.com>*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/cts/AutoTextTest.java b/tests/tests/text/src/android/text/cts/AutoTextTest.java
//Synthetic comment -- index 1f2d1da..cbea4d9 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import android.test.AndroidTestCase;
import android.text.AutoText;
import android.view.View;
import android.content.res.Configuration;

public class AutoTextTest extends AndroidTestCase {

//Synthetic comment -- @@ -30,6 +31,11 @@

// set local as English.
Locale.setDefault(Locale.ENGLISH);
        Configuration config = getContext().getResources().getConfiguration();
        if (!config.locale.equals(Locale.getDefault())) {
                config.locale = Locale.getDefault();
                getContext().getResources().updateConfiguration(config, null);
        }
// New a View instance.
View view = new View(getContext());

//Synthetic comment -- @@ -71,6 +77,11 @@

public void testGetSize() {
Locale.setDefault(Locale.ENGLISH);
        Configuration config = getContext().getResources().getConfiguration();
        if (!config.locale.equals(Locale.getDefault())) {
                config.locale = Locale.getDefault();
                getContext().getResources().updateConfiguration(config, null);
        }
View view = new View(getContext());
// Returns the size of the auto text dictionary. Just make sure it is bigger than 0.
assertTrue(AutoText.getSize(view) > 0);







