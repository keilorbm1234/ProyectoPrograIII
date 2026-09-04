package cr.ac.una.resourcemanager.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String v) throws Exception {
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
    }
}
