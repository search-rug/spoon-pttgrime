package nl.rug.search.ssap.model.parser;

import nl.rug.search.ssap.model.Role;

import java.util.regex.Matcher;

/**
 * Created by feitosadaniel on 23/03/2017.
 */
public abstract class RoleParser {

    private static final java.util.regex.Pattern REGEX_CLASS = java.util.regex.Pattern.compile("^\\s*(([a-zA-Z_$][a-zA-Z\\d_$.]*?)(\\.))?([a-zA-Z_$][a-zA-Z\\d_$]*)$");

    private static final java.util.regex.Pattern REGEX_ATTR = java.util.regex.Pattern.compile("^(.*?)([a-zA-Z_$][a-zA-Z\\d_$.]*)\\s+([a-zA-Z_$][a-zA-Z\\d_$]*)$");

    private static final java.util.regex.Pattern REGEX_METHOD = java.util.regex.Pattern.compile("^(.*?)::(.*)\\((.*)\\):(.*)$");

    private final Role role;

    private final RoleType roleType;

    private final Matcher roleMatcher;

    RoleParser(Role role, RoleType roleType, Matcher roleMatcher){
        this.role = role;
        this.roleType = roleType;
        this.roleMatcher = roleMatcher;
    }

    Role getRole(){ return role; }

    Matcher getMatcher(){ return roleMatcher; }

    public RoleType getRoleType(){
        return roleType;
    }

    public static RoleParser getParser(Role role) {
        Matcher method = REGEX_METHOD.matcher(role.getElement());
        Matcher attr = REGEX_ATTR.matcher(role.getElement());
        Matcher clazz = REGEX_CLASS.matcher(role.getElement());

        if(method.lookingAt())
            return new MethodRole(role, method);

        if(attr.lookingAt())
            return new AttributeRole(role, attr);

        clazz.lookingAt();
        return new ClassRole(role, clazz);
    }
}
