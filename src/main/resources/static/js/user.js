fetch('http://localhost:8080/user/json/userAuthorized')
    .then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            console.log('ERROR')
        }
    })
    .then((data) => {
        tableUser(data)
        $('#aHeaderForAdminAndUser').html('<strong>' + data.email + '</strong>' + ' with roles: ' + data.rolesString)
    })
    .catch((error) => {
        console.log(error)
    })

function tableUser(data) {
    let htmlTableUser;
    htmlTableUser = htmlTableUser + '<tr class="tableRow">' +
        '<td>' + data.id + '</td>' +
        '<td>' + data.name + '</td>' +
        '<td>' + data.nickname + '</td>' +
        '<td>' + data.ladder + '</td>' +
        '<td>' + data.email + '</td>' +
        '<td>' + data.rolesString + '</td>' +
        '<tr>'
    $('#tbodyTableUser').html(htmlTableUser)
}
