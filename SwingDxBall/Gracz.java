package Cwiczenia2.Grafika.gra;

public class Gracz
{
	public String imie;
	int punkty;
	String czas;
	public Gracz(String imie, int punkty, String czas)
	{
		setImie(imie);
		setPunkty(punkty);
		setCzas(czas);
	}


	public void setImie(String imie)
	{
		this.imie = imie;
	}

	public void setPunkty(int punkty)
	{
		this.punkty = punkty;
	}

	public void setCzas(String czas)
	{
		this.czas = czas;
	}

	public String getImie()
	{
		return imie;
	}

	public int getPunkty()
	{
		return punkty;
	}

	public String getCzas()
	{
		return czas;
	}
}
