package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.AssetType;
import lombok.Data;

import java.util.List;

/**
 * The type Asset type dto.
 */
@Data
public class AssetTypeDTO {
    private Long id;

    private String name;
    private boolean enabled;

    private List<AssetSubtypeDTO> subtypes;

    /**
     * To asset type id only asset type.
     *
     * @return the asset type
     */
    public AssetType toAssetTypeIdOnly() {
        AssetType assetType = new AssetType();

        assetType.setId(getId());

        return assetType;
    }

    /**
     * Value of asset type dto.
     *
     * @param assetType the asset type
     * @return the asset type dto
     */
    public static AssetTypeDTO valueOf(AssetType assetType) {
        if (assetType == null) {
            return new AssetTypeDTO();
        }

        AssetTypeDTO dto = new AssetTypeDTO();

        dto.setId(assetType.getId());
        dto.setName(assetType.getName());
        dto.setEnabled(assetType.isEnabled());
        dto.setSubtypes(AssetSubtypeDTO.valueOf(assetType.getSubtypes()));

        return dto;
    }

    /**
     * Value of asset type dto.
     *
     * @param assetTypeId the asset type id
     * @return the asset type dto
     */
    public static AssetTypeDTO valueOf(Long assetTypeId) {
        AssetTypeDTO dto = new AssetTypeDTO();

        dto.setId(assetTypeId);

        return dto;
    }

}
