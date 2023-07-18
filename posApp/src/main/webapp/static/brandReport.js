var data = [];

function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/api/brand";
}

function getBrandList() {
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(response) {
            data = response;
            displayBrandList(response);
        },
        error: handleAjaxError
    });
}


//UI DISPLAY METHODS

function displayBrandList(data) {
    var $tbody = $('#brand-table').find('tbody');
    $tbody.empty();
    for (var i in data) {
        var e = data[i];
        var truncatedFields = {};
        Object.keys(e).forEach(function(key) {
            var value = e[key];
            if (typeof value === 'string' && value.length > 15) {
                value = value.substring(0, 15)+"...";
            }
            truncatedFields[key] = value;
        });
        var row = '<tr>' +
            '<td>' + truncatedFields.id + '</td>' +
            '<td>' + truncatedFields.brand + '</td>' +
            '<td>' + truncatedFields.category + '</td>' +
            '</tr>';

        $tbody.append(row);
    }
}

function printReport(fileName) {
    // Check if data is empty or undefined
    if (!data || data.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }
    var headers = Object.keys(data[0]);
    var dataArray = data.map(obj => Object.values(obj));

    var tsvData = [headers, ...dataArray];
    var tsvContent = tsvData.map(row => row.join('\t')).join('\n');

    var blob = new Blob([tsvContent], {
        type: 'text/tab-separated-values'
    });
    var url = URL.createObjectURL(blob);

    var link = document.createElement('a');
    link.href = url;
    link.download = fileName;
    link.style.display = 'none';

    document.body.appendChild(link);
    link.click();

    document.body.removeChild(link);
    URL.revokeObjectURL(url);
}
//INITIALIZATION CODE
function init() {
    $('#refresh-data').click(getBrandList);
}


$(document).ready(init);
$(document).ready(getBrandList);