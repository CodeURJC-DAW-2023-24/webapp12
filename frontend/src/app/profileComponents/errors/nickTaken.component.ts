import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { Router, ActivatedRoute } from '@angular/router';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-nickTaken',
  templateUrl: './nickTaken.component.html',
})
export class NickTakenComponent {
  title = 'frontend';

  public userType: number;
  public isUser: boolean;
  public nick: string;
  public name: string;
  public lastname: string;
  public email: string;
  public pass: string;
  public rols: string[];

  constructor(public loginService: LoginService) {
    this.userType = -1;
    this.isUser = false;
    this.nick = '';
    this.name = '';
    this.lastname = '';
    this.email = '';
    this.pass = '';
    this.rols = [''];
  }
}
