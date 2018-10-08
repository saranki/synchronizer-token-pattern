window.addEventListener('load', function() {
	retrieveToken();
	checkStatus();
});

/**
 * Check the status in url and display success or failure
 * alert messages based on that
 * 
 * */
function checkStatus() {
	if (getParameterByName("status")
			&& getParameterByName("status") === "failed") {
		bootbox.alert({
			message : "The transaction was not completed!"
		});

	} else if (getParameterByName("status")
			&& getParameterByName("status") === "success") {
		bootbox.alert({
			message : "The transaction was completed successfully!!!"
		});

	}
}

/**
 * Split the url and get the status of the transaction displayed in the url.
 * 
 * **/
function getParameterByName(name, url) {
	if (!url)
		url = window.location.href;
	name = name.replace(/[\[\]]/g, '\\$&');
	var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'), results = regex
			.exec(url);
	if (!results)
		return null;
	if (!results[2])
		return '';
	return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

/**
 * Get the CSRF token from the server by requesting through an AJAX call.
 * Once the token is received embed it to the POST form just above the submit button as a hidden field.
 * 
 * */
function retrieveToken() {
	console.log('Retrieving token...');
	$.ajax({
		type : "GET",
		url : "/token",
		contentType : "text/json",
		success : function(data) {
			console.log("retrieved token.. ", data);
			$("#btn-submit").before(
					'<input type="hidden" name="csrf" value="' + data + '">');
		},
		error : function() {
			console.log("Error in loading token...");
		}
	});
}