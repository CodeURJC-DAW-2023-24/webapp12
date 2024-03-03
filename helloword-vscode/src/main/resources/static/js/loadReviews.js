const NUM_RESULTS = 6;

let timesRequested = 1;

async function loadMoreReviews(id){

    const start = (timesRequested) * NUM_RESULTS;
    const end = start + NUM_RESULTS;
    timesRequested++;

    const response = await fetch(`/loadMoreReviews/${id}/${start}/${end}`);
    const newReviews = await response.text();


    const reviewsDiv = document.getElementById("reviewList");
    reviewsDiv.innerHTML += newReviews;

}