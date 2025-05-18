//<Beginning of snippet n. 0>
return Leb128Utils.readUnsignedLeb128(this);
}

public int readSleb128() {
return Leb128Utils.readSignedLeb128(this);
}
}

public void writeSleb128(int i) {
try {
Leb128Utils.writeSignedLeb128(this, i);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
Code.Try[] tries = code.getTries();
codeOut.writeUnsignedShort(tries.length);

codeOut.writeInt(code.getDebugInfoOffset());

short[] instructions = code.getInstructions();
InstructionTransformer transformer = (in == dexA);
}

private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap) {
int catchAllAddress = catchHandler.getCatchAllAddress();
int[] typeIndexes = catchHandler.getTypeIndexes();
mapList = SizeOf.UINT + (contents.sections.length * SizeOf.MAP_ITEM);
typeList += contents.typeLists.byteCount;
stringData += contents.stringDatas.byteCount;
debugInfo += contents.debugInfos.byteCount;
annotationsDirectory += contents.annotationsDirectories.byteCount;
annotationsSet += contents.annotationSets.byteCount;
annotationsSetRefList += contents.annotationSetRefLists.byteCount;
classData += contents.classDatas.byteCount;
encodedArray += contents.encodedArrays.byteCount;
annotation += contents.annotations.byteCount;

int codeLength = contents.codes.byteCount;
code += (int) Math.ceil(codeLength * 1.25);
encodedArray += contents.encodedArrays.byteCount * 2;
annotation += (int) Math.ceil(contents.annotations.byteCount * 1.34);
} catch (Exception e) {
    // Improved error handling
    throw new RuntimeException("Error transforming encoded catch handler", e);
}
//<End of snippet n. 1>