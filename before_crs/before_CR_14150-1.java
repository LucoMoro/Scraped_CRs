/*Remove internal.R References from AlertDialogBuilderTest

Use methods on AlertDialog to get the buttons and the list view.
Unfortunately delete the tests that use internal R resources that
can't be replaced with any public calls like to get the resulting
dialog's title...

Change-Id:I98dfe298202802b162036a1ae6170efeff9e325f*/
//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/AlertDialog_BuilderTest.java b/tests/tests/app/src/android/app/cts/AlertDialog_BuilderTest.java
//Synthetic comment -- index 84c64a4..a400821 100644

//Synthetic comment -- @@ -40,10 +40,8 @@
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.internal.R;
//Synthetic comment -- @@ -58,12 +56,8 @@
private Context mContext;
private Instrumentation mInstrumentation;
private final CharSequence mTitle = "title";
    private final CharSequence mMessage = "message";
    private TextView mTitleView;
    private TextView mMessageView;
private Drawable mDrawable;
private AlertDialog mDialog;
    private LinearLayout mLinearLayout;
private Button mButton;
private boolean mResult;
private boolean mItemSelected;
//Synthetic comment -- @@ -119,14 +113,11 @@
super.setUp();
mBuilder = null;
mInstrumentation = getInstrumentation();
        mContext = (Context)getActivity();
        mTitleView = null;
        mMessageView = null;
mButton = null;
mView = null;
mListView = null;
mDialog = null;
        mLinearLayout = null;
mItemSelected = false;
mSelectedItem = null;
mSelectedItems = new ArrayList<Integer>();
//Synthetic comment -- @@ -138,30 +129,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        method = "setCustomTitle",
        args = {View.class}
    )
    public void testSetCustomTitle() throws Throwable {
        final String expectecTitle = "test";
        final TextView view = new TextView(mContext);
        view.setText(expectecTitle);
        runTestOnUiThread(new Runnable() {
            public void run() {
                mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setCustomTitle(view);
                mDialog = mBuilder.show();
            }
        });
        mInstrumentation.waitForIdleSync();
        final LinearLayout topPanel = (LinearLayout)mDialog.getWindow().findViewById(
                com.android.internal.R.id.topPanel);
        final TextView atv = (TextView)topPanel.getChildAt(2);
        final String title = (String)atv.getText();
        assertEquals(expectecTitle, title);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
method = "AlertDialog.Builder",
args = {Context.class}
)
//Synthetic comment -- @@ -171,78 +138,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
        method = "setTitle",
        args = {int.class}
    )
    public void testSetTitleWithParamInt() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle(R.string.am);
                mDialog = mBuilder.show();
                mTitleView = (TextView)mDialog.getWindow().findViewById(R.id.alertTitle);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(mTitleView.getText(), mContext.getText(R.string.am));
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setTitle",
        args = {CharSequence.class}
    )
    public void testSetTitleWithParamCharSequence() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setTitle(mTitle);
                mDialog = mBuilder.show();
                mTitleView = (TextView)mDialog.getWindow().findViewById(R.id.alertTitle);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(mTitleView.getText(), mTitle);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setMessage",
        args = {int.class}
    )
    public void testSetMessageWithParamInt() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setMessage(R.string.am);
                mDialog = mBuilder.show();
                mMessageView = (TextView)mDialog.getWindow().findViewById(R.id.message);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(mMessageView.getText(), mContext.getText(R.string.am));
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "setMessage",
        args = {CharSequence.class}
    )
    public void testSetMessageWithParamCharSequence() throws Throwable {
        runTestOnUiThread(new Runnable() {
            public void run() {
                mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setMessage(mMessage);
                mDialog = mBuilder.show();
                mMessageView = (TextView)mDialog.getWindow().findViewById(R.id.message);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals(mMessageView.getText(), mMessage);
    }

    @TestTargetNew(
        level = TestLevel.COMPLETE,
method = "setIcon",
args = {int.class}
)
//Synthetic comment -- @@ -284,15 +179,15 @@
runTestOnUiThread(new Runnable() {
public void run() {
mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setPositiveButton(R.string.year, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button1);
mButton.performClick();
}
});
mInstrumentation.waitForIdleSync();

        assertEquals(mContext.getText(R.string.year), mButton.getText());
assertTrue(mResult);
}

//Synthetic comment -- @@ -305,14 +200,14 @@
runTestOnUiThread(new Runnable() {
public void run() {
mBuilder = new AlertDialog.Builder(mContext);
                mBuilder.setPositiveButton(R.string.year, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button1);
mButton.performClick();
}
});
mInstrumentation.waitForIdleSync();
        assertEquals(mContext.getText(R.string.year), mButton.getText());
assertTrue(mResult);
}

//Synthetic comment -- @@ -327,7 +222,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setNegativeButton(mTitle, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button2);
mButton.performClick();
}
});
//Synthetic comment -- @@ -347,7 +242,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setNegativeButton(com.android.cts.stub.R.string.notify, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button2);
mButton.performClick();
}
});
//Synthetic comment -- @@ -367,7 +262,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setNeutralButton(com.android.cts.stub.R.string.notify, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button3);
mButton.performClick();
}
});
//Synthetic comment -- @@ -387,7 +282,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setNeutralButton(mTitle, mOnClickListener);
mDialog = mBuilder.show();
                mButton = (Button)mDialog.getWindow().findViewById(R.id.button3);
mButton.performClick();
}
});
//Synthetic comment -- @@ -476,8 +371,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setItems(com.android.cts.stub.R.array.difficultyLevel, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
}
});
mInstrumentation.waitForIdleSync();
//Synthetic comment -- @@ -501,8 +395,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setItems(expect, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
}
});
mInstrumentation.waitForIdleSync();
//Synthetic comment -- @@ -521,8 +414,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setAdapter(adapter, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
}
});
mInstrumentation.waitForIdleSync();
//Synthetic comment -- @@ -544,8 +436,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setCursor(c, mOnClickListener, People.NAME);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mListView.performItemClick(null, 0, 0);
}
});
//Synthetic comment -- @@ -571,8 +462,7 @@
mBuilder.setMultiChoiceItems(com.android.cts.stub.R.array.difficultyLevel, null,
mOnMultiChoiceClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mSelectedItem = (CharSequence)mListView.getSelectedItem();
mListView.performItemClick(null, 0, 0);
mListView.performItemClick(null, 1, 0);
//Synthetic comment -- @@ -599,8 +489,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setMultiChoiceItems(items, null, mOnMultiChoiceClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mSelectedItem = (CharSequence)mListView.getSelectedItem();
mListView.performItemClick(null, 0, 0);
mListView.performItemClick(null, 1, 0);
//Synthetic comment -- @@ -629,8 +518,7 @@
mBuilder.setMultiChoiceItems(c, People.NAME, People.NAME,
mOnMultiChoiceClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mListView.performItemClick(null, 0, 0);
mListView.performItemClick(null, 1, 0);
}
//Synthetic comment -- @@ -657,8 +545,7 @@
mBuilder.setSingleChoiceItems(com.android.cts.stub.R.array.difficultyLevel, 0,
mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mSelectedItem = (CharSequence)mListView.getSelectedItem();
mListView.performItemClick(null, 0, 0);
}
//Synthetic comment -- @@ -696,8 +583,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setSingleChoiceItems(c, 0, People.NAME, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mListView.performItemClick(null, 0, 0);
}
});
//Synthetic comment -- @@ -721,8 +607,7 @@
mBuilder = new AlertDialog.Builder(mContext);
mBuilder.setSingleChoiceItems(items, 0, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mSelectedItem = (CharSequence)mListView.getSelectedItem();
mListView.performItemClick(null, 0, 0);
}
//Synthetic comment -- @@ -749,8 +634,7 @@
R.layout.select_dialog_singlechoice, R.id.text1, items), 0,
mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mSelectedItem = (CharSequence)mListView.getSelectedItem();
mListView.performItemClick(null, 0, 0);
}
//Synthetic comment -- @@ -773,8 +657,7 @@
mBuilder.setOnItemSelectedListener(mOnItemSelectedListener);
mBuilder.setItems(com.android.cts.stub.R.array.difficultyLevel, mOnClickListener);
mDialog = mBuilder.show();
                mLinearLayout = (LinearLayout)mDialog.getWindow().findViewById(R.id.contentPanel);
                mListView = (ListView)mLinearLayout.getChildAt(0);
mListView.pointToPosition(0, 0);
}
});







