package nl.rug.search.patterngrime;

import nl.rug.search.ssap.XMLUtil;
import nl.rug.search.ssap.model.Instance;
import nl.rug.search.ssap.model.Role;
import nl.rug.search.ssap.model.System;
import nl.rug.search.ssap.model.parser.AttributeRole;
import nl.rug.search.ssap.model.parser.ClassRole;
import nl.rug.search.ssap.model.parser.MethodRole;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by feitosadaniel on 21/03/2017.
 */
public class GoFUtil {

    private System patternInstances;

    /**
     * Index of roles based on types (classes).
     * ClassRole     - class
     * AttributeRole - declaring class
     * AttributeRole - attribute type (class)
     * MethodRole    - declaring class
     * MethodRole    - return type (class)
     * MethodRole    - parameter type (class)
     */
    private Map<String, Set<Role>> roleMap;

    private GoFUtil(String filename) throws IOException {
        patternInstances = XMLUtil.importFromXML(filename);
        roleMap = createRoleMap(patternInstances);
    }

    private GoFUtil() throws IOException {
        this("./pattern-instances.xml");
    }

    private static Map<String, Set<Role>> createRoleMap(System system) {
        HashMap<String, Set<Role>> roleMap = new HashMap<>();
        Util.safe(system.getPatterns())
                .forEach( p -> Util.safe(p.getInstances())
                .forEach( i -> Util.safe(i.getRoles())
                .forEach( r -> {
                    Set<Role> s = Collections.singleton(r);
                    switch (r.getRoleParser().getRoleType()){
                        case CLASS:
                            ClassRole cr = ((ClassRole) r.getRoleParser());
                            roleMap.merge(cr.getClassQualifiedName(), s, GoFUtil::combineRoleSets);
                            break;
                        case ATTRIBUTE:
                            AttributeRole ar = ((AttributeRole) r.getRoleParser());
                            String declaringClass = ar.getAttributeDeclaringClass();
                            if(!declaringClass.isEmpty())
                                roleMap.merge(declaringClass, s, GoFUtil::combineRoleSets);
                                roleMap.merge(ar.getAttributeType(), s, GoFUtil::combineRoleSets);
                            break;
                        case METHOD:
                            MethodRole mr = ((MethodRole) r.getRoleParser());
                            roleMap.merge(mr.getMethodDeclaringClass(), s, GoFUtil::combineRoleSets);
                            roleMap.merge(mr.getMethodReturnType(), s, GoFUtil::combineRoleSets);
                            Arrays.stream(mr.getMethodParams()).forEach(pa -> {
                                if (!isPrimitiveType(pa))
                                    roleMap.merge(pa, s, GoFUtil::combineRoleSets);
                            });
                    }
                })));
        return roleMap;
    }

    private static boolean isPrimitiveType(String type){
        return type.startsWith("java.lang.") || type.matches("^\\s*[a-z]+\\s*$");
    }

    private static Set<Role> combineRoleSets(Set<Role> set1, Set<Role> set2) {
        Set<Role> newSet = new HashSet<>(set1);
        newSet.addAll(set2);
        return newSet;
    }

    private static class SingletonHelper{
        private static GoFUtil INSTANCE;
        private static void loadFile(String filename) throws IOException {
            INSTANCE = new GoFUtil(filename);
        }
        private static GoFUtil getInstance() throws IOException {
            if (INSTANCE == null)
                INSTANCE = new GoFUtil();
            return INSTANCE;
        }
    }

    public static GoFUtil getInstance() throws IOException {
        return SingletonHelper.getInstance();
    }

    public static void loadPatternInstances(String filename) throws IOException {
        SingletonHelper.loadFile(filename);
    }

    public static void savePatternInstances(String filename) throws IOException {
        XMLUtil.exportToXML(filename, SingletonHelper.getInstance().patternInstances);
    }

    public boolean isPatternParticipantClass(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        return s != null && s.stream().anyMatch(r -> ClassRole.class.isInstance(r.getRoleParser()));
    }

    public Stream<Role> getRolesOfClass(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream().filter(r -> ClassRole.class.isInstance(r.getRoleParser()));
        }
        return Stream.empty();
    }

    public Stream<Instance> getInstancesWithClass(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream()
                    .filter(r -> ClassRole.class.isInstance(r.getRoleParser()))
                    .map(Role::getInstance)
                    .distinct();
        }
        return Stream.empty();
    }

    /**
     * @param r Role
     * @return
     */
    public List<String> getPatternRelatedMethods(Role r) {
        return r.getInstance().getRoles().stream()
                    .map(Role::getRoleParser)
                    .filter(MethodRole.class::isInstance)
                    .map(MethodRole.class::cast)
                    .filter(ar -> ar.getMethodDeclaringClass().equals(r.getElement()))
                    .map(MethodRole::getMethodName)
                    .collect(Collectors.toList());
    }

    /**
     * @param r Role
     * @return
     */
    public List<String> getPatternRelatedAttribute(Role r) {
        return r.getInstance().getRoles().stream()
                .map(Role::getRoleParser)
                .filter(AttributeRole.class::isInstance)
                .map(AttributeRole.class::cast)
                .filter(ar -> ar.getAttributeDeclaringClass().equals(r.getElement()))
                .map(AttributeRole::getAttributeName)
                .collect(Collectors.toList());
    }

    /**
     * Get classes that participate in the same pattern instance.
     * @param i Instance
     * @return
     */
    public List<String> getClassesOfInstance(Instance i) {
        return i.getRoles().stream()
                .map(Role::getRoleParser)
                .filter(ClassRole.class::isInstance)
                .map(ClassRole.class::cast)
                .map(ClassRole::getClassQualifiedName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get packages of the classes that participate in the same pattern instance.
     * @param i Instance
     * @return
     */
    public List<String> getPackagesOfInstance(Instance i) {
        return i.getRoles().stream()
                .map(Role::getRoleParser)
                .filter(ClassRole.class::isInstance)
                .map(ClassRole.class::cast)
                .map(ClassRole::getPackage)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get classes that participate in the same pattern instance.
     * @deprecated Consider all instances that class particiaptes
     * @param classQualifiedName
     * @return
     */
    public String[] getClassesFromSameInstance(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream()
                    .filter(r -> ClassRole.class.isInstance(r.getRoleParser()))
                    .map(r -> r.getInstance().getRoles().stream()
                            .map(Role::getRoleParser)
                            .filter(ClassRole.class::isInstance)
                            .map(ClassRole.class::cast)
                            .map(ClassRole::getClassQualifiedName))
                    .reduce(Stream::concat)
                    .orElse(Stream.empty())
                    .distinct()
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * Get pacakges of the classes that participate in the same pattern instance.
     * @deprecated Consider all instances that class particiaptes
     * @param classQualifiedName
     * @return
     */
    public String[] getPackagesFromInstance(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream()
                    .filter(r -> ClassRole.class.isInstance(r.getRoleParser()))
                    .map(r -> r.getInstance().getRoles().stream()
                            .map(Role::getRoleParser)
                            .filter(ClassRole.class::isInstance)
                            .map(ClassRole.class::cast)
                            .map(ClassRole::getPackage))
                    .reduce(Stream::concat)
                    .orElse(Stream.empty())
                    .distinct()
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * @deprecated Consider all instances that class particiaptes
     * @param classQualifiedName
     * @return
     */
    public String[] getPatternRelatedMethods(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream()
                    .map(Role::getRoleParser)
                    .filter(MethodRole.class::isInstance)
                    .map(MethodRole.class::cast)
                    .filter(r -> r.getMethodDeclaringClass().equals(classQualifiedName))
                    .map(MethodRole::getMethodName)
                    .toArray(String[]::new);
        }
        return new String[0];
    }

    /**
     * @deprecated Consider all instances that class particiaptes
     * @param classQualifiedName
     * @return
     */
    public String[] getPatternRelatedAttribute(String classQualifiedName) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            return s.stream()
                    .map(Role::getRoleParser)
                    .filter(AttributeRole.class::isInstance)
                    .map(AttributeRole.class::cast)
                    .filter(r -> r.getAttributeDeclaringClass().equals(classQualifiedName))
                    .map(AttributeRole::getAttributeName)
                    .toArray(String[]::new);
        }
        return new String[0];
    }


    /**
     * Save the measurement of CgNa for all roles this class play
     * @deprecated
     * @param classQualifiedName
     * @param cgNa
     */
    public void setCgNa(String classQualifiedName, Long cgNa) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            s.stream().filter(r -> r.getRoleParser() instanceof ClassRole).forEach(r -> r.setCgNa(cgNa));
        }
    }

    /**
     * Save the measurement of CgNpm for all roles this class play
     * @deprecated
     * @param classQualifiedName
     * @param cgNpm
     */
    public void setCgNpm(String classQualifiedName, Long cgNpm) {
        Set<Role> s = roleMap.get(classQualifiedName);
        if(s != null){
            s.stream().filter(r -> r.getRoleParser() instanceof ClassRole).forEach(r -> r.setCgNpm(cgNpm));
        }
    }
}
