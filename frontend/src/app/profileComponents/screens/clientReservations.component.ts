import { Component, Renderer2, ElementRef } from '@angular/core';
import { UserService } from '../../service/UserService';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Reservation } from '../../entities/reservation.model';
import { User } from '../../entities/user.model';
import { ReservationService } from '../../service/ReservationService';
import { PageResponse } from '../../interfaces/pageResponse.interface';



@Component({
  selector: 'app-clientReservations',
  templateUrl: './clientReservations.component.html',
  //styleUrl: ''
})
export class ClientReservationsComponent {
  title = 'frontend';

  public user! : User;
  public reservations: Reservation[];
  public page: number;


  constructor(private userService: UserService, private reservationService: ReservationService,
    private renderer: Renderer2, private el: ElementRef,
    private router: Router, private route: ActivatedRoute
  ) {
    this.reservations = [];
    this.page = 0;
  }

  ngOnInit() {
    this.getCurrentUser();
  }
  
  getCurrentUser() {
    this.userService.getCurrentUser().subscribe({
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
    this.reservationService.getReservations(this.user.id, this.page, 6).subscribe({
      next: (pageResponse: PageResponse<Reservation>) => {
        pageResponse.content.forEach(reservation => {
          this.reservations.push(reservation);
        });
        this.page += 1;      },
      error: (err: HttpErrorResponse) => {
          console.log('Unknown error returning reservations');
          console.log(err);
          this.router.navigate(['/error']);
      }
    });
}

}