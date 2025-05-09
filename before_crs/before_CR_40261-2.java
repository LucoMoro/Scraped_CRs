/*Simplify locking and close race in getSecureRandomService()

Change-Id:I5c482d9a2d2a9282d7a5e5d4c4de52be05c60b93*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/fortress/Services.java b/luni/src/main/java/org/apache/harmony/security/fortress/Services.java
//Synthetic comment -- index a441290..4fe0d44 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
private static Provider.Service cachedSecureRandomService;

/**
     * Need refresh flag. Protected by synchronizing on providers.
*/
private static boolean needRefresh;

//Synthetic comment -- @@ -65,112 +65,94 @@
private static final List<Provider> providers = new ArrayList<Provider>(20);

/**
     * Hash for quick provider access by name. Protected by synchronizing on providers.
*/
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
static {
        loadProviders();
    }

    private static void loadProviders() {
        synchronized (providers) {
            String providerClassName = null;
            int i = 1;
            ClassLoader cl = ClassLoader.getSystemClassLoader();

            while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
                try {
                    Class providerClass = Class.forName(providerClassName.trim(), true, cl);
                    Provider p = (Provider) providerClass.newInstance();
                    providers.add(p);
                    providersNames.put(p.getName(), p);
                    initServiceInfo(p);
                } catch (ClassNotFoundException ignored) {
                } catch (IllegalAccessException ignored) {
                } catch (InstantiationException ignored) {
                }
}
            Engine.door.renumProviders();
}
}

/**
* Returns a copy of the registered providers as an array.
*/
    public static Provider[] getProviders() {
        synchronized (providers) {
            return providers.toArray(new Provider[providers.size()]);
        }
}

/**
* Returns a copy of the registered providers as a list.
*/
    public static List<Provider> getProvidersList() {
        synchronized (providers) {
            return new ArrayList<Provider>(providers);
        }
}

/**
* Returns the provider with the specified name.
*/
    public static Provider getProvider(String name) {
if (name == null) {
return null;
}
        synchronized (providers) {
            return providersNames.get(name);
        }
}

/**
* Inserts a provider at a specified 1-based position.
*/
    public static int insertProviderAt(Provider provider, int position) {
        synchronized (providers) {
            int size = providers.size();
            if ((position < 1) || (position > size)) {
                position = size + 1;
            }
            providers.add(position - 1, provider);
            providersNames.put(provider.getName(), provider);
            setNeedRefresh();
            return position;
}
}

/**
* Removes the provider at the specified 1-based position.
*/
    public static void removeProvider(int providerNumber) {
        synchronized (providers) {
            Provider p = providers.remove(providerNumber - 1);
            providersNames.remove(p.getName());
            setNeedRefresh();
        }
}

/**
* Adds information about provider services into HashMap.
*/
    public static void initServiceInfo(Provider p) {
        synchronized (services) {
            for (Provider.Service service : p.getServices()) {
                String type = service.getType();
                if (cachedSecureRandomService == null && type.equals("SecureRandom")) {
                    cachedSecureRandomService = service;
                }
                String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
if (!services.containsKey(key)) {
services.put(key, service);
}
                for (String alias : Engine.door.getAliases(service)) {
                    key = type + "." + alias.toUpperCase(Locale.US);
                    if (!services.containsKey(key)) {
                        services.put(key, service);
                    }
                }
}
}
}
//Synthetic comment -- @@ -178,10 +160,8 @@
/**
* Returns true if services contain any provider information.
*/
    public static boolean isEmpty() {
        synchronized (services) {
            return services.isEmpty();
        }
}

/**
//Synthetic comment -- @@ -194,16 +174,14 @@
* caches should be validated against the result of
* Service.getCacheVersion() before use.
*/
    public static Provider.Service getService(String key) {
        synchronized (services) {
            return services.get(key);
        }
}

/**
* Returns the default SecureRandom service description.
*/
    public static Provider.Service getSecureRandomService() {
getCacheVersion();  // used for side effect of updating cache if needed
return cachedSecureRandomService;
}
//Synthetic comment -- @@ -214,30 +192,26 @@
* implementation to indicate that a provides list of services has
* changed.
*/
    public static void setNeedRefresh() {
        synchronized (providers) {
            needRefresh = true;
        }
}

/**
* Returns the current cache version. This has the possible side
* effect of updating the cache if needed.
*/
    public static int getCacheVersion() {
        synchronized (providers) {
            if (needRefresh) {
                cacheVersion++;
                synchronized (services) {
                    services.clear();
                }
                cachedSecureRandomService = null;
                for (Provider p : providers) {
                    initServiceInfo(p);
                }
                needRefresh = false;
}
            return cacheVersion;
}
}
}







