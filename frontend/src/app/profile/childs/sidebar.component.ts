import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../entities/user.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  //styleUrl: ''
})
export class SidebarComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isClient: boolean;
  public isManager: boolean;
  public isAdmin: boolean;
  public user! : User;

constructor(private userService: UserService) {
    this.userType = [""];
    this.isUser = false;
    this.isClient = false;
    this.isManager = false;
    this.isAdmin = false; 
}

  ngOnInit() {
    this.userService.GetCurrentUser().subscribe({
    next: user =>{
      this.user = user;
      this.userType = user.rols;
      this.isUser = user.rols.includes("USER");
      this.isClient = user.rols.includes("CLIENT");
      this.isManager = user.rols.includes("MANAGER");
      this.isAdmin = user.rols.includes("ADMIN");
      console.log(this.user.rols);   
    },
    error: err => {
      // Handle error here
    }
    });
  }
}