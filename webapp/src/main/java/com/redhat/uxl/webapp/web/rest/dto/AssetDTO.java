package com.redhat.uxl.webapp.web.rest.dto;

import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.dataobjects.domain.types.AssetStoreType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * The type Asset dto.
 */
@Data
public class AssetDTO {
    private Long id;

    private AssetStoreType assetStoreType;
    private AssetTypeDTO assetType;
    private AssetSubtypeDTO assetSubtype;

    private String name;
    private String filename;
    private String path;
    private String description;

    /**
     * Value of asset dto.
     *
     * @param asset the asset
     * @return the asset dto
     */
    public static AssetDTO valueOf(Asset asset) {
        if (asset == null) {
            return new AssetDTO();
        }

        AssetDTO dto = new AssetDTO();

        dto.setId(asset.getId());
        dto.setAssetStoreType(asset.getAssetStoreType());
        dto.setAssetType(AssetTypeDTO.valueOf(asset.getAssetType()));
        dto.setAssetSubtype(AssetSubtypeDTO.valueOf(asset.getAssetSubtype()));
        dto.setName(asset.getName());
        dto.setFilename(asset.getFilename());
        dto.setPath(asset.getPath());
        dto.setDescription(asset.getDescription());

        return dto;
    }

    /**
     * Value of page.
     *
     * @param assetPage the asset page
     * @return the page
     */
    public static Page<AssetDTO> valueOf(Page<Asset> assetPage) {
        if (assetPage == null || !assetPage.hasContent()) {
            return new PageImpl<>(new ArrayList<>(0));
        }

        List<AssetDTO> dtos = new ArrayList<>(assetPage.getNumberOfElements());

        for (Asset asset : assetPage.getContent()) {
            dtos.add(AssetDTO.valueOf(asset));
        }

        Pageable pageable = PageRequest.of(assetPage.getNumber(), assetPage.getSize(), assetPage.getSort());

        return new PageImpl<>(dtos, pageable, assetPage.getTotalElements());
    }

    // Private Helper Methods

}
