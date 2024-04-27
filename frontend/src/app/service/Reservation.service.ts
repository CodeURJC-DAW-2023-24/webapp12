import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Reservation } from '../entities/reservation.model';
import { Observable } from 'rxjs';
import { PageResponse } from '../interfaces/pageResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor(private http: HttpClient) {}

  createReservation(checkIn: string, checkOut: string, numPeople: number, hotelId: number, userId:number): Observable<Reservation> {
    const reservationData = {
      checkIn,
      checkOut,
      numPeople
    };
    return this.http.post<Reservation>(`/api/reservations/users/${userId}/hotels/${hotelId}`, reservationData);
  }

  getReservations(id: number, page:number, size:number): Observable<PageResponse<Reservation>>{
    return this.http.get<PageResponse<Reservation>>(`/api/reservations/users/${id}`, {params: {page: page.toString(), size: size.toString()}});
  }

  getReservationById(id: number): Observable<Reservation>{
    return this.http.get<Reservation>(`/api/reservations/${id}`);
  }

  cancelReservation(id: number): Observable<Reservation>{
    return this.http.delete<Reservation>(`/api/reservations/${id}`);
  }
}
