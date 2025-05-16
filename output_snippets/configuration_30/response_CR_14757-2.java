//<Beginning of snippet n. 0>
public class CustomProgressDialogFragment extends DialogFragment {
    private ProgressBar progressBar;
    private Button hideButton;
    private Button cancelButton;
    private static final int MAX_PROGRESS = 100;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_progress, null);
        
        progressBar = dialogView.findViewById(R.id.progressBar);
        hideButton = dialogView.findViewById(R.id.hideButton);
        cancelButton = dialogView.findViewById(R.id.cancelButton);

        builder.setView(dialogView)
                .setTitle(R.string.select_dialog);

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked hide button, handle action
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User clicked cancel button, handle action
                dismiss();
            }
        });

        progressBar.setMax(MAX_PROGRESS);
        return builder.create();
    }

    public void updateProgress(int progress) {
        if(progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
//<End of snippet n. 0>