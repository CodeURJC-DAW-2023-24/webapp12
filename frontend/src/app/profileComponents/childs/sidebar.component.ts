import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';

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

  ngOnInit() {
    this.userService.getCurrentUser().subscribe({
    next: user =>{
      this.user = user;
      if (user.rols.includes('CLIENT')) {
        this.userType = 'CLIENT';
      } else if (user.rols.includes('MANAGER')) {
        this.userType = 'MANAGER';
      } else if (user.rols.includes('ADMIN')) {
        this.userType = 'ADMIN';
      }
      this.isUser = user.rols.includes("USER");
      this.isClient = user.rols.includes("CLIENT");
      this.isManager = user.rols.includes("MANAGER");
      this.isAdmin = user.rols.includes("ADMIN");
    },
    error: err => {
      console.log('No user logged in' + err);
    }
    });
  }
}
