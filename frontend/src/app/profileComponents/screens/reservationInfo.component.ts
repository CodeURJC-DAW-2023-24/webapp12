import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';
import { Reservation } from '../../entities/reservation.model';

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

constructor(private userService: UserService, private router: Router,
  private route: ActivatedRoute, public loginService: LoginService) {

}

  ngOnInit() {

  }
}
