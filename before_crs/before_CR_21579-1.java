/*Make XML code completion case insensitive

If you've typed "android:textsi" on a TextView, you currently get no
matches. This changeset makes the code completion case insensitive
(the way it is in Java) such that it for example will match
"android:textSize".

Change-Id:I0f698c2a4ee983d6c0fbcce272b851b2710c9a61*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssist.java
//Synthetic comment -- index e1d030f..ad4599d 100644

//Synthetic comment -- @@ -476,9 +476,9 @@

String nsKeyword = nsPrefix == null ? keyword : (nsPrefix + keyword);

            if (keyword.startsWith(wordPrefix) ||
                    (nsPrefix != null && keyword.startsWith(nsPrefix)) ||
                    (nsPrefix != null && nsKeyword.startsWith(wordPrefix))) {
if (nsPrefix != null) {
keyword = nsPrefix + keyword;
}
//Synthetic comment -- @@ -535,6 +535,31 @@
}

/**
* Indicates whether this descriptor describes an element that can potentially
* have children (either sub-elements or text value). If an element can have children,
* we want to explicitly write an opening and a separate closing tag.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/editors/AndroidContentAssistTest.java
new file mode 100644
//Synthetic comment -- index 0000000..8151f3a

//Synthetic comment -- @@ -0,0 +1,33 @@







