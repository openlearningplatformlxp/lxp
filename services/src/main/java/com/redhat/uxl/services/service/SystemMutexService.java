package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;

/**
 * The interface System mutex service.
 */
public interface SystemMutexService {
    /**
     * Acquire boolean.
     *
     * @param mutexType the mutex type
     * @return the boolean
     */
    boolean acquire(SystemMutexType mutexType);

    /**
     * Release boolean.
     *
     * @param mutexType the mutex type
     * @return the boolean
     */
    boolean release(SystemMutexType mutexType);
}
