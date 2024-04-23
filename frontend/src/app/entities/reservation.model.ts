import { Hotel } from './hotel.model';
import { Room } from './room.model';
import { User } from './user.model';

export interface Reservation {
  id: number;
  checkIn: Date;
  checkOut: Date;
  numPeople: number;
  hotel: Hotel;
  room: Room;
  user: User;


}
