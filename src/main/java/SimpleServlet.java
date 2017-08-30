import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import db.Entity;
import db.Report;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        String performer = req.getParameter("performer");
        String activity = req.getParameter("activity");

        Date start = null;
        Date end = null;
        try {
            start = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("startDate"));
            end = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("endDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JsonObject obj = new JsonObject();
        Gson gson = new Gson();
        JsonElement jsonElement = null;
        if (start.after(end)) {
            jsonElement = gson.toJsonTree(Report.nextId);
            obj.add("nextID", jsonElement);
            obj.addProperty("success", false);
        } else {
            Report report = new Report(start, end, performer, activity);
            DBHelper helper = null;
            PreparedStatement statement = null;
            try {
                helper = new DBHelper();
                statement = helper.getInsertPreparedStatement();
                helper.insertReport(statement, report);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                helper.closeStatement(statement);
            }
            jsonElement = gson.toJsonTree(Report.nextId);
            obj.add("nextID", jsonElement);
            obj.addProperty("success", true);
        }
        PrintWriter out = resp.getWriter();
        out.println(obj.toString());
        out.close();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        JsonElement jsonElement = null;
        if (req.getParameter("type") == null || !req.getParameter("type").equals("getListOfAllPerformers")) {
            String performer = req.getParameter("performer");
            Date start = null;
            Date end = null;
            try {
                start = req.getParameter("startDate").equals("") ? null :
                        new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("startDate"));
                end = req.getParameter("endDate").equals("") ? null :
                        new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("endDate"));
            } catch (ParseException e) {
                e.printStackTrace();

            }
            ResultSet resultSet = null;
            DBHelper helper = null;
            try {
                helper = new DBHelper();

                resultSet = helper.getReport(helper.getReadPreparedStatement(start == null ? null : new java.sql.Date(start.getTime()),
                        end == null ? null : new java.sql.Date(end.getTime()), performer));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (resultSet == null) {
                obj.addProperty("success", false);
            } else {
                List<Entity> entityCollection = new ArrayList<Entity>();
                try {
                    while (resultSet.next()) {
                        Report report = new Report();
                        report.setId(resultSet.getInt("id"));
                        report.setStartDate(new Date(resultSet.getDate("startDate").getTime()));
                        report.setEndDate(new Date(resultSet.getDate("endDate").getTime()));
                        report.setPerformer(resultSet.getString("performer"));
                        report.setActivity(resultSet.getString("activity"));
                        entityCollection.add(report);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                jsonElement = gson.toJsonTree(entityCollection);
                obj.addProperty("success", true);
                obj.add("list", jsonElement);
            }
        } else {
            DBHelper helper = null;
            ResultSet setPerformers = null;
            try {
                helper = new DBHelper();
                setPerformers = helper.getReport(helper.getPerformersPreparedStatement());
            } catch (SQLException e) {
            }
            List<String> list = new ArrayList<String>();
            try {
                while (setPerformers.next()) {
                    list.add(setPerformers.getString("performer"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            jsonElement = gson.toJsonTree(list);
            obj.add("listOfAllPerformers", jsonElement);
        }

        PrintWriter out = resp.getWriter();
        out.println(obj.toString());
        out.close();

    }

}
