import db.Report;
import db.connection.DBConnector;

import java.sql.*;

public class DBHelper {
    private final static String SQL_INSERT = "INSERT INTO reports(id, startDate, endDate, performer, activity) VALUES(?,?,?,?,?)";
    private final static String SQL_READ = "SELECT * FROM reports";
    private final static String SQL_READ_PERFORMERS = "SELECT DISTINCT performer FROM reports";
    private final static String SQL_READ_DUE_TO_DATE_PARAMETERS = "SELECT * FROM reports WHERE startDate >=? AND endDate <=?";
    private final static String SQL_READ_DUE_TO_START_PARAMETERS = "SELECT * FROM reports WHERE startDate >=?";
    private final static String SQL_READ_DUE_TO_END_PARAMETERS = "SELECT * FROM reports WHERE endDate <=?";
    private Connection connection;

    public DBHelper() throws SQLException {
        connection = DBConnector.getConnection();
    }

    public PreparedStatement getInsertPreparedStatement() {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_INSERT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }
    public PreparedStatement getPerformersPreparedStatement() {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SQL_READ_PERFORMERS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    public PreparedStatement getReadPreparedStatement(Date start, Date end, String performer) {
        if (performer == null || performer.equals("")) {
            return null;
        }
        PreparedStatement statement = null;

        if (start != null) {
            if (end != null) {
                try {
                    statement = connection.prepareStatement(SQL_READ_DUE_TO_DATE_PARAMETERS +
                            (performer.equals("All Performers") ? "" : " AND performer=" + performer));
                    statement.setDate(1, start);
                    statement.setDate(2, end);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return statement;
            } else {
                try {
                    statement = connection.prepareStatement(SQL_READ_DUE_TO_START_PARAMETERS +
                            (performer.equals("All Performers") ? "" : " AND performer=" + performer));
                    statement.setDate(1, start);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return statement;
            }
        } else {
            if (end != null) {
                try {
                    statement = connection.prepareStatement(SQL_READ_DUE_TO_END_PARAMETERS +
                            (performer.equals("All Performers") ? "" : " AND performer=" + performer));
                    statement.setDate(1, end);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return statement;
            } else {
                try {
                    statement = connection.prepareStatement(
                            performer.equals("All Performers") ? SQL_READ : SQL_READ + " WHERE performer=" + performer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return statement;
            }
        }
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

    public ResultSet getReport(PreparedStatement statement) throws SQLException {
        if (statement != null) {
            return statement.executeQuery();
        }
        else {
            return null;
        }


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
