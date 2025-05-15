//<Beginning of snippet n. 0>
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Window winP = getWindow();
    winP.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
    winP.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    winP.setGravity(Gravity.CENTER);
    // Changed window type to TYPE_SYSTEM_DIALOG to resolve graphical issues in Sim unlock menu.
}
//<End of snippet n. 0>