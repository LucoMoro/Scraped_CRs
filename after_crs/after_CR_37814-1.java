/*Support debug info in dexmerge.

Bug: 4090053

(cherry-pick of bc23c4f3ebaefebb3f1be7732767631f91e165ea.)

Change-Id:I1108933fc03330ff91be3a2edef8b4966977dcd7Signed-off-by: Jesse Wilson <jesse@swank.ca>*/




//Synthetic comment -- diff --git a/dx/src/com/android/dx/io/DexBuffer.java b/dx/src/com/android/dx/io/DexBuffer.java
//Synthetic comment -- index 85fbcb6..e6f908b 100644

//Synthetic comment -- @@ -362,6 +362,10 @@
return Leb128Utils.readUnsignedLeb128(this);
}

        public int readUleb128p1() {
            return Leb128Utils.readUnsignedLeb128(this) - 1;
        }

public int readSleb128() {
return Leb128Utils.readSignedLeb128(this);
}
//Synthetic comment -- @@ -611,6 +615,10 @@
}
}

        public void writeUleb128p1(int i) {
            writeUleb128(i + 1);
        }

public void writeSleb128(int i) {
try {
Leb128Utils.writeSignedLeb128(this, i);








//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index e7276cb..8078f64 100644

//Synthetic comment -- @@ -786,9 +786,13 @@
Code.Try[] tries = code.getTries();
codeOut.writeUnsignedShort(tries.length);

        int debugInfoOffset = code.getDebugInfoOffset();
        if (debugInfoOffset != 0) {
            codeOut.writeInt(debugInfoOut.getPosition());
            transformDebugInfoItem(in.open(debugInfoOffset), indexMap);
        } else {
            codeOut.writeInt(0);
        }

short[] instructions = code.getInstructions();
InstructionTransformer transformer = (in == dexA)
//Synthetic comment -- @@ -815,6 +819,87 @@
}
}

    private static final byte DBG_END_SEQUENCE = 0x00;
    private static final byte DBG_ADVANCE_PC = 0x01;
    private static final byte DBG_ADVANCE_LINE = 0x02;
    private static final byte DBG_START_LOCAL = 0x03;
    private static final byte DBG_START_LOCAL_EXTENDED = 0x04;
    private static final byte DBG_END_LOCAL = 0x05;
    private static final byte DBG_RESTART_LOCAL = 0x06;
    private static final byte DBG_SET_PROLOGUE_END = 0x07;
    private static final byte DBG_SET_EPILOGUE_BEGIN = 0x08;
    private static final byte DBG_SET_FILE = 0x09;

    private void transformDebugInfoItem(DexBuffer.Section in, IndexMap indexMap) {
        int lineStart = in.readUleb128();
        debugInfoOut.writeUleb128(lineStart);

        int parametersSize = in.readUleb128();
        debugInfoOut.writeUleb128(parametersSize);

        for (int p = 0; p < parametersSize; p++) {
            int parameterName = in.readUleb128p1();
            debugInfoOut.writeUleb128p1(indexMap.adjustString(parameterName));
        }

        int addrDiff;    // uleb128   address delta.
        int lineDiff;    // sleb128   line delta.
        int registerNum; // uleb128   register number.
        int nameIndex;   // uleb128p1 string index.    Needs indexMap adjustment.
        int typeIndex;   // uleb128p1 type index.      Needs indexMap adjustment.
        int sigIndex;    // uleb128p1 string index.    Needs indexMap adjustment.

        while (true) {
            int opcode = in.readByte();
            debugInfoOut.writeByte(opcode);

            switch (opcode) {
            case DBG_END_SEQUENCE:
                return;

            case DBG_ADVANCE_PC:
                addrDiff = in.readUleb128();
                debugInfoOut.writeUleb128(addrDiff);
                break;

            case DBG_ADVANCE_LINE:
                lineDiff = in.readSleb128();
                debugInfoOut.writeSleb128(lineDiff);
                break;

            case DBG_START_LOCAL:
            case DBG_START_LOCAL_EXTENDED:
                registerNum = in.readUleb128();
                debugInfoOut.writeUleb128(registerNum);
                nameIndex = in.readUleb128p1();
                debugInfoOut.writeUleb128p1(indexMap.adjustString(nameIndex));
                typeIndex = in.readUleb128p1();
                debugInfoOut.writeUleb128p1(indexMap.adjustType(typeIndex));
                if (opcode == DBG_START_LOCAL_EXTENDED) {
                    sigIndex = in.readUleb128p1();
                    debugInfoOut.writeUleb128p1(indexMap.adjustString(sigIndex));
                }
                break;

            case DBG_END_LOCAL:
            case DBG_RESTART_LOCAL:
                registerNum = in.readUleb128();
                debugInfoOut.writeUleb128(registerNum);
                break;

            case DBG_SET_FILE:
                nameIndex = in.readUleb128p1();
                debugInfoOut.writeUleb128p1(indexMap.adjustString(nameIndex));
                break;

            case DBG_SET_PROLOGUE_END:
            case DBG_SET_EPILOGUE_BEGIN:
            default:
                break;
            }
        }
    }

private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap) {
int catchAllAddress = catchHandler.getCatchAllAddress();
int[] typeIndexes = catchHandler.getTypeIndexes();
//Synthetic comment -- @@ -901,7 +986,6 @@
mapList = SizeOf.UINT + (contents.sections.length * SizeOf.MAP_ITEM);
typeList += contents.typeLists.byteCount;
stringData += contents.stringDatas.byteCount;
annotationsDirectory += contents.annotationsDirectories.byteCount;
annotationsSet += contents.annotationSets.byteCount;
annotationsSetRefList += contents.annotationSetRefLists.byteCount;
//Synthetic comment -- @@ -911,6 +995,7 @@
classData += contents.classDatas.byteCount;
encodedArray += contents.encodedArrays.byteCount;
annotation += contents.annotations.byteCount;
                debugInfo += contents.debugInfos.byteCount;
} else {
// at most 1/4 of the bytes in a code section are uleb/sleb
code += (int) Math.ceil(contents.codes.byteCount * 1.25);
//Synthetic comment -- @@ -920,6 +1005,8 @@
encodedArray += contents.encodedArrays.byteCount * 2;
// at most 1/3 of the bytes in an encoding arrays section are uleb/sleb
annotation += (int) Math.ceil(contents.annotations.byteCount * 1.34);
                // all of the bytes in a debug info section may be uleb/sleb
                debugInfo += contents.debugInfos.byteCount * 2;
}
}








