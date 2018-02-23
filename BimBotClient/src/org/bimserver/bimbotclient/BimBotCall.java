package org.bimserver.bimbotclient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

public class BimBotCall {
	private static final Logger LOGGER = LoggerFactory.getLogger(BimBotCall.class);
	private String inputType;
	private String outputType;
	private ByteSource inputData;
	private Authorization authorization;
	private byte[] outputData;
	private Service service;
	private String dataTitle;
	private String contentType;

	public BimBotCall(Service service, String inputType, String outputType, ByteSource inputData) {
		this.service = service;
		this.inputType = inputType;
		this.outputType = outputType;
		this.inputData = inputData;
	}

	protected void execute(CloseableHttpClient httpclient) throws BimBotExecutionException {
		if (authorization == null) {
			throw new BimBotExecutionException("Authorization required in this client implementation");
		}
		String fullUrl = authorization.getServiceaddress() + "/" + authorization.getSoid();
		LOGGER.info(fullUrl);
		HttpPost post = new HttpPost(fullUrl);
		post.setHeader("Authorization", "Bearer " + authorization.getCode());
		post.setHeader("Input-Type", inputType);
		try {
			post.setEntity(new InputStreamEntity(inputData.openBufferedStream()));
			CloseableHttpResponse httpResponse = httpclient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				outputData = ByteStreams.toByteArray(httpResponse.getEntity().getContent());
				outputType = httpResponse.getHeaders("Output-Type")[0].getValue();
				dataTitle = httpResponse.getHeaders("Data-Title")[0].getValue();
				contentType = httpResponse.getHeaders("Content-Type")[0].getValue();
			} else {
				throw new BimBotExecutionException("HTTP Status code: " + httpResponse.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			throw new BimBotExecutionException(e);
		} catch (IOException e) {
			throw new BimBotExecutionException(e);
		}
	}

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
	}
	
	public byte[] getOutputData() {
		return outputData;
	}
	
	public String getOutputType() {
		return outputType;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public String getDataTitle() {
		return dataTitle;
	}
}