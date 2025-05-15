//<Beginning of snippet n. 0>

private static Provider.Service cachedSecureRandomService;

/**
 * Need refresh flag.
 */
private static boolean needRefresh;

private static final List<Provider> providers = new ArrayList<Provider>(20);
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
private static final Map<String, Provider.Service> services = new HashMap<String, Provider.Service>();
private static final Object lock = new Object();

static {
    loadProviders();
}

private static void loadProviders() {
    synchronized (lock) {
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
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ignored) {
            }
        }
        Engine.door.renumProviders();
    }
}

/**
 * Returns a copy of the registered providers as an array.
 */
public static Provider[] getProviders() {
    synchronized (lock) {
        return providers.toArray(new Provider[providers.size()]);
    }
}

/**
 * Returns a copy of the registered providers as a list.
 */
public static List<Provider> getProvidersList() {
    synchronized (lock) {
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
    synchronized (lock) {
        return providersNames.get(name);
    }
}

/**
 * Inserts a provider at a specified 1-based position.
 */
public static int insertProviderAt(Provider provider, int position) {
    synchronized (lock) {
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
    synchronized (lock) {
        Provider p = providers.remove(providerNumber - 1);
        providersNames.remove(p.getName());
        setNeedRefresh();
    }
}

/**
 * Adds information about provider services into HashMap.
 */
public static void initServiceInfo(Provider p) {
    synchronized (lock) {
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

/**
 * Returns true if services contain any provider information.
 */
public static boolean isEmpty() {
    synchronized (lock) {
        return services.isEmpty();
    }
}

/**
 * caches should be validated against the result of
 * Service.getCacheVersion() before use.
 */
public static Provider.Service getService(String key) {
    synchronized (lock) {
        return services.get(key);
    }
}

/**
 * Returns the default SecureRandom service description.
 */
public static Provider.Service getSecureRandomService() {
    getCacheVersion();  // used for side effect of updating cache if needed
    synchronized (lock) {
        return cachedSecureRandomService;
    }
}

/**
 * implementation to indicate that a provider list of services has changed.
 */
public static void setNeedRefresh() {
    synchronized (lock) {
        needRefresh = true;
    }
}

/**
 * Returns the current cache version. This has the possible side
 * effect of updating the cache if needed.
 */
public static int getCacheVersion() {
    synchronized (lock) {
        if (needRefresh) {
            cacheVersion++;
            services.clear();
            cachedSecureRandomService = null;
            for (Provider p : providers) {
                initServiceInfo(p);
            }
            needRefresh = false;
        }
        return cacheVersion;
    }
}

//<End of snippet n. 0>