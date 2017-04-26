package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Instance;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Calculates efferent coupling of a pattern instance  (modular grime metric).
 * @author Daniel Feitosa.
 */
public class MgCeProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        // Find pattern instances this class is part of
        Stream<Instance> instances = patterns.getInstancesWithClass(element.getQualifiedName());
        instances.forEach(this::calculateMgCe);
    }

    private void calculateMgCe(Instance i){
        if(i.getMgCe() != null)
            return;

        List<String> instanceClasses = patterns.getClassesOfInstance(i);

        // Find dependencies of the instance classes
        Set<CtTypeReference> fanOut = new HashSet<>();
        dependencyMap.keySet().stream().filter(c -> instanceClasses.contains(c.getQualifiedName()))
                .forEach(c -> fanOut.addAll(dependencyMap.get(c)));

        long count = fanOut.stream().filter(p -> !instanceClasses.contains(p.getQualifiedName())).count();
        i.setMgCe(count);
    }
}
