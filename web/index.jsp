<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Test task JSP+JQuery+Ajax+Bootstrap</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/js/scripts.js"></script>
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="http://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/js/bootstrap-datepicker.min.js"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.4.1/css/bootstrap-datepicker3.css"/>
   <%-- <link rel="stylesheet" href="http://cdn.datatables.net/1.10.15/css/jquery.dataTables.min.css">--%>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script>
        $(document).ready(function dateFormat() {
            var date_input = $('input[type="date"]');

            var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
            var options = {
                format: 'M dd, yyyy',
                container: container,
                todayHighlight: true,
                orientation: "top left",
                autoclose: true
            };
            date_input.datepicker(options);
        });
    </script>
</head>
<body>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8"><h1>Hello!This table for add and retrieve reports. Have fun!</h1></div>
        <div class="col-md-2"></div>
    </div>
    <button type="button" class="btn btn-block btn-danger btn-lg" data-toggle="collapse" data-target="#add">Click to add elements
    </button>
    <div id="add" class="collapse">
        <form action="javascript:void(null);" name="addNewReport" method="POST" id="add_rep" onsubmit="addReport()">
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>StartDate</th>
                    <th>EndDate</th>
                    <th>Performer</th>
                    <th>Activity</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td id="ID"></td>
                    <td>
                        <input type="date" name="startDate" class="form-control" placeholder="Input Start Date" required>
                    </td>
                    <td>
                        <input type="date" name="endDate" class="form-control" placeholder="Input End Date" required>
                    </td>
                    <td>
                        <input type="text" name="performer" class="form-control" placeholder="Input Performer" required>
                    </td>
                    <td>
                        <input type="text" name="activity" class="form-control" placeholder="Input Activity" required>
                    </td>
                </tr>
                </tbody>
            </table>
            <input type="submit" class="btn btn-primary" value="ADD NEW REPORT">
        </form>
    </div>
</br>
    <button type="button" class="btn btn-block btn-info btn-lg" data-toggle="collapse" data-target="#show">Click to show elements
    </button>
    <div id="show" class="collapse">
        <form action="javascript:void(null);" name="showReports" method="GET" id="show_rep" onsubmit="showReport()">
            <table class="table">
                <thead>
                <tr>
                    <th>StartDate</th>
                    <th>EndDate</th>
                    <th>Select Performer</th>
                    <th>Select Period</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <input type="date" id="strdt" name="startDate" class="form-control" placeholder="Input Start Date" onchange="clearChooseOption()">
                    </td>
                    <td>
                        <input type="date" id="enddt" name="endDate" class="form-control" placeholder="Input End Date" onchange="clearChooseOption()">
                    </td>
                    <td>
                        <div class="form-group">
                            <select class="form-control" name="performer" id="perf" onfocus="getPerformersList()" required>
                            </select>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <select class="form-control" id="period" onchange="dateFiller()">
                                <option></option>
                                <option>Last Qtr</option>
                                <option>Last Month</option>
                                <option>Last Calendar Year</option>
                                <option>Current Year to Date</option>
                                <option>Current Qtr to Date</option>
                                <option>Current Month to Date</option>
                            </select>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <input type="submit" class="btn btn-primary" value="SHOW REPORTS">
        </form>
    </div>
        <div id="showTableReports">
            <table class="table" id="reportsTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>StartDate</th>
                    <th>EndDate</th>
                    <th>Performer</th>
                    <th>Activity</th>
                </tr>
                </thead>

                <tbody id="resultTable" class="searchable">

                </tbody>
            </table>
        </div>
</div>
</body>
</html>