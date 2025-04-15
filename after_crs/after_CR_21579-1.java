/*Make XML code completion case insensitive

If you've typed "android:textsi" on a TextView, you currently get no
matches. This changeset makes the code completion case insensitive
(the way it is in Java) such that it for example will match
"android:textSize".

Change-Id:I0f698c2a4ee983d6c0fbcce272b851b2710c9a61*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index e1d030f..ad4599d 100644

//Synthetic comment -- @@ -476,9 +476,9 @@

String nsKeyword = nsPrefix == null ? keyword : (nsPrefix + keyword);

            if (startsWith(keyword, wordPrefix) ||
                    (nsPrefix != null && startsWith(keyword, nsPrefix)) ||
                    (nsPrefix != null && startsWith(nsKeyword, wordPrefix))) {
if (nsPrefix != null) {
keyword = nsPrefix + keyword;
}
//Synthetic comment -- @@ -535,6 +535,31 @@
}

/**
     * Returns true if the given word starts with the given prefix. The comparison is not
     * case sensitive.
     *
     * @param word the word to test
     * @param prefix the prefix the word should start with
     * @return true if the given word starts with the given prefix
     */
    static boolean startsWith(String word, String prefix) {
        int prefixLength = prefix.length();
        int wordLength = word.length();
        if (wordLength < prefixLength) {
            return false;
        }

        for (int i = 0; i < prefixLength; i++) {
            if (Character.toLowerCase(prefix.charAt(i))
                    != Character.toLowerCase(word.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
* Indicates whether this descriptor describes an element that can potentially
* have children (either sub-elements or text value). If an element can have children,
* we want to explicitly write an opening and a separate closing tag.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8151f3a

//Synthetic comment -- @@ -0,0 +1,33 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.android.ide.eclipse.adt.internal.editors;

import junit.framework.TestCase;

public class AndroidContentAssistTest extends TestCase {
    public void testStartsWith() {
        assertTrue(AndroidContentAssist.startsWith("", ""));
        assertTrue(AndroidContentAssist.startsWith("a", ""));
        assertTrue(AndroidContentAssist.startsWith("A", ""));
        assertTrue(AndroidContentAssist.startsWith("A", "a"));
        assertTrue(AndroidContentAssist.startsWith("A", "A"));
        assertTrue(AndroidContentAssist.startsWith("Ab", "a"));
        assertTrue(AndroidContentAssist.startsWith("ab", "A"));
        assertTrue(AndroidContentAssist.startsWith("ab", "AB"));
        assertFalse(AndroidContentAssist.startsWith("ab", "ABc"));
        assertFalse(AndroidContentAssist.startsWith("", "ABc"));
    }
}







