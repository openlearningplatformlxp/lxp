package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The interface Asset repository.
 */
public interface AssetRepository extends BaseJpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {
    /**
     * Find by path and filename asset.
     *
     * @param path     the path
     * @param filename the filename
     * @return the asset
     */
    Asset findByPathAndFilename(String path, String filename);

    /**
     * Find by path is null and filename asset.
     *
     * @param filename the filename
     * @return the asset
     */
    Asset findByPathIsNullAndFilename(String filename);
}
