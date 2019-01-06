package de.kreth.clubhelperbackend.google;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.ServletRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;

@Ignore
public class GoogleBaseAdapterTest {

	private GoogleBaseAdapter adapter;
	@Mock
	private ServletRequest request;
	@Mock
	private Credential creditial;
	@Mock
	private File credFile;

	private Reader reader;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(request.getServerName()).thenReturn("localhost");
		adapter = new GoogleBaseAdapter() {

			@Override
			File getClientSecretFile() {
				return credFile;
			}

			@Override
			Credential loadCredential(GoogleClientSecrets clientSecrets, String serverName) throws IOException {
				return creditial;
			}

			@Override
			Reader getSecretFileInputStream() throws FileNotFoundException, IOException {
				return reader;
			}
		};
	}

	@Test
	public void testMissingGoogleSecretFile() throws IOException {
		when(credFile.exists()).thenReturn(false);
		try {
			adapter.checkRefreshToken(request.getServerName());
		} catch (IOException e) {
			assertTrue("Message was: " + e.getMessage(), e.getMessage().contains("Download from google"));
		}
	}

	@Test
	public void testCheckRefreshToken() throws IOException {
		when(credFile.exists()).thenReturn(true);
		reader = new StringReader(getJsonInput());
		when(creditial.getExpiresInSeconds()).thenReturn(10L);
		adapter.checkRefreshToken("localhost");
		verify(creditial).refreshToken();
	}

	private String getJsonInput() {
//		return "{\"web\":{\"client_id\":\"18873888282-iptk63468sf4to7ajihqmq1l5ggkq54o.apps.googleusercontent.com\",\"project_id\":\"clubhelper\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"RxtsmNfj6CmQOX_4enrOl9aM\",\"redirect_uris\":[\"http://localhost:59431/Callback\",\"http://munich.spallek.com:59431/Callback\"]}}";
		return "{\"web\":{\"client_id\":\"00000000000-aaaa0000000000000000000000000000.apps.googleusercontent.com\",\"project_id\":\"clubhelper\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"000000000000000000000000\",\"redirect_uris\":[\"http://localhost:59431/Callback\"]}}";
	}

}
