/*Removed unused Imports and variables

Change-Id:Ib6d79ce3f0b7cf3cbb6716c316ecaefd2dae67f7*/




//Synthetic comment -- diff --git a/src/com/android/calculator2/Calculator.java b/src/com/android/calculator2/Calculator.java
//Synthetic comment -- index d5c5030..98b584f 100644

//Synthetic comment -- @@ -41,7 +41,6 @@
private static final int CMD_BASIC_PANEL    = 2;
private static final int CMD_ADVANCED_PANEL = 3;

private static final int HVGA_WIDTH_PIXELS  = 320;

static final int BASIC_PANEL    = 0;








//Synthetic comment -- diff --git a/src/com/android/calculator2/EventListener.java b/src/com/android/calculator2/EventListener.java
//Synthetic comment -- index 89c9ad8..42f356c 100644

//Synthetic comment -- @@ -18,9 +18,6 @@

import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;

class EventListener implements View.OnKeyListener, 








//Synthetic comment -- diff --git a/src/com/android/calculator2/Logic.java b/src/com/android/calculator2/Logic.java
//Synthetic comment -- index bd066de..259ff51 100644

//Synthetic comment -- @@ -16,12 +16,10 @@

package com.android.calculator2;

import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
//Synthetic comment -- @@ -32,8 +30,6 @@
private Symbols mSymbols = new Symbols();
private History mHistory;
private String  mResult = "";
private boolean mIsError = false;
private int mLineLength = 0;

//Synthetic comment -- @@ -60,8 +56,6 @@
mHistory = history;
mDisplay = display;
mDisplay.setLogic(this);

clearWithHistory(false);
}








//Synthetic comment -- diff --git a/src/com/android/calculator2/PanelSwitcher.java b/src/com/android/calculator2/PanelSwitcher.java
//Synthetic comment -- index c64022c..da98599 100644

//Synthetic comment -- @@ -23,9 +23,6 @@
import android.widget.FrameLayout;
import android.content.Context;
import android.util.AttributeSet;

class PanelSwitcher extends FrameLayout {
private static final int MAJOR_MOVE = 60;
//Synthetic comment -- @@ -33,7 +30,6 @@

private GestureDetector mGestureDetector;
private int mCurrentView;
private View children[];

private int mWidth;
//Synthetic comment -- @@ -43,9 +39,8 @@
private TranslateAnimation inRight;
private TranslateAnimation outRight;

    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
private int mPreviousMove;

public PanelSwitcher(Context context, AttributeSet attrs) {







