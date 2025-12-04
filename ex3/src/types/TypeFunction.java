package types;

import ast.AstStmtList;

import java.util.List;

public class TypeFunction extends Type {
	public Type returnType; /* The return type of the function */
	public List<Type> params; /* types of input params */

	public TypeFunction(Type returnType, String name, List<Type> params) {
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TypeFunction)) return false;
		TypeFunction other = (TypeFunction) obj;

		if (!returnType.equals(other.returnType)) return false;
		if (other.params.size() != params.size()) return false;
		for (int i = 0; i < params.size(); i++){
			if (!params.get(i).equals(other.params.get(i))) return false;
		}
		return true;
	}
}
