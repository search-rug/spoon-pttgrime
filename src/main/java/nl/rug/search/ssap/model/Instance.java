package nl.rug.search.ssap.model;

import com.thoughtworks.xstream.annotations.*;

import java.util.List;

/**
 * Created by feitosadaniel on 22/03/2017.
 */
@XStreamAliasType("instance")
public class Instance {

    @XStreamImplicit
    private List<? extends Role> roles;

    @XStreamOmitField
    private Pattern pattern;

    public void setRoles(List<? extends Role> roles) {
        this.roles = roles;
        this.roles.forEach(r -> r.setInstance(this));
    }
    public List<? extends Role> getRoles() { return roles; }

    protected void setPattern(Pattern pattern) { this.pattern = pattern; }
    public Pattern getPattern() { return pattern; }

    //TODO move the metrics (MgCa, MgCe, OgCa, OgNp) somewhere better (e.g. a list of metrics)

    @XStreamAlias("mg-ca")
    @XStreamAsAttribute
    private Long mgCa;

    @XStreamAlias("mg-ce")
    @XStreamAsAttribute
    private Long mgCe;

    @XStreamAlias("og-ca")
    @XStreamAsAttribute
    private Long ogCa;

    @XStreamAlias("og-np")
    @XStreamAsAttribute
    private Long ogNp;

    public Long getMgCa() { return mgCa; }
    public void setMgCa(Long mgCa) { this.mgCa = mgCa; }

    public Long getMgCe() { return mgCe; }
    public void setMgCe(Long mgCe) { this.mgCe = mgCe; }

    public Long getOgCa() { return ogCa; }
    public void setOgCa(Long ogCa) { this.ogCa = ogCa; }

    public Long getOgNp() { return ogNp; }
    public void setOgNp(Long ogNp) { this.ogNp = ogNp; }
}
