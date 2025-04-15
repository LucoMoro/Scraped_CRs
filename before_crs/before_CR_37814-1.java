/*Support debug info in dexmerge.

Bug: 4090053

(cherry-pick of bc23c4f3ebaefebb3f1be7732767631f91e165ea.)

Change-Id:I1108933fc03330ff91be3a2edef8b4966977dcd7Signed-off-by: Jesse Wilson <jesse@swank.ca>*/
//Synthetic comment -- diff --git a/dx/src/com/android/dx/io/DexBuffer.java b/dx/src/com/android/dx/io/DexBuffer.java
//Synthetic comment -- index 85fbcb6..e6f908b 100644

//Synthetic comment -- @@ -362,6 +362,10 @@
return Leb128Utils.readUnsignedLeb128(this);
}

public int readSleb128() {
return Leb128Utils.readSignedLeb128(this);
}
//Synthetic comment -- @@ -611,6 +615,10 @@
}
}

public void writeSleb128(int i) {
try {
Leb128Utils.writeSignedLeb128(this, i);








//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index e7276cb..8078f64 100644

//Synthetic comment -- @@ -786,9 +786,13 @@
Code.Try[] tries = code.getTries();
codeOut.writeUnsignedShort(tries.length);

        // TODO: retain debug info
        // code.getDebugInfoOffset();
        codeOut.writeInt(0);

short[] instructions = code.getInstructions();
InstructionTransformer transformer = (in == dexA)
//Synthetic comment -- @@ -815,6 +819,87 @@
}
}

private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap) {
int catchAllAddress = catchHandler.getCatchAllAddress();
int[] typeIndexes = catchHandler.getTypeIndexes();
//Synthetic comment -- @@ -901,7 +986,6 @@
mapList = SizeOf.UINT + (contents.sections.length * SizeOf.MAP_ITEM);
typeList += contents.typeLists.byteCount;
stringData += contents.stringDatas.byteCount;
            debugInfo += contents.debugInfos.byteCount;
annotationsDirectory += contents.annotationsDirectories.byteCount;
annotationsSet += contents.annotationSets.byteCount;
annotationsSetRefList += contents.annotationSetRefLists.byteCount;
//Synthetic comment -- @@ -911,6 +995,7 @@
classData += contents.classDatas.byteCount;
encodedArray += contents.encodedArrays.byteCount;
annotation += contents.annotations.byteCount;
} else {
// at most 1/4 of the bytes in a code section are uleb/sleb
code += (int) Math.ceil(contents.codes.byteCount * 1.25);
//Synthetic comment -- @@ -920,6 +1005,8 @@
encodedArray += contents.encodedArrays.byteCount * 2;
// at most 1/3 of the bytes in an encoding arrays section are uleb/sleb
annotation += (int) Math.ceil(contents.annotations.byteCount * 1.34);
}
}








