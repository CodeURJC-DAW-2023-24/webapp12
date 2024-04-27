import { Component } from '@angular/core';
import { ReviewService } from '../../service/Review.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../service/User.service';
import { Review } from '../../entities/review.model';
import { User } from '../../entities/user.model';
import { HotelService } from '../../service/Hotel.service';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { Hotel } from '../../entities/hotel.model';
import { LoginService } from '../../service/Login.service';



@Component({
    selector: 'app-hotelReviews',
    templateUrl: './hotelReviews.component.html',
    styleUrls: ["../../../assets/css/hotelPages.component.css", "./hotelReviews.component.css"]
})
export class HotelReviewsComponent{
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



    constructor(private reviewService: ReviewService,
      private userService: UserService,
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
            this.page += 1;
          },
          error: (err: HttpErrorResponse) => {
            console.log('Unknown error returning reservations');
            console.log(err);
            this.router.navigate(['/error']);
          }
        });
      }
    }

    addReview(comment: string): void {
      this.reviewService.createReview(this.rating, comment, this.hotelId).subscribe({
        next: _ => {
          //Store the current URL
          let currentUrl = this.router.url;
          //Navigate to a temporary URL
          this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          //Navigate back to the current URL
          this.router.navigate([currentUrl]);
          });
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
          this.router.navigate(['/error']);
        }
      });
    }

    getUserImg(userId: number): string {
        return `/api/users/${userId}/image`;
        }
}


