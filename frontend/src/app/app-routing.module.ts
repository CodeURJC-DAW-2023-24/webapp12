import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './profileScreens/login.component';
import { ProfileComponent } from './profileScreens/profile.component';
import { RegisterComponent } from './profileScreens/register.component';
import { LoginErrorComponent } from './profileScreens/loginError.component';
import { ErrorComponent } from './globalComponents/error.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'loginError', component: LoginErrorComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
