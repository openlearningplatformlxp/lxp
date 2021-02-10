package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.errors.code.ErrorCodeGeneral;
import com.redhat.uxl.commonjava.errors.exception.GeneralException;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.datalayer.repository.AssetRepository;
import com.redhat.uxl.datalayer.repository.AssetSubtypeRepository;
import com.redhat.uxl.datalayer.repository.AssetTypeRepository;
import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.dataobjects.domain.AssetDb;
import com.redhat.uxl.dataobjects.domain.AssetS3;
import com.redhat.uxl.dataobjects.domain.AssetSubtype;
import com.redhat.uxl.dataobjects.domain.AssetType;
import com.redhat.uxl.dataobjects.domain.types.AssetStoreType;
import com.redhat.uxl.services.service.AmazonS3Service;
import com.redhat.uxl.services.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * The type Asset service.
 */
@Service
@Slf4j
@Transactional
public class AssetServiceImpl implements AssetService {
  @Inject
  private AmazonS3Service amazonS3Service;

  @Inject
  private AssetRepository assetRepository;

  @Inject
  private AssetSubtypeRepository assetSubtypeRepository;

  @Inject
  private AssetTypeRepository assetTypeRepository;

  @Override
  @Timed
  @Transactional
  public Asset addAsset(Asset asset, MultipartFile file) {
    if (asset == null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Given Asset cannot be null.");
    }

    if (asset instanceof AssetDb) {
      byte[] fileContents = null;

      if (file != null) {
        try {
          fileContents = file.getBytes();
        } catch (IOException e) {
          throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR,
              "Unable to get file content.");
        }
      }

      return addAsset((AssetDb) asset, fileContents);
    } else if (asset instanceof AssetS3) {
      return addAsset((AssetS3) asset, file);
    }

    throw new GeneralException(ErrorCodeGeneral.NOT_IMPLEMENTED,
        "Add Asset not implemented for given asset type: " + asset.getAssetStoreType());
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Asset getAsset(Long id) {
    Asset asset = assetRepository.findOne(id);

    if (asset != null && asset.getAssetType() != null
        && asset.getAssetType().getSubtypes() != null) {
      asset.getAssetType().setSubtypes(null); // we don't need the subtypes... so don't get them
    }

    return asset;
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public Asset getAssetByPathAndFilename(String pathAndFilename) {
    if (StrUtils.isBlank(pathAndFilename)) {
      return null;
    }

    String path = "";
    String filename = "";
    int index = pathAndFilename.lastIndexOf("/");

    if (index >= 0) {
      path = pathAndFilename.substring(0, index);
      filename = pathAndFilename.substring(index + 1);
    }

    if (StrUtils.isNotBlank(path)) {
      return assetRepository.findByPathAndFilename(path, filename);
    } else {
      return assetRepository.findByPathIsNullAndFilename(filename);
    }
  }

  @Override
  @Timed
  @Transactional(readOnly = true)
  public String mergeTemplateIntoString(String templateLocation, String encoding,
      Map<String, Object> model) {
    Asset asset = getAssetByPathAndFilename(templateLocation);

    if (asset == null) {
      log.warn("No Asset for Template - path: {}", templateLocation);
      return null;
    }

    return mergeTemplateIntoString(asset, encoding, model);
  }

  @Override
  @Timed
  public String mergeTemplateIntoString(Asset asset, String encoding, Map<String, Object> model) {
    String content;

    try {
      if (asset == null) {
        log.warn("No Asset for Template");
        return null;
      } else if (!AssetStoreType.DB.equals(asset.getAssetStoreType())) {
        log.warn("Only DB Store Type is supported for Templates.");
        return null;
      }

      AssetDb assetDb = (AssetDb) asset;
      byte[] contentBytes = assetDb.getContent();

      if (contentBytes == null) {
        return null;
      }

      RuntimeServices rs = RuntimeSingleton.getRuntimeServices();
      StringReader sr = new StringReader(new String(contentBytes, encoding));
      SimpleNode sn = rs.parse(sr, "theTemplate");

      Template t = new Template();
      t.setRuntimeServices(rs);
      t.setData(sn);
      t.setEncoding(encoding);
      t.initDocument();

      VelocityContext vc = new VelocityContext(model);

      StringWriter sw = new StringWriter();
      t.merge(vc, sw);

      content = sw.toString();
    } catch (Exception e) {
      log.error("Could not fill Template - exception: {}", e.getMessage());

      return null;
    }

    return content;
  }

  @Override
  @Timed
  @Transactional
  public Asset updateAsset(Long assetId, Asset asset, MultipartFile file) {
    if (asset == null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Given Asset cannot be null.");
    } else if (!assetId.equals(asset.getId())) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Passed Asset Id does not match Entity Asset Id.");
    }

    if (asset instanceof AssetDb) {
      byte[] fileContents = null;

      if (file != null) {
        try {
          fileContents = file.getBytes();
        } catch (IOException e) {
          throw new GeneralException(ErrorCodeGeneral.INTERNAL_SERVER_ERROR,
              "Unable to get file content.");
        }
      }

      return updateAsset(assetId, (AssetDb) asset, fileContents);
    } else if (asset instanceof AssetS3) {
      return updateAsset(assetId, (AssetS3) asset, file);
    }

    throw new GeneralException(ErrorCodeGeneral.NOT_IMPLEMENTED,
        "Add Asset not implemented for given asset type: " + asset.getAssetStoreType());
  }

  // Private Helper Methods

  private Asset addAsset(AssetDb assetDb, byte[] fileContents) {
    if (assetDb == null || assetDb.getId() != null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Passed Asset is invalid.");
    }

    AssetType assetType = assetTypeRepository.findOne(assetDb.getAssetType().getId());
    AssetSubtype assetSubtype = assetSubtypeRepository.findOne(assetDb.getAssetSubtype().getId());

    AssetDb addAsset = new AssetDb();

    addAsset.setAssetType(assetType);
    addAsset.setAssetSubtype(assetSubtype);
    addAsset.setName(assetDb.getName());
    addAsset.setPath(assetDb.getPath());
    addAsset.setFilename(assetDb.getFilename());
    addAsset.setDescription(assetDb.getDescription());

    // AssetDB specific fields

    addAsset.setAssetStoreType(AssetStoreType.DB);
    addAsset.setContent(fileContents != null ? fileContents : assetDb.getContent());
    addAsset.setContentType(assetDb.getContentType());

    if (addAsset != null && addAsset.getAssetType() != null
        && addAsset.getAssetType().getSubtypes() != null) {
      addAsset.getAssetType().getSubtypes().size(); // load the subtypes
    }

    return assetRepository.save(addAsset);
  }

  private Asset addAsset(AssetS3 assetS3, MultipartFile file) {
    if (assetS3 == null || assetS3.getId() != null) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST, "Passed Asset is invalid.");
    }

    AssetType assetType = assetTypeRepository.findOne(assetS3.getAssetType().getId());
    AssetSubtype assetSubtype = assetSubtypeRepository.findOne(assetS3.getAssetSubtype().getId());

    AssetS3 addAsset = new AssetS3();

    addAsset.setAssetType(assetType);
    addAsset.setAssetSubtype(assetSubtype);
    addAsset.setName(assetS3.getName());
    addAsset.setPath(assetS3.getPath());
    addAsset.setFilename(assetS3.getFilename());
    addAsset.setDescription(assetS3.getDescription());

    // AssetS3 specific fields

    addAsset.setAssetStoreType(AssetStoreType.S3);
    addAsset.setContentType(assetS3.getContentType());

    if (file != null) {
      try {
        amazonS3Service.uploadFile(file,
            (addAsset.getPath() != null ? addAsset.getPath() : "") + "/" + addAsset.getFilename());
      } catch (Exception e) {
        int failed = 1;
      }
    }

    if (addAsset != null && addAsset.getAssetType() != null
        && addAsset.getAssetType().getSubtypes() != null) {
      addAsset.getAssetType().getSubtypes().size(); // load the subtypes
    }

    return assetRepository.save(addAsset);
  }

  private Asset updateAsset(Long assetId, AssetDb assetDb, byte[] fileContents) {
    if (!assetId.equals(assetDb.getId())) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Passed Asset Id does not match Entity Asset Id.");
    }

    AssetType assetType = assetTypeRepository.findOne(assetDb.getAssetType().getId());
    AssetSubtype assetSubtype = assetSubtypeRepository.findOne(assetDb.getAssetSubtype().getId());

    Asset updateAsset = assetRepository.findOne(assetId);

    updateAsset.setAssetType(assetType);
    updateAsset.setAssetSubtype(assetSubtype);
    updateAsset.setName(assetDb.getName());
    updateAsset.setPath(assetDb.getPath());
    updateAsset.setFilename(assetDb.getFilename());
    updateAsset.setDescription(assetDb.getDescription());

    // Asset DB specific fields

    AssetDb updateAssetDb = (AssetDb) updateAsset;

    if (fileContents != null) {
      updateAssetDb.setContent(fileContents);
    } else if (assetDb.getContent() != null) {
      updateAssetDb.setContent(assetDb.getContent());
    }

    updateAssetDb.setContentType(assetDb.getContentType());

    updateAsset = assetRepository.saveAndFlush(updateAsset);

    if (updateAsset != null && updateAsset.getAssetType() != null
        && updateAsset.getAssetType().getSubtypes() != null) {
      updateAsset.getAssetType().getSubtypes().size(); // load the subtypes
    }

    return updateAsset;
  }

  private Asset updateAsset(Long assetId, AssetS3 assetS3, MultipartFile file) {
    if (!assetId.equals(assetS3.getId())) {
      throw new GeneralException(ErrorCodeGeneral.BAD_REQUEST,
          "Passed Asset Id does not match Entity Asset Id.");
    }

    AssetType assetType = assetTypeRepository.findOne(assetS3.getAssetType().getId());
    AssetSubtype assetSubtype = assetSubtypeRepository.findOne(assetS3.getAssetSubtype().getId());

    Asset updateAsset = assetRepository.findOne(assetId);

    updateAsset.setAssetType(assetType);
    updateAsset.setAssetSubtype(assetSubtype);
    updateAsset.setName(assetS3.getName());
    updateAsset.setPath(assetS3.getPath());
    updateAsset.setFilename(assetS3.getFilename());
    updateAsset.setDescription(assetS3.getDescription());

    // Asset S3 specific fields

    AssetS3 updateAssetDb = (AssetS3) updateAsset;

    updateAssetDb.setContentType(assetS3.getContentType());

    updateAsset = assetRepository.saveAndFlush(updateAsset);

    if (file != null) {
      try {
        amazonS3Service.uploadFile(file,
            (updateAssetDb.getPath() != null ? updateAssetDb.getPath() : "") + "/"
                + updateAssetDb.getFilename());
      } catch (Exception e) {
        int failed = 1;
      }
    }

    if (updateAsset != null && updateAsset.getAssetType() != null
        && updateAsset.getAssetType().getSubtypes() != null) {
      updateAsset.getAssetType().getSubtypes().size(); // load the subtypes
    }

    return updateAsset;
  }
}
