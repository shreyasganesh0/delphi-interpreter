package interpreter;

public class VarReference {
    private final Environment env;
    private final String name;

    public VarReference(Environment env, String name) {
        this.env = env;
        this.name = name;
    }

    public Object get() {
        return env.get(name);
    }

    public void set(Object value) {
        env.set(name, value);
    }
}
