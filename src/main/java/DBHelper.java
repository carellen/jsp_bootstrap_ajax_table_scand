import db.Report;
import db.connection.DBConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {
    private final static String SQL_INSERT = "INSERT INTO reports(id, startDate, endDate, performer, activity) VALUES(?,?,?,?,?)";
    private Connection connection;

    public DBHelper() throws SQLException {
        connection = DBConnector.getConnection();
    }

    public PreparedStatement getPreparedStatement() {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_INSERT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }
    public boolean insertReport(PreparedStatement statement, Report report) {
        boolean flag = false;
        try {
            statement.setInt(1, report.getId());
            statement.setDate(2, new Date(report.getStartDate().getTime()));
            statement.setDate(3, new Date(report.getEndDate().getTime()));
            statement.setString(4, report.getPerformer());
            statement.setString(5, report.getActivity());
            statement.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
    public void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
