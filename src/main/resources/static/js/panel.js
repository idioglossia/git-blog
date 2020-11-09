$(document).on("click", ".delete-button", function () {
    var deleteType  = $(this).data('delete-type');
    var dataName = null;
    var deleteUrl = null;
    switch (deleteType) {
        case 'username':
            dataName = $(this).data('username');
            deleteUrl = '/api/users/'+dataName;
        break;
        case 'tag':
            dataName = $(this).data('tagname');
            deleteUrl = '/api/tags/'+dataName;
        break;
    }

    if(dataName == null)
        return;

    $(".modal-body #dataIdHolder").text(dataName);
    $('#deleteModal').modal('show').one('click', '#delete', function (e) {
        $.ajax({
            url: deleteUrl,
            type: 'DELETE',
            success: function(result) {
                location.reload();
            }
        });
    });
});