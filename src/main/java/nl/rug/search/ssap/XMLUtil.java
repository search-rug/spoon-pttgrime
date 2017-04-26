package nl.rug.search.ssap;

import com.thoughtworks.xstream.XStream;
import nl.rug.search.ssap.model.Instance;
import nl.rug.search.ssap.model.Pattern;
import nl.rug.search.ssap.model.Role;
import nl.rug.search.ssap.model.System;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by feitosadaniel on 24/03/2017.
 */
public class XMLUtil {

    private static class SingletonHelper{
        private static final XStream INSTANCE = new XStream();
        static {
            INSTANCE.processAnnotations(System.class);
            INSTANCE.processAnnotations(Pattern.class);
            INSTANCE.processAnnotations(Instance.class);
            INSTANCE.processAnnotations(Role.class);
            INSTANCE.ignoreUnknownElements();
        }
    }

    private static XStream getXStream(){
        return XMLUtil.SingletonHelper.INSTANCE;
    }

    public static void exportToXML(String filename, System system) throws IOException {
        exportToXML(new FileOutputStream(filename), system);
    }

    public static void exportToXML(OutputStream out, System system) throws IOException {
        try(Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
            w.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            XMLUtil.getXStream().toXML(system, w);
        }
    }

    public static System importFromXML(String filename) throws IOException {
        try(InputStream in = new FileInputStream(filename)) {
            return importFromXML(in);
        }
    }

    public static System importFromXML(File file) throws IOException {
        try(InputStream in = new FileInputStream(file)) {
            return importFromXML(in);
        }
    }

    public static System importFromXML(InputStream in) throws IOException {
        System sys = (System) XMLUtil.getXStream().fromXML(in);

        if(sys.getPatterns() == null)
            sys.setPatterns(new ArrayList<>());

        sys.getPatterns().forEach(p -> {
            if(p.getInstances() == null)
                p.setInstances(new ArrayList<>());
            else {
                p.setInstances(p.getInstances());
            }
            p.getInstances().forEach(i -> {
                if(i.getRoles() == null)
                    i.setRoles(new ArrayList<>());
                else {
                    i.setRoles(i.getRoles());
                }
            });
        });

        return sys;
    }
}
