/*The center aligned test has to have 1 pixel margin

If either a container size or drawable size is odd then left(top)
and right(bottom) delta would not be equal, there would be 1 pixel
difference. So the test should allow 1 pixel difference.

Change-Id:I7e0d06f144a50560b60a4635383d9bed513b9ec9*/
//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/FrameLayoutTest.java b/tests/tests/widget/src/android/widget/cts/FrameLayoutTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 1f8cd23..172d8a9

//Synthetic comment -- @@ -379,8 +379,8 @@
int topDelta = rect.top - container.getTop();
int bottomDelta = container.getBottom() - rect.bottom;

        assertTrue(Math.abs(leftDelta - rightDelta) < 1);
        assertTrue(Math.abs(topDelta - bottomDelta) < 1);
}

private AttributeSet getAttributeSet() throws XmlPullParserException, IOException {







