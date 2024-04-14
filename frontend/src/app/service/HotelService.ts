import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { Hotel } from '../entities/hotel.model';
import { PageResponse } from '../interfaces/pageResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class HotelService {
  constructor(private http: HttpClient) {}

  getRecommendedHotels(page: number, size: number): Observable<PageResponse<Hotel>> {
    return this.http.get<PageResponse<Hotel>>('/api/hotels/recommended', {params: {page: page.toString(), size: size.toString()}});
}

  getHotelsBySearch(page: number, size: number, searchValue: string){
    return this.http.get<PageResponse<Hotel>>('/api/hotels/specific', {params: {page: page.toString(), size: size.toString(), searchValue: searchValue}});
  }

}