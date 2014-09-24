package pl.pwlsltsk.workshop.amazon.emr;

import java.sql.*;

/**
 * @author pwlsltsk
 *
 * Run JDBC query against Impala cluster
 */
public class Exercise09 {

    private static final String MASTER_NODE = "";
    private static final String URL = String.format("jdbc:hive2://%s:21050/;auth=noSasl", MASTER_NODE);

    public static void main(String[] args) throws SQLException {

        DriverManager.registerDriver(new org.apache.hive.jdbc.HiveDriver());

        final String createQuery = "create external table if not exists tweets (tdate string, ttext string)" +
                "row format delimited fields terminated by '\\t' stored as textfile location 'hdfs:///data'";

        final String selectQuery = "select substr(tdate, 1, 10) as tday, count(*) as tcount from " +
                "tweets where ttext like 'RT%' group by tday order by tcount desc limit 30";

        //TODO
    }
}
