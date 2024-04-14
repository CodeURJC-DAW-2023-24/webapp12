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

  getReservations(id: number, page:number, size:number): Observable<PageResponse<Reservation>>{
    console.log("a");
    return this.http.get<PageResponse<Reservation>>(`/api/reservations/users/${id}`, {params: {page: page.toString(), size: size.toString()}});
  }
}
