const NUM_RESULTS_REVIEWS = 6;

let timesRequestedReviews = 1;

async function loadMoreReviews(id){

    const start = (timesRequestedReviews) * NUM_RESULTS_REVIEWS;
    const end = start + NUM_RESULTS_REVIEWS;
    timesRequestedReviews++;

    const response = await fetch(`/loadMoreReviews/${id}/${start}/${end}`);
    const newReviews = await response.text();


    const reviewsDiv = document.getElementById("reviewList");
    reviewsDiv.innerHTML += newReviews;

}