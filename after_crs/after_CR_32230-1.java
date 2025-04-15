/*Remove PKCS12_PASSWORD_DIALOG in order to correctly recreate it on onCreateDialog

If an empty password is supplied for certificate password dialog
showDialog is called without removing it. As a result onCreateDialog is
not called leaving the dialog without visible UI to the user.

Change-Id:I83ffd680313eb9c69214113d3091a70910a80ae2Signed-off-by: Shuhrat Dehkanov <uzbmaster@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/certinstaller/CertInstaller.java b/src/com/android/certinstaller/CertInstaller.java
//Synthetic comment -- index d37cd72..34cc764 100644

//Synthetic comment -- @@ -317,6 +317,10 @@
private Dialog createPkcs12PasswordDialog() {
View view = View.inflate(this, R.layout.password_dialog, null);
mView.setView(view);
        if (mView.getEmptyPasswordError()) {
            mView.showError(R.string.password_empty_error);
            mView.setEmptyPasswordError(false);
        }

String title = mCredentials.getName();
title = TextUtils.isEmpty(title)
//Synthetic comment -- @@ -329,7 +333,8 @@
public void onClick(DialogInterface dialog, int id) {
String password = mView.getText(R.id.credential_password);
if (TextUtils.isEmpty(password)) {
                            mView.setEmptyPasswordError(true);
                            removeDialog(PKCS12_PASSWORD_DIALOG);
showDialog(PKCS12_PASSWORD_DIALOG);
} else {
mNextAction = new Pkcs12ExtractAction(password);








//Synthetic comment -- diff --git a/src/com/android/certinstaller/ViewHelper.java b/src/com/android/certinstaller/ViewHelper.java
//Synthetic comment -- index 81ff628..812e35a 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
*/
class ViewHelper {
private View mView;
    private boolean mEmptyPasswordError;

void setView(View view) {
mView = view;
//Synthetic comment -- @@ -49,4 +50,12 @@
TextView v = (TextView) mView.findViewById(viewId);
if (v != null) v.setText(textId);
}

    void setEmptyPasswordError(boolean emptyPassword) {
        mEmptyPasswordError = emptyPassword;
    }

    boolean getEmptyPasswordError() {
        return mEmptyPasswordError;
    }
}







