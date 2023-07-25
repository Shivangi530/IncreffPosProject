var table;
var data;
function getBrandUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content");
    return baseUrl + "/api/brand";
}

function getBrandList() {
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(response) {
            data=response;
            displayBrandList(response);
        },
        error: handleAjaxError
    });
}

// UI DISPLAY METHODS

function displayBrandList(data) {
    table.clear().draw();
    var dataRows = [];
    for (var i in data) {
        var e = data[i];
        var trimmedBrand = e.brand.length > 15 ? e.brand.substring(0, 15) + '...' : e.brand;
        var trimmedCategory = e.category.length > 15 ? e.category.substring(0, 15) + '...' : e.category;
        var row = '<tr>' +
            '<td>' + trimmedBrand + '</td>' +
            '<td>' + trimmedCategory + '</td>' +
            '</tr>';
        dataRows.push([trimmedBrand, trimmedCategory]);
    }
    table.rows.add(dataRows).draw();
}

function printReport() {
    // Check if data is empty or undefined
    if (!data || data.length === 0) {
        console.error('No data available to generate the report.');
        return;
    }

    // Extract only the "brand" and "category" fields from the data
    var filteredData = data.map(({ brand, category }) => ({ brand, category }));

    // Prepare headers and TSV data for the writeFileData function
    var headers = ['Brand', 'Category'];
    var tsvData = filteredData.map(obj => [obj.brand, obj.category]);

    // Call the createTsvFile function with the filtered TSV data and file name
    createTsvFile(tsvData, headers, 'BrandReport.tsv');
}

// INITIALIZATION CODE
function init() {
    $('#refresh-data').click(getBrandList);
    // Initial data fetch
    getBrandList();
    table = $('#brand-table').DataTable({
        columnDefs: [
            { targets: [0, 1], className: "text-center" }
        ],
        searching: true,
        info: true,
        lengthMenu: [
            [5, 10, 20, -1],
            [5, 10, 20, 'All']
        ],
        deferRender: true,
    });
}

$(document).ready(function() {
    init();
    getBrandList();
});
