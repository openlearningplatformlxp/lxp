package com.redhat.uxl.webapp.web.controller;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.services.service.AssetService;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

/**
 * The type Pdf controller.
 */
@Controller
@RequestMapping(value = "/pdf")
@Slf4j
public class PdfController extends AssetBaseController {

    @Inject
    private AssetService assetService;

    /**
     * Generate pdf by path.
     *
     * @param request  the request
     * @param response the response
     */
    @RequestMapping(value = "/generate/**")
    public final void generatePdfByPath(HttpServletRequest request, HttpServletResponse response) {
        String pathAndFilename = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        pathAndFilename = pathAndFilename.replaceFirst("/pdf/generate", "");

        if (StrUtils.isBlank(pathAndFilename)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        Asset asset = assetService.getAssetByPathAndFilename(pathAndFilename);

        getPdfAsset(asset, templateOptions(), response);
    }
}
