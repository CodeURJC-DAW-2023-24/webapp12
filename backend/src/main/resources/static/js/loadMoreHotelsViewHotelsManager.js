const NUM_RESULTS = 6;

let timesRequested = 1;

async function loadMoreHotelsManagerView(){

    const start = (timesRequested) * NUM_RESULTS;
    const end = start + NUM_RESULTS;
    timesRequested++;

    const response = await fetch(`/loadMoreHotelsManagerView/${start}/${end}`);
    const newHotels = await response.text();


    const hotelsDiv = document.getElementById("managerHotelsList");
    hotelsDiv.innerHTML += newHotels;

}
