$('#form').on('submit', function(e) {
	e.preventDefault();
	var key = $('#id').value();
	var authcode = $('#authcode').value();
	$.ajax({
		url: "./bot/",
		method: "post",
		contents: {
			"key": key,
			"authcode": authcode
		}
		onFinish: function(response) {
			alert(response);
		}
	});
});