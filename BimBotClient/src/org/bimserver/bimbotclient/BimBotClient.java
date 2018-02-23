package org.bimserver.bimbotclient;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BimBotClient {
	private CloseableHttpClient httpclient;
	private static final String CENTRAL_REPO_URL = "https://raw.githubusercontent.com/opensourceBIM/BIMserver-Repository/master/serviceproviders.json";
	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public BimBotClient() {
		httpclient = HttpClients.createDefault();
	}
	
	public void execute(BimBotCall bimBotCall) throws BimBotExecutionException {
		bimBotCall.execute(httpclient);
	}
	
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected CloseableHttpClient getHttpclient() {
		return httpclient;
	}
	
	public BimBotServer getServer(String name, String description, String serviceListPath) {
		return new BimBotServer(this, name, description, serviceListPath);
	}

	public ServiceProviderRegistry getCentralServiceProviderRegistry() {
		return new ServiceProviderRegistry(this, CENTRAL_REPO_URL);
	}
}
