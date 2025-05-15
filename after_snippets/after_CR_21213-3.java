
//<Beginning of snippet n. 0>


if (name != null) {
String newValue;
if (combinePackage) {
                newValue = AndroidManifest.extractActivityName(newName, getAppPackage());
} else {
newValue = newName;
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


private static boolean sRefactorAppPackage = false;

/**
* Releases SSE read model; saves SSE model if exists edit model
* Called in dispose method of refactoring change classes
*

//<End of snippet n. 1>








