package nl.rug.search.ssap.model.parser;

import nl.rug.search.ssap.model.Instance;
import nl.rug.search.ssap.model.Role;

import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * Created by feitosadaniel on 23/03/2017.
 */
public class AttributeRole extends RoleParser {

    AttributeRole(Role r, Matcher m) { super(r, RoleType.ATTRIBUTE, m); }

    public String[] getAttributeModifiers() {
        String[] params = getMatcher().group(1).split("\\s+");
        return Arrays.stream(params).filter(x -> x.matches("[^\\s]+")).map(String::trim).toArray(String[]::new);
    }
    public String getAttributeType() {
        return getMatcher().group(2);
    }
    public String getAttributeName() {
        return getMatcher().group(3);
    }
    public String getAttributeDeclaringClass() {
        Instance i = getRole().getInstance();
        switch (i.getPattern().getName()) {
            case "Singleton":
                return  i.getRoles().stream().filter(r -> r.getName().equals("Singleton")).findFirst().get().getElement();
            case "Decorator":
                return  i.getRoles().stream().filter(r -> r.getName().equals("Component")).findFirst().get().getElement();
        }

        throw new UnsupportedOperationException(String.format("Can't find declaring clas for patter '{}'", i.getPattern().getName()));
    }
}
