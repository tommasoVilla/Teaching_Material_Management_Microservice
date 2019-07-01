package it.uniroma2.dicii.sdcc.teaching_material_management.Rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckRest {

    @RequestMapping(method = RequestMethod.GET, path ="" )
    public ResponseEntity healthCheck()  {
        return new ResponseEntity(HttpStatus.OK);
    }

}
