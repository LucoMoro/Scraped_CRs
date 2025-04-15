/*Make division by zero return an error

Attempts by the user to divide by zero now return an error instead of infinity, addressing the issue described inhttp://code.google.com/p/android/issues/detail?id=16707. This is achieved by reusing functionality fir handling NaN:s. Regarding dividing by zero:http://www.math.utah.edu/~pa/math/0by0.htmlChange-Id:I29710a20ca33d3f373a5bb3a98c0fc75e3de6b11Signed-off-by: Viktor Smedby <vsmedby@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/calculator2/Logic.java b/src/com/android/calculator2/Logic.java
//Synthetic comment -- index 0e8c19f..d8943f5 100644

//Synthetic comment -- @@ -33,8 +33,6 @@
private boolean mIsError = false;
private int mLineLength = 0;

// the two strings below are the result of Double.toString() for Infinity & NaN
// they are not output to the user and don't require internationalization
private static final String INFINITY = "Infinity"; 
//Synthetic comment -- @@ -174,11 +172,11 @@
}

String result = Util.doubleToString(mSymbols.eval(input), mLineLength, ROUND_DIGITS);
        if (result.equals(NAN) || result.contains(INFINITY)) { //treat NaN and (-)Infinity as Error
mIsError = true;
return mErrorString;
}
        return result.replace('-', MINUS);
}

static boolean isOperator(String text) {







