package nl.rug.search.patterngrime.spoon.processors;

import nl.rug.search.patterngrime.GoFUtil;
import nl.rug.search.patterngrime.Util;
import org.apache.log4j.Level;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by feitosadaniel on 09/04/2017.
 */
public abstract class PatternGrimeProcessor extends AbstractProcessor<CtType> {

    static CtPackage rootPackage = null;
    static Map<CtType, Set<CtTypeReference>> dependencyMap = new HashMap<>();
    GoFUtil patterns = null;
    CtType element = null;


    public void process(CtType element) {

        if(!CtClass.class.isInstance(element) && !CtInterface.class.isInstance(element))
            return;

        if(rootPackage == null) {
            rootPackage = Util.getRootPackage(element);
            rootPackage.filterChildren((CtClass clazz) -> true)
                    .forEach((CtClass clazz) -> dependencyMap.put(clazz, clazz.getUsedTypes(true)));
            rootPackage.filterChildren((CtInterface clazz) -> true)
                    .forEach((CtInterface clazz) -> dependencyMap.put(clazz, clazz.getUsedTypes(true)));
        }
        this.element = element;
        try {
            patterns = GoFUtil.getInstance();
        } catch (IOException e) {
            getFactory().getEnvironment().report(this, Level.FATAL, "Cannot load Pattern Instances. Check stack trace for details");
            e.printStackTrace();
            return;
        }

        // Process elements
        if (patterns.isPatternParticipantClass(element.getQualifiedName())) {
            // System.out.println(String.format("[%s] %s", this.getClass(), element.getQualifiedName()));
            calculateMetric();
        }

        //TODO Find a way to save output after running all processors
    }

    abstract void calculateMetric();
}
