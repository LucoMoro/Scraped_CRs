//<Beginning of snippet n. 0>

AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle(R.string.select_dialog);

LayoutInflater inflater = LayoutInflater.from(context);
View dialogView = inflater.inflate(R.layout.custom_progress_dialog, null);
ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
progressBar.setMax(MAX_PROGRESS);
builder.setView(dialogView);

builder.setPositiveButton(R.string.alert_dialog_hide, new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int whichButton) {
        /* User clicked Yes so do some stuff */
    }
});

builder.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int whichButton) {
        /* User clicked No so do some stuff */
    }
});

AlertDialog dialog = builder.create();
dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
    @Override
    public void onCancel(DialogInterface dialog) {
        // Handle dialog cancellation if necessary
    }
});

// Show or manage the dialog lifecycle appropriately
dialog.show();

//<End of snippet n. 0>