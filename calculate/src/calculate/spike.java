package calculate;

public class spike {
	static String[] cities = {"Esbjerg",
            "Aarhus",
            "Kolding",
            "Odense",
            "Sonderborg"};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(String city: cities)
		{
			String test = "\"Esbjerg\"";
			if(test.contains(city))
			{
				System.out.println(city);
			}
		}
	}

}
