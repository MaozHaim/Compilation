package types;

public abstract class Type
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Type other))
			return false;
		return this.name.equals(other.name);
	}
}
