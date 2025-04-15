/*Clarify ViewStub.setVisibility() documentation to state that it works after inflation.

Change-Id:I8414b7002af1a51a10db95758e36c471b51e6355Signed-off-by: Tim Hutt <tdhutt@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/view/ViewStub.java b/core/java/android/view/ViewStub.java
//Synthetic comment -- index 69a26c2..a5dc3ae 100644

//Synthetic comment -- @@ -212,7 +212,8 @@
/**
* When visibility is set to {@link #VISIBLE} or {@link #INVISIBLE},
* {@link #inflate()} is invoked and this StubbedView is replaced in its parent
     * by the inflated layout resource.
*
* @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
*







