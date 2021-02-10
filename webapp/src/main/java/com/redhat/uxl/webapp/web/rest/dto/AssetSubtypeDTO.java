package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.AssetSubtype;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Asset subtype dto.
 */
@Data
public class AssetSubtypeDTO {
    private Long id;

    private String name;
    private String defaultContentType;
    private boolean text;
    private boolean enabled;

    /**
     * To asset subtype id only asset subtype.
     *
     * @return the asset subtype
     */
    public AssetSubtype toAssetSubtypeIdOnly() {
        AssetSubtype assetSubtype = new AssetSubtype();

        assetSubtype.setId(getId());

        return assetSubtype;
    }

    /**
     * Value of asset subtype dto.
     *
     * @param assetSubtype the asset subtype
     * @return the asset subtype dto
     */
    public static AssetSubtypeDTO valueOf(AssetSubtype assetSubtype) {
        if (assetSubtype == null) {
            return new AssetSubtypeDTO();
        }

        AssetSubtypeDTO dto = new AssetSubtypeDTO();

        dto.setId(assetSubtype.getId());
        dto.setName(assetSubtype.getName());
        dto.setDefaultContentType(assetSubtype.getDefaultContentType());
        dto.setText(assetSubtype.isText());
        dto.setEnabled(assetSubtype.isEnabled());

        return dto;
    }

    /**
     * Value of asset subtype dto.
     *
     * @param assetSubtypeId the asset subtype id
     * @return the asset subtype dto
     */
    public static AssetSubtypeDTO valueOf(Long assetSubtypeId) {
        AssetSubtypeDTO dto = new AssetSubtypeDTO();

        dto.setId(assetSubtypeId);

        return dto;
    }

    /**
     * Value of list.
     *
     * @param assetSubtypes the asset subtypes
     * @return the list
     */
    public static List<AssetSubtypeDTO> valueOf(List<AssetSubtype> assetSubtypes) {
        if (assetSubtypes == null || assetSubtypes.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<AssetSubtypeDTO> dtos = new ArrayList<>(assetSubtypes.size());

        for (AssetSubtype assetSubtype : assetSubtypes) {
            dtos.add(AssetSubtypeDTO.valueOf(assetSubtype));
        }

        return dtos;
    }
}
