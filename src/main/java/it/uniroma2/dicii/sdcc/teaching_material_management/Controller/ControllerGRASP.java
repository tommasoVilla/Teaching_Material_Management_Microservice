package it.uniroma2.dicii.sdcc.teaching_material_management.Controller;

import it.uniroma2.dicii.sdcc.teaching_material_management.Dao.FileDAO;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.FileAlreadyExistsException;
import it.uniroma2.dicii.sdcc.teaching_material_management.error.NotSuchFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/*ControllerGRASP encapsulate the application logic behind rest service and is the only responsible to communicate
  whit objects related with persistence layer */
@Service
public class ControllerGRASP {

    @Autowired
    FileDAO fileDAO;

    public String generateTemporaryUploadLink(String fileName) throws FileAlreadyExistsException {
        return fileDAO.generateTemporaryUploadLink(fileName);
    }

    public String generateTemporaryDownloadLink(String fileName) throws NotSuchFileException {
        return fileDAO.generateTemporaryDownloadLink(fileName);
    }

    public ArrayList<String> listObjectByPrefix(String prefix) {
        return fileDAO.findByPrefix(prefix);
    }
}
