import { Component } from '@angular/core';
import { LoginService } from '../../service/Login.service';

@Component({
  selector: 'app-loginError',
  templateUrl: './loginError.component.html',
})
export class LoginErrorComponent {
  title = 'frontend';



constructor(public loginService: LoginService) {

}

}
