package org.kakia.goodreads.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraConnectionProperties {

    private File secureConnectBundle;

    public File getSecureConnectBundle() {
        System.out.println("getSecureConnectionBundle..." + secureConnectBundle==null?"false": secureConnectBundle.getAbsolutePath() );
        return secureConnectBundle;
    }

    public void setSecureConnectBundle(File secureConnectBundle) {
        System.out.println("setSecureConnectionBundle..." + secureConnectBundle==null?"false": secureConnectBundle.getAbsolutePath() );
        this.secureConnectBundle = secureConnectBundle;
    }

    
}
