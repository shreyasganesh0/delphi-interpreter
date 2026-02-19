package interpreter;

import java.util.*;

public class ObjectInstance {

    public final ClassDefinition classDef;

    private final Map<String, Object> fields = new LinkedHashMap<>();

    public ObjectInstance(ClassDefinition classDef, Map<String, ClassDefinition> registry) {
        this.classDef = classDef;
        for (Map.Entry<String, String> e : classDef.allFields(registry).entrySet()) {
            fields.put(e.getKey().toLowerCase(), defaultValue(e.getValue()));
        }
    }

    private Object defaultValue(String type) {
        if (type == null) return null;
        switch (type.toLowerCase()) {
            case "integer": return 0;
            case "real":    return 0.0;
            case "boolean": return false;
            case "char":    return '\0';
            case "string":  return "";
            default:        return null;   
        }
    }

    public Object getField(String name) {
        return fields.get(name.toLowerCase());
    }

    public void setField(String name, Object value) {
        fields.put(name.toLowerCase(), value);
    }

    public boolean hasField(String name) {
        return fields.containsKey(name.toLowerCase());
    }

    @Override
    public String toString() {
        return classDef.name + "@" + System.identityHashCode(this) + fields;
    }
}
