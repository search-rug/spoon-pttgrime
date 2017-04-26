package nl.rug.search.ssap.model.parser;

import nl.rug.search.ssap.model.Role;

import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * Created by feitosadaniel on 23/03/2017.
 */
public class MethodRole extends RoleParser {

    MethodRole(Role r, Matcher m) {
        super(r, RoleType.METHOD, m);
    }

    public String getMethodDeclaringClass() {
        return getMatcher().group(1);
    }
    public String getMethodName() {
        return getMatcher().group(2);
    }
    public String[] getMethodParams() {
        String[] params = getMatcher().group(3).split("\\s*,\\s*");
        return Arrays.stream(params).filter(x -> x.matches("[^\\s]+")).toArray(String[]::new);
    }
    public String getMethodReturnType() {
        return getMatcher().group(4);
    }
}

