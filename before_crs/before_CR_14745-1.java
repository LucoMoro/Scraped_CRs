/*Removed unused Imports and variables, Replaced int Declarations with Enum

Change-Id:Ib6d79ce3f0b7cf3cbb6716c316ecaefd2dae67f7*/
//Synthetic comment -- diff --git a/src/com/android/calculator2/Calculator.java b/src/com/android/calculator2/Calculator.java
//Synthetic comment -- index d5c5030..6cbcf92 100644

//Synthetic comment -- @@ -41,7 +41,7 @@
private static final int CMD_BASIC_PANEL    = 2;
private static final int CMD_ADVANCED_PANEL = 3;

    private static final int HVGA_HEIGHT_PIXELS = 480;
private static final int HVGA_WIDTH_PIXELS  = 320;

static final int BASIC_PANEL    = 0;








//Synthetic comment -- diff --git a/src/com/android/calculator2/EventListener.java b/src/com/android/calculator2/EventListener.java
//Synthetic comment -- index 89c9ad8..42f356c 100644

//Synthetic comment -- @@ -18,9 +18,6 @@

import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;

class EventListener implements View.OnKeyListener, 








//Synthetic comment -- diff --git a/src/com/android/calculator2/Logic.java b/src/com/android/calculator2/Logic.java
//Synthetic comment -- index bd066de..259ff51 100644

//Synthetic comment -- @@ -16,12 +16,10 @@

package com.android.calculator2;

import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.content.res.Configuration;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
//Synthetic comment -- @@ -32,8 +30,6 @@
private Symbols mSymbols = new Symbols();
private History mHistory;
private String  mResult = "";
    private Button mEqualButton;
    private final String mEnterString;
private boolean mIsError = false;
private int mLineLength = 0;

//Synthetic comment -- @@ -60,8 +56,6 @@
mHistory = history;
mDisplay = display;
mDisplay.setLogic(this);
        mEqualButton = equalButton;
        mEnterString = context.getText(R.string.enter).toString();

clearWithHistory(false);
}








//Synthetic comment -- diff --git a/src/com/android/calculator2/PanelSwitcher.java b/src/com/android/calculator2/PanelSwitcher.java
//Synthetic comment -- index c64022c..5b40ed2 100644

//Synthetic comment -- @@ -23,9 +23,6 @@
import android.widget.FrameLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.os.Handler;

import java.util.Map;

class PanelSwitcher extends FrameLayout {
private static final int MAJOR_MOVE = 60;
//Synthetic comment -- @@ -33,7 +30,6 @@

private GestureDetector mGestureDetector;
private int mCurrentView;
    private View mChild, mHistoryView;
private View children[];

private int mWidth;
//Synthetic comment -- @@ -43,10 +39,10 @@
private TranslateAnimation inRight;
private TranslateAnimation outRight;

    private static final int NONE  = 1;
    private static final int LEFT  = 2;
    private static final int RIGHT = 3;
    private int mPreviousMove;

public PanelSwitcher(Context context, AttributeSet attrs) {
super(context, attrs);
//Synthetic comment -- @@ -110,27 +106,27 @@

void moveLeft() {
//  <--
        if (mCurrentView < children.length - 1 && mPreviousMove != LEFT) {
children[mCurrentView+1].setVisibility(View.VISIBLE);
children[mCurrentView+1].startAnimation(inLeft);
children[mCurrentView].startAnimation(outLeft);
children[mCurrentView].setVisibility(View.GONE);

mCurrentView++;
            mPreviousMove = LEFT;
}
}

void moveRight() {
//  -->
        if (mCurrentView > 0 && mPreviousMove != RIGHT) {
children[mCurrentView-1].setVisibility(View.VISIBLE);
children[mCurrentView-1].startAnimation(inRight);
children[mCurrentView].startAnimation(outRight);
children[mCurrentView].setVisibility(View.GONE);

mCurrentView--;
            mPreviousMove = RIGHT;
}
}








