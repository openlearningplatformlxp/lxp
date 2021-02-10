package com.redhat.uxl.dataobjects.domain.types;

/**
 * The enum Permission operation.
 */
public enum PermissionOperation {
    /**
     * View permission operation.
     */
    VIEW(1),
    /**
     * Create permission operation.
     */
    CREATE(2),
    /**
     * Update permission operation.
     */
    UPDATE(3),
    /**
     * Delete permission operation.
     */
    DELETE(4),

    /**
     * Accessible permission operation.
     */
    ACCESSIBLE(1),

    /**
     * Enabled permission operation.
     */
    ENABLED(1);

    /**
     * The Sequence.
     */
    int sequence;

  PermissionOperation(int sequence) {
    this.sequence = sequence;
  }

    /**
     * Gets sequence.
     *
     * @return the sequence
     */
    public int getSequence() {
    return sequence;
  }
}
