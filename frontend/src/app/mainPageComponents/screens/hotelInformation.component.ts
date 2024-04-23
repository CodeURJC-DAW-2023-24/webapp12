import { Component, Renderer2, ElementRef } from '@angular/core';
import { ReviewService } from '../../service/Review.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../service/User.service';
import { Reservation } from '../../entities/reservation.model';
import { Review } from '../../entities/review.model';
import { User } from '../../entities/user.model';
import { HotelService } from '../../service/Hotel.service';
import { ReservationService } from '../../service/Reservation.service';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { Hotel } from '../../entities/hotel.model';
import { LoginService } from '../../service/Login.service';



@Component({
    selector: 'app-hotelInformation',
    templateUrl: './hotelInformation.component.html',
    //styleUrl: ''
    styleUrls: ["../../../assets/css/hotelPages.component.css", "./hotelReviews.component.css"]
})
export class HotelInformationComponent{
    title = 'frontend';

    public hotelImageUrl!: string;
    public hotelId!: number;
    public hotel!: Hotel;
    public isUser!: boolean;
    public user!: User;
  //los de hotelInformation
    public isClient!: boolean;
    public checkIn!: string;
    public checkOut!: string;
    public numPeople!: number;
    public numPeopleOptions: number[] = [1, 2, 3, 4];
    public numRooms!: number;
    public userId!: number;
    public reservationId!: number;
    public reservation! : Reservation;


    constructor(private reservationService: ReservationService,
      private userService: UserService,
      private renderer: Renderer2, private el: ElementRef,
      private router: Router, private route: ActivatedRoute,
      private hotelService: HotelService, public loginService: LoginService) {
        this.route.params.subscribe(params => {
          this.hotelId = params['hotelId'];
          this.reservationId = params['reservationId'];
        });
    }

    ngOnInit() {
      // this.getCurrentReservation();
      this.getCurrentUser();
    }

    getCurrentUser() {
      this.userService.getCurrentUser().subscribe({
        next: (user: User) => {
          if (user) {
            this.user = user;
            this.userId = user.id;
            this.isClient = user.rols.includes('CLIENT');
          } else {
            console.log('User is undefined');
          }
        },
        error: err => {
          if (err.status === 403) {
            console.log('Forbidden error');
            this.router.navigate(['/error']);
          } else {
            console.log('No user logged in');
            this.router.navigate(['/error']);
          }
        }
      });
    }

    getHotel(){
      this.hotelService.getHotelById(this.hotelId).subscribe({
          next: (hotel: Hotel) => {
              this.hotel = hotel;
              this.hotelImageUrl = `/api/hotels/${hotel.id}/image`

              if(this.hotel.imageFile.size()===0){
                  this.router.navigate(['/error']);
              }
              else{
                  console.log('Hotel image found');
              }
          },
          error: (err: HttpErrorResponse) => {
              if (err.status === 403) {
                  console.log('Forbidden error');
                  this.router.navigate(['/error']);

              } else {
                  console.log('Unknown error');
                  this.router.navigate(['/error']);
              }
          }
      });
    }
    // getCurrentReservation() {
    //   this.reservationService.getReservationById(this.reservationId).subscribe({
    //     next: (reservation: Reservation) => {
    //       this.reservation = reservation;
    //     },
    //     error: err => {
    //       if (err.status === 403) {
    //         console.log('Forbidden error');
    //         this.router.navigate(['/error']);
    //       } else {
    //         console.log('No user logged in');
    //         this.router.navigate(['/error']);
    //       }
    //     }
    //   });
    // }

    addReservation(hotelId: number): void {
      this.reservationService.createReservation(this.checkIn, this.checkOut, this.numPeople, hotelId).subscribe({
        next: _ => {
          // Store the current URL
          const currentUrl = this.router.url;

          // Navigate to a temporary URL
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            // Navigate back to the current URL
            this.router.navigate([currentUrl]);
          });
        },
        error: (err: HttpErrorResponse) => {
          // Handle errors
          this.router.navigate(['/error']);
        }
      });
    }
}


