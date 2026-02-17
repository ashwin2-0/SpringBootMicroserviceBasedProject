package com.lwd.gateway.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class SSLTrustConfig {

    @Bean
    public RestTemplate discoveryClientOptionalArgs() throws Exception {
        // 1. Setup a Trust-All strategy (Ignores that it's self-signed)
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(new TrustAllStrategy())
                .build();

        // 2. Setup NoopHostnameVerifier (Ignores that CN is 'ashwin ingle' instead of 'localhost')
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(sslSocketFactory)
                        .build())
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}

/*
    Purpose of creating this SSLTrustConfig class is to configure the RestTemplate used by the Discovery
     Client (Eureka Client) to trust the self-signed SSL certificate of the Eureka Server.

This error is slightly different from the last one, but it’s a very common hurdle when
 working with SSL. You’ve successfully bypassed the "Trust" issue, but now you’ve hit
 Hostname Verification.

What went wrong?
When you created your certificate with keytool,
you likely put "ashwin ingle" in the "First and Last Name" field (which becomes the Common Name or CN).

The error Certificate for <localhost> doesn't match common name: ashwin ingle
means Java is being a strict security guard again. It's saying: "The ID card says
the owner is 'ashwin ingle', but I'm trying to talk to 'localhost'. Since
 the names don't match, I’m blocking the connection to prevent a spoofing attack."


 */