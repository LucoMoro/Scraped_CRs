/*Fix error in include cycle detection

The code to detect cycles in include dependencies was wrong; it would
incorrectly identify some valid DAGs as having a cycle. We don't
necessarily have a cycle just because we encounter a node we've
already seen; it is only a cycle if we encounter a vertex that we are
currently visiting further back in the depth-search.

Change-Id:I3149c80d54258e6fff4cb0a0b1a3cefcb1db56f2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 888f674..7833e67 100644

//Synthetic comment -- @@ -713,8 +713,8 @@
// Perform DFS on the include graph and look for a cycle; if we find one, produce
// a chain of includes on the way back to show to the user
if (mIncludes.size() > 0) {
            Set<String> seen = new HashSet<String>(mIncludes.size());
            String chain = dfs(from, seen);
if (chain != null) {
addError(from, chain);
} else {
//Synthetic comment -- @@ -727,22 +727,24 @@
/** Format to chain include cycles in: a=>b=>c=>d etc */
private final String CHAIN_FORMAT = "%1$s=>%2$s"; //$NON-NLS-1$

    private String dfs(String from, Set<String> seen) {
        seen.add(from);

List<String> includes = mIncludes.get(from);
if (includes != null && includes.size() > 0) {
for (String include : includes) {
                if (seen.contains(include)) {
return String.format(CHAIN_FORMAT, from, include);
}
                String chain = dfs(include, seen);
if (chain != null) {
return String.format(CHAIN_FORMAT, from, chain);
}
}
}

return null;
}








