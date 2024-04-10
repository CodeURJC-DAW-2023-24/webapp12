import { Hotel } from './hotel.model';
import { User } from './user.model';

export interface Review {
  id: number;
  score: number;
  comment?: string;
  date: Date;
  hotel: Hotel;
  user: User;
}