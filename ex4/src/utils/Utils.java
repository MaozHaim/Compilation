package utils;

import types.Type;
import types.TypeClass;
import types.TypeNil;

import java.util.List;

public class Utils {
    public static boolean matchTypesArgsParams(List<Type> argsTypes, List<Type> paramsTypes) {
        if (argsTypes.size() != paramsTypes.size()) { return false; }
        for (int i = 0; i < argsTypes.size(); i++) {
            Type currentArgument = argsTypes.get(i);
            Type currentParameter = paramsTypes.get(i);
            boolean typesMatch = (currentParameter.name).equals(currentArgument.name);
            boolean isClass = currentArgument instanceof TypeClass && currentParameter instanceof TypeClass;
            if (isClass) {
                return ((TypeClass) currentArgument).isSubtypeOf((TypeClass) currentParameter);
            }
            boolean objectAndNil = (currentArgument instanceof TypeNil) && (currentParameter.isArray() ||
                    currentParameter instanceof TypeClass);
            if (!(typesMatch || objectAndNil)) { return false; }
        }
        return true;
    }
}
