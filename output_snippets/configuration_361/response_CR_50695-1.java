//<Beginning of snippet n. 0>
public class ToastTest {
    private Toast toast;

    public ToastTest(Context context) {
        if (context != null) {
            toast = Toast.makeText(context, "foo", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
ShowFinder finder = new ShowFinder(node);
method.accept(finder);
if (!finder.isShowCalled()) {
    context.report(ISSUE, method, context.getLocation(node),
    "Toast created but not shown: did you forget to call show() ?", null);
}
//<End of snippet n. 1>