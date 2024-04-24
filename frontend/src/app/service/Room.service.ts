import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Room } from '../entities/room.model';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  constructor(private http: HttpClient) {}

  areAllRoomsBooked(): Observable<boolean> {
    return this.http.get<boolean>('/api/reservations/areAllRoomsBooked');
  }

  getAllRooms(): Observable<Room[]> {
    return this.http.get<Room[]>('/api/notRooms');
  }

  getRoomById(roomId: number): Observable<Room> {
    return this.http.get<Room>(`/api/rooms/${roomId}`);
  }

}
