import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Reservation } from '../entities/reservation.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  constructor(private http: HttpClient) {}

  getReservations(id: number, page:number, size:number): Observable<Reservation[]>{
    return this.http.get<Reservation[]>(`/api/reservations/${id}`, {params: {page: page.toString(), size: size.toString()}});
  }
}