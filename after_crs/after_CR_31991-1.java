/*Fix typos

Signed-off-by: Chris Dearman <chris@mips.com>*/




//Synthetic comment -- diff --git a/tests/core/libcore/src/Dummy.java b/tests/core/libcore/src/Dummy.java
//Synthetic comment -- index cfe0c81..64b67de 100644

//Synthetic comment -- @@ -17,7 +17,7 @@
/**
* We really just want the core-tests classes from the static java
* library, but the build system currently needs at least one input
 * file, not just because its a sanity check, but because the static
* class files and resources are included in the output of the local
* java compilation.
*/








//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/GradientDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/GradientDrawableTest.java
//Synthetic comment -- index 6849c69..d2599e4 100644

//Synthetic comment -- @@ -449,7 +449,7 @@
method = "getConstantState",
args = {}
)
    @ToBeFixed(bug = "", explanation = "can not assert the inner fields, because the class" +
" GradientState is package protected.")
public void testGetConstantState() {
GradientDrawable gradientDrawable = new GradientDrawable();








//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/graphics/drawable/cts/InsetDrawableTest.java b/tests/tests/graphics/src/android/graphics/drawable/cts/InsetDrawableTest.java
//Synthetic comment -- index 32dc5ca..c3a0b45 100644

//Synthetic comment -- @@ -431,7 +431,7 @@
method = "getConstantState",
args = {}
)
    @ToBeFixed(bug = "", explanation = "can not assert the inner fields, because the class" +
" InsetState is package protected.")
public void testGetConstantState() {
Drawable d = mContext.getResources().getDrawable(R.drawable.pass);








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/OrientationEventListenerTest.java b/tests/tests/view/src/android/view/cts/OrientationEventListenerTest.java
//Synthetic comment -- index 570ef88..130f01b 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
level = TestLevel.SUFFICIENT,
notes = "Test {@link OrientationEventListener#enable()}. "
+ "This method is simply called to make sure that no exception is thrown. "
                    + "The registration of the listener can not be tested because there is "
+ "no way to simulate sensor events",
method = "enable",
args = {}
//Synthetic comment -- @@ -65,7 +65,7 @@
level = TestLevel.SUFFICIENT,
notes = "Test {@link OrientationEventListener#disable()}. "
+ "This method is simply called to make sure that no exception is thrown. "
                    + "The registration of the listener can not be tested because there is "
+ "no way to simulate sensor events",
method = "disable",
args = {}








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/OrientationListenerTest.java b/tests/tests/view/src/android/view/cts/OrientationListenerTest.java
//Synthetic comment -- index f21519c..b3437cd 100644

//Synthetic comment -- @@ -65,7 +65,7 @@
level = TestLevel.COMPLETE,
notes = "Test {@link OrientationListener#enable()}. "
+ "This method is simply called to make sure that no exception is thrown. "
                    + "The registration of the listener can not be tested because there is no way "
+ "to simulate sensor events on the emulator",
method = "enable",
args = {}
//Synthetic comment -- @@ -74,14 +74,14 @@
level = TestLevel.COMPLETE,
notes = "Test {@link OrientationListener#disable()}. "
+ "This method is simply called to make sure that no exception is thrown. "
                    + "The registration of the listener can not be tested because there is no way "
+ "to simulate sensor events on the emulator",
method = "disable",
args = {}
)
})
@ToBeFixed(explanation = "Can not simulate sensor events on the emulator.")
    public void testRegistrationOfOrientationListener() {
// these methods are called to assure that no exception is thrown
MockOrientationListener listener = new MockOrientationListener(mContext);
listener.disable();







