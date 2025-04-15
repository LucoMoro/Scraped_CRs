/*Fix ContentResolverTest Race Conditions

Issue 9398

Add some DelayedCheck around some potentially asynchronous calls
and add synchronization around members that are set and get by
different threads.

Change-Id:I4a04bfd97e899ad147f858f8a176ce704c66134f*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContentResolverTest.java b/tests/tests/content/src/android/content/cts/ContentResolverTest.java
//Synthetic comment -- index f28b6f0..b866375 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
//Synthetic comment -- @@ -34,7 +35,7 @@
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.test.AndroidTestCase;
import android.accounts.Account;

import java.io.FileNotFoundException;
import java.io.IOException;
//Synthetic comment -- @@ -627,7 +628,7 @@
"ContentResolver#unregisterContentObserver(ContentObserver) when the input " +
"params are null")
public void testRegisterContentObserver() {
        MockContentObserver mco = new MockContentObserver();

mContentResolver.registerContentObserver(TABLE1_URI, true, mco);
assertFalse(mco.hadOnChanged());
//Synthetic comment -- @@ -636,8 +637,12 @@
values.put(COLUMN_KEY_NAME, "key10");
values.put(COLUMN_VALUE_NAME, 10);
mContentResolver.update(TABLE1_URI, values, null, null);

        assertTrue(mco.hadOnChanged());

mco.reset();
mContentResolver.unregisterContentObserver(mco);
//Synthetic comment -- @@ -677,14 +682,19 @@
)
@ToBeFixed(bug = "1695243", explanation = "should add @throws clause into javadoc of " +
"ContentResolver#notifyChange(Uri, ContentObserver) when uri is null")
    public void testNotifyChange1() throws InterruptedException {
        MockContentObserver mco = new MockContentObserver();

mContentResolver.registerContentObserver(TABLE1_URI, true, mco);
assertFalse(mco.hadOnChanged());

mContentResolver.notifyChange(TABLE1_URI, mco);
        assertTrue(mco.hadOnChanged());

mContentResolver.unregisterContentObserver(mco);
}
//Synthetic comment -- @@ -697,13 +707,18 @@
@ToBeFixed(bug = "1695243", explanation = "should add @throws clause into javadoc of " +
"ContentResolver#notifyChange(Uri, ContentObserver, boolean) when uri is null ")
public void testNotifyChange2() {
        MockContentObserver mco = new MockContentObserver();

mContentResolver.registerContentObserver(TABLE1_URI, true, mco);
assertFalse(mco.hadOnChanged());

mContentResolver.notifyChange(TABLE1_URI, mco, false);
        assertTrue(mco.hadOnChanged());

mContentResolver.unregisterContentObserver(mco);
}
//Synthetic comment -- @@ -789,16 +804,16 @@
}

@Override
        public void onChange(boolean selfChange) {
super.onChange(selfChange);
mHadOnChanged = true;
}

        public boolean hadOnChanged() {
return mHadOnChanged;
}

        public void reset() {
mHadOnChanged = false;
}
}







