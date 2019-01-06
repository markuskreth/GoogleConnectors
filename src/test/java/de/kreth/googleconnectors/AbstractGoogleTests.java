package de.kreth.googleconnectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletRequest;

import org.junit.Before;

public class AbstractGoogleTests {

	protected ServletRequest request;

	public AbstractGoogleTests() {
		super();
	}

	@Before
	public void initMocks() {
		request = mock(ServletRequest.class);
		when(request.getLocalName()).thenReturn("localhost");
		when(request.getServerName()).thenReturn("localhost");
		when(request.getRemoteHost()).thenReturn("localhost");
	}

}