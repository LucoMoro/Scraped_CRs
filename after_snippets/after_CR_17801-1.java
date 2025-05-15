
//<Beginning of snippet n. 0>


return pkg instanceof ToolPackage;
}

    @Override
    void saveProperties(Properties props) {
        super.saveProperties(props);

        if (getMinPlatformToolsRevision() != MIN_PLATFORM_TOOLS_REV_INVALID) {
            props.setProperty(PROP_MIN_PLATFORM_TOOLS_REV,
                              Integer.toString(getMinPlatformToolsRevision()));
        }
    }

/**
* The tool package executes tools/lib/post_tools_install[.bat|.sh]
* {@inheritDoc}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}
return "";
}

    /**
     * Returns the short description of the parent package of the new archive, if not null.
     * Otherwise returns the default Object toString result.
     * <p/>
     * This is mostly helpful for debugging. For UI display, use the {@link IDescription}
     * interface.
     */
    @Override
    public String toString() {
        if (mNewArchive != null) {
            Package p = mNewArchive.getParentPackage();
            if (p != null) {
                return p.getShortDescription();
            }
        }
        return super.toString();
    }
}

//<End of snippet n. 1>








