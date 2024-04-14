import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-nickTaken',
  templateUrl: './nickTaken.component.html',
  //styleUrl: ''
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
  //public user! : User;

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) {
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