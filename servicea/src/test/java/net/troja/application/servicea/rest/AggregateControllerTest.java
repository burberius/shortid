package net.troja.application.servicea.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import net.troja.application.servicea.IdConversionService;
import net.troja.application.servicea.data.AggregationDataRepository;
import net.troja.application.servicea.model.AggregationData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AggregateControllerTest {
    @Autowired
    private AggregateController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AggregationDataRepository repository;

    @Autowired
    private IdConversionService idService;

    @Mock
    private ServiceBClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller.setClient(client);
        repository.deleteAll();
    }

    @Test
    public void createSuccess() {
        assertThat(repository.count(), equalTo(0L));
        final AggregationData data = new AggregationData(UUID.randomUUID(), "just a test");
        final Metadata metadata = new Metadata(idService.convert(data.getUuid()), Integer.toHexString(data.hashCode()));
        when(client.create(metadata)).thenReturn(true);

        final ResponseEntity<AggregationData> responseEntity = restTemplate.postForEntity("/aggregate", data, AggregationData.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(repository.count(), equalTo(1L));
    }

    @Test
    public void createFailed() {
        final AggregationData data = new AggregationData(UUID.randomUUID(), "just a test");
        final Metadata metadata = new Metadata(idService.convert(data.getUuid()), Integer.toHexString(data.hashCode()));
        when(client.create(metadata)).thenReturn(false);

        final ResponseEntity<AggregationData> responseEntity = restTemplate.postForEntity("/aggregate", data, AggregationData.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void findByUuidSuccess() {
        final AggregationData data = new AggregationData(UUID.randomUUID(), "just a test");
        final Metadata metadata = new Metadata(idService.convert(data.getUuid()), Integer.toHexString(data.hashCode()));
        repository.save(data);
        when(client.findByUniqueId(idService.convert(data.getUuid()))).thenReturn(metadata);

        final ResponseEntity<AggregationData> responseEntity = restTemplate.getForEntity("/aggregate?uuid=" + data.getUuid(), AggregationData.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.FOUND));
        assertThat(responseEntity.getBody(), equalTo(data));
    }

    @Test
    public void findByUuidFailed() {
        final AggregationData data = new AggregationData(UUID.randomUUID(), "just a test");
        final Metadata metadata = new Metadata(idService.convert(data.getUuid()), "nixda");
        repository.save(data);
        when(client.findByUniqueId(idService.convert(data.getUuid()))).thenReturn(metadata);

        final ResponseEntity<AggregationData> responseEntity = restTemplate.getForEntity("/aggregate?uuid=" + data.getUuid(), AggregationData.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void findByUuidNotFound() {
        assertThat(repository.count(), equalTo(0L));
        final ResponseEntity<AggregationData> responseEntity = restTemplate.getForEntity("/aggregate?uuid=" + UUID.randomUUID(), AggregationData.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
