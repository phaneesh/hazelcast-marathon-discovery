package client;

import client.auth.TokenAuthRequestInterceptor;
import client.utils.MarathonException;
import client.utils.ModelUtils;
import feign.Feign;
import feign.Feign.Builder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import static java.util.Arrays.asList;

public class MarathonClient {
	static class MarathonHeadersInterceptor implements RequestInterceptor {
		@Override
		public void apply(RequestTemplate template) {
			template.header("Accept", "application/json");
			template.header("Content-Type", "application/json");
		}
	}
	
	static class MarathonErrorDecoder implements ErrorDecoder {
		@Override
		public Exception decode(String methodKey, Response response) {
			return new MarathonException(response.status(), response.reason());
		}
	}
	
	public static Marathon getInstance(String endpoint) {
		return getInstance(endpoint, null);
	}

	/**
	 * The generalized version of the method that allows more in-depth customizations via
	 * {@link RequestInterceptor}s.
	 *
	 * @param endpoint
	 * 		URL of Marathon
	 */
	public static Marathon getInstance(String endpoint, RequestInterceptor... interceptors) {
		Builder b = Feign.builder()
				.encoder(new GsonEncoder(ModelUtils.GSON))
				.decoder(new GsonDecoder(ModelUtils.GSON))
				.errorDecoder(new MarathonErrorDecoder());
		if (interceptors!=null)
			b.requestInterceptors(asList(interceptors));
		b.requestInterceptor(new MarathonHeadersInterceptor());
		return b.target(Marathon.class, endpoint);
	}

	/**
	 * Creates a Marathon client proxy that performs HTTP basic authentication.
	 */
	public static Marathon getInstanceWithBasicAuth(String endpoint, String username, String password) {
		return getInstance(endpoint,new BasicAuthRequestInterceptor(username,password));
	}

	/**
	 * Creates a Marathon client proxy that uses a token for authentication.
	 *
	 * @param endpoint URL of Marathon
	 * @param token token
     * @return Marathon client
     */
	public static Marathon getInstanceWithTokenAuth(String endpoint, String token) {
		return getInstance(endpoint, new TokenAuthRequestInterceptor(token));
	}
}
