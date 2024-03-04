window.onload = function () {

  console.log(hotelNames)
  console.log(reviewsAverage)

  //Better to construct options first and then pass it as a parameter
  var options = {
    title: {
      text: "Average review by hotel"
    },
    data: null
  };

  let hotelsData = {
    type: "column",
    dataPoints : []
  }

  for (let i = 0; i < hotelNames.length; i++) {
    hotelsData.dataPoints.push({label : hotelNames[i], y : Number(reviewsAverage[i])});
  }

  options.data = hotelsData;

  $("#chartContainer").CanvasJSChart(options);
}
