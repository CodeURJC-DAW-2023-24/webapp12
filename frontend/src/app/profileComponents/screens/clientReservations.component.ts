import { Component, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Reservation } from '../../entities/reservation.model';
import { User } from '../../entities/user.model';
import { ReservationService } from '../../service/Reservation.service';
import { PageResponse } from '../../interfaces/pageResponse.interface';
import { LoginService } from '../../service/Login.service';



@Component({
  selector: 'app-clientReservations',
  templateUrl: './clientReservations.component.html',
  //styleUrl: ''
})
export class ClientReservationsComponent {
  title = 'frontend';

  public user! : User;
  public userId!: number;
  public reservations: Reservation[];
  public page: number;
  public totalPages: number;


  constructor(private userService: UserService, private reservationService: ReservationService,
    private renderer: Renderer2, private el: ElementRef,
    private router: Router, private route: ActivatedRoute, public loginService: LoginService
  ) {
    this.route.params.subscribe(param =>{
      this.userId = param['userId'];

    })
    this.reservations = [];
    this.page = 0;
    this.totalPages = 1;
  }

  ngOnInit() {
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.userService.getUserById(this.userId).subscribe({
      next: (user: User) => {
        this.user = user;
        this.getReservations();
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

  getReservations(){
    if(this.page < this.totalPages){
      this.reservationService.getReservations(this.user.id, this.page, 1).subscribe({ //size change to 1 for testing. normal size would be 6
        next: (pageResponse: PageResponse<Reservation>) => {
          this.totalPages = pageResponse.totalPages;
          pageResponse.content.forEach(reservation => {
            this.reservations.push(reservation);
            console.log(this.reservations)
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
  }
}
