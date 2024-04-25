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
    selector: 'app-clientList',
    templateUrl: './clientList.component.html'
})
export class ClientListComponent{
    title = 'frontend';

    // hotel attributes
    public hotel!: Hotel;
    public hotelId! : number;

    // client attributes
    public clients!: User[];

    // pageable attributes
    public totalPages : number;
    public page : number;



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
        this.clients = [];

    }

    ngOnInit() {
      this.getHotel();
    }


    getHotel(){
      this.hotelService.getHotelById(this.hotelId).subscribe({
          next: (hotel: Hotel) => {
              this.hotel = hotel;
              this.getClients(2);

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



    getClients(quantity: number){
      if(this.page < this.totalPages){
        this.userService.getHotelClients(this.hotelId, this.page, quantity).subscribe({
          next: (pageResponse: PageResponse<User>) => {
            // console.log("Tras el next");
            this.totalPages = pageResponse.totalPages;
            // console.log("Contenido de la pagina:", pageResponse.content);
            pageResponse.content.forEach(client => {
              this.clients.push(client);
            });

            // Increment the page number after each successful API call
            this.page += 1;
          },
          error: (err: HttpErrorResponse) => {
            console.log('Unknown error returning hotels');
            console.log(err);
            this.router.navigate(['/error']);
          }
        });
      }
    }





    getUserImg(userId: number): string {
        return `/api/users/${userId}/image`;
        }
}


