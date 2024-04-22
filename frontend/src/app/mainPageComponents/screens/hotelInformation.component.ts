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
    // styleUrls: ["../../../assets/css/hotelPages.component.css", "./hotelReviews.component.css"]
})
export class HotelInformationComponent{
    title = 'frontend';

    public hotelImageUrl!: string;
    public hotelId!: number;
    public hotel!: Hotel;
    public totalreviews!: number;
    public hotelReviews!: Review[];
    public percentageReview!: number[];
    public numReviewsForScore!: number[];
    public page!: number;
    public totalPages!: number;
    public rating!: number;
    public comment!: string;
    public isUser!: boolean;
    public user!: User;
  //los de hotelInformation
    public isClient!: boolean;
    public checkIn!: String;
    public checkOut!: String;
    public numPeople!: number;
    numPeopleOptions: number[] = [1, 2, 3, 4];
    public numRooms!: number;


    constructor(private reviewService: ReviewService,
      private userService: UserService,
      private renderer: Renderer2, private el: ElementRef,
      private router: Router, private route: ActivatedRoute,
      private hotelService: HotelService, public loginService: LoginService) {
        this.route.params.subscribe(params => {
          this.hotelId = params['hotelId'];
        });
        this.totalPages = 1;
        this.page = 0;
        this.hotelReviews = [];
        this.isUser = false;
    }

    ngOnInit() {
      this.getCurrentUser();
      this.getHotel();
      this.setReviewPercentages();

    }

    getCurrentUser() {
      this.userService.getCurrentUser().subscribe({
        next: user => {
          console.log("returned");
            this.user = user;
            this.isUser = true;
        },
        error: err => {
          if (err.status === 403) {
            console.log('Forbidden error');
            this.router.navigate(['/error']);
          } else if (err.status === 404) {
            console.log('User not logged in');
            this.isUser = false;
          }
        }
      });
    }

    getHotel(){
      this.hotelService.getHotelById(this.hotelId).subscribe({
          next: (hotel: Hotel) => {
              this.hotel = hotel;
              this.hotelImageUrl = `/api/hotels/${hotel.id}/image`
              this.getReviews();
              this.setNumReviewsForScore();
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

    setNumReviewsForScore(){
      this.numReviewsForScore = [0, 0, 0, 0, 0];
      if (this.hotel?.reviews) {
        this.hotel.reviews.forEach(review => {
          this.numReviewsForScore[review.score - 1] += 1;
        });
      }
    }

    setReviewPercentages(){
      this.reviewService.getPercentageOfReviewsByScore(this.hotelId).subscribe((percentages: number[]) => {
        this.percentageReview = percentages;
        console.log(this.percentageReview);
      });
    }

    getReviews(){
      if(this.page < this.totalPages){
        this.reviewService.getReviews(this.hotelId, this.page, 6).subscribe({
          next: (pageResponse: PageResponse<Review>) => {
            this.totalPages = pageResponse.totalPages;
            pageResponse.content.forEach(review => {
              this.hotelReviews.push(review);
            });
            // Increment the page number after each successful API call
            this.page += 1;
          },
          error: (err: HttpErrorResponse) => {
            console.log('Unknown error returning reservations');
            console.log(err);
            this.router.navigate(['/error']);
          }
        });
      }
      console.log("reviews cargados")
    }

    addReview(comment: string): void {
      this.reviewService.createReview(this.rating, comment, this.hotelId).subscribe({
        next: _ => {

          // Guarda la URL actual
          let currentUrl = this.router.url;

          // Navega a una URL temporal
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          // Navega de nuevo a la URL actual
          this.router.navigate([currentUrl]);
          });
        },
        error: (err: HttpErrorResponse) => {
          // Handle other errors
          this.router.navigate(['/error']);
        }
      });
    }

    getUserImg(userId: number): string {
        return `/api/users/${userId}/image`;
        }
}


