package types;

public class TypeArray extends Type{
    // "name" (from Type) will be the type of the elements.
    public Type typeOfElements;

    @Override
    public boolean isArray() {
        return true;
    }

    public TypeArray(String name, Type typeOfElements) {
        this.name = name;
        this.typeOfElements = typeOfElements;
    }
}
