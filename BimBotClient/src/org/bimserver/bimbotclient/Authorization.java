package org.bimserver.bimbotclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.google.common.base.Charsets;

public class Authorization {

	private String code;
	private String address;
	private String soid;
	private String serviceaddress;

	public static Authorization parseUri(String redirectUri) throws URISyntaxException {
		List<NameValuePair> parse = URLEncodedUtils.parse(new URI(redirectUri), Charsets.UTF_8);
		Authorization authorization = new Authorization();
		for (NameValuePair nvp : parse) {
			if (nvp.getName().equals("code")) {
				authorization.code = nvp.getValue();
			} else if (nvp.getName().equals("address")) {
				authorization.address = nvp.getValue();
			} else if (nvp.getName().equals("soid")) {
				authorization.soid = nvp.getValue();
			} else if (nvp.getName().equals("serviceaddress")) {
				authorization.serviceaddress = nvp.getValue();
			}
		}
		return authorization;
	}

	public String getAddress() {
		return address;
	}

	public String getCode() {
		return code;
	}

	public String getServiceaddress() {
		return serviceaddress;
	}

	public String getSoid() {
		return soid;
	}
}