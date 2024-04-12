import { Component } from '@angular/core';
import { UserService } from '../service/UserService';
import { User } from '../entities/user.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  //styleUrl: ''
})
export class RegisterComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public user! : User;

constructor(private userService: UserService) {
    this.userType = [""];
    this.isUser = false;   
}

  ngOnInit() {
    this.userService.GetCurrentUser().subscribe({
    next: user =>{
      this.user = user;
      this.userType = user.rols;
      this.isUser = user.rols.includes("USER");
    },
    error: err => {
      console.log(err);
    }
    });
  }
    

}