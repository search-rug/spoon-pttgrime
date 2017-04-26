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
public class ClassRoleTest {
    private Map<Role, ClassRole> classRoles;
    static final Map<Role, Object[]> tests;
    static
    {
        tests = new HashMap<>();
        tests.put(new Role("", "org.example1.ClassName$InnerClass"), new Object[]{
                "org.example1.ClassName$InnerClass",
                "org.example1",
                "ClassName$InnerClass"
        });
        tests.put(new Role("", "org.example2.Class_Name"), new Object[]{
                "org.example2.Class_Name",
                "org.example2",
                "Class_Name"
        });
        tests.put(new Role("", "org.exam_ple3.ClassName"), new Object[]{
                "org.exam_ple3.ClassName",
                "org.exam_ple3",
                "ClassName"
        });
        tests.put(new Role("", "org.example4.ClassName$1"), new Object[]{
                "org.example4.ClassName$1",
                "org.example4",
                "ClassName$1"
        });
        tests.put(new Role("", "Class2Name"), new Object[]{
                "Class2Name",
                null,
                "Class2Name"
        });
    }

    @Before
    public void setUp() throws Exception {
        classRoles = new HashMap<>(tests.size());
        tests.forEach((s, l) -> classRoles.put(s, ((ClassRole) RoleParser.getParser(s))));
    }

    @Test
    public void getClassQualifiedName() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[0], classRoles.get(s).getClassQualifiedName()));
    }

    @Test
    public void getPackage() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[1], classRoles.get(s).getPackage()));
    }

    @Test
    public void getClassSimpleName() throws Exception {
        tests.forEach((s, l) -> assertEquals(l[2], classRoles.get(s).getClassSimpleName()));
    }

}