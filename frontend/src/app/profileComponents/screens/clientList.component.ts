import { Component, Renderer2, ElementRef } from '@angular/core';
import { ReviewService } from '../../service/Review.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { HotelService } from '../../service/Hotel.service';
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



    constructor(private userService: UserService,
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
              this.getClients(1);

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



    getClients(quantity: number){
      if(this.page < this.totalPages){
        this.userService.getHotelClients(this.hotelId, this.page, quantity).subscribe({
          next: (pageResponse: PageResponse<User>) => {
            this.totalPages = pageResponse.totalPages;
            pageResponse.content.forEach(client => {
              this.clients.push(client);
            });

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


