import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

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

  getAllHotelsBySearch(searchValue: string): Observable<Hotel[]>{
    return this.http.get<Hotel[]>('/api/hotels/specific/all', {params: {searchValue: searchValue}});
  }

  createNewHotel(formData: FormData): Observable<Hotel>{
    return this.http.post<Hotel>('/api/hotels', formData);
  }

  editHotelImage(id: number, file: File){
    const formData = new FormData();
    formData.append('imageFile', file);
    return this.http.post<HttpResponse<any>>(`/api/hotels/${id}/image`, formData);

  }

  getHotelById(id: number): Observable<Hotel>{
    return this.http.get<Hotel>(`/api/hotels/${id}`);
  }

  getManagerHotels(id: number, page: number, size: number){
    return this.http.get<PageResponse<Hotel>>(`/api/hotels/manager/${id}`, {params: {page: page.toString(), size: size.toString()}});
  }

  updateHotelDetails(id: number, updates: FormData){
    return this.http.put<Hotel>(`/api/hotels/${id}`, updates);
  }

  deleteHotel(id: number){
    return this.http.delete(`/api/hotels/${id}`);
  }

  getImagePath(id: number){
    return this.http.get(`/api/hotels/${id}/imagePath`);
  }

  getAllHotelsSize(){
    return this.http.get<number>('/api/hotels/size');
  }

}
