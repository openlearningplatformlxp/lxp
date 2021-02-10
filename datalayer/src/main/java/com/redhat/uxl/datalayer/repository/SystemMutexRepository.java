package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.SystemMutex;
import com.redhat.uxl.dataobjects.domain.types.SystemMutexType;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * The interface System mutex repository.
 */
public interface SystemMutexRepository extends BaseJpaRepository<SystemMutex, Long> {
    /**
     * Acquire int.
     *
     * @param mutexType the mutex type
     * @return the int
     */
    @Modifying
    @Query("update SystemMutex sm set sm.acquired = true where sm.acquired = false and sm.mutexType = (?1)")
    int acquire(SystemMutexType mutexType);

    /**
     * Acquire int.
     *
     * @param mutexType the mutex type
     * @param system    the system
     * @return the int
     */
    @Modifying
    @Query("update SystemMutex sm set sm.acquired = true, sm.acquiredBySystem = (?2) where sm.acquired = false and sm.mutexType = (?1)")
    int acquire(SystemMutexType mutexType, String system);

    /**
     * Find by mutex type and acquired is false system mutex.
     *
     * @param mutexType the mutex type
     * @return the system mutex
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "0") })
    SystemMutex findByMutexTypeAndAcquiredIsFalse(SystemMutexType mutexType);

    /**
     * Release int.
     *
     * @param systemMutexType the system mutex type
     * @return the int
     */
    @Modifying
    @Query("update SystemMutex sm set sm.acquired = false, sm.acquiredBySystem = null where sm.acquired = true and sm.mutexType = (?1)")
    int release(SystemMutexType systemMutexType);

    /**
     * Release all for system int.
     *
     * @param system the system
     * @return the int
     */
    @Modifying
    @Query("update SystemMutex sm set sm.acquired = false, sm.acquiredBySystem = null where sm.acquiredBySystem = (?1)")
    int releaseAllForSystem(String system);
}
