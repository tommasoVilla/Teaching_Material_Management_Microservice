package it.uniroma2.dicii.sdcc.teaching_material_management.Rest;

import it.uniroma2.dicii.sdcc.teaching_material_management.Controller.ControllerGRASP;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.FileAlreadyExistsException;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.NotSuchFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/teaching_material_management/api/v1.0/")
public class ControllerRest {

    @Autowired
    private ControllerGRASP controllerGRASP;

    /* generateTemporaryUploadLink is a service that provide to requester a link usable to upload on a persistence
     * layer a file with given fileName. The client will use this url to make a HTTP PUT request containing in the body
     * a file named fileName. If a document with specified name already exists in data layer an HTTP CONFLICT error is
     * returned. */
    @RequestMapping(method = RequestMethod.GET, path ="upload/{fileName}" )
    public ResponseEntity<String> generateTemporaryUploadLink(@PathVariable String fileName)  {
        try {
            String temporaryUploadLink = controllerGRASP.generateTemporaryUploadLink(fileName);
            return new ResponseEntity<>(temporaryUploadLink, HttpStatus.OK);
        } catch (FileAlreadyExistsException e){
            return new ResponseEntity<>("", HttpStatus.CONFLICT);
        }
    }

    /* generateTemporaryDownloadLink is a service that provide to requester a link usable to download from a persistence
     * layer the file with given fileName. The client will use this url to make a HTTP GET with no further parameters.
     * If the document with specified name is not present in data layer an HTTP NOT FOUND error is returned */
    @RequestMapping(method = RequestMethod.GET, path ="download/{fileName}" )
    public ResponseEntity<String> generateTemporaryDownloadLink(@PathVariable String fileName)  {
        try {
            String temporaryDownloadLink = controllerGRASP.generateTemporaryDownloadLink(fileName);
            return new ResponseEntity<>(temporaryDownloadLink, HttpStatus.OK);
        } catch (NotSuchFileException e){
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }

    /*listObjectsByPrefix is a service that retrieves all document in persistence layer starting with given prefix.
    * The prefix is something like <academic_year>*<department>*<course_name>, so the methods returns the name of each
    * file belonging to a specific course. If no course are found an empty list of strings is returned */
    @RequestMapping(method = RequestMethod.GET, path ="list/{prefix}" )
    public ResponseEntity<ArrayList<String>> listObjectsByPrefix(@PathVariable String prefix)  {

        ArrayList<String> objectNames = controllerGRASP.listObjectByPrefix(prefix);
        return new ResponseEntity<>(objectNames, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path ="" )
    public ResponseEntity healthCheck()  {
        return new ResponseEntity(HttpStatus.OK);
    }
}
