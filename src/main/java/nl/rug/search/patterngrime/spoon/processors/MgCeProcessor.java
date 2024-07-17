package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Instance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getLogger(MgCeProcessor.class);

    void calculateMetric() {
        // Find pattern instances this class is part of
        log.debug("In MgCe Processor");
        Stream<Instance> instances = patterns.getInstancesWithClass(element.getQualifiedName());
        instances.forEach(this::calculateMgCe);
    }

    private void calculateMgCe(Instance i){
        log.debug("calculating mgce");
        if(i.getMgCe() != null)
            return;

        List<String> instanceClasses = patterns.getClassesOfInstance(i);

        // Find dependencies of the instance classes
        Set<CtTypeReference> fanOut = new HashSet<>();
        dependencyMap.keySet().stream().filter(c -> instanceClasses.contains(c.getQualifiedName()))
                .forEach(c -> fanOut.addAll(dependencyMap.get(c)));

        long count = fanOut.stream().filter(p -> !instanceClasses.contains(p.getQualifiedName())).count();
        log.debug("setting mgce");
        i.setMgCe(count);
    }
}
