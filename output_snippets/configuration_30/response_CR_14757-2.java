//<Beginning of snippet n. 0>
public class MyProgressDialogFragment extends DialogFragment {
    private static final int MAX_PROGRESS = 100;
    private ProgressBar progressBar;
    private Button hideButton;
    private Button cancelButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress_dialog, null);
        
        progressBar = dialogView.findViewById(R.id.progressBar);
        progressBar.setMax(MAX_PROGRESS);
        
        builder.setView(dialogView)
               .setTitle(R.string.select_dialog);
        
        AlertDialog dialog = builder.create();

        hideButton = dialogView.findViewById(R.id.hide_button);
        hideButton.setText(R.string.alert_dialog_hide);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked hide
                dialog.dismiss();
            }
        });

        cancelButton = dialogView.findViewById(R.id.cancel_button);
        cancelButton.setText(R.string.alert_dialog_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked cancel
                dialog.dismiss();
            }
        });
        
        return dialog;
    }

    // Method to update progress
    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
    }
}
//<End of snippet n. 0>