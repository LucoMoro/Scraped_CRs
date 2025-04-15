/*Fix races in OldPreferencesTest.

Bug: 6972612
Change-Id:I8adef78824a4a42cf1411c8ef8279ae9b184ee25*/




//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/prefs/OldPreferencesTest.java b/luni/src/test/java/libcore/java/util/prefs/OldPreferencesTest.java
//Synthetic comment -- index aa5f088..f8a8154 100644

//Synthetic comment -- @@ -29,8 +29,10 @@

public final class OldPreferencesTest extends TestCase {

    private static final boolean ENCOURAGE_RACES = false;

    private static final String longKey;
    private static final String longValue;

static {
StringBuilder key = new StringBuilder(Preferences.MAX_KEY_LENGTH);
//Synthetic comment -- @@ -316,8 +318,7 @@
String longName = name.toString();

Preferences root = Preferences.userRoot();
        Preferences parent = Preferences.userNodeForPackage(Preferences.class);
Preferences pref = parent.node("mock");

try {
//Synthetic comment -- @@ -352,8 +353,7 @@

public void testNodeExists() throws BackingStoreException {

        Preferences parent = Preferences.userNodeForPackage(Preferences.class);
Preferences pref = parent.node("mock");

try {
//Synthetic comment -- @@ -393,16 +393,14 @@
}

public void testParent() {
        Preferences parent = Preferences.userNodeForPackage(Preferences.class);
Preferences pref = parent.node("mock");

assertSame(parent, pref.parent());
}

public void testPut() throws BackingStoreException {
        Preferences pref = Preferences.userNodeForPackage(Preferences.class);
pref.put("", "emptyvalue");
assertEquals("emptyvalue", pref.get("", null));
pref.put("testPutkey", "value1");
//Synthetic comment -- @@ -468,8 +466,7 @@
}

public void testPutDouble() {
        Preferences pref = Preferences.userNodeForPackage(Preferences.class);
try {
pref.putDouble(null, 3);
fail();
//Synthetic comment -- @@ -580,8 +577,7 @@
}

public void testRemove() throws BackingStoreException {
        Preferences pref = Preferences.userNodeForPackage(Preferences.class);
pref.remove("key");

pref.put("key", "value");
//Synthetic comment -- @@ -606,8 +602,7 @@
}

public void testRemoveNode() throws BackingStoreException {
        Preferences pref = Preferences.userNodeForPackage(Preferences.class);
Preferences child = pref.node("child");
Preferences child1 = pref.node("child1");
Preferences grandchild = child.node("grandchild");
//Synthetic comment -- @@ -636,11 +631,12 @@
nl = new MockNodeChangeListener();
pref.addNodeChangeListener(nl);
child1 = pref.node("mock1");
            nl.waitForEvent(1);
assertEquals(1, nl.getAdded());
nl.reset();
pref.node("mock1");
            // There shouldn't be an event, but wait in case one arrives...
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
assertEquals(0, nl.getAdded());
nl.reset();
} finally {
//Synthetic comment -- @@ -653,7 +649,7 @@
pref.addNodeChangeListener(nl);
pref.addNodeChangeListener(nl);
child1 = pref.node("mock2");
            nl.waitForEvent(2);
assertEquals(2, nl.getAdded());
nl.reset();
} finally {
//Synthetic comment -- @@ -667,7 +663,7 @@
pref.addNodeChangeListener(nl);
child1 = pref.node("mock3");
child1.removeNode();
            nl.waitForEvent(2);
assertEquals(1, nl.getRemoved());
nl.reset();
} finally {
//Synthetic comment -- @@ -680,7 +676,7 @@
pref.addNodeChangeListener(nl);
child1 = pref.node("mock6");
child1.removeNode();
            nl.waitForEvent(4);
assertEquals(2, nl.getRemoved());
nl.reset();
} finally {
//Synthetic comment -- @@ -695,27 +691,29 @@
child1 = pref.node("mock4");
child1.addNodeChangeListener(nl);
pref.node("mock4/mock5");
            nl.waitForEvent(1);
assertEquals(1, nl.getAdded());
nl.reset();
child3 = pref.node("mock4/mock5/mock6");
            // There shouldn't be an event, but wait in case one arrives...
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
assertEquals(0, nl.getAdded());
nl.reset();

child3.removeNode();
            // There shouldn't be an event, but wait in case one arrives...
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
assertEquals(0, nl.getRemoved());
nl.reset();

pref.node("mock4/mock7");
            nl.waitForEvent(1);
assertEquals(1, nl.getAdded());
nl.reset();

child1.removeNode();
            nl.waitForEvent(2);
            assertEquals(2, nl.getRemoved());
nl.reset();
} finally {
try {
//Synthetic comment -- @@ -749,7 +747,7 @@
try {
pref.clear();
pl.waitForEvent(2);
                assertEquals(2, pl.getChanged());
} catch(BackingStoreException bse) {
pl.reset();
fail("BackingStoreException is thrown");
//Synthetic comment -- @@ -784,7 +782,7 @@
try {
pref.clear();
pl.waitForEvent(3);
                assertEquals(3, pl.getChanged());
} catch(BackingStoreException bse) {
fail("BackingStoreException is thrown");
}
//Synthetic comment -- @@ -868,25 +866,38 @@
static class MockNodeChangeListener implements NodeChangeListener {
private int added = 0;
private int removed = 0;
        private int eventCount = 0;

        public void waitForEvent(int count) {
            synchronized (this) {
                while (eventCount < count) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
}
}

        public void childAdded(NodeChangeEvent e) {
            if (ENCOURAGE_RACES) try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
            synchronized (this) {
                ++eventCount;
                ++added;
                notifyAll();
            }
}

        public void childRemoved(NodeChangeEvent e) {
            if (ENCOURAGE_RACES) try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
            synchronized (this) {
                ++eventCount;
                ++removed;
                notifyAll();
            }
}

        public synchronized int getAdded() {
return added;
}

//Synthetic comment -- @@ -894,7 +905,8 @@
return removed;
}

        public synchronized void reset() {
            eventCount = 0;
added = 0;
removed = 0;
}
//Synthetic comment -- @@ -902,47 +914,35 @@

private static class MockPreferenceChangeListener implements PreferenceChangeListener {
private int changed = 0;
        private int eventCount = 0;

public void waitForEvent(int count) {
            synchronized (this) {
                while (eventCount < count) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
}
}
}
}

        public void preferenceChange(PreferenceChangeEvent pce) {
            if (ENCOURAGE_RACES) try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
            synchronized (this) {
                ++eventCount;
                ++changed;
                notifyAll();
            }
}

        public synchronized int getChanged() {
return changed;
}

        public synchronized void reset() {
            eventCount = 0;
changed = 0;
}
}








