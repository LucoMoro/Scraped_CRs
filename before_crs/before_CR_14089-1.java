/*Fix last character of password field being visible after rotation

This is a fix forhttp://code.google.com/p/android/issues/detail?id=907.Note that
that issue was declined without comment, but the bug (while incredibly minor)
does exist. This can be seen on the facebook app, as well as many third party apps.

Change-Id:I8f1449c47228f5f757a5baf389656e51c817b150*/
//Synthetic comment -- diff --git a/core/java/android/text/method/PasswordTransformationMethod.java b/core/java/android/text/method/PasswordTransformationMethod.java
//Synthetic comment -- index fad4f64..b769b76 100644

//Synthetic comment -- @@ -51,6 +51,8 @@
sp.removeSpan(vr[i]);
}

sp.setSpan(new ViewReference(view), 0, 0,
Spannable.SPAN_POINT_POINT);
}
//Synthetic comment -- @@ -100,10 +102,7 @@
int pref = TextKeyListener.getInstance().getPrefs(v.getContext());
if ((pref & TextKeyListener.SHOW_PASSWORD) != 0) {
if (count > 0) {
                    Visible[] old = sp.getSpans(0, sp.length(), Visible.class);
                    for (int i = 0; i < old.length; i++) {
                        sp.removeSpan(old[i]);
                    }

if (count == 1) {
sp.setSpan(new Visible(sp, this), start, start + count,
//Synthetic comment -- @@ -125,14 +124,18 @@
if (sourceText instanceof Spannable) {
Spannable sp = (Spannable) sourceText;

                Visible[] old = sp.getSpans(0, sp.length(), Visible.class);
                for (int i = 0; i < old.length; i++) {
                    sp.removeSpan(old[i]);
                }
}
}
}

private static class PasswordCharSequence
implements CharSequence, GetChars
{







