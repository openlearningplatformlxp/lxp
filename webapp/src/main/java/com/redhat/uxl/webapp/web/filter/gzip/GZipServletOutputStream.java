package com.redhat.uxl.webapp.web.filter.gzip;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * The type G zip servlet output stream.
 */
class GZipServletOutputStream extends ServletOutputStream {
    private OutputStream stream;

    /**
     * Instantiates a new G zip servlet output stream.
     *
     * @param output the output
     * @throws IOException the io exception
     */
    public GZipServletOutputStream(OutputStream output) throws IOException {
        super();
        this.stream = output;
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener listener) {

    }
}
