/*Protect broken code against native crashes.

It doesn't make any sense to access Matcher or BreakIterator concurrently from
multiple threads, but we shouldn't crash in native code.

Bug:http://code.google.com/p/android/issues/detail?id=41143Change-Id:I5d0ed97be50ffb7c6cc281ac6293cf82f17e7b80*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Matcher.java b/luni/src/main/java/java/util/regex/Matcher.java
//Synthetic comment -- index 6ac32eb..162fe53 100644

//Synthetic comment -- @@ -28,7 +28,8 @@
private Pattern pattern;

/**
     * The address of the native peer.
     * Uses of this must be manually synchronized to avoid native crashes.
*/
private int address;

//Synthetic comment -- @@ -247,9 +248,11 @@
}

private void resetForInput() {
        synchronized (this) {
            setInputImpl(address, input, regionStart, regionEnd);
            useAnchoringBoundsImpl(address, anchoringBounds);
            useTransparentBoundsImpl(address, transparentBounds);
        }
}

/**
//Synthetic comment -- @@ -383,7 +386,9 @@
throw new IndexOutOfBoundsException("start=" + start + "; length=" + input.length());
}

        synchronized (this) {
            matchFound = findImpl(address, input, start, matchOffsets);
        }
return matchFound;
}

//Synthetic comment -- @@ -396,7 +401,9 @@
* @return true if (and only if) a match has been found.
*/
public boolean find() {
        synchronized (this) {
            matchFound = findNextImpl(address, input, matchOffsets);
        }
return matchFound;
}

//Synthetic comment -- @@ -408,7 +415,9 @@
* @return true if (and only if) the {@code Pattern} matches.
*/
public boolean lookingAt() {
        synchronized (this) {
            matchFound = lookingAtImpl(address, input, matchOffsets);
        }
return matchFound;
}

//Synthetic comment -- @@ -420,7 +429,9 @@
*         region.
*/
public boolean matches() {
        synchronized (this) {
            matchFound = matchesImpl(address, input, matchOffsets);
        }
return matchFound;
}

//Synthetic comment -- @@ -496,7 +507,9 @@
* @return the number of groups.
*/
public int groupCount() {
        synchronized (this) {
            return groupCountImpl(address);
        }
}

/**
//Synthetic comment -- @@ -536,8 +549,10 @@
* @return the {@code Matcher} itself.
*/
public Matcher useAnchoringBounds(boolean value) {
        synchronized (this) {
            anchoringBounds = value;
            useAnchoringBoundsImpl(address, value);
        }
return this;
}

//Synthetic comment -- @@ -564,8 +579,10 @@
* @return the {@code Matcher} itself.
*/
public Matcher useTransparentBounds(boolean value) {
        synchronized (this) {
            transparentBounds = value;
            useTransparentBoundsImpl(address, value);
        }
return this;
}

//Synthetic comment -- @@ -595,43 +612,38 @@
}

/**
     * Returns this matcher's region start, that is, the index of the first character that is
* considered for a match.
*/
public int regionStart() {
return regionStart;
}

/**
     * Returns this matcher's region end, that is, the index of the first character that is
* not considered for a match.
*/
public int regionEnd() {
return regionEnd;
}

/**
     * Returns true if and only if more input might change a successful match into an
* unsuccessful one.
*/
public boolean requireEnd() {
        synchronized (this) {
            return requireEndImpl(address);
        }
}

/**
     * Returns true if and only if the last match hit the end of the input.
*/
public boolean hitEnd() {
        synchronized (this) {
            return hitEndImpl(address);
        }
}

@Override protected void finalize() throws Throwable {








//Synthetic comment -- diff --git a/luni/src/main/java/libcore/icu/NativeBreakIterator.java b/luni/src/main/java/libcore/icu/NativeBreakIterator.java
//Synthetic comment -- index 32bb647..4156f9a 100644

//Synthetic comment -- @@ -27,7 +27,10 @@
private static final int BI_LINE_INSTANCE = 3;
private static final int BI_SENT_INSTANCE = 4;

    // The address of the native peer.
    // Uses of this must be manually synchronized to avoid native crashes.
private final int address;

private final int type;
private String string;
private CharacterIterator charIterator;
//Synthetic comment -- @@ -158,17 +161,17 @@
private static native int getWordInstanceImpl(String locale);
private static native int getLineInstanceImpl(String locale);
private static native int getSentenceInstanceImpl(String locale);
    private static synchronized native int cloneImpl(int address);

    private static synchronized native void closeImpl(int address);

    private static synchronized native void setTextImpl(int address, String text);
    private static synchronized native int precedingImpl(int address, String text, int offset);
    private static synchronized native boolean isBoundaryImpl(int address, String text, int offset);
    private static synchronized native int nextImpl(int address, String text, int n);
    private static synchronized native int previousImpl(int address, String text);
    private static synchronized native int currentImpl(int address, String text);
    private static synchronized native int firstImpl(int address, String text);
    private static synchronized native int followingImpl(int address, String text, int offset);
    private static synchronized native int lastImpl(int address, String text);
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/text/BreakIteratorTest.java b/luni/src/test/java/libcore/java/text/BreakIteratorTest.java
//Synthetic comment -- index 45bf9b9..47701c8 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package libcore.java.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

public class BreakIteratorTest extends junit.framework.TestCase {
//Synthetic comment -- @@ -172,4 +173,31 @@
}
}

    // http://code.google.com/p/android/issues/detail?id=41143
    // This code is inherently unsafe and crazy;
    // we're just trying to provoke native crashes!
    public void testConcurrentBreakIteratorAccess() throws Exception {
        final BreakIterator it = BreakIterator.getCharacterInstance();

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; ++i) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 4096; ++i) {
                        it.setText("some example text");
                        for (int index = it.first(); index != BreakIterator.DONE; index = it.next()) {
                        }
                    }
                }
            });
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
}








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java b/luni/src/test/java/libcore/java/util/regex/OldMatcherTest.java
//Synthetic comment -- index 07d99e9..bd6109b 100644

//Synthetic comment -- @@ -17,6 +17,7 @@

package libcore.java.util.regex;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
//Synthetic comment -- @@ -606,4 +607,40 @@
assertTrue("\u00ea".matches("\\w")); // LATIN SMALL LETTER E WITH CIRCUMFLEX
assertFalse("\u00ea".matches("\\W")); // LATIN SMALL LETTER E WITH CIRCUMFLEX
}

    // http://code.google.com/p/android/issues/detail?id=41143
    public void testConcurrentMatcherAccess() throws Exception {
        final Pattern p = Pattern.compile("(^|\\W)([a-z])");
        final Matcher m = p.matcher("");

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; ++i) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 4096; ++i) {
                        String s = "some example text";
                        m.reset(s);
                        try {
                            StringBuffer sb = new StringBuffer(s.length());
                            while (m.find()) {
                                m.appendReplacement(sb, m.group(1) + m.group(2));
                            }
                            m.appendTail(sb);
                        } catch (Exception expected) {
                            // This code is inherently unsafe and crazy;
                            // we're just trying to provoke native crashes!
                        }
                    }
                }
            });
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
}







