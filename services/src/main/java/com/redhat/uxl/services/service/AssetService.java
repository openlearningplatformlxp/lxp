package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.Asset;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * The interface Asset service.
 */
public interface AssetService {

    /**
     * Add asset asset.
     *
     * @param asset the asset
     * @param file  the file
     * @return the asset
     */
    Asset addAsset(Asset asset, MultipartFile file);

    /**
     * Gets asset.
     *
     * @param id the id
     * @return the asset
     */
    Asset getAsset(Long id);

    /**
     * Gets asset by path and filename.
     *
     * @param pathAndFilename the path and filename
     * @return the asset by path and filename
     */
    Asset getAssetByPathAndFilename(String pathAndFilename);

    /**
     * Merge template into string string.
     *
     * @param templateLocation the template location
     * @param encoding         the encoding
     * @param model            the model
     * @return the string
     */
    String mergeTemplateIntoString(String templateLocation, String encoding, Map<String, Object> model);

    /**
     * Merge template into string string.
     *
     * @param asset    the asset
     * @param encoding the encoding
     * @param model    the model
     * @return the string
     */
    String mergeTemplateIntoString(Asset asset, String encoding, Map<String, Object> model);

    /**
     * Update asset asset.
     *
     * @param assetId the asset id
     * @param asset   the asset
     * @param file    the file
     * @return the asset
     */
    Asset updateAsset(Long assetId, Asset asset, MultipartFile file);
}
