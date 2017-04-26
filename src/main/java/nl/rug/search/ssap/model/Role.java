package nl.rug.search.ssap.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import nl.rug.search.ssap.model.parser.RoleParser;

/**
 * Created by feitosadaniel on 22/03/2017.
 */
@XStreamAliasType("role")
public class Role {

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String element;

    @XStreamOmitField
    private Instance instance;

    @XStreamOmitField
    private RoleParser roleParser;

    public Role() {}

    public Role(String name, String element) {
        setName(name);
        setElement(element);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getElement() { return element; }
    public void setElement(String element) {
        this.element = element;
    }

    void setInstance(Instance instance) { this.instance = instance; }
    public Instance getInstance() { return instance; }

    public RoleParser getRoleParser() {
        if(roleParser == null)
            roleParser = RoleParser.getParser(this);
        return roleParser;
    }


    //TODO move the metrics (CgNa and CgNpm) somewhere better (e.g. a list of metrics)

    @XStreamAlias("cg-na")
    @XStreamAsAttribute
    private Long cgNa;

    @XStreamAlias("cg-npm")
    @XStreamAsAttribute
    private Long cgNpm;

    public Long getCgNa() { return cgNa; }
    public void setCgNa(Long cgNa) { this.cgNa = cgNa; }

    public Long getCgNpm() { return cgNpm; }
    public void setCgNpm(Long cgNpm) { this.cgNpm = cgNpm; }
}
