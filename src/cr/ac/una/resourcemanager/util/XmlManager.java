package cr.ac.una.resourcemanager.util;

import cr.ac.una.resourcemanager.exception.PersistenceException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;

public class XmlManager {

    public static <T> void guardar(T objeto, String rutaArchivo, Class<T> clase){
        try {
            File archivo = new File(rutaArchivo);
            File parentDir = archivo.getParentFile();
            if(parentDir != null && !parentDir.exists()){ parentDir.mkdirs(); }

            JAXBContext jaxbContext = JAXBContext.newInstance(clase);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(objeto,archivo);
            } catch (Exception e) {
            throw new PersistenceException("Error al guardar XML en: " + rutaArchivo, e);
        }
    }

    public static <T> T cargar(String rutaArchivo, Class<T> clase){
        File archivo = new File(rutaArchivo);
        if(!archivo.exists() || archivo.length() == 0){ return null; }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clase);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (T) unmarshaller.unmarshal(archivo);

        } catch (Exception e){
            throw new PersistenceException("Error al cargar XML desde: " + rutaArchivo, e);
        }

    }
}
