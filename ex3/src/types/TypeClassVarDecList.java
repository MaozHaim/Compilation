package types;

public class TypeClassVarDecList
{
	public TypeClassMemberDec head;
	public TypeClassVarDecList tail;
	
	public TypeClassVarDecList(TypeClassMemberDec head, TypeClassVarDecList tail)
	{
		this.head = head;
		this.tail = tail;
	}	
}
