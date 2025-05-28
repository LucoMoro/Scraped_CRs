//<Beginning of snippet n. 0>
public class ProgressDialogFragment extends DialogFragment {
    private static final int MAX_PROGRESS = 100;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.select_dialog);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_with_progress, null);
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        progressBar.setMax(MAX_PROGRESS);
        builder.setView(dialogView);

        builder.setPositiveButton(R.string.alert_dialog_hide, (dialog, which) -> {
            // User clicked Hide so do some stuff
        });

        builder.setNegativeButton(R.string.alert_dialog_cancel, (dialog, which) -> {
            // User clicked Cancel so do some stuff
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Implement loading logic here using Kotlin Coroutines or another method if required
    }
}
//<End of snippet n. 0>