import { Component } from '@angular/core';
import { UserService } from '../service/UserService';
import { User } from '../entities/user.model';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  //styleUrl: ''
})
export class ErrorComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isManager: boolean;
  public isAdmin: boolean;
  public user! : User;

constructor(private userService: UserService) {
    this.userType = [""];
    this.isUser = false;
    this.isManager = false;
    this.isAdmin = false; 
}

  ngOnInit() {
    this.userService.GetCurrentUser().subscribe({
    next: user =>{
      this.user = user;
      this.userType = user.rols;
      this.isUser = user.rols.includes("USER");
      this.isManager = user.rols.includes("MANAGER");
      this.isAdmin = user.rols.includes("ADMIN");
    },
    error: err => {
      // Handle error here
    }
    });
  }
}