package cfg;

public class Variable {

    // String name -> Variable-type with same name
    String name;
    State state;
    int lineNum;

    public Variable(String name, State state, int lineNum) {
        this.name = name;
        this.state = state;
        this.lineNum = lineNum;
    }

    // duplicator constructor
    public Variable(Variable other){
        this.name = other.name;
        this.state = other.state;
        this.lineNum = other.lineNum;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            Variable variable = (Variable) o;
            return (this.name).equals(variable.name);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return name.hashCode(); // Match on "name" exclusively.
    }

    // SET1 = {(x,INITIALIZED,5)}, SET2 ={(x,UNINITIALIZED, 10), (y,INITIALIZED,9)}
    /*
    int x = 5;
    while (x < 6) {
        int y = 10;
        int x;
    }
    END_WHILE:
    print(x);
     */
}