package nl.rug.search.patterngrime;

import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by feitosadaniel on 26/03/2017.
 */
public class Util {

    private static final Pattern REGEX_CLASS = java.util.regex.Pattern.compile("^\\s*(([a-zA-Z_$][a-zA-Z\\d_$.]*?)(\\.))?([a-zA-Z_$][a-zA-Z\\d_$]*)$");

    public static CtPackage getRootPackage(CtType<?> t){
        CtPackage p = t.getPackage();
        if(p == null)
            p = t.getParent((CtPackage i) -> true);
        return getRootPackage(p);
    }

    public static CtPackage getRootPackage(CtPackage p){
        CtPackage root = p;
        while(root.getDeclaringPackage() != null){
            root = root.getDeclaringPackage();
        }
        return root;
    }

    public static String getPackageFromClass(String classQualifiedName) {
        Matcher m = REGEX_CLASS.matcher(classQualifiedName);
        if(m.lookingAt())
            return m.group(2);
        else
            return null;
    }

    public static <T> List<T> safe( List<T> other ) {
        return other == null ? Collections.emptyList() : other;
    }
}
