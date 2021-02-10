package com.redhat.uxl.dataobjects.domain.types;

import java.util.ArrayList;
import java.util.List;

/**
 * The enum Discovery program type.
 */
public enum DiscoveryProgramType {
    /**
     * Program discovery program type.
     */
    PROGRAM,
    /**
     * Text discovery program type.
     */
    TEXT;

    /**
     * Gets all types.
     *
     * @return the all types
     */
    public static List<DiscoveryProgramType> getAllTypes() {
    List<DiscoveryProgramType> types = new ArrayList<>();
    types.add(DiscoveryProgramType.PROGRAM);
    types.add(DiscoveryProgramType.TEXT);

    return types;
  }
}
