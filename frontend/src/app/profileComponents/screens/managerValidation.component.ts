import { Component } from '@angular/core';
import { UserService } from '../../service/User.service';
import { User } from '../../entities/user.model';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-managerValidation',
  templateUrl: './managerValidation.component.html',
  //styleUrl: ''
})
export class ManagerValidationComponent{
  title = 'frontend';

  public unvalidatedManagers: User[];


  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute, public loginService: LoginService) {
    this.unvalidatedManagers = [];
  }

  //Por hacer: en unvalidatedManagers poner el array de managers no validados
  ngOnInit() {}

  //Por hacer: marcar manager como validado
  acceptManager(idManager: number) {}

  //Por hacer: marcar manager como no validado
  rejectManager(idManager: number) {}
}
