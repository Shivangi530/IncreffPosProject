
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	danger(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}
function createTsvFile(dataToWrite, headers, fileName) {
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: "\t"
    };

    // Prepend the headers to the data array
    dataToWrite.unshift(headers);

    var data = Papa.unparse(dataToWrite, config);
    var blob = new Blob([data], { type: 'text/tsv;charset=utf-8;' });
    var fileUrl = null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, fileName);
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }

    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', fileName);
    tempLink.click();
}



function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}
// Reset input fields
function resetFormFields($form) {
    $form.find('input').val('').attr('placeholder', function() {
        return $(this).attr('placeholder');
    });
}

// ERROR FUNCTIONS
window.warning = function(message) {
  popup(message,"warning");
}
window.danger=function(message) {
  popup(message,"danger");
}
window.success=function(message) {
    var alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-success';
        alertDiv.role = 'alert';
        alertDiv.textContent = message;

        alertDiv.style.position = 'fixed';
        alertDiv.style.top = '12%';
        alertDiv.style.right = '0%';
        alertDiv.style.transform = 'translateX(-50%)';

    document.body.appendChild(alertDiv);

    setTimeout(function() {
          alertDiv.remove();
    }, 2000);
}
window.popup=function(message,text) {
    var alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-'+text;
    alertDiv.role = 'alert';
    alertDiv.textContent = message;

    alertDiv.style.position = 'fixed';
    alertDiv.style.top = '12%';
    alertDiv.style.right = '0%';
    alertDiv.style.transform = 'translateX(-50%)';
    alertDiv.style.zIndex = '9999';

    var closeButton = document.createElement('button');
    closeButton.className = 'close';
    closeButton.setAttribute('type', 'button');
    closeButton.setAttribute('data-dismiss', 'alert');
    closeButton.setAttribute('aria-label', 'Close');

    var closeSpan = document.createElement('span');
    closeSpan.setAttribute('aria-hidden', 'true');
    closeSpan.innerHTML = '&times;';

    closeButton.appendChild(closeSpan);
    alertDiv.appendChild(closeButton);

    document.body.appendChild(alertDiv);

}

