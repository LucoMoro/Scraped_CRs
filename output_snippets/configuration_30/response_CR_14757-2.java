//<Beginning of snippet n. 0>
AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle(R.string.select_dialog);
View dialogLayout = LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null);
ProgressBar progressBar = dialogLayout.findViewById(R.id.progress_bar);
builder.setView(dialogLayout);

AlertDialog dialog = builder.create();
dialog.setCancelable(false);
dialog.show();

KotlinScope.launch(Dispatchers.Main) {
    try {
        for (int progress = 0; progress <= MAX_PROGRESS; progress++) {
            progressBar.setProgress(progress);
            delay(100); // Simulate work being done
        }
    } catch (Exception e) {
        // Handle exception if needed
    } finally {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
//<End of snippet n. 0>