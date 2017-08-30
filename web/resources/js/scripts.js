
function dateFiller() {
    var currDate = new Date();

    var chosenOption = document.getElementById("period");
    var options = {year: 'numeric', month: 'short', day: 'numeric'};
    var startDateField = document.getElementById("strdt");
    var endDateField = document.getElementById("enddt");
   // var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var lastMonth = currDate.getMonth() - 1;
    var currQtr = Math.ceil((currDate.getMonth() + 1)/3);
    var firstMonthsOfQtr = ["Jan", "Apr", "Jul", "Oct"];

    //WARNING! This beautiful part of code doesn't work properly in IE(what a shame...)
    switch (chosenOption.options[chosenOption.selectedIndex].text) {
        case "Last Qtr" : switch (currQtr) {
            case 1 : startDateField.value = "Oct 1, " + (currDate.getFullYear() - 1);
                endDateField.value = "Dec 31, " + (currDate.getFullYear() - 1);
                break;
            case 2 : startDateField.value = "Jan 1, " + currDate.getFullYear();
                endDateField.value = "Mar 31, " + currDate.getFullYear();
                break;
            case 3 : startDateField.value = "Apr 1, " + currDate.getFullYear();
                endDateField.value = "Jun 30, " + currDate.getFullYear();
                break;
            case 4 : startDateField.value = "Jul 1, " + currDate.getFullYear();
                endDateField.value = "Sep 30, " + currDate.getFullYear();
                break;
        }
            break;
        case "Last Month" : if (lastMonth == -1) {
            startDateField.value = "Dec" + " 1, " + (currDate.getFullYear() - 1);
            endDateField.value = "Dec 31, " + (currDate.getFullYear() - 1);

        } else {

            startDateField.value = new Date(currDate.getFullYear(), currDate.getMonth() - 1, 1)
                .toLocaleDateString("en-US", options);

            endDateField.value = new Date(currDate.getFullYear(), currDate.getMonth(), 0)
                .toLocaleDateString("en-US", options);
        }
            break;
        case "Last Calendar Year" :
            startDateField.value = "Jan 1, " + (currDate.getFullYear() - 1);
            endDateField.value = "Dec 31, " + (currDate.getFullYear() - 1);
            break;
        case "Current Year to Date" :
            startDateField.value = "Jan 1, " + currDate.getFullYear();
            endDateField.value = currDate.toLocaleDateString("en-US", options);
            break;
        case "Current Qtr to Date" :
            startDateField.value = firstMonthsOfQtr[currQtr - 1] + " 1, " + currDate.getFullYear();
            endDateField.value = currDate.toLocaleDateString("en-US", options);
            break;
        case "Current Month to Date" :
            startDateField.value = new Date(currDate.getFullYear(), currDate.getMonth(), 1)
                .toLocaleDateString("en-US", options);
            endDateField.value = currDate.toLocaleDateString("en-US", options);
            break;
    }
}

function clearChooseOption() {
     document.getElementById("period").selectedIndex = 0;
}

function clearFormAfterAddNewReport() {
    document.getElementById("add_rep").reset();
}

function addReport() {
    var msg   = $('#add_rep').serialize();
    $.ajax({
        type: 'POST',
        url: 'SimpleServlet',
        data: msg,
        dataType: "json",
        success: function (data) {
            if (!data.success) {
                alert("Warning! Start Date goes after End Date!");
            }
            $('#ID').html(data.nextID);
        }
    });
    clearFormAfterAddNewReport();
}
function showReport() {

    var msg   = $('#show_rep').serialize();
    $.ajax({
        type: 'GET',
        url: 'SimpleServlet',
        data: msg,
        dataType: "json",
        success: function(data) {
                if (data.success) {
                //for refreshing datatable we need to reinitialize it
                $('#reportsTable').DataTable().destroy();
                    var options = {year: 'numeric', month: 'short', day: 'numeric'};
                $('#reportsTable').DataTable({
                    data: data.list,
                    columns: [
                        {data: 'id'},
                        {data: 'startDate',
                            'render': function (jsonData) {
                                var date = new Date(jsonData).toLocaleDateString("en-US", options);
                                return date;
                            }},
                        {data: 'endDate',
                            'render': function (jsonData) {
                            var date = new Date(jsonData).toLocaleDateString("en-US", options);
                            return date;
                        }},
                        {data: 'performer'},
                        {data: 'activity'}
                    ]
                });
            }
            else {
                alert("Wrong date format!");
        }
            },
        error: function (req, status, err) {
            console.log('something went wrong', status, err);
        }

    });
}

function getPerformersList() {
    $.ajax({
        type: 'GET',
        url: 'SimpleServlet',
        data: "type=getListOfAllPerformers",
        dataType: "json",
        success: function (data) {
            $('#perf').html(" <option>All Performers</option>");
            for (i = 0; i < data.listOfAllPerformers.length; i++) {
                $('#perf').append("<option>" + data.listOfAllPerformers[i] + "</option>");
            }
        },
        error: function (req, status, err) {
            console.log('something went wrong', status, err);
        }
    });
}