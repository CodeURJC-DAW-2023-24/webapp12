import { Component } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  //styleUrl: ''
})
export class ProfileComponent {
  title = 'frontend';

  public userType: string[];
  public isUser: boolean;
  public isClient: boolean;
  public isManager: boolean;
  public isAdmin: boolean;
  public isValidated: boolean;
  public user! : User;
  public imageUrl!: string;

constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) {
    this.userType = [""];
    this.isUser = false;
    this.isClient = false;
    this.isManager = false;
    this.isAdmin = false;
    this.isValidated = false;
    
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
      this.isValidated = user.validated !== undefined ? user.validated : false;
      this.imageUrl = `/api/users/${user.id}/image`;
    },
    error: err => {
      if (err.status === 403) {
        console.log('Forbidden error');
        this.router.navigate(['/error']);
        
      }else {
        console.log('Unknown error');
        this.router.navigate(['/error']);
      }
    }
    });
  }
}