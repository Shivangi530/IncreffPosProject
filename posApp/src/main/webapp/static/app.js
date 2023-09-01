//HELPER METHOD
function toJson($form) {
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for (s in serialized) {
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function handleAjaxError(response) {
    var response = JSON.parse(response.responseText);
    danger(response.message);
}

function readFileData(file, callback) {
    var config = {
        header: true,
        delimiter: "\t",
        skipEmptyLines: "greedy",
        beforeFirstChunk: function(chunk) {
            var lines = chunk.split('\n');
            var headers = lines[0].split('\t');
            var uniqueHeaders = Array.from(new Set(headers)); // Remove duplicates
            if (headers.length !== uniqueHeaders.length) {
                warning("Duplicate headers detected.");
                return false;
            }
        },
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
    var blob = new Blob([data], {
        type: 'text/tsv;charset=utf-8;'
    });
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

function writeFileData(arr) {
    var config = {
        quoteChar: '',
        escapeChar: '',
        delimiter: "\t"
    };

    var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {
        type: 'text/tsv;charset=utf-8;'
    });
    var fileUrl = null;

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
    popup(message, "warning");
}
window.danger = function(message) {
    popup(message, "danger");
}
window.success = function(message) {
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

window.popup = function(message, text) {
    var alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-' + text;
    alertDiv.role = 'alert';
    alertDiv.textContent = message;

//    alertDiv.style.position = 'fixed';
//    alertDiv.style.top = '12%';
//    alertDiv.style.right = '0%';
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

// Format date
function formatDate(inputDate) {
    // Step 1: Convert input date string to a JavaScript Date object
    var dateParts = inputDate.split(',');
    var year = parseInt(dateParts[0], 10);
    var month = parseInt(dateParts[1], 10);
    var day = parseInt(dateParts[2], 10);

    // Step 2: Create a new Date object (months are 0-based in JavaScript Date)
    var date = new Date(year, month - 1, day);

    // Step 3: Format the date components into "dd-mm-yyyy" format
    var formattedDate = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();

    // Step 4: Return the formatted date string
    return formattedDate;
}

// Check file headers
function checkHeader(file, header_list, callback) {
    var allHeadersPresent = true;

    Papa.parse(file, {
        delimiter: "\t",
        step: function(results, parser) {
            console.log(results.data);
            if (results.data.length !== header_list.length) {
                allHeadersPresent = false;
            } else {
                for (var i = 0; i < header_list.length; i++) {
                    if (!results.data.includes(header_list[i])) {
                        allHeadersPresent = false;
                        break;
                    }
                }
            }
            parser.abort();
        },
        complete: function(results) {
            if (allHeadersPresent) {
                readFileData(file, callback);
            } else {
                danger("The file format is incorrect");
            }
        }
    });
}