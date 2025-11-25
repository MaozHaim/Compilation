package ast;

import java.util.Arrays;
import java.util.List;

public class AstExpLt extends AstExp
{
	public AstExp left;
	public AstExp right;
	
	public AstExpLt(AstExp left, AstExp right)
	{
		super("exp -> exp LT exp");

		this.left = left;
		this.right = right;
	}

	@Override
	protected String GetNodeName() {
		return "ExpLt";
	}
	@Override
	protected List<? extends AstNode> GetChildren() {
		return Arrays.asList(left, right);
	}
}
