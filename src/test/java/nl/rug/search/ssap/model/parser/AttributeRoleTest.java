package nl.rug.search.ssap.model.parser;

import nl.rug.search.ssap.model.Role;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by feitosadaniel on 24/03/2017.
 */
public class AttributeRoleTest {

    private Map<Role, AttributeRole> attributeRoles;
    static final Map<Role, Object[]> tests;
    static
    {
        tests = new HashMap<>();
        tests.put(new Role("", "private org.example.Class_Name var_name"), new Object[]{
                new String[]{ "private" },
                "org.example.Class_Name",
                "var_name"
        });
        tests.put(new Role("", "public static org.example.ClassName$InnerClass varName"), new Object[]{
                new String[]{ "public", "static" },
                "org.example.ClassName$InnerClass",
                "varName"
        });
        tests.put(new Role("", "org.example.ClassName$InnerClass varName"), new Object[]{
                new String[]{ },
                "org.example.ClassName$InnerClass",
                "varName"
        });
    }

    @Before
    public void setUp() throws Exception {
        attributeRoles = new HashMap<>(tests.size());
        tests.forEach((s, l) -> attributeRoles.put(s, ((AttributeRole) RoleParser.getParser(s))));
    }

    @Test
    public void getAttributeModifiers() throws Exception {
        tests.forEach((s, l) -> assertArrayEquals((String[])l[0], attributeRoles.get(s).getAttributeModifiers()));
    }

    @Test
    public void getAttributeType() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[1], attributeRoles.get(s).getAttributeType()));
    }

    @Test
    public void getAttributeName() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[2], attributeRoles.get(s).getAttributeName()));
    }

}