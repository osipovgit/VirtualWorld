function goToNextYear(documentLocation) {
    $.post(documentLocation + '/next_year').then(function (data) {
        if (data === "Wait until you\'ve, finished") {
            alert(data);
        }
        getMessages()
        displayPerks(documentLocation)
    })
}

function getMessages() {
    $.post(document.location + '/get_messages').then(function (data) {
        var text = ""
        JSON.parse(data, function (key, value) {
            if (key === "Notification") {
                text += "<p>" + value + "</p>";
            }
        });
        document.getElementById("events").innerHTML = text;

    })
}

function raiseHealth(id) {
    $.post(document.location + '/raise_health', {"id": id}).then(function (data){
        if (data !== "") {
            alert(data)
        }
        displayPerks(document.location)
        getCurrentYear()
        getMessages()
    })
}

function getCurrentYear() {
    $.post(document.location + '/get_current_year').then(function (data) {
        document.getElementById("year").innerHTML = "<h3>Now: " + data + " year</h3>";

    })
}

function checkingAllActions() {
    $.post(document.location + '/checking_all_actions').then(function (data) {
        if (data === "true") {
            var txt = "<button class='button-next' onClick='goToNextYear(document.location)' style='float: left'>"
                + "<span>Go to next year </span></button>";
        } else {
            var txt = "<button disabled class='button-disabled' style='float: left'>Wait until you've, finished</button>";
        }
        document.getElementById("next-year-button").innerHTML = txt;
    })
}

function displayPerks(documentLocation) {
    $.post(documentLocation + '/display').then(function (jsonPerksTable) {
        $('#table').empty();
        document.getElementById("table").value = "";
        var txt = "<table style='border-collapse: collapse'>" +
            "<tr style='background-color: limegreen; color: white'>" +
            "<td>ID</td><td>Profession</td><td>Age</td><td>Generation</td><td>Sex</td><td>Health</td>" +
            "<td>Marriage ID</td><td>Talent</td><td>Raise Health</td></td>";
        JSON.parse(jsonPerksTable, function (key, value) {
            if (key === "id") {
                txt += "<tr>";
            }
            if (key === "pc_id") {
                txt += "<td><button class='button-heal' type='submit' onclick='raiseHealth("
                    + value + ")'><span style='after'>Heal</span></button></td>";
            }
            else if (typeof value !== "object") {
                txt += "<td>" + value + "</td>";
            }
            if (key === "pc_id") {
                txt += "</tr>";
            }
        });
        txt += "</tr></table>"
        document.getElementById("table").innerHTML = txt;
    })
    checkingAllActions();
    getCurrentYear();
}

$(document).ready(function () {
    displayPerks(document.location);
    getMessages();
})