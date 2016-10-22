package net.troja.application.servicea.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServiceBClientTest {
    private final ServiceBClient classToTest = new ServiceBClient();

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        classToTest.setRestTemplate(restTemplate);
    }

    @Test
    public void createSuccess() {
        final Metadata data = new Metadata("test", "data");
        final ResponseEntity<Metadata> entity = new ResponseEntity<Metadata>(HttpStatus.CREATED);
        when(restTemplate.postForEntity("http://localhost:8081/metadata", data, Metadata.class)).thenReturn(entity);

        final boolean result = classToTest.create(data);

        assertThat(result, equalTo(true));
    }

    @Test
    public void createFailed() {
        final Metadata data = new Metadata("test", "data");
        final ResponseEntity<Metadata> entity = new ResponseEntity<Metadata>(HttpStatus.BAD_REQUEST);
        when(restTemplate.postForEntity("http://localhost:8081/metadata", data, Metadata.class)).thenReturn(entity);

        final boolean result = classToTest.create(data);

        assertThat(result, equalTo(false));
    }

    @Test
    public void findByUniqueIdSuccess() {
        final String uniqueId = "1234";
        final Metadata data = new Metadata(uniqueId, "aaaa");
        final ResponseEntity<Metadata> entity = new ResponseEntity<Metadata>(data, HttpStatus.FOUND);
        when(restTemplate.getForEntity("http://localhost:8081/metadata?uniqueid=" + uniqueId, Metadata.class)).thenReturn(entity);

        final Metadata metadata = classToTest.findByUniqueId(uniqueId);
        assertThat(metadata, notNullValue());
    }

    @Test
    public void findByUniqueIdFailed() {
        final String uniqueId = "1234";
        final ResponseEntity<Metadata> entity = new ResponseEntity<Metadata>(HttpStatus.NOT_FOUND);
        when(restTemplate.getForEntity("http://localhost:8081/metadata?uniqueid=" + uniqueId, Metadata.class)).thenReturn(entity);

        final Metadata metadata = classToTest.findByUniqueId(uniqueId);
        assertThat(metadata, nullValue());
    }
}
