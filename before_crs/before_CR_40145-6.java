/*Avoid ConcurrentModificationException on providers

Previously concurrently adding/removing providers in one thread and
doing algorithm lookups on another thread would cause
ConcurrentModificationExceptions such as:

java.util.ConcurrentModificationException
        at java.util.ArrayList$ArrayListIterator.next(ArrayList.java:569)
        at org.apache.harmony.security.fortress.Services.updateServiceInfo(Services.java:186)
        at org.apache.harmony.security.fortress.Services.refresh(Services.java:234)
        at org.apache.harmony.security.fortress.Engine.getInstance(Engine.java:137)
        at java.security.KeyFactory.getInstance(KeyFactory.java:81)

Also do some general cleanup and add similar protection on services.

Change-Id:I706c45655924dfccc3383fca57653d0c5b461721*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 68a2917..9091f73 100644

//Synthetic comment -- @@ -88,7 +88,6 @@
*/
public SecureRandom() {
super(0);
        Services.refresh();
Provider.Service service = Services.getSecureRandomService();
if (service == null) {
this.provider = null;








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/fortress/Engine.java b/luni/src/main/java/org/apache/harmony/security/fortress/Engine.java
//Synthetic comment -- index 8a67ac2..f1dd43c 100644

//Synthetic comment -- @@ -93,15 +93,15 @@
/** used to test for cache hit */
private final String algorithm;
/** used to test for cache validity */
        private final int refreshNumber;
/** cached result */
private final Provider.Service service;

private ServiceCacheEntry(String algorithm,
                                  int refreshNumber,
Provider.Service service) {
this.algorithm = algorithm;
            this.refreshNumber = refreshNumber;
this.service = service;
}
}
//Synthetic comment -- @@ -134,12 +134,12 @@
if (algorithm == null) {
throw new NoSuchAlgorithmException("Null algorithm name");
}
        Services.refresh();
Provider.Service service;
ServiceCacheEntry cacheEntry = this.serviceCache;
if (cacheEntry != null
&& cacheEntry.algorithm.equalsIgnoreCase(algorithm)
                && Services.refreshNumber == cacheEntry.refreshNumber) {
service = cacheEntry.service;
} else {
if (Services.isEmpty()) {
//Synthetic comment -- @@ -150,7 +150,7 @@
if (service == null) {
throw notFound(serviceName, algorithm);
}
            this.serviceCache = new ServiceCacheEntry(algorithm, Services.refreshNumber, service);
}
return new SpiAndProvider(service.newInstance(param), service.getProvider());
}








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/fortress/Services.java b/luni/src/main/java/org/apache/harmony/security/fortress/Services.java
//Synthetic comment -- index d97e0f4..a441290 100644

//Synthetic comment -- @@ -15,11 +15,6 @@
*  limitations under the License.
*/

/**
* @author Boris V. Kuznetsov
* @version $Revision$
*/

package org.apache.harmony.security.fortress;

import java.security.Provider;
//Synthetic comment -- @@ -34,204 +29,215 @@
/**
* This class contains information about all registered providers and preferred
* implementations for all "serviceName.algName".
 *
*/

public class Services {

    // The HashMap that contains information about preferred implementations for
    // all serviceName.algName in the registered providers.
    // Set the initial size to 600 so we don't grow to 1024 by default because
    // initialization adds a few entries more than the growth threshold.
private static final Map<String, Provider.Service> services
= new HashMap<String, Provider.Service>(600);
    // Save default SecureRandom service as well.
    // Avoids similar provider/services iteration in SecureRandom constructor
    private static Provider.Service secureRandom;

    // Need refresh flag
    private static boolean needRefresh; // = false;

/**
     * Refresh number
*/
    static int refreshNumber = 1;

    // Registered providers
private static final List<Provider> providers = new ArrayList<Provider>(20);

    // Hash for quick provider access by name
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
static {
loadProviders();
}

    // Load statically registered providers and init Services Info
private static void loadProviders() {
        String providerClassName = null;
        int i = 1;
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Provider p;

        while ((providerClassName = Security.getProperty("security.provider."
                + i++)) != null) {
            try {
                p = (Provider) Class
                        .forName(providerClassName.trim(), true, cl)
                        .newInstance();
                providers.add(p);
                providersNames.put(p.getName(), p);
                initServiceInfo(p);
            } catch (ClassNotFoundException e) { // ignore Exceptions
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
}
}
        Engine.door.renumProviders();
}

/**
     * Returns registered providers
     *
     * @return
*/
public static Provider[] getProviders() {
        return providers.toArray(new Provider[providers.size()]);
}

/**
     * Returns registered providers as List
     *
     * @return
*/
public static List<Provider> getProvidersList() {
        return new ArrayList<Provider>(providers);
}

/**
     * Returns the provider with the specified name
     *
     * @param name
     * @return
*/
public static Provider getProvider(String name) {
if (name == null) {
return null;
}
        return providersNames.get(name);
}

/**
     * Inserts a provider at a specified position
     *
     * @param provider
     * @param position
     * @return
*/
public static int insertProviderAt(Provider provider, int position) {
        int size = providers.size();
        if ((position < 1) || (position > size)) {
            position = size + 1;
}
        providers.add(position - 1, provider);
        providersNames.put(provider.getName(), provider);
        setNeedRefresh();
        return position;
}

/**
     * Removes the provider
     *
     * @param providerNumber
*/
public static void removeProvider(int providerNumber) {
        Provider p = providers.remove(providerNumber - 1);
        providersNames.remove(p.getName());
        setNeedRefresh();
}

/**
     *
* Adds information about provider services into HashMap.
     *
     * @param p
*/
public static void initServiceInfo(Provider p) {
        for (Provider.Service serv : p.getServices()) {
            String type = serv.getType();
            if (secureRandom == null && type.equals("SecureRandom")) {
                secureRandom = serv;
            }
            String key = type + "." + serv.getAlgorithm().toUpperCase(Locale.US);
            if (!services.containsKey(key)) {
                services.put(key, serv);
            }
            for (String alias : Engine.door.getAliases(serv)) {
                key = type + "." + alias.toUpperCase(Locale.US);
if (!services.containsKey(key)) {
                    services.put(key, serv);
}
}
}
}

/**
     *
     * Updates services hashtable for all registered providers
     *
     */
    public static void updateServiceInfo() {
        services.clear();
        secureRandom = null;
        for (Provider p : providers) {
            initServiceInfo(p);
        }
        needRefresh = false;
    }

    /**
     * Returns true if services contain any provider information
     * @return
*/
public static boolean isEmpty() {
        return services.isEmpty();
}

/**
     * Returns service description.
     * Call refresh() before.
*
     * @param key in the format TYPE.ALGORITHM
     * @return
*/
public static Provider.Service getService(String key) {
        return services.get(key);
}

/**
* Returns the default SecureRandom service description.
     * Call refresh() before.
*/
public static Provider.Service getSecureRandomService() {
        return secureRandom;
}

/**
     * Set flag needRefresh
     *
*/
public static void setNeedRefresh() {
        needRefresh = true;
}

/**
     * Refresh services info
     *
*/
    public static void refresh() {
        if (needRefresh) {
            refreshNumber++;
            updateServiceInfo();
}
}
}







