//<Beginning of snippet n. 0>
if (args.size() == 2) {
    // Handle case with 2 arguments
} else if (args.size() == 3) {
    // Handle case with 3 arguments
} else {
    context.report(ISSUE, context.getLocation(call), "Unexpected number of arguments", null);
}
//<End of snippet n. 0>