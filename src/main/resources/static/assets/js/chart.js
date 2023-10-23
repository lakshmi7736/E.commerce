$(function () {

  // =====================================
  // Profit
  // =====================================


//to get x-axis details
        // Get all date elements with the specified id
        var dateTds = document.querySelectorAll("#dateTd");

        // Create an array to store the dates
        var dateArray = [];

        dateTds.forEach(function(dateTd) {
            // Extract the date text from each element and push it to the array
            var dateText = dateTd.textContent;
            dateArray.push(dateText);
        });

        // Log the array of dates to the console
        console.log("Array of Dates:", dateArray);

//end of  x-axis details


//to get y-axis details

   // Get grand total with the specified id
        var grandTd = document.getElementById("grandTd");

       // Get the text content of the span element
           var grandTdValueText = grandTd.textContent;

           // Convert the BigDecimal to a JavaScript number
           var grandTdValue = parseFloat(grandTdValueText);

        // Log the value to the console
        console.log("grandTd:", grandTdValue);

 //end of  y-axis details



 //to get order amount details
     // Get all order amount elements with the specified id
     var orderTds = document.querySelectorAll("#orderTd");

     // Create an array to store the order amount
     var orderArray = [];

     orderTds.forEach(function(orderTd) {
         // Extract the order amount from each element and push it to the array
         var orderAmount = orderTd.textContent;
         orderArray.push(orderAmount);
     });
     // Log the array of order amounts to the console
     console.log("Array of Orders:", orderArray);


 //end of order amount details


  var chartOptions = {
    series: [
      { name: "Order amount:", data: orderArray },
    ],

    chart: {
      type: "bar",
      height: 345,
      offsetX: -15,
      toolbar: { show: true },
      foreColor: "#adb0bb",
      fontFamily: 'inherit',
      sparkline: { enabled: false },
    },


    colors: ["#5D87FF", "#49BEFF"],


    plotOptions: {
      bar: {
        horizontal: false,
        columnWidth: "35%",
        borderRadius: [6],
        borderRadiusApplication: 'end',
        borderRadiusWhenStacked: 'all'
      },
    },
    markers: { size: 0 },

    dataLabels: {
      enabled: false,
    },


    legend: {
      show: false,
    },


    grid: {
      borderColor: "rgba(0,0,0,0.1)",
      strokeDashArray: 3,
      xaxis: {
        lines: {
          show: false,
        },
      },
    },

    xaxis: {

      type: "Order Date",
      categories: dateArray,
      labels: {
        style: { cssClass: "grey--text lighten-2--text fill-color" },
      },
    },


    yaxis: {
      show: true,
      min: 0,
      max: grandTdValue,
      tickAmount: 4,
      labels: {
        style: {
          cssClass: "grey--text lighten-2--text fill-color",
        },
      },
    },
    stroke: {
      show: true,
      width: 3,
      lineCap: "butt",
      colors: ["transparent"],
    },


    tooltip: { theme: "light" },

    responsive: [
      {
        breakpoint: 600,
        options: {
          plotOptions: {
            bar: {
              borderRadius: 3,
            }
          },
        }
      }
    ]


  };


var chart = new ApexCharts(document.querySelector("#chartSalesReport"), chartOptions);
chart.render();


   // Listen for the custom event
      window.addEventListener('myCustomEvent', function (event) {
          console.log("Message received in dashboard.js: Hey from inline script!");
      });
})