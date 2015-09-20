package de.robertbloy.openshift.rest.ApiTest;

import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLSession;

import com.openshift.internal.restclient.OpenShiftAPIVersion;
import com.openshift.internal.restclient.ResourceFactory;
import com.openshift.internal.restclient.model.Project;
import com.openshift.internal.restclient.model.template.Template;
import com.openshift.restclient.ClientFactory;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ISSLCertificateCallback;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.authorization.BasicAuthorizationStrategy;
import com.openshift.restclient.authorization.TokenAuthorizationStrategy;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.template.ITemplate;
import com.openshift.restclient.utils.Samples;

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
		
		String url = "https://ec2-54-152-173-30.compute-1.amazonaws.com:8443";

		//sudo oadm policy add-cluster-role-to-user cluster-admin john --config=/etc/origin/master/admin.kubeconfig 
		BasicAuthorizationStrategy strategy = new BasicAuthorizationStrategy("john", "smith",null);
			
		IClient client = new ClientFactory().create(url, sslCertCallback);
		
		//curl -u john -kv -H "X-CSRF-Token: xxx" 'https://ec2-54-152-173-30.compute-1.amazonaws.com:8443/oauth/authorize?client_id=openshift-challenging-client&response_type=token'

		client.setAuthorizationStrategy(new TokenAuthorizationStrategy("PL9ZsSxJ3dvSI3i5GqVvTa4-8fclHrhs1BD4znR0Q_g"));
		List<IProject> projects = client.list(ResourceKind.PROJECT, "test-namespace");
		
		// Liste der Projekte
		for ( IProject project : projects ) {
			System.out.println ( project.getName()  + ", namespace: " + project.getNamespace() );
		}

//		System.out.println ( "-------------------" );
//		
//		projects = client.list(ResourceKind.PROJECT, "mein-erstes-projekt");
//		// Liste der Projekte
//		for ( IProject project : projects ) {
//			System.out.println ( project.getName() );
//		}
//		
//		// Liste der Projekte
//		for ( IProject project : projects ) {
//			System.out.println ( project.getName() );
//		}
		
		// Liste aller Templates
		List<ITemplate> templates = client.list(ResourceKind.TEMPLATE);
		for ( ITemplate template : templates ) {
			System.out.println ( template.getName() );			
		}

//		// ein Projekt erzeugen
		ResourceFactory factory = new ResourceFactory(client);
		
		Project project = factory.create("v1", ResourceKind.PROJECT);
		project.setName("mein-drittes-projekt");
		Template template = factory.create("v1", ResourceKind.TEMPLATE);
		template.setName("django-psql-example");
		
		project = client.create(project);
		template = client.create(template, project.getNamespace());

		List<ITemplate> list = client.list(ResourceKind.TEMPLATE, project.getName());
		for (ITemplate t : list) {
			System.out.println (t.toString());
		}
		
//		IList resources = client.getResourceFactory().create(template.getApiVersion(), ResourceKind.LIST);
//		resources.addAll(template.getItems());
//		return client.create(resources, this.namespace);
		
		// siehe: https://issues.jboss.org/browse/JBIDE-20314
		// eine App hinzufügen
		
		// App enfernen
		
		// Projekt löschen
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
