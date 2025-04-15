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
     * <li> A search, either by filename/line number, or for fqmn might provide only 1 result.
     *    In such a case, just open that result. Give preference to the file name/line # search
     *    since that is the most accurate (gets to the line number). </li>
* <li> The search might not provide any results. e.g, the method name may be of the form
*    "com.x.y$1.methodName". Searches for methods within anonymous classes will fail. In
*    such a case, if the fileName:lineNumber argument is available, a search for that
//Synthetic comment -- @@ -91,18 +93,37 @@
*/
@Override
public boolean revealMethod(String fqmn, String fileName, int lineNumber, String perspective) {
        // Search by filename:linenumber. If there is just one result for it, that would
        // be the correct match that is accurate to the line
        List<SearchMatch> fileMatches = Collections.emptyList();
        if (fileName != null && lineNumber >= 0) {
            fileMatches = searchForFile(fileName);
            if (fileMatches.size() == 1) {
                return revealLineMatch(fileMatches, fileName, lineNumber, perspective);
            }
        }

        List<SearchMatch> methodMatches = searchForMethod(fqmn);

        // if there is a unique method name match:
        //    1. if there are > 1 file name matches, try to see if they can be narrowed down
        //    2. if not, display the method match
        if (methodMatches.size() == 1) {
            if (fileMatches.size() > 0) {
                List<SearchMatch> filteredMatches = filterMatchByResource(fileMatches,
                                                        methodMatches.get(0).getResource());
                if (filteredMatches.size() == 1) {
                    return revealLineMatch(filteredMatches, fileName, lineNumber, perspective);
                }
            }

            return displayMethod((IMethod) methodMatches.get(0).getElement(), perspective);
}

// no matches for search by method, so search by filename
        if (methodMatches.size() == 0) {
            if (fileMatches.size() > 0) {
                return revealLineMatch(fileMatches, fileName, lineNumber, perspective);
} else {
return false;
}
//Synthetic comment -- @@ -111,12 +132,12 @@
// multiple matches for search by method, narrow down by filename
if (fileName != null) {
return revealLineMatch(
                    filterMatchByFileName(methodMatches, fileName),
fileName, lineNumber, perspective);
}

// prompt the user
        SearchMatch match = getMatchToDisplay(methodMatches, fqmn);
if (match == null) {
return false;
} else {
//Synthetic comment -- @@ -206,6 +227,19 @@
return filteredMatches;
}

    private List<SearchMatch> filterMatchByResource(List<SearchMatch> matches,
            IResource resource) {
        List<SearchMatch> filteredMatches = new ArrayList<SearchMatch>(matches.size());

        for (SearchMatch m: matches) {
            if (m.getResource().equals(resource)) {
                filteredMatches.add(m);
            }
        }

        return filteredMatches;
    }

private SearchMatch getMatchToDisplay(List<SearchMatch> matches, String searchTerm) {
// no matches for given search
if (matches.size() == 0) {







