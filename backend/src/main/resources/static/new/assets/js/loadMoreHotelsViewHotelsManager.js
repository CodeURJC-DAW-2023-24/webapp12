const NUM_RESULTS_MANAGER_HOTELS = 6;

let timesRequestedManagerHotels = 1;

async function loadMoreHotelsManagerView(){

    const start = (timesRequestedManagerHotels) * NUM_RESULTS_MANAGER_HOTELS;
    const end = start + NUM_RESULTS_MANAGER_HOTELS;
    timesRequestedManagerHotels++;

    const response = await fetch(`/loadMoreHotelsManagerView/${start}/${end}`);
    const newHotels = await response.text();


    const hotelsDiv = document.getElementById("managerHotelsList");
    hotelsDiv.innerHTML += newHotels;

}
