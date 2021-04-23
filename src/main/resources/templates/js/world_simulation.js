console.log(document.location)
function goToNextYear(documentLocation) {
    $.post(documentLocation + '/next_year').then(function (data) {
        displayPerks(documentLocation)
        var text = "<p>"
        text += data + "</p>"
        document.getElementById("events").innerHTML = text;
    })
}

function displayPerks(documentLocation) {
    $.post(documentLocation + '/display').then(function (jsonGameRooms) {
        $('#table').empty();
        document.getElementById("table").value = "";
        var txt = "<table border='1'><tr><td>ID</td><td>Profession</td><td>Age</td><td>Sex</td><td>Health</td><td>Marriage ID</td><td>Talent</td></td>";
        JSON.parse(jsonGameRooms, function (key, value) {
            if (key === "id") {
                txt += "<tr>";
            }
            if (typeof value !== "object") {
                txt += "<td>" + value + "</td>";
            }
            if (key === "talent") {
                txt += "</tr>";
            }
        });
        txt += "</tr></table>"
        document.getElementById("table").innerHTML = txt;
    })

}

// $(document).ready(function (){console.log(document.location)
// displayPerks(document.location)})
