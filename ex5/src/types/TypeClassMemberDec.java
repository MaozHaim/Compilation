package types;

import symboltable.Metadata;

public class TypeClassMemberDec {
    public Type t;
    public String name; // ID of the member
    public Metadata metadata; /* This is necessary because once we close a class, we're only left with this object,
                                 and we *need* its metadata. */

    public TypeClassMemberDec(Type t, String name, Metadata metadata) {
        this.t = t;
        this.name = name;
        this.metadata = metadata;
    }

    public String toString() {
        return (t instanceof TypeFunction ? "FUNCTION: " : "ATTRIBUTE: ") + name; // hehe
    }
}
