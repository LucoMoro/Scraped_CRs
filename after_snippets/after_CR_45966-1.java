
//<Beginning of snippet n. 0>


sb.append(context.getText(
com.android.internal.R.string.passwordIncorrect));
}
                } else if (err == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                    if (sc.equals(SC_PIN)) {
                        sb.append(context.getText(
                            com.android.internal.R.string.enablePin));
                    }
} else {
sb.append(context.getText(
com.android.internal.R.string.mmiError));

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


sb.append("\n");
sb.append(context.getText(
com.android.internal.R.string.needPuk2));
                } else if (err == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                    if (sc.equals(SC_PIN)) {
                        sb.append(context.getText(
                            com.android.internal.R.string.enablePin));
                    }
} else if (err == CommandException.Error.FDN_CHECK_FAILURE) {
Log.i(LOG_TAG, "FDN_CHECK_FAILURE");
sb.append(context.getText(com.android.internal.R.string.mmiFdnError));

//<End of snippet n. 1>








