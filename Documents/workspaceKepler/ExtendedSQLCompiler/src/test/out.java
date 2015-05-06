package test;
import java.sql.*;
import java.util.HashMap;

class Object {

    String cust;

    int sum0;
    int num0;
    int avg0;
    int sum1;
    int sum2;
    int num2;
    int avg2;

    public Object (String cust) {
        this.cust = cust;
        this.sum0 = 0;
        this.num0 = 0;
        this.sum1 = 0;
        this.sum2 = 0;
        this.num2 = 0;
    }

}

public class out {
    public static void main(String[] args) {

        String usr = "postgres";
        String pwd = "password";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        HashMap<String, Object> map = new HashMap<String, Object>();

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Success loading Driver!");
        } catch (Exception e) {
            System.out.println("Fail loading Driver!");
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url, usr, pwd);
            System.out.println("Success connecting server!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

            while (rs.next()) {

                String key = rs.getString("cust");

                if (!map.containsKey(key)) {
                    Object object = new Object(rs.getString("cust"));
                    map.put(key, object);
                }

                Object temp = map.get(key);

                if (rs.getString("state").equals("���NY���")) {
                    temp.sum0 += Integer.parseInt(rs.getString("quant"));
                    temp.num0++;
                } else if (rs.getString("state").equals("���NJ���")) {
                    temp.sum1 += Integer.parseInt(rs.getString("quant"));
                } else if (rs.getString("state").equals("���CT���")) {
                    temp.sum2 += Integer.parseInt(rs.getString("quant"));
                    temp.num2++;
                }

            }

            System.out.println("CUSTOMER  1_sum_quant  2_sum_quant  3_sum_quant");
            System.out.println("========  ===========  ===========  ===========");
            for (Object temp: map.values()) {
            temp.avg0 = temp.sum0/temp.num0;
            temp.avg2 = temp.sum2/temp.num2;

                if (temp.sum0 > 2 * temp.sum1 ) {
                    System.out.format("%-10s", temp.cust);
                    System.out.format("%11d", temp.sum0);
                    System.out.format("%13d", temp.sum1);
                    System.out.format("%13d", temp.sum2);
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection URL or username or password errors!");
            e.printStackTrace();
        }

    }
}
