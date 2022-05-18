$(document).ready(function() {

    // blocks/header
    async function aHeader() {
        try {
            const response = await fetch('http://localhost:8080/user/userLogined')
            const data = await response.json()
            console.log(response)
            $('#aHeader').html('<strong>' + data.email + '</strong>' + ' with roles: ' + data.rolesString)
        } catch (error) {
            console.log(error)
        }
    }

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

    //request users
    async function requestAllUsers() {
        try {
            const response = await fetch('http://localhost:8080/admin/getUsers')
            const data = await response.json()
            console.log(response)
            tableAdmin(data)
            aHeader()
        } catch (error) {
            console.log(error)
        }
    }
    requestAllUsers()

    // select for blocks/new_User and blocks/edit_window
    async function selects() {
        try {
            const response = await fetch('http://localhost:8080/admin/getRoles')
            const data = await response.json()
            console.log(response)
            selectCreatNew(data)
            selectModalEdit(data)
        } catch (error) {
            console.log(error)
        }
    }
    selects()

    function selectCreatNew(data) {
        $.each(data, function (key, value) {
            $('#rolesCreate').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
        })
    }

    function selectModalEdit(data) {
        $.each(data, function (key, value) {
            $('#rolesModalEdit').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
        })
    }

    // blocks/edit_window
    document.getElementById('staticBackdropEdit')
    .addEventListener('show.bs.modal', async function (event) {
        try {
            const response = await fetch('http://localhost:8080/admin/userFromId/' + event
                .relatedTarget
                .getAttribute('data-bs-whatever'))
            const data = await response.json()
            console.log(response)
            $('#editButtonModal').attr('value', data.id)
            $('#idEdit').attr('value', data.id)
            $('#nameEdit').attr('value', data.name)
            $('#nicknameEdit').attr('value', data.nickname)
            $('#ladderEdit').attr('value', data.ladder)
            $('#emailEdit').attr('value', data.email)
            $('#passwordEdit').attr('value', data.password)
            $('#rolesModalEdit option:selected').each(function () {
                this.selected = false;
            })
        } catch (error) {
            console.log(error)
        }
    })

    // blocks/delete_window
    document.getElementById('staticBackdropDelete')
    .addEventListener('show.bs.modal', async function (event) {
        try {
            const response = await fetch('http://localhost:8080/admin/userFromId/' + event
                .relatedTarget
                .getAttribute('data-bs-whatever'))
            const data = await response.json()
            console.log(response)
            $('#deleteButtonModal').attr('value', data.id)
            $('#idDelete').attr('value', data.id)
            $('#nameDelete').attr('value', data.name)
            $('#nicknameDelete').attr('value', data.nickname)
            $('#ladderDelete').attr('value', data.ladder)
            $('#emailDelete').attr('value', data.email)
            $('#passwordDelete').attr('value', data.password)
            $('#rolesModalDelete').empty()
            $.each(data.roles, function (key, value) {
                $('#rolesModalDelete').prepend('<option value="' + value.id + '">' + value.roleString + '</option>');
            })
        } catch (error) {
            console.log(error)
        }
    })

    // create User
    function userFromCreate() {
        let name = $('#nameCreate').val()
        let nickname = $('#nicknameCreate').val()
        let ladder = $('#ladderCreate').val()
        let email = $('#emailCreate').val()
        let password = $('#passwordCreate').val()
        let roles = $('#rolesCreate').val()
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

    $('#createButton').click(async function () {
        try {
            await fetch('http://localhost:8080/admin/createUser', {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(userFromCreate())
            })
            requestAllUsers()
            $('#createForm')[0].reset()
        } catch (error) {
            console.log(error)
        }
        return tableAdmin();
    })

    // edit User
    function userFormEdit() {
        let id = $('#idEdit').val()
        let name = $('#nameEdit').val()
        let nickname = $('#nicknameEdit').val()
        let ladder = $('#ladderEdit').val()
        let email = $('#emailEdit').val()
        let password = $('#passwordEdit').val()
        let roles = $('#rolesModalEdit').val()
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

    $('.editButtonModal').on('click', async function () {
        try {
            await fetch('http://localhost:8080/admin/editUser', {
                method: 'PATCH',
                headers: {
                    'Content-type': 'application/json'
                },
                body: JSON.stringify(userFormEdit())
            })
            requestAllUsers()
            $('#staticBackdropEdit').modal('hide');
        } catch (error) {
            console.log(error)
        }
    })

    // delete User
    $('.deleteButtonModal').each(async function () {
        $(this).click(async function () {
            let userId = $(this).attr('value')
            try {
                await fetch('http://localhost:8080/admin/deleteUser/' + userId, {
                    method: 'DELETE',
                })
                requestAllUsers()
                $('#staticBackdropDelete').modal('hide')
            } catch (error) {
                console.log(error)
            }
        })
    })
})