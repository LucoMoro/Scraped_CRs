/*Some changes added to compile and run with Java 6 and Java 7.

  - correction of the API to match with the Java 7 API:
    - JDBC framework.

Change-Id:I2a7061833c8dd85fcaaba928535e0a5e767edda8*/
//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBC2z/JDBCConnection.java b/src/main/java/SQLite/JDBC2z/JDBCConnection.java
//Synthetic comment -- index 59c1d3e..3918888 100644

//Synthetic comment -- @@ -2,6 +2,7 @@

import java.sql.*;
import java.util.*;

public class JDBCConnection
implements java.sql.Connection, SQLite.BusyHandler {
//Synthetic comment -- @@ -512,9 +513,30 @@
	throw new SQLException("unsupported");
}

    public boolean isWrapperFor(java.lang.Class iface) throws SQLException {
	return false;
    }

}









//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBC2z/JDBCDatabaseMetaData.java b/src/main/java/SQLite/JDBC2z/JDBCDatabaseMetaData.java
//Synthetic comment -- index 0e56b02..e63d6d7 100644

//Synthetic comment -- @@ -1681,8 +1681,18 @@
	throw new SQLException("unsupported");
}

    public boolean isWrapperFor(java.lang.Class iface) throws SQLException {
	return false;
    }

}








//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBC2z/JDBCPreparedStatement.java b/src/main/java/SQLite/JDBC2z/JDBCPreparedStatement.java
//Synthetic comment -- index a5afca0..1418395 100644

//Synthetic comment -- @@ -964,4 +964,8 @@
	throw new SQLFeatureNotSupportedException();
}

}








//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBC2z/JDBCResultSet.java b/src/main/java/SQLite/JDBC2z/JDBCResultSet.java
//Synthetic comment -- index 7b3777f..f2dd39d 100644

//Synthetic comment -- @@ -1,6 +1,7 @@
package SQLite.JDBC2z;

import java.sql.*;
import java.math.BigDecimal;

public class JDBCResultSet implements java.sql.ResultSet {
//Synthetic comment -- @@ -669,17 +670,6 @@
	return getObject(col);
}

    public Object getObject(int columnIndex, java.util.Map map) 
	throws SQLException {
	throw new SQLFeatureNotSupportedException();
    }

    public Object getObject(String columnName, java.util.Map map)
	throws SQLException {
	int col = findColumn(columnName);
	return getObject(col, map);
    }

public java.sql.Ref getRef(int columnIndex) throws SQLException {
	throw new SQLFeatureNotSupportedException();
}
//Synthetic comment -- @@ -1597,8 +1587,28 @@
	throw new SQLException("unsupported");
}

    public boolean isWrapperFor(java.lang.Class iface) throws SQLException {
	return false;
    }

}








//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBC2z/JDBCStatement.java b/src/main/java/SQLite/JDBC2z/JDBCStatement.java
//Synthetic comment -- index 52dd4b0..44af48d 100644

//Synthetic comment -- @@ -318,8 +318,16 @@
	throw new SQLException("unsupported");
}

    public boolean isWrapperFor(java.lang.Class iface) throws SQLException {
	return false;
    }

}








//Synthetic comment -- diff --git a/src/main/java/SQLite/JDBCDriver.java b/src/main/java/SQLite/JDBCDriver.java
//Synthetic comment -- index c90035d..f96b867 100644

//Synthetic comment -- @@ -2,6 +2,7 @@

import java.sql.*;
import java.util.Properties;

public class JDBCDriver implements java.sql.Driver {

//Synthetic comment -- @@ -141,4 +142,8 @@
public boolean jdbcCompliant() {
	return false;
}
}







