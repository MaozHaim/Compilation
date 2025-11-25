package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpEq extends AstExp
{
	public AstExp left;
	public AstExp right;
	
	public AstExpEq(AstExp left, AstExp right)
	{
		super("exp -> exp EQ exp");

		this.left = left;
		this.right = right;
	}

	@Override
	protected String GetNodeName() {
		return "ExpEq";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, right);
	}
}
