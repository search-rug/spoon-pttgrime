package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Instance;
import spoon.reflect.declaration.CtType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates afferent coupling of a pattern instance  (modular grime metric).
 * @author Daniel Feitosa.
 */
public class MgCaProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        // Find pattern instances this class is part of
        Stream<Instance> instances = patterns.getInstancesWithClass(element.getQualifiedName());
        instances.forEach(this::calculateMgCa);
    }

    private void calculateMgCa(Instance i){
        if(i.getMgCa() != null)
            return;

        List<String> instanceClasses = patterns.getClassesOfInstance(i);

        // Find uses of the instance classes
        Stream<CtType> instanceClassesUses = dependencyMap.keySet().stream().filter(c -> dependencyMap.get(c).stream()
                .anyMatch(t -> instanceClasses.contains(t.getQualifiedName()))).map(CtType::getTopLevelType);

        // Compute coupling
        long count = instanceClassesUses.filter(c -> !instanceClasses.contains(c.getQualifiedName())).distinct().count();

        i.setMgCa(count);
    }
}
