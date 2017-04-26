package nl.rug.search.ssap.model;

import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Feitosa
 */
@XStreamAliasType("system")
public class System {

    @XStreamImplicit
    private List<Pattern> patterns = new ArrayList<>();

    public void setPatterns(List<Pattern> patterns) { this.patterns = patterns; }
    public List<Pattern> getPatterns() { return patterns; }
}
