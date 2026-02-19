package interpreter;

import java.util.*;

public class Environment {

    private final Map<String, Object> vars = new LinkedHashMap<>();
    private final Environment parent;

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void define(String name, Object value) {
        vars.put(name.toLowerCase(), value);
    }

    public Object get(String name) {
        String key = name.toLowerCase();
        if (vars.containsKey(key)) return vars.get(key);
        if (parent != null) return parent.get(name);
        throw new RuntimeException("Undefined variable: " + name);
    }

    public boolean has(String name) {
        String key = name.toLowerCase();
        if (vars.containsKey(key)) return true;
        if (parent != null) return parent.has(name);
        return false;
    }

    public void set(String name, Object value) {
        String key = name.toLowerCase();
        if (vars.containsKey(key)) {
            vars.put(key, value);
        } else if (parent != null) {
            parent.set(name, value);
        } else {
            vars.put(key, value);
        }
    }

    public Environment getParent() { return parent; }

    @Override
    public String toString() {
        return vars.toString() + (parent != null ? " -> " + parent : "");
    }
}
