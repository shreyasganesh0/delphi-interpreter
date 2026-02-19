package interpreter;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.*;

public class ClassDefinition {

    public final String name;
    public final String parentClassName;   

    public final Map<String, String> fields = new LinkedHashMap<>();

    public final Map<String, String> visibility = new LinkedHashMap<>();

    public final Map<String, ParserRuleContext> constructors = new LinkedHashMap<>();

    public final Map<String, ParserRuleContext> destructors = new LinkedHashMap<>();

    public final Map<String, ParserRuleContext> methods = new LinkedHashMap<>();

    public final Set<String> virtualMethods = new HashSet<>();

    public final List<String> implementedInterfaces = new ArrayList<>();

    public ClassDefinition(String name, String parentClassName) {
        this.name = name;
        this.parentClassName = parentClassName;
    }

    public ParserRuleContext lookupMethod(String methodName,
                                          Map<String, ClassDefinition> classRegistry) {
        String key = methodName.toLowerCase();
        if (methods.containsKey(key)) return methods.get(key);
        if (constructors.containsKey(key)) return constructors.get(key);
        if (destructors.containsKey(key)) return destructors.get(key);

        if (parentClassName != null) {
            ClassDefinition parent = classRegistry.get(parentClassName.toLowerCase());
            if (parent != null) return parent.lookupMethod(methodName, classRegistry);
        }
        return null;
    }

    public Map<String, String> allFields(Map<String, ClassDefinition> classRegistry) {
        Map<String, String> result = new LinkedHashMap<>();
        if (parentClassName != null) {
            ClassDefinition parent = classRegistry.get(parentClassName.toLowerCase());
            if (parent != null) result.putAll(parent.allFields(classRegistry));
        }
        result.putAll(fields);   
        return result;
    }

    @Override
    public String toString() {
        return "ClassDef(" + name +
                (parentClassName != null ? " extends " + parentClassName : "") + ")";
    }
}
