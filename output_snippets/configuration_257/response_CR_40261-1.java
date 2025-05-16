//<Beginning of snippet n. 0>
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
private static final Set<Provider> providers = Collections.synchronizedSet(new HashSet<>());
private static final Map<String, Provider.Service> services = new ConcurrentHashMap<>();
private static transient Provider.Service cachedSecureRandomService;
private static transient int cacheVersion;
private static transient boolean needRefresh;

static {
    loadProviders();
}

private static void loadProviders() {
    synchronized (providers) {
        String providerClassName = null;
        int i = 1;
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Set<String> allowedProviders = Set.of("AllowedProvider1", "AllowedProvider2"); // Example whitelist

        while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
            if (!allowedProviders.contains(providerClassName.trim())) {
                continue; // Ignore unallowed provider names
            }
            try {
                Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
                Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
                providers.add(p);
                providersNames.put(p.getName(), p);
                initServiceInfo(p);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                // Log the exception or handle it accordingly
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
        return providers.toArray(new Provider[0]);
    }
}

/**
* Returns a copy of the registered providers as a list.
*/
public static List<Provider> getProvidersList() {
    synchronized (providers) {
        return new ArrayList<>(providers);
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
        if (providerNumber < 1 || providerNumber > providers.size()) {
            return;
        }
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
            services.putIfAbsent(key, service);
            for (String alias : Engine.door.getAliases(service)) {
                key = type + "." + alias.toUpperCase(Locale.US);
                services.putIfAbsent(key, service);
            }
        }
    }
}

/**
* Returns true if services contain any provider information.
*/
public static boolean isEmpty() {
    synchronized (services) {
        return services.isEmpty();
    }
}

/**
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

/**
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
//<End of snippet n. 0>