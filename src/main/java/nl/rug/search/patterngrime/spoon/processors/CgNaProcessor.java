package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Role;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;

import java.util.List;
import java.util.stream.Stream;

/**
 * Calculates number of alien attributes (class grime metric).
 * @author Daniel Feitosa.
 */
public class CgNaProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        patterns.getRolesOfClass(element.getQualifiedName()).forEach(this::calculateCgNa);
    }

    private void calculateCgNa(Role r) {
        // Get class attributes
        Stream<CtField> s = element.getFields().stream();
        List<String> pra = patterns.getPatternRelatedAttribute(r);

        long count = s.filter(f -> !pra.contains(f.getSimpleName())).count();

        r.setCgNa(count);
    }
}
