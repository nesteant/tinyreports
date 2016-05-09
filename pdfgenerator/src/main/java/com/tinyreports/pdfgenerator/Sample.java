package com.tinyreports.pdfgenerator;

import com.tinyreports.pdfgenerator.render.TinyRenderer;

import javax.net.ssl.*;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * User: Antonns
 * Date: 12.04.12
 * Time: 22:43
 */
public class Sample {
    public static void main(String[] args) throws Exception {

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // First create a trust manager that won't care.
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
//
//		// Now put the trust manager into an SSLContext.
//		SSLContext sslcontext = SSLContext.getInstance("TLS");
//		sslcontext.init(null, new TrustManager[] {trustManager}, null);
//
//		// Use the above SSLContext to create your socket factory
//		// (I found trying to extend the factory a bit difficult due to a
//		// call to createSocket with no arguments, a method which doesn't
//		// exist anywhere I can find, but hey-ho).
//		SSLSocketFactory sf = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//		// If you want a thread safe client, use the ThreadSafeConManager, but
//		// otherwise just grab the one from the current client, and get hold of its
//		// schema registry. THIS IS THE KEY THING.
//		SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();
//		// Register our new socket factory with the typical SSL port and the
//		// correct protocol name.
//		schemeRegistry.register(new Scheme("https", 443, sf));
        TinyRenderer renderer = new TinyRenderer("/tmp/test render");
        //URL reportUrl = new URL("https://ws.local.opvizor.com/webservice/ws/reporting/render/6?isdesk-access-token=06c1244f-712d-4897-95f3-680611f8f0ea&isdesk-user-timezone=GMT%2B03%3A00&reportType=text/html");
        URL reportUrl = new URL("https://ws.opvizor.com/webservice/ws/reporting/render/40?isdesk-access-token=3897bbcc-b246-4e52-ad5e-b504727614aa&isdesk-user-timezone=GMT%2B03%3A00&reportType=text/html");
        FileOutputStream str = new FileOutputStream("/tmp/result.pdf");
        renderer.setUnwrapSvg(true);
        renderer.render(reportUrl, 1000, str);
    }
}
