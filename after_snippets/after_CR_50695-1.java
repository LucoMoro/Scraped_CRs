
//<Beginning of snippet n. 0>


public ToastTest(Context context) {
Toast.makeText(context, "foo", Toast.LENGTH_LONG);
}

    @android.annotation.SuppressLint("ShowToast")
    private void checkSuppress1(Context context) {
        Toast toast = Toast.makeText(this, "MyToast", Toast.LENGTH_LONG);
    }

    private void checkSuppress2(Context context) {
        @android.annotation.SuppressLint("ShowToast")
        Toast toast = Toast.makeText(this, "MyToast", Toast.LENGTH_LONG);
    }
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


ShowFinder finder = new ShowFinder(node);
method.accept(finder);
if (!finder.isShowCalled()) {
            context.report(ISSUE, node, context.getLocation(node),
"Toast created but not shown: did you forget to call show() ?", null);
}
}

//<End of snippet n. 1>








