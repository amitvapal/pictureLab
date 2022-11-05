
public class Temperature
{
	//instance variables (state,member, fields)
	private double fTemp;
	// constructors have no return type

	public Temperature()
	{
		fTemp=0;
	}
	public Temperature(double f)
	{
		fTemp=f;
		//this.fTemp = fTemp; this is bad way kinda
		
		
	}
	
	//modifier method "setter"
	public void setFahr(double f)
	{
		fTemp = f;
	}
	
	//Accessory method "getter"
	public double getFahr()
	{
		return fTemp;
	}
	
	public double getCelsius()
	{
		return 5.0/9*(fTemp-32);
		
	}
	
	public double getKelvin()
	{
		return 5.0/9*(fTemp-32) + 273.0;
		
	}
	
	public boolean isEthylFreezing()
	{
		return fTemp<=-173;
	}
	public boolean isEthylBoiling()
	{
		return fTemp>=172;
	}
	public boolean isWaterFreezing()
	{
		return (fTemp>=32);
	}
	
	public boolean isWaterBoiling()
	{
		return (fTemp>=212);
	}
	public boolean isOxygenFreezing()
	{
		return (fTemp<=361.8);
	}
	public boolean isOxygenBoiling()
	{
		return (fTemp>=-297.3);
	}
	
	public String getEthylStatus()
	{
		if(isEthylFreezing())
			return "Freezing";
		else if(isEthylBoiling())
			return "Boiling";
		else
			return "---------------";
	
	}
	
	public String getWaterStatus()
	{
		if(isWaterFreezing())
			return "Freezing";
		else if(isWaterBoiling())
			return "Boiling";
		else
			return "---------------";
		
	
	}
	
	public String getOxygenStatus()
	{
		if(isOxygenFreezing())
			return "Freezing";
		else if(isOxygenBoiling())
			return "Boiling";
		else
			return "---------------";
	
	}
	
	/**
	 * String representation of a Temperature Object
	 * Allows you to use with System.out.println with an object's identifier
	 */
	public String toString()
	{
		String fmt = "%12.2f%12.2f%12.2f%12.2f%12s%12s%12s%n";
		return String.format(fmt, getFahr(), getCelsius(), getKelvin(), getEthylStatus(), getWaterStatus(), getOxygenStatus());
		
	}
	
	
	
	
	
	
	
	

}
