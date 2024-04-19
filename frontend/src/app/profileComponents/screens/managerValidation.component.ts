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
}
