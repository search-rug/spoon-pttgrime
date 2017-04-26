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
public class MethodRoleTest {

    private Map<Role, MethodRole> methodRoles;
    static final Map<Role, Object[]> tests;
    static
    {
        tests = new HashMap<>();

        tests.put(new Role("", "org.example.ClassName::methodName(java.lang.String, int):org.example.ReturnClass"), new Object[]{
                "org.example.ClassName",
                "methodName",
                new String[]{ "java.lang.String", "int" },
                "org.example.ReturnClass"
        });
        tests.put(new Role("", "org.example.Class_Name::method_name(int):org.example.Return_Class"), new Object[]{
                "org.example.Class_Name",
                "method_name",
                new String[]{ "int" },
                "org.example.Return_Class"
        });
        tests.put(new Role("", "org.example.ClassName::methodName():void"), new Object[]{
                "org.example.ClassName",
                "methodName",
                new String[]{ },
                "void"
        });
    }

    @Before
    public void setUp() throws Exception {
        methodRoles = new HashMap<>(tests.size());
        tests.forEach((s, l) -> methodRoles.put(s, ((MethodRole) RoleParser.getParser(s))));
    }

    @Test
    public void getMethodDeclaringClass() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[0], methodRoles.get(s).getMethodDeclaringClass()));
    }

    @Test
    public void getMethodName() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[1], methodRoles.get(s).getMethodName()));
    }

    @Test
    public void getMethodParams() throws Exception {
        tests.forEach((s, l) -> assertArrayEquals((String[])l[2], methodRoles.get(s).getMethodParams()));
    }

    @Test
    public void getMethodReturnType() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[3], methodRoles.get(s).getMethodReturnType()));
    }

}