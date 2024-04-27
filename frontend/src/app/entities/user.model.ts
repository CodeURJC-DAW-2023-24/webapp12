import { Hotel } from "./hotel.model";
import { Reservation } from "./reservation.model";
import { Review } from "./review.model";

  export interface User {
    id: number;
    name: string;
    nick: string;
    lastname: string;
    bio?: string;
    location?: string;
    language?: string;
    phone: string;
    email: string;
    imageFile?: any;
    imagePath?: string;
    image?: boolean;
    organization?: string;
    validated?: boolean;
    rejected?: boolean;
    collectionRols?: string[];
    rols: string[];
    username: string;
    password: string;
    reservations?: Reservation[];
    reviews?: Review[];
    hotels?: Hotel[];
  }
