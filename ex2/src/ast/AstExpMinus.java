package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpMinus extends AstExp
{
	public AstExp left;
	public AstExp right;
	
	public AstExpMinus(AstExp left, AstExp right)
	{
		super("exp -> exp minus exp");

		this.left = left;
		this.right = right;
	}

	@Override
	protected String GetNodeName() {
		return "ExpMinus";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, right);
	}
}
