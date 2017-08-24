package db;

import java.util.Date;

public class Report implements Entity{

    public static int nextId = 0;
    private int id;
    private Date startDate;
    private Date endDate;
    private String performer;
    private String activity;

    public Report(Date startDate, Date endDate, String performer, String activity) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.performer = performer;
        this.activity = activity;
        this.id = nextId++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", performer='" + performer + '\'' +
                ", activity='" + activity + '\'' +
                '}';
    }
}