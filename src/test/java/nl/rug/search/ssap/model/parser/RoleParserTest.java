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
public class RoleParserTest {

    private Map<Role, RoleParser> roleParsers;
    private static final Map<Role, Object[]> tests;
    static
    {
        tests = new HashMap<>();
        AttributeRoleTest.tests.forEach((s, l) -> tests.put(s, new Object[]{
                RoleType.ATTRIBUTE,
                AttributeRole.class
        }));
        MethodRoleTest.tests.forEach((s, l) -> tests.put(s, new Object[]{
                RoleType.METHOD,
                MethodRole.class
        }));
        ClassRoleTest.tests.forEach((s, l) -> tests.put(s, new Object[]{
                RoleType.CLASS,
                ClassRole.class
        }));
    }

    @Before
    public void setUp() throws Exception {
        roleParsers = new HashMap<>(tests.size());
        tests.forEach((s, l) -> roleParsers.put(s, RoleParser.getParser(s)));
    }

    @Test
    public void getRoleType() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[0], roleParsers.get(s).getRoleType()));
    }

    @Test
    public void getParser() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[1], roleParsers.get(s).getClass()));
    }

}