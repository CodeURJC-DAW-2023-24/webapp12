import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';
import { Reservation } from '../../entities/reservation.model';
import { ReservationService } from '../../service/Reservation.service';

@Component({
  selector: 'app-reservationinfo',
  templateUrl: './reservationInfo.component.html',
  //styleUrl: ''
})
export class ReservationInfoComponent {
  title = 'frontend';

  public reservationId!: number;
  public reservation! : Reservation;
  public user!: User;
  public userId!: number;

constructor(private userService: UserService, private reservationService: ReservationService, private router: Router,
  private route: ActivatedRoute, public loginService: LoginService) {
    this.route.params.subscribe(params => {
      this.reservationId = params['reservationId'];
    });

}

  ngOnInit() {
    this.getCurrentReservation();
    this.getCurrentUser();
  }

  getCurrentUser() {
    this.userService.getCurrentUser().subscribe({
      next: (user: User) => {
        this.user = user;
        this.userId = user.id;
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

  getCurrentReservation() {
    this.reservationService.getReservationById(this.reservationId).subscribe({
      next: (reservation: Reservation) => {
        this.reservation = reservation;
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

  goToClientReservations(){
    this.router.navigate(['clientReservations/', this.userId]);
  }

  cancelReservation(){
    this.reservationService.cancelReservation(this.reservationId).subscribe({
      next: (reservation: Reservation) => {
        this.router.navigate(['clientReservations/', this.userId]);
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
}
