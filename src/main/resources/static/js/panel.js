$(document).on("click", ".user-delete-button", function () {
    var username = $(this).data('username');
    $(".modal-body #usernameHolder").text(username);
    // As pointed out in comments,
    // it is unnecessary to have to manually call the modal.
    $('#deleteModal').modal('show').one('click', '#delete', function (e) {
        //todo: delete user
        window.alert("Delete!");
    });
});