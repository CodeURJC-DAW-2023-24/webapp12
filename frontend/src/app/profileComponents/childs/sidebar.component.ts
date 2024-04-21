import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  title = 'frontend';

  public userType: string;
  public isUser: boolean;
  public isClient: boolean;
  public isManager: boolean;
  public isAdmin: boolean;
  public user! : User;

constructor(private userService: UserService) {
    this.userType = "";
    this.isUser = false;
    this.isClient = false;
    this.isManager = false;
    this.isAdmin = false; 
  }
}