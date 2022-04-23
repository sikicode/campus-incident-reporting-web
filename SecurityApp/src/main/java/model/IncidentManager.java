package model;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IncidentManager {

    public static String selectedDay = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    public static String selectedMonth = new SimpleDateFormat("yyyy/MM/dd").format(new Date()).substring(0, 7);
    public static String selectedYear = new SimpleDateFormat("yyyy/MM/dd").format(new Date()).substring(0, 4);


    public static int[] countIncidentsByType(List<Incident> incidents)
    {
        int[] result = new int[9];

        // Order corresponds to display on the frontend
        for(Incident inc: incidents)
        {
            if(inc.getCrimeCode() == 1) //Murder
                result[0]++;
            else if(inc.getCrimeCode() == 2) //Rape
                result[1]++;
            else if(inc.getCrimeCode() == 3) //Robbery
                result[2]++;
            else if(inc.getCrimeCode() == 4) //Assault
                result[3]++;
            else if(inc.getCrimeCode() == 5) //Burglary
                result[4]++;
            else if(inc.getCrimeCode() == 6) //Larceny
                result[5]++;
            else if(inc.getCrimeCode() == 7) //Auto Theft
                result[6]++;
            else if(inc.getCrimeCode() == 8) //Arson
                result[7]++;
            else // crime code = 9 is Shooting
                result[8]++;
        }

        return result;
    }
}
