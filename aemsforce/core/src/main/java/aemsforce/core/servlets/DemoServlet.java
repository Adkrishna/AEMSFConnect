package aemsforce.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.mcm.salesforce.SalesforceClient;
import com.adobe.cq.mcm.salesforce.SalesforceResponse;
import com.adobe.cq.mcm.salesforce.SalesforceSearchParameters;

import com.day.cq.wcm.webservicesupport.Configuration;
import com.day.cq.wcm.webservicesupport.ConfigurationManager;

import aemsforce.core.models.SalesforceProcess;


@Component(immediate = true, service = Servlet.class, property = { 
		"sling.servlet.resourceTypes=/apps/aem-reusable/components/page/page", 
		"sling.servlet.methods=GET" ,
		"sling.servlet.extensions=xml"
		}
)

public class DemoServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SalesforceProcess.class);
	private String pagePath;
	Resource resource;
	
	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws ServletException, IOException {
		String[] cloudConfigs = new String[10];
		cloudConfigs[0]="/etc/cloudservices/salesforce/salesforceconnect";
		ResourceResolver resolver=request.getResourceResolver();
		ConfigurationManager manager=resolver.adaptTo(ConfigurationManager.class);
		 Configuration salesforceConfig = manager.getConfiguration("salesforce",cloudConfigs);
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain"); 
		out.print("Test Servlet");
		LOG.error("Inside Servlet");
		 String instanceUrl = (String)salesforceConfig.get("instanceurl", "");
	      String accessToken = (String)salesforceConfig.get("accesstoken", "");
	      String clientId = (String)salesforceConfig.get("customerkey", "");
	      
	      String encryptedCustomerSecret = (String)salesforceConfig.get("customersecret", "");
	      String encryptedRefereshToken = (String)salesforceConfig.get("refreshtoken", "");
	      
	      SalesforceSearchParameters parameters=new SalesforceSearchParameters();
	      parameters.setSearchVal("");
	      parameters.setSearchType("");
	      parameters.setSearchVal("");
	      
	      SalesforceClient client = new SalesforceClient();
	      client.setClientId(clientId);
	      client.setAccessToken(accessToken);
	      client.setInstanceURL(instanceUrl);
	      client.setMethod(SalesforceClient.AvailableMethods.GET);
	      client.setContentType("application/json");
	      client.setRefreshToken(encryptedRefereshToken);
	      client.setPath("/services/data/v20.0/query/");
	      try {
	 
			client.addParameter("q",buildSOSL(parameters) );
			  SalesforceResponse sresponse = client.executeRequest();
			  LOG.error("Loggin Response"+sresponse.getBodyAsJSON());

			out.println(sresponse.getBodyAsJSON());

	      
	      } catch (Exception e) {
		
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
	    
		SalesforceProcess process=new SalesforceProcess();
		out.println("Instance URL"+instanceUrl);
		out.flush();

	}
	
	protected String buildSOSL(SalesforceSearchParameters parameters) throws Exception
	  {
	    StringBuilder query = new StringBuilder();
	    query.append("SELECT NAME");
	    if ((parameters.getResultProperties() != null) && (parameters.getResultProperties().length > 0))
	    {
	      for (int i = 0; i < parameters.getResultProperties().length; i++) {
	        if (parameters.getResultProperties()[i] != null) {
	          query.append(parameters.getResultProperties()[i] + ", ");
	        }
	      }
	      query.deleteCharAt(query.lastIndexOf(","));
	    }
	    
	 query.append(" FROM CONTACT");
	 
	    if ((parameters.getSearchOperator() != null) && (parameters.getSearchType() != null) && (parameters.getSearchVal() != null)) {
	      query.append("WHERE " + parameters.getSearchType() + " " + parameters.getSearchOperator() + " " + getEncodedSearchVal(parameters.getSearchOperator(), parameters.getSearchVal()));
	    } 
	   LOG.error("Query String "+query.toString());
	   
	    return query.toString();
	    
	    
	  }
	  private String getEncodedSearchVal(String operator, String searchVal)
	  {
	    Double searchValue = null;
	    try
	    {
	      searchValue = Double.valueOf(Double.parseDouble(searchVal));
	      return searchValue.toString();
	    }
	    catch (NumberFormatException e) {}
	    return "'" + searchVal + "'";
	  }

}

