package types;

// TODO i think we can delete this class (we use TypeClassMemberDec instead)
public class TypeClassVarDec
{
	public Type t;
	public String name;
	
	public TypeClassVarDec(Type t, String name)
	{
		this.t = t;
		this.name = name;
	}
}
