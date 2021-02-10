package com.redhat.uxl.dataobjects.domain.util;

import java.sql.Types;
import org.hibernate.dialect.H2Dialect;

/**
 * The type Fixed h 2 dialect.
 */
public class FixedH2Dialect extends H2Dialect {
    /**
     * Instantiates a new Fixed h 2 dialect.
     */
    public FixedH2Dialect() {
    super();
    registerColumnType(Types.FLOAT, "real");
  }
}
