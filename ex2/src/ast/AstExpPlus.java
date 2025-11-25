package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpPlus extends AstExp
{
	public AstExp left;
	public AstExp right;
	
	public AstExpPlus(AstExp left, AstExp right)
	{
		super("exp -> exp plus exp");

		this.left = left;
		this.right = right;
	}

	@Override
	protected String GetNodeName() {
		return "ExpPlus";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, right);
	}
}
