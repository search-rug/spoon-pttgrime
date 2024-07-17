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

    public String getPackage() {
        if (getMatcher().matches()) {
            return getMatcher().group(2);
        }
        return "";
    }

    public String getClassSimpleName() {
        if (getMatcher().matches()) {
            return getMatcher().group(4);
        }
        return "";
    }
}
