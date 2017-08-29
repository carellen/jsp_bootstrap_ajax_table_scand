

import db.Base;
import db.BaseImpl;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {
    Base base = new BaseImpl();


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
            base.addEntity(report.getId(), report);
            DBHelper helper = null;
            PreparedStatement statement = null;
            try {
                helper = new DBHelper();
                statement = helper.getPreparedStatement();
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

            List<Entity> entityCollection = getReportsList(req);
            if (entityCollection == null) {
                obj.addProperty("success", false);
            } else {
                jsonElement = gson.toJsonTree(entityCollection);
                obj.addProperty("success", true);
                obj.add("list", jsonElement);
            }
        } else {
            Set<String> setPerfomers = new HashSet<String>();
            for (Entity entity:
                    base.getAllEntities().values()) {
                setPerfomers.add(((Report)entity).getPerformer());
            }
            jsonElement = gson.toJsonTree(setPerfomers);
            obj.add("listOfAllPerformers", jsonElement);
        }

        PrintWriter out = resp.getWriter();
        out.println(obj.toString());
        out.close();

    }

    //retrieve reports from database
    //return empty list if found nothing
    //return null when got wrong date period
    private List<Entity> getReportsList(HttpServletRequest req) {
        List<Entity> result = new ArrayList<Entity>();

        String performer = req.getParameter("performer");
        Date start = null;
        Date end = null;
        try {
            start = req.getParameter("startDate").equals("") ? null :
                    new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("startDate"));
            end = req.getParameter("endDate").equals("") ? null :
                    new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(req.getParameter("endDate"));
        } catch (ParseException e) {
           // e.printStackTrace();
            return null;
        }
        if (start == null && end == null) {
            if (performer.equals("All Performers")) {
                result.addAll(base.getAllEntities().values());
                return result;
            } else {
                for (Entity e :
                        base.getAllEntities().values()) {
                    String perfor = ((Report) e).getPerformer();
                    if (perfor.equals(performer)) {
                        result.add(e);
                    }
                }
                return result;
            }
        }
        if (start == null) {
            if (performer.equals("All Performers")) {
                for (Entity e :
                        base.getAllEntities().values()) {
                    Date before = ((Report) e).getEndDate();
                    if (before.before(end) || before.equals(end)) {
                        result.add(e);
                    }
                }
                return result;
            } else {
                for (Entity e :
                        base.getAllEntities().values()) {
                    String perfor = ((Report) e).getPerformer();
                    Date before = ((Report) e).getEndDate();
                    if (perfor.equals(performer) && (before.before(end) || before.equals(end))) {
                        result.add(e);
                    }
                }
                return result;
            }
        }
        if (end == null) {
            if (performer.equals("All Performers")) {
                for (Entity e :
                        base.getAllEntities().values()) {
                    Date after = ((Report) e).getStartDate();
                    if (after.after(start) || after.equals(start)) {
                        result.add(e);
                    }
                }
                return result;
            } else {
                for (Entity e :
                        base.getAllEntities().values()) {
                    String perfor = ((Report) e).getPerformer();
                    Date after = ((Report) e).getStartDate();
                    if (perfor.equals(performer) && (after.after(start) || after.equals(start))) {
                        result.add(e);
                    }
                }
                return result;
            }
        }

        if (start.after(end)) {
            return null;
        } else {
            if (performer.equals("All Performers")) {
                for (Entity e :
                        base.getAllEntities().values()) {
                    Date after = ((Report) e).getStartDate();
                    Date before = ((Report) e).getEndDate();
                    if ((after.after(start) || after.equals(start)) && (before.before(end) || before.equals(end))) {
                        result.add(e);
                    }
                }
                return result;
            } else {
                for (Entity e :
                        base.getAllEntities().values()) {
                    String perfor = ((Report) e).getPerformer();
                    Date after = ((Report) e).getStartDate();
                    Date before = ((Report) e).getEndDate();
                    if (perfor.equals(performer) && (after.after(start) || after.equals(start))
                            && (before.before(end) || before.equals(end))) {
                        result.add(e);
                    }
                }
                return result;
            }
        }
    }
}
