package de.kreth.googleconnectors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public abstract class GoogleBaseAdapter {

	private static final int GOOGLE_SECRET_PORT = 59431;

	/** Application name. */
	protected static final String APPLICATION_NAME = "ClubHelperBackend";

	/** Directory to store user credentials for this application. */
	private static final File DATA_STORE_DIR = new File(System.getProperty("catalina.base"), ".credentials");

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 */
	static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, CalendarScopes.CALENDAR);

	private static Credential credential;

	protected static final Logger log = LoggerFactory.getLogger(GoogleBaseAdapter.class);

	/** Global instance of the {@link FileDataStoreFactory}. */
	private final FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the HTTP transport. */
	private final HttpTransport HTTP_TRANSPORT;

	public GoogleBaseAdapter() throws GeneralSecurityException, IOException {
		super();
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		DATA_STORE_DIR.mkdirs();
		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	}

	protected void checkRefreshToken(String serverName) throws IOException {

		if (credential == null) {
			credential = authorize(serverName);
		}

		if ((credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() < 3600)) {

			if (log.isDebugEnabled()) {
				log.debug("Security needs refresh, trying.");
			}
			boolean result = credential.refreshToken();
			if (log.isDebugEnabled()) {
				log.debug("Token refresh " + (result ? "successfull." : "failed."));
			}
		}
	}

	public Sheets.Builder createSheetsBuilder() {
		if (credential == null) {
			throw new IllegalStateException("credential is null, checkRefreshToken need to be called before.");
		}
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
	}

	public com.google.api.services.calendar.Calendar.Builder createCalendarBuilder() {
		if (credential == null) {
			throw new IllegalStateException("credential is null, checkRefreshToken need to be called before.");
		}
		return new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential);
	}

	public final boolean refreshToken() throws IOException {
		return credential.refreshToken();
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param request
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private synchronized Credential authorize(String serverName) throws IOException {
		log.info("Credential directory is: {}", DATA_STORE_DIR.getAbsolutePath());
		if (credentialIsValid()) {
			credential.refreshToken();
			return credential;
		}

		Reader in = getSecretFileInputStream();

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);
		if (log.isTraceEnabled()) {
			log.trace("client secret json resource loaded.");
		}

		if (log.isDebugEnabled()) {
			log.debug("Configuring google LocalServerReceiver on " + serverName + ":" + GOOGLE_SECRET_PORT);
		}

		Credential credential = loadCredential(clientSecrets, serverName);

		log.debug("Credentials saved to {}", DATA_STORE_DIR.getAbsolutePath());

		credential.setExpiresInSeconds(Long.valueOf(691200L));

		return credential;
	}

	Credential loadCredential(GoogleClientSecrets clientSecrets, String serverName) throws IOException {
		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(DATA_STORE_FACTORY)
						.setAccessType("offline")
						.setApprovalPrompt("force").build();

		VerificationCodeReceiver localServerReceiver = initReceiver(serverName);

		Credential credential = new AuthorizationCodeInstalledApp(flow, localServerReceiver).authorize("user");
		return credential;
	}

	private VerificationCodeReceiver initReceiver(String serverName) {
		LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder().setHost(serverName)
				.setPort(GOOGLE_SECRET_PORT).build();
		return localServerReceiver;
	}

	Reader getSecretFileInputStream() throws FileNotFoundException, IOException {
		// Load client secrets.
		InputStream in = getClass().getResourceAsStream("/client_secret.json");
		if (in == null) {
			File inHome = getClientSecretFile();
			if (inHome.exists()) {
				if (log.isInfoEnabled()) {
					log.info("Google secret not found as Resource, using user Home file.");
				}
				in = new FileInputStream(inHome);
			}
			else {
				log.error("Failed to load client_secret.json. Download from google console.");
				throw new IOException(
						"Failed to load google secret file.\n"
								+ inHome.getAbsolutePath()
								+ "\nDownload from google console and install on Server");
			}
		}
		return new InputStreamReader(in, StandardCharsets.UTF_8);
	}

	File getClientSecretFile() {
		File inHome = new File(System.getProperty("user.home"), "client_secret.json");
		return inHome;
	}

	boolean credentialIsValid() {
		return credential != null
				&& (credential.getExpiresInSeconds() != null && credential.getExpiresInSeconds() < 3600);
	}

}
