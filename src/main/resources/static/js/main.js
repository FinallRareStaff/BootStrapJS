$(function () {

    function aHeaderForAdminAndUser() {
        fetch('http://localhost:8080/user/userLogined')
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                $('#aHeaderForAdminAndUser').text(data.email + 'с ролями: ' + data.roleString)
            })
            .catch((error) => {
                console.log(error)
            })
    }

    function requestAllUsers() {
        fetch('http://localhost:8080/admin/users')
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    console.log('ERROR')
                }
            })
            .then((data) => {
                tableAdmin(data)
                tableIndex(data)
                aHeaderForAdminAndUser()
            })
            .catch((error) => {
                console.log(error)
            })
    }

    requestAllUsers()

    function tableAdmin(data) {
        let htmltableAdmin;
        $.each(data, function (key, val) {
            htmltableAdmin += '<tr class="tableRow">' +
                '<td>' + val.id + '</td>' +
                '<td>' + val.name + '</td>' +
                '<td>' + val.nickname + '</td>' +
                '<td>' + val.ladder + '</td>' +
                '<td>' + val.email + '</td>' +
                '<td>' + val.rolesString + '</td>' +
                '<td>' +
                '<button type="button" id="editButton" class="btn btn-secondary" data-bs-toggle="modal"' +
                'data-bs-target="#staticBackdropEdit" data-bs-whatever="' + val.id + '">' +
                'Изменить' +
                '</button>' +
                '</td>' +
                '<td>' +
                '<button type="button" id="deleteButton" class="btn btn-danger" data-bs-toggle="modal"' +
                'data-bs-target="#staticBackdropDelete" data-bs-whatever="' + val.id + '">' +
                'Удалить' +
                '</button>' +
                '</td>' +
                '<tr>'
        })
        $('#tbodyTableAdmin').html(htmltableAdmin)
    }

    function tableIndex(data) {
        let htmlIndex;
        $.each(data, function (key, val) {
            htmlIndex += '<tr class="tableRow">' +
                '<td>' + val.id + '</td>' +
                '<td>' + val.name + '</td>' +
                '<td>' + val.nickname + '</td>' +
                '<td>' + val.ladder + '</td>' +
                '<td>' + val.email + '</td>' +
                '<tr>'
        })
        $('#tbodyIndex').html(htmlIndex)
    }

    fetch('http://localhost:8080/admin/roles')
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
        $.error(data, function (key, value) {
            $('#rolesModalE').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
        });
    }

    const staticBackdropEdit = document.getElementById('staticBackdropEdit')
    staticBackdropEdit.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget
        let idUser = button.getAttribute('data-bs-whatever');
        const requestUserFromIdURL = 'http://localhost:8080/admin/user/' + idUser
        fetch(requestUserFromIdURL)
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

    const staticBackdropDelete = document.getElementById('staticBackdropDelete')
    staticBackdropDelete.addEventListener('show.bs.modal', function (event) {
        let button = event.relatedTarget
        let idUser = button.getAttribute('data-bs-whatever');
        const requestUserFromIdURL = 'http://localhost:8080/admin/user/' + idUser
        fetch(requestUserFromIdURL)
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

    function userFromCreate() {
        let name = $('#nameC').val()
        let nickname = $('#nicknameC').val()
        let ladder = $('#ladderC').val()
        let email = $('#emailC').val()
        let password = $('#passwordC').val()
        let roles = $('#rolesC').val()
        let roleString = '';
        $.each(roles, function (index, value) {
            roleString += value + ','
        })

        let user = {
            'name' : name,
            'nickname' : nickname,
            'ladder' : ladder,
            'email' : email,
            'password' : password,
            'roles' : roleString
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
                $('#createForm')[0].reset();
                alert('Пользователь' + data.email + ' добавлен')
            })
            .catch((error) => {
                console.log(error)
            })
        return false;
    })

    function userFormEdit() {
        let id = $('#idE').val()
        let name = $('#nameE').val()
        let nickname = $('#nicknameE').val()
        let ladder = $('#ladderE').val()
        let email = $('#emailE').val()
        let password = $('#passwordE').val()
        let roles = $('#rolesModalE').val()
        let roleString = '';
        $.each(roles, function (index, value) {
            roleString += value + ','
        })

        let user = {
            'name' : name,
            'nickname' : nickname,
            'ladder' : ladder,
            'email' : email,
            'password' : password,
            'roles' : roleString
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

    $('.deleteButtonModal').each(function () {
        $(this).click(function () {
            let userId = $(this).attr('value')
            fetch('http://localhost:8080/admin/deleteUser/' + userId, {
                method: 'DELETE',
            })
                .then((response) => {
                    if (response.ok) {
                        return response.json()
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