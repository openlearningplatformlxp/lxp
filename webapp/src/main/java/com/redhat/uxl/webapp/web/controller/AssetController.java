package com.redhat.uxl.webapp.web.controller;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.services.service.AssetService;
import com.redhat.uxl.webapp.web.rest.dto.DetailedAssetDTO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

/**
 * The type Asset controller.
 */
@Controller
@RequestMapping(value = "/asset")
@Slf4j
public class AssetController extends AssetBaseController {

    @Inject
    private AssetService assetService;

    /**
     * Fetch file content.
     *
     * @param file     the file
     * @param response the response
     */
    @RequestMapping(value = "/fetch", method = RequestMethod.POST)
    public final void fetchFileContent(@RequestParam(value = "file", required = false) MultipartFile file,
            HttpServletResponse response) {
        resendFileContentHelper(file, response);
    }

    /**
     * Gets asset by id.
     *
     * @param assetId  the asset id
     * @param request  the request
     * @param response the response
     */
    @RequestMapping(value = "/id/{assetId}/**")
    public final void getAssetById(@PathVariable Long assetId, HttpServletRequest request,
            HttpServletResponse response) {
        Asset asset = assetService.getAsset(assetId);

        getAsset(asset, response);
    }

    /**
     * Gets asset by path.
     *
     * @param request  the request
     * @param response the response
     */
    @RequestMapping(value = "/path/**")
    public final void getAssetByPath(HttpServletRequest request, HttpServletResponse response) {
        String pathAndFilename = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        pathAndFilename = pathAndFilename.replaceFirst("/asset/path", "");

        if (StrUtils.isBlank(pathAndFilename)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        Asset asset = assetService.getAssetByPathAndFilename(pathAndFilename);

        getAsset(asset, response);
    }

    /**
     * Add asset file and metadata.
     *
     * @param name           the name
     * @param description    the description
     * @param path           the path
     * @param filename       the filename
     * @param content        the content
     * @param contentType    the content type
     * @param assetStoreType the asset store type
     * @param assetTypeId    the asset type id
     * @param assetSubtypeId the asset subtype id
     * @param file           the file
     * @param response       the response
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public final void addAssetFileAndMetadata(@RequestParam String name,
            @RequestParam(required = false) String description, @RequestParam(required = false) String path,
            @RequestParam String filename, @RequestParam(required = false) String content,
            @RequestParam String contentType, @RequestParam String assetStoreType, @RequestParam Long assetTypeId,
            @RequestParam Long assetSubtypeId, @RequestParam(required = false) MultipartFile file,
            HttpServletResponse response) {

        DetailedAssetDTO assetDTO = DetailedAssetDTO.valueOf(null, name, description, path, filename, content,
                contentType, assetStoreType, assetTypeId, assetSubtypeId);

        if (assetDTO == null || assetDTO.getId() != null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        Asset asset = assetService.addAsset(DetailedAssetDTO.toAsset(assetDTO), file);
    }

    /**
     * Upload asset file and metadata.
     *
     * @param assetId        the asset id
     * @param id             the id
     * @param name           the name
     * @param description    the description
     * @param path           the path
     * @param filename       the filename
     * @param content        the content
     * @param contentType    the content type
     * @param assetStoreType the asset store type
     * @param assetTypeId    the asset type id
     * @param assetSubtypeId the asset subtype id
     * @param file           the file
     * @param response       the response
     */
    @RequestMapping(value = "/upload/{assetId}", method = RequestMethod.POST)
    public final void uploadAssetFileAndMetadata(@PathVariable Long assetId, @RequestParam Long id,
            @RequestParam String name, @RequestParam(required = false) String description,
            @RequestParam(required = false) String path, @RequestParam String filename,
            @RequestParam(required = false) String content, @RequestParam String contentType,
            @RequestParam String assetStoreType, @RequestParam Long assetTypeId, @RequestParam Long assetSubtypeId,
            @RequestParam(required = false) MultipartFile file, HttpServletResponse response) {

        DetailedAssetDTO assetDTO = DetailedAssetDTO.valueOf(id, name, description, path, filename, content,
                contentType, assetStoreType, assetTypeId, assetSubtypeId);

        if (assetDTO == null || assetDTO.getId() == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        } else if (!assetId.equals(assetDTO.getId())) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
                    "Path variable id does not match body content id.");
        }

        Asset asset = assetService.updateAsset(assetId, DetailedAssetDTO.toAsset(assetDTO), file);
    }
}
