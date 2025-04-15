/*Provider should enumerate services in the order they are added

Bug:http://code.google.com/p/android/issues/detail?id=21449Change-Id:Ie520a8b47adab0583ed1c5cb0da7f43c2eb452ee*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/security/Provider.java b/luni/src/main/java/java/security/Provider.java
//Synthetic comment -- index 2de3751..018e268 100644

//Synthetic comment -- @@ -27,12 +27,13 @@
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.harmony.security.fortress.Services;

/**
//Synthetic comment -- @@ -57,18 +58,18 @@

// Contains "Service.Algorithm" and Provider.Service classes added using
// putService()
    private transient LinkedHashMap<String, Service> serviceTable;

// Contains "Service.Alias" and Provider.Service classes added using
// putService()
    private transient LinkedHashMap<String, Service> aliasTable;

// Contains "Service.Algorithm" and Provider.Service classes added using
// put()
    private transient LinkedHashMap<String, Service> propertyServiceTable;

// Contains "Service.Alias" and Provider.Service classes added using put()
    private transient LinkedHashMap<String, Service> propertyAliasTable;

// The properties changed via put()
private transient Properties changedProperties;
//Synthetic comment -- @@ -414,22 +415,22 @@
return returnedService;
}

        String key = key(type, algorithm);
Object o = null;
if (serviceTable != null) {
            o = serviceTable.get(key);
}
if (o == null && aliasTable != null) {
            o = aliasTable.get(key);
}
if (o == null) {
updatePropertyServiceTable();
}
if (o == null && propertyServiceTable != null) {
            o = propertyServiceTable.get(key);
}
if (o == null && propertyAliasTable != null) {
            o = propertyAliasTable.get(key);
}

if (o != null) {
//Synthetic comment -- @@ -454,9 +455,9 @@
return lastServicesSet;
}
if (serviceTable != null) {
            lastServicesSet = new LinkedHashSet<Service>(serviceTable.values());
} else {
            lastServicesSet = new LinkedHashSet<Service>();
}
if (propertyServiceTable != null) {
lastServicesSet.addAll(propertyServiceTable.values());
//Synthetic comment -- @@ -481,15 +482,15 @@
}
servicesChanged();
if (serviceTable == null) {
            serviceTable = new LinkedHashMap<String, Service>(128);
}
        serviceTable.put(key(s.type, s.algorithm), s);
if (s.aliases != null) {
if (aliasTable == null) {
                aliasTable = new LinkedHashMap<String, Service>(256);
}
for (String alias : s.getAliases()) {
                aliasTable.put(key(s.type, alias), s);
}
}
serviceInfoToProperties(s);
//Synthetic comment -- @@ -510,11 +511,11 @@
}
servicesChanged();
if (serviceTable != null) {
            serviceTable.remove(key(s.type, s.algorithm));
}
if (aliasTable != null && s.aliases != null) {
for (String alias: s.getAliases()) {
                aliasTable.remove(key(s.type, alias));
}
}
serviceInfoFromProperties(s);
//Synthetic comment -- @@ -584,7 +585,7 @@
serviceName = service_alias.substring(0, i);
aliasName = service_alias.substring(i + 1);
if (propertyAliasTable != null) {
                propertyAliasTable.remove(key(serviceName, aliasName));
}
if (propertyServiceTable != null) {
for (Iterator<Service> it = propertyServiceTable.values().iterator(); it
//Synthetic comment -- @@ -608,12 +609,11 @@
serviceName = k.substring(0, j);
algorithm = k.substring(j + 1);
if (propertyServiceTable != null) {
                Provider.Service ser = propertyServiceTable.remove(key(serviceName, algorithm));
if (ser != null && propertyAliasTable != null
&& ser.aliases != null) {
for (String alias : ser.aliases) {
                        propertyAliasTable.remove(key(serviceName, alias));
}
}
}
//Synthetic comment -- @@ -624,7 +624,7 @@
serviceName = k.substring(0, j);
algorithm = k.substring(j + 1, i);
if (propertyServiceTable != null) {
                Object o = propertyServiceTable.get(key(serviceName, algorithm));
if (o != null) {
s = (Provider.Service) o;
s.attributes.remove(attribute);
//Synthetic comment -- @@ -667,20 +667,20 @@
serviceName = service_alias.substring(0, i);
aliasName = service_alias.substring(i + 1);
algorithm = value;
                String propertyServiceTableKey = key(serviceName, algorithm);
Object o = null;
if (propertyServiceTable == null) {
                    propertyServiceTable = new LinkedHashMap<String, Service>(128);
} else {
                    o = propertyServiceTable.get(propertyServiceTableKey);
}
if (o != null) {
s = (Provider.Service) o;
s.addAlias(aliasName);
if (propertyAliasTable == null) {
                        propertyAliasTable = new LinkedHashMap<String, Service>(256);
}
                    propertyAliasTable.put(key(serviceName, aliasName), s);
} else {
String className = (String) changedProperties
.get(serviceName + "." + algorithm);
//Synthetic comment -- @@ -689,11 +689,11 @@
l.add(aliasName);
s = new Provider.Service(this, serviceName, algorithm,
className, l, new HashMap<String, String>());
                        propertyServiceTable.put(propertyServiceTableKey, s);
if (propertyAliasTable == null) {
                            propertyAliasTable = new LinkedHashMap<String, Service>(256);
}
                        propertyAliasTable.put(key(serviceName, aliasName), s);
}
}
continue;
//Synthetic comment -- @@ -706,10 +706,10 @@
if (i == -1) { // <crypto_service>.<algorithm_or_type>=<className>
serviceName = key.substring(0, j);
algorithm = key.substring(j + 1);
                String propertyServiceTableKey = key(serviceName, algorithm);
Object o = null;
if (propertyServiceTable != null) {
                    o = propertyServiceTable.get(propertyServiceTableKey);
}
if (o != null) {
s = (Provider.Service) o;
//Synthetic comment -- @@ -719,21 +719,20 @@
value, Collections.<String>emptyList(),
Collections.<String,String>emptyMap());
if (propertyServiceTable == null) {
                        propertyServiceTable = new LinkedHashMap<String, Service>(128);
}
                    propertyServiceTable.put(propertyServiceTableKey, s);

}
} else {
                // <crypto_service>.<algorithm_or_type> <attribute_name>=<attrValue>
serviceName = key.substring(0, j);
algorithm = key.substring(j + 1, i);
String attribute = key.substring(i + 1);
                String propertyServiceTableKey = key(serviceName, algorithm);
Object o = null;
if (propertyServiceTable != null) {
                    o = propertyServiceTable.get(propertyServiceTableKey);
}
if (o != null) {
s = (Provider.Service) o;
//Synthetic comment -- @@ -747,9 +746,9 @@
s = new Provider.Service(this, serviceName, algorithm,
className, new ArrayList<String>(), m);
if (propertyServiceTable == null) {
                            propertyServiceTable = new LinkedHashMap<String, Service>(128);
}
                        propertyServiceTable.put(propertyServiceTableKey, s);
}
}
}
//Synthetic comment -- @@ -794,6 +793,10 @@
return null;
}

    private static String key(String type, String algorithm) {
        return type + '.' + algorithm.toUpperCase(Locale.US);
    }

/**
* {@code Service} represents a service in the Java Security infrastructure.
* Each service describes its type, the algorithm it implements, to which








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/luni/util/TwoKeyHashMap.java b/luni/src/main/java/org/apache/harmony/luni/util/TwoKeyHashMap.java
deleted file mode 100644
//Synthetic comment -- index 35e6c62..0000000

//Synthetic comment -- @@ -1,566 +0,0 @@








//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/security/ProviderTest.java b/luni/src/test/java/libcore/java/security/ProviderTest.java
//Synthetic comment -- index 695908b..78608d0 100644

//Synthetic comment -- @@ -17,14 +17,20 @@
package libcore.java.security;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -195,4 +201,48 @@
}
}
}

    /**
     * http://code.google.com/p/android/issues/detail?id=21449
     */
    public void testSecureRandomImplementationOrder() {
        Provider srp = new SRProvider();
        try {
            int position = Security.insertProviderAt(srp, 1); // first is one, not zero
            assertEquals(1, position);
            SecureRandom sr = new SecureRandom();
            if (!sr.getAlgorithm().equals("SecureRandom1")) {
                throw new IllegalStateException("Expected SecureRandom1");
            }
        } finally {
            Security.removeProvider(srp.getName());
        }
    }

    public static class SRProvider extends Provider {

        SRProvider() {
            super("SRProvider", 1.42, "SecureRandom Provider");
            put("SecureRandom.SecureRandom1", SecureRandom1.class.getName());
            put("SecureRandom.SecureRandom2", SecureRandom2.class.getName());
            put("SecureRandom.SecureRandom3", SecureRandom3.class.getName());
        }
    }

    public static abstract class AbstractSecureRandom extends SecureRandomSpi {
        protected void engineSetSeed(byte[] seed) {
            throw new UnsupportedOperationException();
        }
        protected void engineNextBytes(byte[] bytes) {
            throw new UnsupportedOperationException();
        }
        protected byte[] engineGenerateSeed(int numBytes) {
            throw new UnsupportedOperationException();
        }
    }

    public static class SecureRandom1 extends AbstractSecureRandom {}
    public static class SecureRandom2 extends AbstractSecureRandom {}
    public static class SecureRandom3 extends AbstractSecureRandom {}

}







