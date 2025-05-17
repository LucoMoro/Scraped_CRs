//<Beginning of snippet n. 0>
AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setTitle(R.string.select_dialog)
       .setMessage(R.string.loading_message)
       .setPositiveButton(R.string.alert_dialog_hide, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               /* User clicked Hide so do some stuff */
           }
       })
       .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               /* User clicked Cancel so do some stuff */
           }
       });

AlertDialog dialog = builder.create();
dialog.setCancelable(false);
dialog.show();

ProgressBar progressBar = findViewById(R.id.progress_bar);
progressBar.setMax(MAX_PROGRESS);
progressBar.setVisibility(View.VISIBLE);

// Update progress in a non-blocking way, e.g., in a background thread
//<End of snippet n. 0>