package nl.rug.search.ssap.model.parser;

import nl.rug.search.ssap.model.Role;

import java.util.regex.Matcher;

/**
 * Created by feitosadaniel on 23/03/2017.
 */
public class ClassRole extends RoleParser {

    ClassRole(Role r, Matcher m) {
        super(r, RoleType.CLASS, m);
    }

    public String getClassQualifiedName() {
        return getRole().getElement();
    }

    public String getPackage() { return getMatcher().group(2); }

    public String getClassSimpleName() { return getMatcher().group(4); }
}
