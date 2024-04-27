import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review } from '../entities/review.model';
import { PageResponse } from '../interfaces/pageResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpClient) {}

  createReview(score: number, comment:string, hotelId: number): Observable<Review>{
      return this.http.post<Review>(`/api/reviews/hotels/${hotelId}`, {score, comment});
  }

  getReviews(id: number, page:number, size:number): Observable<PageResponse<Review>>{
    return this.http.get<PageResponse<Review>>(`/api/reviews/hotels/${id}`, {params: {page: page.toString(), size: size.toString()}});
  }

  getReviewById(id: number): Observable<Review>{
    return this.http.get<Review>(`/api/reviews/${id}`);
  }

  getPercentageOfReviewsByScore(hotelId: number): Observable<number[]>{
    return this.http.get<number[]>(`/api/reviews/percentage/hotels/${hotelId}`);
  }


}
