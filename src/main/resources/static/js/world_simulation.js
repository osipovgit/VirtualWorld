function checkingAllActions() {
    $.post(document.location + '/checking_all_actions').then(function (data) {
        let txt;
        if (data === "true") {
            txt = "<button class='button-next' onClick='goToNextYear()' style='float: left'>"
                + "<span>Go to next year </span></button>"
        } else {
            txt = "<button class='button-disabled' onClick='goToNextYear()' "
                + "style='float: left'>Wait until you've, finished</button>"
        }
        document.getElementById("next-year-button").innerHTML = txt;
    })
}

function decideFate(destiny) {
    alert(destiny)
}

function displayPerks() {
    $.post(document.location + '/display').then(function (jsonPerksTable) {
        $.post(document.location + '/checking_all_actions').then(function (data) {
            $('#table').empty()
            document.getElementById("table").value = ""

            let txt = "<table style='border-collapse: collapse'>" +
                "<tr style='background-color: limegreen; color: white'>" +
                "<td>ID</td><td>Profession</td><td>Age</td><td>Generation</td><td>Sex</td><td>Health</td>" +
                "<td>Marriage ID</td><td>Talent</td><td>Raise Health</td></td>"

            JSON.parse(jsonPerksTable, function (key, value) {
                if (key === "id") {
                    txt += "<tr>"
                }
                if (key === "pc_id") {
                    if (data === "true") {
                        txt += "<td><button class='button-heal-disabled' type='submit' onclick='raiseHealth("
                            + value + ")'><span style='after'>Heal</span></button></td>"
                    } else {
                        txt += "<td><button class='button-heal' type='submit' onclick='raiseHealth("
                            + value + ")'><span style='after'>Heal</span></button></td>"
                    }
                } else if (typeof value !== "object") {
                    txt += "<td>" + value + "</td>"
                }
                if (key === "pc_id") {
                    txt += "</tr>"
                }
            })
            txt += "</tr></table>"
            document.getElementById("table").innerHTML = txt
        })
        checkingAllActions()
        getCurrentState()
        getNotifications()
    })
}

function getActions() {
    $.post(document.location + '/get_actions').then(function (actionJson) {
        $('#table').empty()
        let txt = "<div>"
        JSON.parse(actionJson, function (key, value) {
            if (key === "profession") {
                txt += "<p>" + value + "</p><button class='' type='submit' onclick='decideFate(\"DOCTOR\")'>" +
                    "<span style='after'>DOCTOR</span></button>"
                    + "<button class='' type='submit' onclick='decideFate(\"FARMER\")'>"
                    + "<span style='after'>FARMER</span></button>"
                    + "<button class='' type='submit' onclick='decideFate(\"SHERIFF\")'>"
                    + "<span style='after'>SHERIFF</span></button>"
            } else if (key === "caught") {
                txt += "<p>" + value + "<button class='' type='submit' onclick='decideFate(\"IMPRISON\")'>" +
                    "<span style='after'>IMPRISON</span></button>"
                    + "<button class='' type='submit' onclick='decideFate(\"KILL\")'>"
                    + "<span style='after'>KILL</span></button>"
            }
        })
        document.getElementById("actions").innerHTML = txt + "</div>"
    })
}

function getCurrentState() {
    $.post(document.location + '/get_current_state').then(function (data) {
        var txt = "<div>"
        JSON.parse(data, function (key, value) {
            if (typeof value !== "object") {
                txt += "<p>" + key + ": " + value + "</p>"
            }
        })
        document.getElementById("state").innerHTML = txt + "</div>"
    })
}

function getNotifications() {
    $.post(document.location + '/get_messages').then(function (data) {
        var text = ""
        JSON.parse(data, function (key, value) {
            if (key === "Notification") {
                text += "<p>" + value + "</p>"
            }
        })
        document.getElementById("events").innerHTML = text

    })
}

function goToNextYear() {
    $.post(document.location + '/next_year').then(function (data) {
        if (data === "Wait until you\'ve, finished") {
            swal({
                title: "Oops",
                text: data,
                icon: "warning",
                button: "I got it!",
            })
        } else if (data !== "") {
            swal({
                title: "All villagers of simulation died.",
                text: data,
                icon: "success",
                button: "Try again!",
            }).then(function (response) {
                document.location = '/'
            })
        } else {
            getNotifications()
            displayPerks()
            getCurrentState()
            getActions()
        }
    })
}

function raiseHealth(id) {
    $.post(document.location + '/raise_health', {"id": id}).then(function (data) {
        if (data !== "") {
            swal({
                title: "Go to next year!",
                text: data,
                icon: "error",
                button: "I got it!",
            })
        }
        displayPerks()
        getCurrentState()
        getNotifications()
    })
}

$(document).ready(function () {
    displayPerks()
    checkingAllActions()
    getCurrentState()
    getNotifications()
    getActions()
})