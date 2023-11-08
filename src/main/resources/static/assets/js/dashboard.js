$(function () {


  // =====================================
  // Profit
  // =====================================
//total order placed

          //to get x-axis details
                  // Get all date elements with the specified id
                  var dateTds = document.querySelectorAll("#dateFilter");

                  // Create an array to store the dates
                  var dateArray = [];

                  dateTds.forEach(function(dateFilter) {
                      // Extract the date text from each element and push it to the array
                      var dateText = dateFilter.textContent;
                      dateArray.push(dateText);
                  });

                  // Log the array of dates to the console
                  console.log("Array of Dates:", dateArray);

          //end of  x-axis details


          //to get y-axis details

           // Get grand total with the specified id
          // Use document.getElementById to access the element by its ID

          var grandTd = document.getElementById("grandFilter");


              // Get the text content of the span element
              var grandTdValueText = grandTd.textContent;

              // Convert the text content to a JavaScript number
              var grandTdValue = parseFloat(grandTdValueText);

             // Log the array of dates to the console
               console.log("grandTdValue :", grandTdValue);

           //end of  y-axis details



           //to get order amount details
               // Get all order amount elements with the specified id
               var orderTds = document.querySelectorAll("#orderFilter");

               // Create an array to store the order amount
               var orderArray = [];

               orderTds.forEach(function(orderFilter) {
                   // Extract the order amount from each element and push it to the array
                   var orderAmount = orderFilter.textContent;
                   orderArray.push(orderAmount);
               });
               // Log the array of order amounts to the console
               console.log("Array of Orders:", orderArray);


//end of order amount details



//refund amount details


    //to get x-axis details
            // Get all date elements with the specified id
            var dateTdsq = document.querySelectorAll("#refundDateFilter");

            // Create an array to store the dates
            var datesArray = [];

            dateTdsq.forEach(function(refundDateFilter) {
                // Extract the date text from each element and push it to the array
                var datesText = refundDateFilter.textContent;
                datesArray.push(datesText);
            });

            // Log the array of dates to the console
            console.log("Array of Dates refunded :", dateArray);

    //end of  x-axis details


    //to get y-axis details

     // Get grand total with the specified id
    // Use document.getElementById to access the element by its ID

    var grandTdq = document.getElementById("refundGrandFilter");

        // Get the text content of the span element
        var grandTdqValueText = refundGrandFilter.textContent;

        // Convert the text content to a JavaScript number
        var grandTdqValue = parseFloat(grandTdqValueText);

       // Log the array of dates to the console
         console.log("grandTdqValue :", grandTdqValue);


     //end of  y-axis details



     //to get order amount details
         // Get all order amount elements with the specified id
         var orderTdsq = document.querySelectorAll("#refundOrderFilter");

         // Create an array to store the order amount
         var ordersArray = [];

         orderTdsq.forEach(function(refundOrderFilter) {
             // Extract the order amount from each element and push it to the array
             var ordersAmount = refundOrderFilter.textContent;
             ordersArray.push(ordersAmount);
         });
         // Log the array of order amounts to the console
         console.log("Array of Orders refunded:", ordersArray);


//end-of refund amount details



//monthly profit

// Get all profit amount elements with the specified id

// Calculate profit and store in a new array
    var profitArray = [];

    for (var i = 0; i < orderArray.length; i++) {
        var refund = ordersArray[i] || 0; // If ordersArray[i] is null, assume it's 0
        var profit = orderArray[i] - refund;
        profitArray.push(profit);
    }     console.log("Array of profit :", profitArray);


//end-of profit


  var chart = {
    series: [
        { name: "Order amount:", data: orderArray },
        {name:"Refunded amount :",data:ordersArray},
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
      type: "category",
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

  var chart = new ApexCharts(document.querySelector("#chart"), chart);
  chart.render();


  // =====================================
  // Breakup
  // =====================================
  var breakup = {
    color: "#adb5bd",
    series: [38, 40, 25],
    labels: ["2022", "2021", "2020"],
    chart: {
      width: 180,
      type: "donut",
      fontFamily: "Plus Jakarta Sans', sans-serif",
      foreColor: "#adb0bb",
    },
    plotOptions: {
      pie: {
        startAngle: 0,
        endAngle: 360,
        donut: {
          size: '75%',
        },
      },
    },
    stroke: {
      show: false,
    },

    dataLabels: {
      enabled: false,
    },

    legend: {
      show: false,
    },
    colors: ["#5D87FF", "#ecf2ff", "#F9F9FD"],

    responsive: [
      {
        breakpoint: 991,
        options: {
          chart: {
            width: 150,
          },
        },
      },
    ],
    tooltip: {
      theme: "dark",
      fillSeriesColor: false,
    },
  };

  var chart = new ApexCharts(document.querySelector("#breakup"), breakup);
  chart.render();



  // =====================================
  // Earning
  // =====================================




  var earning = {
    chart: {
      id: "sparkline3",
      type: "area",
      height: 60,
      sparkline: {
        enabled: true,
      },
      group: "sparklines",
      fontFamily: "Plus Jakarta Sans', sans-serif",
      foreColor: "#adb0bb",
    },
    series: [
      {
        name: "Profit amount:",
         data: profitArray,
        color: "#49BEFF",
      },
    ],
    stroke: {
      curve: "smooth",
      width: 2,
    },
    fill: {
      colors: ["#f3feff"],
      type: "solid",
      opacity: 0.05,
    },

    markers: {
      size: 0,
    },
    tooltip: {
      theme: "dark",
      fixed: {
        enabled: true,
        position: "right",
      },
      x: {
        show: false,
      },
    },
  };
  new ApexCharts(document.querySelector("#earning"), earning).render();
})