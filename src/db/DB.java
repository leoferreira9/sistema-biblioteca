package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    public static Properties getProperties(){
        try {
            Properties prop = new Properties();
            prop.load(DB.class.getResourceAsStream("/database/db.properties"));
            return prop;
        }catch (IOException e){
            System.out.println("❌ Erro ao carregar db.properties");
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        try {
            Properties prop = getProperties();
            return DriverManager.getConnection(
                    prop.getProperty("db.url"),
                    prop.getProperty("db.user"),
                    prop.getProperty("db.password")
            );
        }catch (SQLException e){
            System.out.println("❌ Erro ao conectar ao banco: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
