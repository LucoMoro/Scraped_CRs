/*WIP: Fix incorrect transformations for glBufferSubData

Change-Id:I8b78a7df6cd2e40d19ff811ced3e0e7cbe3298c6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/BufferSubDataTransform.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/BufferSubDataTransform.java
//Synthetic comment -- index 844c7dc..6bd1f8e 100644

//Synthetic comment -- @@ -44,12 +44,14 @@
IGLProperty property = mAccessor.getProperty(state);
mOldData = (byte[]) property.getValue();

        if (mNewData != null) {
mNewData = new byte[mOldData.length];
ByteBuffer bb = ByteBuffer.wrap(mNewData);
bb.put(mOldData); // copy all of the old buffer
bb.rewind();
            bb.put(mSubData, mOffset, mSubData.length); // update with the sub buffer data
}

property.setValue(mNewData);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/state/transforms/StateTransformFactory.java
//Synthetic comment -- index 576e87b..df92ea7 100644

//Synthetic comment -- @@ -425,7 +425,7 @@
IStateTransform transform = new BufferSubDataTransform(
new CurrentVboPropertyAccessor(msg.getContextId(),
target,
                        GLStateType.BUFFER_SIZE),
offset, data);

return Collections.singletonList(transform);







