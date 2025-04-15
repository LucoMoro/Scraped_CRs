/*Improve location accuracy of stack trace to source mapping.

If the file name & line number is available from the stack trace,
preference should be given to that as it provides accuracy to the
exact line. If multiple results are obtained for the file name,
then that list can be narrowed down using a method name search.

Change-Id:If71bc23d4c1127f2e0150396741369409e5a8f08*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/SourceRevealer.java
//Synthetic comment -- index 1b9b455..91df3e9 100644

//Synthetic comment -- @@ -73,7 +73,9 @@
* Reveal the source for given fully qualified method name.<br>
*
* The method should take care of the following scenarios:<ol>
     * <li> A search for fqmn might provide only 1 result. In such a case, just open that result. </li>
* <li> The search might not provide any results. e.g, the method name may be of the form
*    "com.x.y$1.methodName". Searches for methods within anonymous classes will fail. In
*    such a case, if the fileName:lineNumber argument is available, a search for that
//Synthetic comment -- @@ -91,18 +93,37 @@
*/
@Override
public boolean revealMethod(String fqmn, String fileName, int lineNumber, String perspective) {
        List<SearchMatch> matches = searchForMethod(fqmn);

        // display the unique match
        if (matches.size() == 1) {
            return displayMethod((IMethod) matches.get(0).getElement(), perspective);
}

// no matches for search by method, so search by filename
        if (matches.size() == 0) {
            if (fileName != null) {
                return revealLineMatch(searchForFile(fileName),
                    fileName, lineNumber, perspective);
} else {
return false;
}
//Synthetic comment -- @@ -111,12 +132,12 @@
// multiple matches for search by method, narrow down by filename
if (fileName != null) {
return revealLineMatch(
                    filterMatchByFileName(matches, fileName),
fileName, lineNumber, perspective);
}

// prompt the user
        SearchMatch match = getMatchToDisplay(matches, fqmn);
if (match == null) {
return false;
} else {
//Synthetic comment -- @@ -206,6 +227,19 @@
return filteredMatches;
}

private SearchMatch getMatchToDisplay(List<SearchMatch> matches, String searchTerm) {
// no matches for given search
if (matches.size() == 0) {







