import { Reservation } from './reservation.model';
import { Hotel } from './hotel.model';

export interface Room {
  id: number;
  maxClients: number;
  cost: number;
  reservations?: Reservation[];
  hotel: Hotel;
}