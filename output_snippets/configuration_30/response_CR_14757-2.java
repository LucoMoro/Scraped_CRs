//<Beginning of snippet n. 0>
AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle(R.string.select_dialog);

LayoutInflater inflater = getLayoutInflater();
View dialogView = inflater.inflate(R.layout.custom_progress_dialog, null);
ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
progressBar.setMax(MAX_PROGRESS);
builder.setView(dialogView);

builder.setPositiveButton(getText(R.string.alert_dialog_hide), new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
        /* User clicked Hide so do some stuff */
    }
});
builder.setNegativeButton(getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
        /* User clicked Cancel so do some stuff */
    }
});

AlertDialog dialog = builder.create();
dialog.show();
//<End of snippet n. 0>