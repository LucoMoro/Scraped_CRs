//<Beginning of snippet n. 0>
AlertDialog.Builder builder = new AlertDialog.Builder(context);
builder.setTitle(R.string.select_dialog);
View dialogLayout = LayoutInflater.from(context).inflate(R.layout.progress_dialog_layout, null);
ProgressBar progressBar = dialogLayout.findViewById(R.id.progress_bar);
builder.setView(dialogLayout);

AlertDialog dialog = builder.create();
dialog.setCancelable(true);
dialog.setOnCancelListener(dialogInterface -> {
    KotlinScope.launch(Dispatchers.Main) {
        dialog.dismiss();
    }
});
dialog.show();

KotlinScope.launch(Dispatchers.Main) {
    try {
        for (int progress = 0; progress <= MAX_PROGRESS; progress++) {
            if (progress > MAX_PROGRESS) break; // Prevent invalid progress
            progressBar.setProgress(progress);
            delay(100); // Simulate work being done
        }
    } catch (Exception e) {
        Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    } finally {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
//<End of snippet n. 0>