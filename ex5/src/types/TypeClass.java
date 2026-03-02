package types;

import ir.InitialConstVal;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class TypeClass extends Type
{
	public TypeClass father;
	public List<TypeClassMemberDec> dataMembers;
	public List<Pair<TypeClass,TypeFunction>> methodsInfo;
	public List<String> attributes;
	private final List<InitialConstVal> initialValues; // for every attribute by order, saves the initial value (0 if not provided)


	public TypeClass(TypeClass father, String name, List<TypeClassMemberDec> dataMembers)
	{
		this.name = name;
		this.father = father;
		this.dataMembers = dataMembers;
		this.methodsInfo = new ArrayList<>();
		this.attributes = new ArrayList<>();
		this.initialValues = new ArrayList<>();
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


	public void addInitialVal(InitialConstVal val){
		initialValues.add(val);
	}


	public List<InitialConstVal> getInitialValues() {
		List<InitialConstVal> totalList = new ArrayList<>();

		if (father != null) totalList.addAll(father.getInitialValues());
		totalList.addAll(initialValues);
		return totalList;
	}


	public int getAttributeIndex(String attribute){
		for (int i = 0; i < attributes.size(); i++){
			if (attributes.get(i).equals(attribute)) return i + 1; // 1 based array
		}

		throw new RuntimeException("attribute not found in " + name);
	}


	public int getMethodIndex(String method){
		for (int i = 0; i < methodsInfo.size(); i++){
			String currentMethod = methodsInfo.get(i).second.name;
			if (currentMethod.equals(method)) return i;
		}

		throw new RuntimeException("attribute not found in " + name);
	}
}
