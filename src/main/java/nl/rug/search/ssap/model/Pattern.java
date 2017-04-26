package nl.rug.search.ssap.model;

import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by feitosadaniel on 22/03/2017.
 */
@XStreamAliasType("pattern")
public class Pattern {

    @XStreamAsAttribute
    private String name;

    @XStreamImplicit
    private List<Instance> instances;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
        this.instances.forEach(i -> i.setPattern(this));
    }
    public List<Instance> getInstances() { return instances; }

    public Optional<String> getCategory(){
        switch (name) {
            case "Factory Method"          : return Optional.of("Creational");
            case "Prototype"               : return Optional.of("Creational");
            case "Singleton"               : return Optional.of("Creational");
            case "(Object)Adapter-Command" : return Optional.of("Structural-Behavioral");
            case "Composite"               : return Optional.of("Structural");
            case "Decorator"               : return Optional.of("Structural");
            case "Observer"                : return Optional.of("Behavioral");
            case "State-Strategy"          : return Optional.of("Behavioral");
            case "Template Method"         : return Optional.of("Behavioral");
            case "Visitor"                 : return Optional.of("Behavioral");
            case "Proxy"                   : return Optional.of("Structural");
            case "Proxy2"                  : return Optional.of("Structural");
            default                        : return Optional.empty();
        }
    }
}
