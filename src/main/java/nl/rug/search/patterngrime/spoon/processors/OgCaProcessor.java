package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.patterngrime.Util;
import nl.rug.search.ssap.model.Instance;
import spoon.reflect.declaration.CtType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates fan-in at the package level (organizational grime metric).
 * @author Daniel Feitosa.
 */
public class OgCaProcessor extends PatternGrimeProcessor {

    void calculateMetric() {
        // Find pattern instances this class is part of
        Stream<Instance> instances = patterns.getInstancesWithClass(element.getQualifiedName());
        instances.forEach(this::calculateOgCa);
    }

    private void calculateOgCa(Instance i) {
        if(i.getOgCa() != null)
            return;

        List<String> instancePackages = patterns.getPackagesOfInstance(i);

        // Get classes that depend on the instance packages
        Stream<CtType> instancePacakgesUses = dependencyMap.keySet().stream().filter(c -> dependencyMap.get(c).stream()
                .anyMatch(t -> instancePackages.contains(Util.getPackageFromClass(t.getQualifiedName()))));

        // Extract list of packages from dependent classes
        List<String> fanInPackages = instancePacakgesUses
                .map(CtType::getQualifiedName)
                .map(Util::getPackageFromClass)
                .distinct().collect(Collectors.toList());
        fanInPackages.removeAll(instancePackages);

        long count = fanInPackages.size();
        i.setOgCa(count);
    }
}
