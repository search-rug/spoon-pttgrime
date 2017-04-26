package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.ssap.model.Instance;
import nl.rug.search.ssap.model.Role;
import nl.rug.search.ssap.model.parser.ClassRole;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates number of packages within the pattern instance (organizational grime metric).
 * @author Daniel Feitosa.
 */
public class OgNpProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        // Find pattern instances this class is part of
        Stream<Instance> instances = patterns.getInstancesWithClass(element.getQualifiedName());
        instances.forEach(this::calculateOgNp);
    }

    private void calculateOgNp(Instance i) {
        if(i.getOgNp() != null)
            return;

        List<String> instancePackages = patterns.getPackagesOfInstance(i);

        long count = instancePackages.size();
        i.setOgNp(count);
    }
}
