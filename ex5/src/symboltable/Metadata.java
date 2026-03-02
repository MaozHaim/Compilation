package symboltable;


public class Metadata {
    private boolean isVariable;
    private boolean isMethod;
    private int offset;
    private VAR_ROLE role;
    private String className;


    public Metadata() {
        this.isVariable = false;
        this.isMethod = false;
        this.offset = -1;
        this.role = null;
    }


    public boolean isVariable() {
        return isVariable;
    }


    private void checkIsVariable() throws Exception {
        if (!isVariable) throw new Exception("Not a variable.");
    }


    public void setAsVariable() {
        isVariable = true;
    }


    public boolean isMethod() {
        return isMethod;
    }


    public void setAsMethod() {
        isMethod = true;
    }


    public int getOffset() {
        return offset;
    }


    public void setOffset(int offset) throws Exception {
        checkIsVariable();
        this.offset = offset;
    }


    public void setGlobal() throws Exception {
        checkIsVariable();
        role = VAR_ROLE.GLOBAL;
    }


    public void setAttribute() throws Exception {
        checkIsVariable();
        role = VAR_ROLE.ATTRIBUTE;
    }


    public void setParameter() throws Exception {
        checkIsVariable();
        role = VAR_ROLE.PARAMETER;
    }


    public void setLocal() throws Exception {
        checkIsVariable();
        role = VAR_ROLE.LOCAL;
    }


    public VAR_ROLE getRole() {
        return this.role;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public enum VAR_ROLE {
        GLOBAL("Global"),
        LOCAL("Local"),
        ATTRIBUTE("Attribute"),
        PARAMETER("Parameter");

        public final String role;

        VAR_ROLE(String location) {
            this.role = location;
        }
    }
}
