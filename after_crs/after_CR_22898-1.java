/*Escape Strings extracted with the Extract String refactoring

Escape any single or double quotes inside the string
value definition in strings.xml:
  This'll work   =>  "This'll work"
  Escape '"      =>  Escape \'\"

Change-Id:I21cb506e10e837feb0e435a21cb50aaa5342f0fa*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 955a0b2..dce8160 100644

//Synthetic comment -- @@ -1202,6 +1202,9 @@

IStructuredModel smodel = null;

        // Single and double quotes must be escaped in the <string>value</string> declaration
        tokenString = escapeString(tokenString);

try {
IStructuredDocument sdoc = null;
boolean checkTopElement = true;
//Synthetic comment -- @@ -1432,6 +1435,34 @@
}

/**
     * Escape a string value to be placed in a string resource file such that it complies with
     * the escaping rules described here:
     *   http://developer.android.com/guide/topics/resources/string-resource.html
     * This method assumes that the String is not escaped already.
     */
    private static String escapeString(String s) {
        if ((s.indexOf('"') != -1)) {
            // Must escape each quote or apostrophe.
            // (We cannot escape the string by surrounding it with apostrophes so we don't
            // need to distinguish between containsQuotes and contains both.)
            StringBuilder sb = new StringBuilder(s.length() + 2);
            for (int i = 0, n = s.length(); i < n; i++) {
                char c = s.charAt(i);
                if (c == '\'' || c == '"') {
                    sb.append('\\');
                }
                sb.append(c);
            }
            return sb.toString();
        } else if ((s.indexOf('\'') != -1)) {
            // Surround string with quotes
            return '"' + s + '"';
        } else {
            return s;
        }
    }

    /**
* Computes the changes to be made to the source Android XML file and
* returns a list of {@link Change}.
* <p/>







