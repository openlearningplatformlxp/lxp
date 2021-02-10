package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.dataobjects.domain.AssetDb;
import com.redhat.uxl.dataobjects.domain.AssetS3;
import com.redhat.uxl.dataobjects.domain.types.AssetStoreType;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Detailed asset dto.
 */
@Data
public class DetailedAssetDTO extends AssetDTO {
    private AssetStoreType assetStoreType;

    private String content;
    private String contentType;

    private String createdBy;
    private DateTime createdDate;
    private String lastModifiedBy;
    private DateTime lastModifiedDate;

    /**
     * Value of detailed asset dto.
     *
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
     * @return the detailed asset dto
     */
    public static DetailedAssetDTO valueOf(Long id, String name, String description, String path, String filename,
            String content, String contentType, String assetStoreType, Long assetTypeId, Long assetSubtypeId) {
        DetailedAssetDTO dto = new DetailedAssetDTO();

        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setPath(path);
        dto.setFilename(filename);
        dto.setContent(content);
        dto.setContentType(contentType);
        dto.setAssetStoreType(AssetStoreType.valueOf(assetStoreType));
        dto.setAssetType(AssetTypeDTO.valueOf(assetTypeId));
        dto.setAssetSubtype(AssetSubtypeDTO.valueOf(assetSubtypeId));

        return dto;
    }

    /**
     * To asset asset.
     *
     * @param dto the dto
     * @return the asset
     */
    public static Asset toAsset(DetailedAssetDTO dto) {
        if (dto == null) {
            throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST);
        }

        Asset asset;

        if (AssetStoreType.DB.equals(dto.getAssetStoreType())) {
            AssetDb assetDb = new AssetDb();

            assetDb.setAssetStoreType(AssetStoreType.DB);
            assetDb.setContent(dto.getContent() != null ? dto.getContent().getBytes() : null);
            assetDb.setContentType(dto.getContentType());

            asset = assetDb;
        } else if (AssetStoreType.S3.equals(dto.getAssetStoreType())) {
            AssetS3 assetS3 = new AssetS3();

            assetS3.setAssetStoreType(AssetStoreType.S3);
            assetS3.setContentType(dto.getContentType());

            asset = assetS3;
        } else {
            asset = new Asset();
        }

        asset.setId(dto.getId());
        asset.setAssetType(dto.getAssetType().toAssetTypeIdOnly());
        asset.setAssetSubtype(dto.getAssetSubtype().toAssetSubtypeIdOnly());
        asset.setName(dto.getName());
        asset.setFilename(dto.getFilename());
        asset.setPath(dto.getPath());
        asset.setDescription(dto.getDescription());

        return asset;
    }
}
