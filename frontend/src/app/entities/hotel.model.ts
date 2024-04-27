import { User } from './user.model';
import { Room } from './room.model';
import { Reservation } from './reservation.model';
import { Review } from './review.model';

export interface Hotel {
  id: number;
  name: string;
  description: string;
  location: string;
  rating: number;
  imageFile?: any;
  image: boolean;
  imagePath: string;
  manager: User;
  rooms?: Room[];
  reservations?: Reservation[];
  reviews?: Review[];
  rol:string;
}
