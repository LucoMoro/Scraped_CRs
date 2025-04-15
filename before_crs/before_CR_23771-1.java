/*Merge 9d342967 from master to r12. do not merge.

Fix broken equals() in new qualifiers.

Change-Id:Id083d11a2941d120ca6fd9438a5a12ed7502ab92*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenHeightQualifier.java
//Synthetic comment -- index 2899631..5437f71 100644

//Synthetic comment -- @@ -150,6 +150,7 @@
return ""; //$NON-NLS-1$
}

@Override
public int hashCode() {
return mValue;
//Synthetic comment -- @@ -157,13 +158,19 @@

@Override
public boolean equals(Object obj) {
        if (this == obj)
return true;
        if (getClass() != obj.getClass())
return false;
ScreenHeightQualifier other = (ScreenHeightQualifier) obj;
        if (mValue != other.mValue)
return false;
return true;
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/ScreenWidthQualifier.java
//Synthetic comment -- index 8748864..cf1e71a 100644

//Synthetic comment -- @@ -157,13 +157,19 @@

@Override
public boolean equals(Object obj) {
        if (this == obj)
return true;
        if (getClass() != obj.getClass())
return false;
ScreenWidthQualifier other = (ScreenWidthQualifier) obj;
        if (mValue != other.mValue)
return false;
return true;
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java b/ide_common/src/com/android/ide/common/resources/configuration/SmallestScreenWidthQualifier.java
//Synthetic comment -- index e151805..2f82e79 100644

//Synthetic comment -- @@ -157,13 +157,19 @@

@Override
public boolean equals(Object obj) {
        if (this == obj)
return true;
        if (getClass() != obj.getClass())
return false;
SmallestScreenWidthQualifier other = (SmallestScreenWidthQualifier) obj;
        if (mValue != other.mValue)
return false;
return true;
}
}







