package com.redhat.uxl.services.service.impl;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type File service.
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * The Course token.
     */
    @Value("${totara.webservice.token}")
  protected String COURSE_TOKEN;

    /**
     * The File upload.
     */
    @Value("${totara.file.upload}")
  protected String FILE_UPLOAD;

  private static final String Boundary = "--7d021a37605f0";

  @Override
  @Timed
  public void upload(Long userId, Long evidenceId, MultipartFile file, String fileArea,
      String component) throws Exception {
    System.setProperty("jsse.enableSNIExtension", "false");

    // Convert multipart to
    String tmpDirectory = System.getProperty("java.io.tmpdir")
        + System.getProperty("file.separator") + UUID.randomUUID().toString();
    File tmpDir = new File(tmpDirectory);
    tmpDir.mkdirs();

    List<File> list = new ArrayList<File>();

    File tmpFile =
        new File(tmpDirectory + System.getProperty("file.separator") + file.getOriginalFilename());
    file.transferTo(tmpFile);
    list.add(tmpFile);

    String url = FILE_UPLOAD + COURSE_TOKEN + "&userid=" + Long.toString(userId) + "&filearea="
        + fileArea + "&itemid=" + Long.toString(evidenceId) + "&component=" + component;
    upload(new URL(url), list);

    try {
      FileUtils.deleteDirectory(new File(tmpDirectory));
    } catch (Exception e) {
      // file not deleted - ok
    }
  }

  private void upload(URL url, List<File> files) throws Exception {
    HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();

    theUrlConnection.setDoOutput(true);
    theUrlConnection.setDoInput(true);
    theUrlConnection.setUseCaches(false);
    theUrlConnection.setChunkedStreamingMode(1024);

    theUrlConnection.setRequestProperty("Content-Type",
        "multipart/form-data; boundary=" + Boundary);

    DataOutputStream httpOut = new DataOutputStream(theUrlConnection.getOutputStream());

    for (int i = 0; i < files.size(); i++) {

      File f = files.get(i);
      String str = "--" + Boundary + "\r\n" + "Content-Disposition: form-data;name=\"file" + i
          + "\"; filename=\"" + f.getName() + "\"\r\n" + "Content-Type: image/bmp\r\n" + "\r\n";

      httpOut.write(str.getBytes());

      FileInputStream uploadFileReader = new FileInputStream(f);
      int numBytesToRead = 1024;
      int availableBytesToRead;
      while ((availableBytesToRead = uploadFileReader.available()) > 0) {
        byte[] bufferBytesRead;
        bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead]
            : new byte[availableBytesToRead];
        uploadFileReader.read(bufferBytesRead);
        httpOut.write(bufferBytesRead);
        httpOut.flush();
      }
      httpOut.write(("--" + Boundary + "--\r\n").getBytes());

    }

    httpOut.write(("--" + Boundary + "--\r\n").getBytes());

    httpOut.flush();
    httpOut.close();

    // read & parse the response
    InputStream is = theUrlConnection.getInputStream();
    StringBuilder response = new StringBuilder();
    byte[] respBuffer = new byte[4096];
    while (is.read(respBuffer) >= 0) {
      response.append(new String(respBuffer).trim());
    }
    is.close();
  }
}
