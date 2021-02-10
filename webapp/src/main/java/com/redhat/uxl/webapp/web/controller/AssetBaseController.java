package com.redhat.uxl.webapp.web.controller;

import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.dataobjects.domain.Asset;
import com.redhat.uxl.dataobjects.domain.AssetDb;
import com.redhat.uxl.dataobjects.domain.AssetS3;
import com.redhat.uxl.services.service.AmazonS3Service;
import com.redhat.uxl.services.service.AssetService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.util.XRRuntimeException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * The type Asset base controller.
 */
@Component
@Slf4j
public class AssetBaseController extends AbstractBaseController {
    private static final int MAX_READ_SIZE = 64000;

    @Inject
    private AmazonS3Service amazonS3Service;

    @Inject
    private AssetService assetService;

    /**
     * Gets asset.
     *
     * @param asset    the asset
     * @param response the response
     */
    protected void getAsset(Asset asset, HttpServletResponse response) {
        if (asset == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        if (asset instanceof AssetDb) {
            getAssetDb((AssetDb) asset, response);
        } else if (asset instanceof AssetS3) {
            getAssetS3((AssetS3) asset, response);
        } else {
            response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
        }
    }

    /**
     * Gets asset db.
     *
     * @param assetDb  the asset db
     * @param response the response
     */
    protected void getAssetDb(AssetDb assetDb, HttpServletResponse response) {
        // Set response headers and info - must be done before setting response headers and info.

        response.setHeader("Cache-Control", "public");
        response.addHeader("Cache-Control", "max-age=86400"); // cache locally for 1 day max

        response.setContentType(assetDb.getContentType());
        response.setStatus(HttpStatus.OK.value());

        OutputStream responseOutStream = null;

        try {
            responseOutStream = response.getOutputStream();

            responseOutStream.write(assetDb.getContent(), 0, assetDb.getContent().length);
        } catch (IOException e) {
            log.error("Asset IOException for Asset Id: {}", assetDb.getId());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (responseOutStream != null) {
                try {
                    responseOutStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close asset response output stream.", e);
                }
            }
        }
    }

    /**
     * Gets asset s 3.
     *
     * @param assetS3  the asset s 3
     * @param response the response
     */
    protected void getAssetS3(AssetS3 assetS3, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();

        if (StrUtils.isNotBlank(assetS3.getPath())) {
            sb.append(assetS3.getPath()).append('/');
        }

        sb.append(assetS3.getFilename());

        try {
            response.sendRedirect(amazonS3Service.makeExternalURL(sb.toString()));
        } catch (Exception e) {
            log.error("Unable to redirect to S3 asset.");
        }
    }

    /**
     * Gets pdf asset.
     *
     * @param asset           the asset
     * @param templateOptions the template options
     * @param response        the response
     */
    protected void getPdfAsset(Asset asset, TemplateOptions templateOptions, HttpServletResponse response) {
        if (asset == null) {
            outputPdfError(HttpStatus.NOT_FOUND, "PDF File or Template not found!", templateOptions, response);
            return;
        }

        if (asset instanceof AssetDb) {
            getPdfAssetDb((AssetDb) asset, templateOptions, response);
        } else if (asset instanceof AssetS3) {
            outputPdfError(HttpStatus.NOT_IMPLEMENTED, "AssetS3 not supported for PDF File or Template!",
                    templateOptions, response);
        } else {
            outputPdfError(HttpStatus.NOT_IMPLEMENTED, "Asset type not supported for PDF File or Template!",
                    templateOptions, response);
        }
    }

    /**
     * Gets pdf asset db.
     *
     * @param assetDb         the asset db
     * @param templateOptions the template options
     * @param response        the response
     */
    protected void getPdfAssetDb(AssetDb assetDb, TemplateOptions templateOptions, HttpServletResponse response) {
        try {
            String content = assetService.mergeTemplateIntoString(assetDb, CharEncoding.UTF_8,
                    templateOptions.getModel());

            if (assetDb != null && StrUtils.isBlank(templateOptions.getFilename())) {
                templateOptions.setFilename(assetDb.getFilename());
            }

            outputPdf(HttpStatus.OK, new StringBuilder(content), templateOptions, response);
        } catch (Exception e) {
            outputPdfError(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating PDF!", e, templateOptions, response);
        }
    }

    /**
     * Output pdf.
     *
     * @param status              the status
     * @param renderedPdfTemplate the rendered pdf template
     * @param templateOptions     the template options
     * @param response            the response
     */
    protected void outputPdf(HttpStatus status, StringBuilder renderedPdfTemplate, TemplateOptions templateOptions,
            HttpServletResponse response) {
        outputPdf(status, renderedPdfTemplate, templateOptions, response, true);
    }

    /**
     * Output pdf.
     *
     * @param status              the status
     * @param renderedPdfTemplate the rendered pdf template
     * @param templateOptions     the template options
     * @param response            the response
     * @param outputErrorAsPdf    the output error as pdf
     */
    protected void outputPdf(HttpStatus status, StringBuilder renderedPdfTemplate, TemplateOptions templateOptions,
            HttpServletResponse response, boolean outputErrorAsPdf) {
        ITextRenderer renderer = new ITextRenderer();

        try {
            Document document = XMLResource.load(new ByteArrayInputStream(renderedPdfTemplate.toString().getBytes()))
                    .getDocument();

            renderer.setDocument(document, null);
            renderer.layout();
        } catch (XRRuntimeException e) {
            if (outputErrorAsPdf) {
                outputPdfError(HttpStatus.BAD_REQUEST, "Template error - probably invalid xhtml OR unescaped data.", e,
                        templateOptions, response);
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            return;
        } catch (Exception e) {
            if (outputErrorAsPdf) {
                outputPdfError(HttpStatus.BAD_REQUEST, "Error creating pdf.", e, templateOptions, response);
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            return;
        }

        response.setDateHeader(HttpHeaders.EXPIRES, 0);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "post-check=0, pre-check=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, (templateOptions.isDownload() ? "attachment" : "inline")
                + "; filename=\"" + templateOptions.getFilename() + "\"");

        response.setContentType("application/pdf");

        response.setStatus(HttpStatus.OK.value());
        // response.setStatus(status.value()); // Response status must be 200 for Chrome to render the
        // PDF.

        OutputStream responseOutStream = null;

        try {
            responseOutStream = response.getOutputStream();
            renderer.createPDF(responseOutStream);
        } catch (XRRuntimeException e) {
            if (outputErrorAsPdf) {
                outputPdfError(HttpStatus.BAD_REQUEST, "Template error - probably invalid xhtml OR unescaped data.", e,
                        templateOptions, response);
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } catch (Exception e) {
            if (outputErrorAsPdf) {
                outputPdfError(HttpStatus.BAD_REQUEST, "Error creating pdf.", e, templateOptions, response);
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } finally {
            if (responseOutStream != null) {
                try {
                    responseOutStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close pdf response output stream.", e);
                }
            }
        }
    }

    /**
     * Output pdf error.
     *
     * @param status          the status
     * @param errorMessage    the error message
     * @param templateOptions the template options
     * @param response        the response
     */
    protected void outputPdfError(HttpStatus status, String errorMessage, TemplateOptions templateOptions,
            HttpServletResponse response) {
        outputPdfError(status, errorMessage, null, templateOptions, response);
    }

    /**
     * Output pdf error.
     *
     * @param status          the status
     * @param errorMessage    the error message
     * @param e               the e
     * @param templateOptions the template options
     * @param response        the response
     */
    protected void outputPdfError(HttpStatus status, String errorMessage, Exception e, TemplateOptions templateOptions,
            HttpServletResponse response) {
        StringBuilder xhtml = new StringBuilder();

        xhtml.append("<html>");

        if (e != null) {
            xhtml.append("    <head>");
            xhtml.append("        <style>");
            xhtml.append("            @page {size: landscape;}");
            xhtml.append("        </style>");
            xhtml.append("    </head>");
        }

        xhtml.append("    <body>");
        xhtml.append("        <h2 style=\"padding-top: 50px; text-align: center;\">Error:</h2>");
        xhtml.append("        <p style=\"padding-top: 5px; text-align: center;\">");
        xhtml.append(StringEscapeUtils.escapeHtml4(errorMessage));
        xhtml.append("        </p>");

        if (e != null) {
            xhtml.append("        <pre style=\"font-size: 10px; white-space: pre-wrap;\">");
            xhtml.append(StringEscapeUtils.escapeHtml4(org.xhtmlrenderer.util.Util.stack_to_string(e)));
            xhtml.append("        </pre>");
        }

        xhtml.append("    </body>");
        xhtml.append("</html>");

        templateOptions.setFilename("error.pdf");

        outputPdf(status, xhtml, templateOptions, response, false);
    }

    /**
     * Resend file content helper.
     *
     * @param file     the file
     * @param response the response
     */
    protected void resendFileContentHelper(MultipartFile file, HttpServletResponse response) {
        OutputStream responseOutStream = null;
        InputStream inputStream = null;

        try {
            byte[] byteBuffer = new byte[MAX_READ_SIZE];

            inputStream = file.getInputStream();

            int readCount = inputStream.read(byteBuffer);

            // Set response headers and info - must be done before setting response content.

            response.setHeader("Cache-Control", "public");
            response.addHeader("Cache-Control", "max-age=0");

            response.setContentType("text/plain");
            response.setStatus(HttpStatus.OK.value());

            // Return content - must be done after setting response headers and info.

            responseOutStream = response.getOutputStream();

            while (readCount != -1) {
                responseOutStream.write(byteBuffer, 0, readCount);

                readCount = inputStream.read(byteBuffer);
            }
        } catch (IOException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close file input stream.", e);
                }
            }

            if (responseOutStream != null) {
                try {
                    responseOutStream.close();
                } catch (Exception e) {
                    log.debug("Unable to close response output stream.", e);
                }
            }
        }
    }

    /**
     * Template options template options.
     *
     * @return the template options
     */
    protected TemplateOptions templateOptions() {
        return templateOptions(null, false, null);
    }

    /**
     * Template options template options.
     *
     * @param model    the model
     * @param download the download
     * @param filename the filename
     * @return the template options
     */
    protected TemplateOptions templateOptions(Map model, boolean download, String filename) {
        TemplateOptions options = new TemplateOptions();

        options.setModel(model);
        options.setDownload(download);
        options.setFilename(filename);

        return options;
    }

    /**
     * The type Template options.
     */
    @Data
    protected class TemplateOptions {
        private boolean download;
        private String filename;
        private Map model;
    }
}
