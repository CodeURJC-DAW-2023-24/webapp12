const NUM_RESULTS = 6;

let timesRequested = 1;

async function loadMoreReservations(id){

    const start = (timesRequested) * NUM_RESULTS;
    const end = start + NUM_RESULTS;
    timesRequested++;

    const response = await fetch(`/loadMoreReservations/${start}/${end}`);
    const newReviews = await response.text();


    const reviewsDiv = document.getElementById("reservationList");
    reviewsDiv.innerHTML += newReviews;

}