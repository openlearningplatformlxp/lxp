package com.redhat.uxl.webapp.web.rest.util;

import java.io.IOException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 * The type Ssl socket factory wrapper.
 */
public class SSLSocketFactoryWrapper extends SSLConnectionSocketFactory {

    private SSLParameters sslParameters;

    /**
     * Instantiates a new Ssl socket factory wrapper.
     *
     * @param sslContext the ssl context
     */
    public SSLSocketFactoryWrapper(SSLContext sslContext) {
        super(sslContext);
    }

    @Override
    protected void prepareSocket(SSLSocket socket) throws IOException {
        setParameters(socket);
    }

    /**
     * Sets ssl parameters.
     *
     * @param sslParameters the ssl parameters
     */
    public void setSslParameters(SSLParameters sslParameters) {
        this.sslParameters = sslParameters;
    }

    private void setParameters(SSLSocket socket) {
        socket.setSSLParameters(sslParameters);
    }

}
