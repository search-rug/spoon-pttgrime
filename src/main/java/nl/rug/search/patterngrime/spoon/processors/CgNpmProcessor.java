package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Role;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

import java.util.List;
import java.util.stream.Stream;

/**
 * Calculates number of alien public methods (class grime metric).
 * @author Daniel Feitosa.
 */
public class CgNpmProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        patterns.getRolesOfClass(element.getQualifiedName()).forEach(this::calculateCgNpm);
    }

    private void calculateCgNpm(Role r) {
        // "getAllMethods" returns all the accessible methods (concrete and abstract)
        // "getMethods" returns the methods that are directly declared by the class or interface
        Stream<CtMethod> s = element.getMethods().stream();

        List<String> prm = patterns.getPatternRelatedMethods(r);

        // All interface methods are public (thus, should not check)
        if(CtClass.class.isInstance(element))
            s = s.filter(m -> m.getVisibility() == ModifierKind.PUBLIC);
        long count = s.filter(m -> !prm.contains(m.getSimpleName())).count();

        r.setCgNpm(count);
    }
}
