package de.robertbloy.openshift.rest.ApiTest;

import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLSession;

import com.openshift.restclient.ClientFactory;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ISSLCertificateCallback;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.authorization.TokenAuthorizationStrategy;
import com.openshift.restclient.model.IProject;

/**
 * Hello world!
 *
 */
public class App 
{
	private SslCertCallback sslCertCallback = new SslCertCallback();
	
    public static void main( String[] args )
    {
    	new App().execute();
    }

	private void execute() {
		
		String url = "https://ec2-54-88-56-127.compute-1.amazonaws.com:8443";
		//String url = "https://www.google.de";
		
		IClient client = new ClientFactory().create(url, sslCertCallback);
		client.setAuthorizationStrategy(new TokenAuthorizationStrategy("ADSASEAWRA-AFAEWAAA"));
		List<IProject> projects = client.list(ResourceKind.PROJECT, "test-namespace");
		
	}
	
	class SslCertCallback implements ISSLCertificateCallback {

		public boolean allowCertificate(X509Certificate[] chain) {
			return true;
		}

		public boolean allowHostname(String hostname, SSLSession session) {
			System.out.println ( "allowHostname: " + hostname);
			return true;
		}
		
	}
}
