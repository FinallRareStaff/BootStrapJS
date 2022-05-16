async function aHeader() {
    try {
        const response = await fetch('http://localhost:8080/user/userLogined')
        const data = await response.json()
        tableUser(data)
        $('#aHeader').html('<strong>' + data.email + '</strong>' + ' with roles: ' + data.rolesString)
    } catch (error) {
        console.log(error)
    }
}
aHeader()

function tableUser(data) {
    let htmlTableUser;
    htmlTableUser = htmlTableUser + '<tr>' +
        '<td>' + data.id + '</td>' +
        '<td>' + data.name + '</td>' +
        '<td>' + data.nickname + '</td>' +
        '<td>' + data.ladder + '</td>' +
        '<td>' + data.email + '</td>' +
        '<td>' + data.rolesString + '</td>' +
        '<tr>'
    $('#tbodyTableUser').html(htmlTableUser)
}

