$(document).on("click", ".user-delete-button", function () {
    var username = $(this).data('username');
    $(".modal-body #usernameHolder").text(username);
    $('#deleteModal').modal('show').one('click', '#delete', function (e) {
        $.ajax({
            url: '/api/users/'+username+'/delete',
            type: 'DELETE',
            success: function(result) {
                location.reload();
            }
        });
    });
});