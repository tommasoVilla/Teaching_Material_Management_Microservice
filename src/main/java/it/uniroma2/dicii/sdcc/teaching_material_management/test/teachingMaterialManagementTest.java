package it.uniroma2.dicii.sdcc.teaching_material_management.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.Objects;

/* Testing the service that provides link to download and upload file. It is assumed that a document "test.txt"
 * already exist in data-store.*/

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class teachingMaterialManagementTest {

    @LocalServerPort
    int randomServerPort;

    /* The follow test checks the scenario in which the client sent a request to obtain a temporary upload link for
    * a file surely not present in data-store. The client obtain HTTP OK and an usable link.*/
    @Test
    public void testUploadSuccess() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort +
                "/teaching_material_management/upload/not_existing_document.test";
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(result.getBody()).contains("https://teachingmaterialmanagement.s3.amazonaws.com/"));
    }

    /* The follow test checks the scenario in which the client sent a request to obtain a temporary upload link for
     * a file surely present in data-store. The client obtain an HTTP 409 CONFLICT error.*/
    @Test
    public void testUploadConflict() throws Exception {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "http://localhost:" + randomServerPort +
                    "/teaching_material_management/upload/test.txt";
            URI uri = new URI(baseUrl);
            restTemplate.getForEntity(uri, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(409, e.getRawStatusCode());
        }
    }

    /* The follow test checks the scenario in which the client sent a request to obtain a temporary download link for
     * a file surely present in data-store. The client obtain HTTP OK and an usable link.*/
    @Test
    public void testDownloadSuccess() throws Exception{

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort +
                "/teaching_material_management/download/test.txt";
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertTrue(Objects.requireNonNull(result.getBody()).contains("https://teachingmaterialmanagement.s3.amazonaws.com/"));
    }

    /* The follow test checks the scenario in which the client sent a request to obtain a temporary download link for
     * a file surely not present in data-store. The client obtain an HTTP 404 NOT FOUND error.*/
    @Test
    public void testDownloadNotFound() throws Exception {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "http://localhost:" + randomServerPort +
                    "/teaching_material_management/download/not_existing_document.test";
            URI uri = new URI(baseUrl);
            restTemplate.getForEntity(uri, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e){
            Assert.assertEquals(404, e.getRawStatusCode());
        }
    }

    /* The follow test checks the scenario in which the client sent a request to obtain a list of document starting by
     * a pattern not present in data-store. The client obtain an empty list of strings.*/
    @Test
    public void testFindByPatternNotSuccess() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort +
                "/teaching_material_management/list/not_existing_start_name_pattern";
        URI uri = new URI(baseUrl);
        ResponseEntity<String[]> result = restTemplate.getForEntity(uri, String[].class);
        String[] names = result.getBody();

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        assert names != null;
        Assert.assertEquals(0, names.length);
    }

    /* The follow test checks the scenario in which the client sent a request to obtain a list of document starting by
     * a pattern present in data-store. The client obtain a not empty list of strings.*/
    @Test
    public void testFindByPatternSuccess() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort +
                "/teaching_material_management/list/test";
        URI uri = new URI(baseUrl);
        ResponseEntity<String[]> result = restTemplate.getForEntity(uri, String[].class);
        String[] names = result.getBody();

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());
        assert names != null;
        Assert.assertTrue(names.length != 0);
    }
}
