package types;

import java.util.List;

public class TypeClass extends Type
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TypeClass father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public List<TypeClassMemberDec> dataMembers;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeClass(TypeClass father, String name, List<TypeClassMemberDec> dataMembers)
	{
		this.name = name;
		this.father = father;
		this.dataMembers = dataMembers;
	}

	public boolean isSubtypeOf(TypeClass other){
		for (TypeClass current = this; current != null; current = current.father){
			if (current.equals(other)) return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof TypeClass)) return false;
		TypeClass other = (TypeClass) obj;
		return this.name.equals(other.name);
	}
}
