package aemsforce.core.models;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.mcm.salesforce.SalesforceSearchParameters;
import com.adobe.granite.crypto.CryptoSupport;
import com.day.cq.wcm.webservicesupport.Configuration;

@Component
public class SalesforceProcess {
	private static final Logger log=LoggerFactory.getLogger(SalesforceProcess.class);
	
	@Reference
	private CryptoSupport cryptoSupport;
	public void  search(Configuration cloudConfig, SalesforceSearchParameters parameters, ResourceResolver resolver)
	{
		String instanceUrl="",accessToken="",clientId="",encryptedCustomerSecret="",encryptedRefreshToken="";
		 if (cloudConfig != null)
		 {
		 instanceUrl = (String)cloudConfig.get("instanceurl", "");
		 accessToken = (String)cloudConfig.get("accesstoken", "");
		 clientId = (String)cloudConfig.get("customerkey", "");
		 encryptedCustomerSecret = (String)cloudConfig.get("customersecret", "");
		// encryptedRefereshToken = (String)cloudConfig.get("refreshtoken", "");
		      
		 }
		 
		 System.out.println("Instance URL "+instanceUrl);
	}

	
}
