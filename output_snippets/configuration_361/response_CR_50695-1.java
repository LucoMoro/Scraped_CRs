//<Beginning of snippet n. 0>
@SuppressLint("ShowToast")
public ToastTest(Context context) {
    if (context instanceof Activity || context instanceof View) {
        Toast toast = Toast.makeText(context, "foo", Toast.LENGTH_LONG);
        toast.show();
    } else {
        throw new IllegalArgumentException("Invalid context provided for Toast.");
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
ShowFinder finder = new ShowFinder(node);
method.accept(finder);
if (!finder.isShowCalled()) {
    context.report(ISSUE, method, context.getLocation(node),
    "Toast created but not shown: did you forget to call show() ?", null);
    Log.w("ToastTest", "Show method not called on toast.");
}
//<End of snippet n. 1>