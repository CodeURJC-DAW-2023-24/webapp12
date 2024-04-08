const NUM_RESULTS_HOTELS = 6;

let timesRequestedHotels = 1;

async function loadMoreHotels(){

    const start = (timesRequestedHotels) * NUM_RESULTS_HOTELS + 1;
    const end = start + NUM_RESULTS_HOTELS;
    timesRequestedHotels++;

    const response = await fetch(`/loadMoreHotels/${start}/${end}`);
    const newHotels = await response.text();


    const hotelsDiv = document.getElementById("hotelsList");
    hotelsDiv.innerHTML += newHotels;

}