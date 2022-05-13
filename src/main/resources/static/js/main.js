$(function () {

    // blocks/header
    function aHeader() {
        fetch('http://localhost:8080/user/userLogined')
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                $('#aHeader').html('<strong>' + data.email + '</strong>' + ' with roles: ' + data.rolesString)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    //request users
    function requestAllUsers() {
        fetch('http://localhost:8080/admin/getUsers')
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                tableAdmin(data)
                aHeader()
            })
            .catch((error) => {
                console.log(error)
            })
    }

    requestAllUsers()

    // blocks/admin_page
    function tableAdmin(data) {
        let htmlTableAdmin;
        $.each(data, function (key, val) {
            htmlTableAdmin += '<tr class="tableRow">' +
                '<td>' + val.id + '</td>' +
                '<td>' + val.name + '</td>' +
                '<td>' + val.nickname + '</td>' +
                '<td>' + val.ladder + '</td>' +
                '<td>' + val.email + '</td>' +
                '<td>' + val.rolesString + '</td>' +
                '<td>' +
                '<button type="button" id="editButton" class="btn btn-info" data-bs-toggle="modal"' +
                'data-bs-target="#staticBackdropEdit" data-bs-whatever="' + val.id + '">' +
                'Edit' +
                '</button>' +
                '</td>' +
                '<td>' +
                '<button type="button" id="deleteButton" class="btn btn-danger" data-bs-toggle="modal"' +
                'data-bs-target="#staticBackdropDelete" data-bs-whatever="' + val.id + '">' +
                'Delete' +
                '</button>' +
                '</td>' +
                '</tr>'
        })
        $('#tbodyTableAdmin').html(htmlTableAdmin)
    }

    // select for blocks/new_User and blocks/edit_window
    fetch('http://localhost:8080/admin/getRoles')
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                console.log('ERROR')
            }
        })
        .then((data) => {
            selectCreatNew(data)
            selectModalE(data)
        })
        .catch((error) => {
            console.log(error)
        })

    function selectCreatNew(data) {
        $.each(data, function (key, value) {
            $('#rolesC').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
        })
    }

    function selectModalE(data) {
        $.each(data, function (key, value) {
            $('#rolesModalE').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
        })
    }

    // blocks/edit_window
    const staticBackdropEdit = document.getElementById('staticBackdropEdit')
    staticBackdropEdit.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget
        let idUser = button.getAttribute('data-bs-whatever');
        fetch('http://localhost:8080/admin/userFromId/' + idUser)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                $('#editButtonModal').attr('value', data.id)
                $('#idE').attr('value', data.id)
                $('#nameE').attr('value', data.name)
                $('#nicknameE').attr('value', data.nickname)
                $('#ladderE').attr('value', data.ladder)
                $('#emailE').attr('value', data.email)
                $('#passwordE').attr('value', data.password)
                $('#rolesModalE option:selected').each(function () {
                    this.selected = false;
                })
            })
            .catch((error) => {
                console.log(error)
            })
    })

    // blocks/delete_window
    const staticBackdropDelete = document.getElementById('staticBackdropDelete')
    staticBackdropDelete.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget
        let idUser = button.getAttribute('data-bs-whatever');
        fetch('http://localhost:8080/admin/userFromId/' + idUser)
            .then((response) => {
                if (response.ok) {
                    return response.json()
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                $('#deleteButtonModal').attr('value', data.id)
                $('#idD').attr('value', data.id)
                $('#nameD').attr('value', data.name)
                $('#nicknameD').attr('value', data.nickname)
                $('#ladderD').attr('value', data.ladder)
                $('#emailD').attr('value', data.email)
                $('#passwordD').attr('value', data.password)
                $('#rolesModalD').empty()
                $.each(data.roles, function (key, value) {
                    $('#rolesModalD').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
                })
            })
            .catch((error) => {
                console.log(error)
            })
    })

    // create User
    function userFromCreate() {
        let name = $('#nameC').val()
        let nickname = $('#nicknameC').val()
        let ladder = $('#ladderC').val()
        let email = $('#emailC').val()
        let password = $('#passwordC').val()
        let roles = $('#rolesC').val()
        let rolesString = '';
        $.each(roles, function (index, value) {
            rolesString += value + ','
        })

        let user = {
            'name' : name,
            'nickname' : nickname,
            'ladder' : ladder,
            'email' : email,
            'password' : password,
            'roles' : rolesString
        }
        return user
    }

    $('#createButton').click(function () {
        fetch('http://localhost:8080/admin/createUser', {
            method: 'POST',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify(userFromCreate())
        })
            .then((response) => {
                if (response.ok) {
                    return response.json()
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                console.log(data)
                requestAllUsers()
                $('#createForm')[0].reset()
            })
            .catch((error) => {
                console.log(error)
            })
        return false;
    })

    // edit User
    function userFormEdit() {
        let id = $('#idE').val()
        let name = $('#nameE').val()
        let nickname = $('#nicknameE').val()
        let ladder = $('#ladderE').val()
        let email = $('#emailE').val()
        let password = $('#passwordE').val()
        let roles = $('#rolesModalE').val()
        let rolesString = '';
        $.each(roles, function (index, value) {
            rolesString += value + ','
        })

        let user = {
            'id' : id,
            'name' : name,
            'nickname' : nickname,
            'ladder' : ladder,
            'email' : email,
            'password' : password,
            'roles' : rolesString
        }
        return user
    }


    $('.editButtonModal').on('click', function () {
        fetch('http://localhost:8080/admin/editUser', {
            method: 'PATCH',
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify(userFormEdit())
        })
            .then((response) => {
                if (response.ok) {
                    return response.json()
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                console.log(data)
                requestAllUsers()
                $('#staticBackdropEdit').modal('hide');
            })
            .catch((error) => {
                console.log(error)
            })
    })

    // delete User
    $('.deleteButtonModal').each(function () {
        $(this).click(function () {
            let userId = $(this).attr('value')
            fetch('http://localhost:8080/admin/deleteUser/' + userId, {
                method: 'DELETE',
            })
                .then((response) => {
                    if (response.ok) {
                        console.log('delete user')
                        requestAllUsers()
                        $('#staticBackdropDelete').modal('hide');
                    } else {
                        console.log('ERROR')
                    }
                })
                .catch((error) => {
                    console.log(error)
                })
        })
    })
})