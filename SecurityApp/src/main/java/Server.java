import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.IncidentManager;
import model.Incident;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import java.util.*;

import static spark.Spark.port;


public class Server {

    private static final String YOUR_DOMAIN_NAME = "sandbox04f80204cd5d41ad8894e69e291879f3.mailgun.org";

    //Add incident from form to the database
    private static void addIncident(float longitude, float latitude, String descriptions, int crimeCode, String date1, String location , String email) {
        String sql1= "";

        // Create prepared statement to insert into the database
        int id=1;

        try (Connection conn = getConnection()) {
            // Create prepared statement to insert into db
            String sql2="SELECT id FROM users WHERE email='"+email+"';";
            PreparedStatement st1 = conn.prepareStatement(sql2);
            //st1.setString(1, email);
            st1.executeQuery();

            ResultSet rs= st1.getResultSet();
            System.out.print("result set" + rs);

            if (rs.next()) {
                id = rs.getInt(1);
                System.out.print(id+"inside while");
            }
            PreparedStatement st = conn.prepareStatement("INSERT INTO incidents(longitude,latitude,description,crimeCode,dateAndTime, location,user_id) VALUES(?,?,?,?,?,?,?);");
            st.setFloat(1, longitude);
            st.setFloat(2, latitude);
            st.setString(3, descriptions);
            st.setInt(4, crimeCode);
            st.setString(5, date1);
            st.setString(6, location);
            // System.out.print(id+"still1");
            st.setInt(7, id);
            st.executeUpdate();
            
        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
    }


    // Send emails when new incident reported
    public static JsonNode sendSimpleMessage(String description, String location) throws UnirestException, SQLException {
        ResultSet rs = getUserEmails();
        List<String> ls = new ArrayList();
        String subject = "An incident happened at " + location;
        String email_list = "";
        while (rs.next())
        {
            email_list += rs.getString("email");
            email_list += ",";
        }
        System.out.print(email_list);
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
                .basicAuth("api",
                        "b2c938f91c6272b3e508abaae1a5470c-7005f37e-a4926a37")
                .field("from", "Incident rahulraman.3499@gmail.com")
//                .field("to", email_list)
                .field("to", email_list)

                .field("subject", subject)
                .field("text", description)
                // possibly add descirption and address a
                .asJson();
        return request.getBody();

    }

    // Get email info for users
    private static ResultSet getUserEmails() {
        String query = "SELECT email FROM users;";
        ResultSet emails = null;
        try (Connection conn = getConnection()) {
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            emails = st.getResultSet();
        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    //Add a new user to database after google sign-up
    private static void addUser(String name, String email ) {
        try (Connection conn = getConnection()) {
            PreparedStatement st = conn.prepareStatement("INSERT INTO users(name,email) VALUES (?,?) ON CONFLICT (email) DO NOTHING;");
            st.setString(1, name);
            st.setString(2, email);
            st.executeUpdate();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
    }

    //Get all the incidents within the database
    private static ResultSet getIncidents() {
        ResultSet incidents = null;
        try (Connection conn = getConnection()) {

            PreparedStatement st = conn.prepareStatement("SELECT * FROM incidents;");
            st.execute();
            incidents = st.getResultSet();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return incidents;
    }

    //Get all the incidents within the database, with most recent first
    private static ResultSet getIncidentsOrderedByDate() {
        ResultSet incidents = null;
        try (Connection conn = getConnection()) {

            PreparedStatement st = conn.prepareStatement("SELECT * FROM incidents ORDER BY dateAndTime DESC;");
            st.execute();
            incidents = st.getResultSet();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return incidents;
    }

    // Select incidents happened on a specific date from the databse
    private static ResultSet getIncidentsByDate(String pickedDate) {
        ResultSet incidents = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM incidents WHERE dateAndTime LIKE '" + pickedDate +"%' ORDER BY dateAndTime DESC;";

            PreparedStatement st = conn.prepareStatement(sql);
            st.executeQuery();
            incidents = st.getResultSet();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return incidents;
    }

    // Select incidents happened in a specific month from the database
    private static ResultSet getIncidentsByMonth(String pickedMonth) {
        ResultSet incidents = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM incidents WHERE dateAndTime LIKE '" + pickedMonth +"%' ORDER BY dateAndTime DESC;";

            PreparedStatement st = conn.prepareStatement(sql);
            st.executeQuery();
            incidents = st.getResultSet();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return incidents;
    }

    // Select incidents happened in a specific year from the database
    private static ResultSet getIncidentsByYear(String pickedYear) {
        ResultSet incidents = null;
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM incidents WHERE dateAndTime LIKE '" + pickedYear +"%' ORDER BY dateAndTime DESC;";

            PreparedStatement st = conn.prepareStatement(sql);
            st.executeQuery();
            incidents = st.getResultSet();

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
        return incidents;
    }

    static int PORT = 7000;
    private static int getPort() {
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            PORT = Integer.parseInt(herokuPort);
        }
        return PORT;
    }

    public static void main(String[] args) {

        port(getPort());
        workWithDatabase();
        Spark.staticFiles.location("/public");


        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            ResultSet rs = getIncidents();
            List<Incident> ls = new ArrayList<Incident>();
            while (rs.next()) {
                ls.add(new Incident(rs.getFloat(2),rs.getFloat(3),rs.getString(4),rs.getInt(5),rs.getString(6),rs.getString(7),rs.getInt(8)));
            }
            model.put("incidents", ls);
            String json = new Gson().toJson(ls);
            model.put("json",json);
            return new ModelAndView(model, "public/mainpage.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/intro", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/intro.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/login", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/login.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/signup", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/signup.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/contact", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "public/contact.vm");
        }, new VelocityTemplateEngine());


        Spark.get("/incidents", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            ResultSet rs = getIncidentsOrderedByDate();
            List<Incident> ls = new ArrayList<Incident>();
            while (rs.next()) {
                ls.add(new Incident(rs.getFloat(2),rs.getFloat(3),rs.getString(4),rs.getInt(5),rs.getString(6),rs.getString(7),rs.getInt(8)));
            }
            model.put("incidents", ls);

            return new ModelAndView(model, "public/incidents.vm");
        }, new VelocityTemplateEngine());

        Spark.post("/mainpage", (req, res) -> {
            String longitude = req.queryParams("latitude");
            String latitude = req.queryParams("longitude");
            String description = req.queryParams("description");
            String location = req.queryParams("address");
            String crimecode = req.queryParams("crimecode");
            String date=req.queryParams("date");
            String email= req.queryParams("email");

            addIncident(Float.parseFloat(latitude),Float.parseFloat(longitude),description,Integer.valueOf(crimecode), date,location, email);
            try {
                JsonNode jsonNode = sendSimpleMessage(description,location);
                System.out.println(jsonNode.toString());
            } catch (UnirestException e) {
                e.printStackTrace();
            }            res.status(201);
            res.type("application/json");
            return 1;
        });

        Spark.post("/login", (req, res) -> {
            String name = req.queryParams("name");
            String email = req.queryParams("email");
            addUser(name,email);
            res.status(201);
            res.type("application/json");
            return 1;

        });

        Spark.post("/incidents-today", (req, res) -> {
            IncidentManager.selectedDay = req.queryParams("picked_day");

            return 1;
        });

        Spark.post("/incidents-monthly", (req, res) -> {
            IncidentManager.selectedMonth = req.queryParams("picked_month");

            return 1;
        });

        Spark.post("/incidents-annual", (req, res) -> {
            IncidentManager.selectedYear = req.queryParams("picked_year");

            return 1;
        });

        Spark.get("/incidents-daily", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            ResultSet rs = getIncidentsByDate(IncidentManager.selectedDay);

            List<Incident> ls = new ArrayList<Incident>();
            while (rs.next()) {
                ls.add(new Incident(rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
            }
            int[] lsCounted = IncidentManager.countIncidentsByType(ls);
            String json = new Gson().toJson(ls);

            model.put("incidents",ls);
            model.put("types", lsCounted);
            String formatted_date = (IncidentManager.selectedDay).replaceAll("/", "-");
            model.put("selected_day", formatted_date);

            model.put("json",json);
            return new ModelAndView(model, "public/incidents-daily.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/incidents-annual", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            ResultSet rs = getIncidentsByYear(IncidentManager.selectedYear);

            List<Incident> ls = new ArrayList<Incident>();
            while (rs.next()) {
                ls.add(new Incident(rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
            }
            int[] lsCounted = IncidentManager.countIncidentsByType(ls);

            model.put("incidents",ls);
            model.put("types", lsCounted);
            model.put("selected_year", IncidentManager.selectedYear);

            return new ModelAndView(model, "public/incidents-annual.vm");
        }, new VelocityTemplateEngine());

        Spark.get("/incidents-monthly", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            ResultSet rs = getIncidentsByMonth(IncidentManager.selectedMonth);

            List<Incident> ls = new ArrayList<Incident>();
            while (rs.next()) {
                ls.add(new Incident(rs.getFloat(2), rs.getFloat(3), rs.getString(4), rs.getInt(5), rs.getString(6), rs.getString(7), rs.getInt(8)));
            }
            int[] lsCounted = IncidentManager.countIncidentsByType(ls);

            model.put("incidents",ls);
            model.put("types", lsCounted);
            String formatted_month = (IncidentManager.selectedMonth).replaceAll("/", "-");
            model.put("selected_month", formatted_month);

            return new ModelAndView(model, "public/incidents-monthly.vm");
        }, new VelocityTemplateEngine());

    }

    // Set up the database
    private static void workWithDatabase(){
        try (Connection conn = getConnection()) {
            String sql_inc = "";
            String sql_user = "";

            if ("SQLite".equalsIgnoreCase(conn.getMetaData().getDatabaseProductName())) { // running locally
                sql_inc = "CREATE TABLE IF NOT EXISTS incidents (id SERIAL PRIMARY KEY, " +
                        "longitude DECIMAL NOT NULL, latitude DECIMAL NOT NULL, description VARCHAR(10000), " +
                        "crimeCode INTEGER NOT NULL, dateAndTime VARCHAR(100) NOT NULL, location VARCHAR(100) NOT NULL, " +
                        //"user_id INTEGER FOREIGN KEY);";
                        "user_id INTEGER NOT NULL);";
                sql_user = "CREATE TABLE IF NOT EXISTS users (user_id SERIAL PRIMARY KEY, name VARCHAR(100));";
            }
            else {
                sql_inc = "CREATE TABLE IF NOT EXISTS incidents (id SERIAL PRIMARY KEY, " +
                        "longitude DECIMAL NOT NULL, latitude DECIMAL NOT NULL, description VARCHAR(10000), " +
                        "crimeCode INTEGER NOT NULL, dateAndTime VARCHAR(100) NOT NULL, location VARCHAR(100) NOT NULL, " +
                        // "user_id INTEGER FOREIGN KEY);";
                        "user_id INTEGER NOT NULL);";
                sql_user = "CREATE TABLE IF NOT EXISTS users (user_id SERIAL PRIMARY KEY, name VARCHAR(100));";
            }

            Statement st = conn.createStatement();
            st.execute(sql_inc);
            st.execute(sql_user);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }
    }

    //create connection to DB
    private static Connection getConnection() throws URISyntaxException, SQLException {
        String databaseUrl = System.getenv("DATABASE_URL" +
                "");
        if (databaseUrl == null) {
            // Not on Heroku, so use SQLite
            return DriverManager.getConnection("jdbc:sqlite:./SecurityApp.db");
        }

        URI dbUri = new URI(databaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

        return DriverManager.getConnection(dbUrl, username, password);
    }
}