import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './profileComponents/screens/login.component';
import { ProfileComponent } from './profileComponents/screens/profile.component';
import { RegisterComponent } from './profileComponents/screens/register.component';
import { LoginErrorComponent } from './profileComponents/errors/loginError.component';
import { NickTakenComponent } from './profileComponents/errors/nickTaken.component';

import { ErrorComponent } from './globalComponents/error.component';

import { MainPageComponent } from './mainPageComponents/screens/mainPage.component';



const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'loginError', component: LoginErrorComponent },
  { path: 'nickTaken', component: NickTakenComponent },
  {path: 'mainPage', component: MainPageComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
