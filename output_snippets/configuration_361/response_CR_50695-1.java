//<Beginning of snippet n. 0>
@SuppressLint("ShowToast")
public ToastTest(Context context) {
    if (context == null) {
        throw new IllegalArgumentException("Context cannot be null.");
    }
    if (!(context instanceof Activity) && !(context instanceof View)) {
        throw new IllegalArgumentException("Invalid context provided for Toast.");
    }
    Toast toast = Toast.makeText(context, "foo", Toast.LENGTH_LONG);
    toast.show();
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
ShowFinder finder = new ShowFinder(node);
method.accept(finder);
if (!finder.isShowCalled()) {
    context.report(ISSUE, method, context.getLocation(node),
    "Toast created but not shown: did you forget to call show() ?", null);
    Log.w("ToastTest", "Show method not called on toast.");
    Toast.makeText(context, "Toast created but not shown", Toast.LENGTH_SHORT).show();
}
//<End of snippet n. 1>