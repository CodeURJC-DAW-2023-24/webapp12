import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { Hotel } from '../entities/hotel.model';

@Injectable({
  providedIn: 'root',
})
export class HotelService {
  constructor(private http: HttpClient) {}

  getRecommendedHotels(page: number, size: number): Observable<Hotel[]>{
    return this.http.get<Hotel[]>('/api/hotels/recommended', {params: {page: page.toString(), size: size.toString()}});
  }

  getHotelsBySearch(page: number, size: number, searchValue: string){
    return this.http.get<Hotel[]>('/api/hotels/specific', {params: {page: page.toString(), size: size.toString(), searchValue: searchValue}});
  }

}