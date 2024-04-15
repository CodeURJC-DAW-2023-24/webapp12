import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './profileComponents/screens/login.component';
import { ProfileComponent } from './profileComponents/screens/profile.component';
import { RegisterComponent } from './profileComponents/screens/register.component';
import { LoginErrorComponent } from './profileComponents/errors/loginError.component';
import { NickTakenComponent } from './profileComponents/errors/nickTaken.component';

import { ErrorComponent } from './globalComponents/error.component';
import { AuthGuardService } from './service/AuthGuard.service';

import { MainPageComponent } from './mainPageComponents/screens/mainPage.component';
import { EditProfileComponent } from './profileComponents/screens/editProfile.component';
import { ClientReservationsComponent } from './profileComponents/screens/clientReservations.component';
import { AddHotelComponent } from './profileComponents/screens/addHotel.component';
import { ViewHotelsManagerComponent } from './profileComponents/screens/viewHotelsManager.component';



const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], data: { role: 'USER' } },
  { path: 'error', component: ErrorComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'loginError', component: LoginErrorComponent },
  { path: 'nickTaken', component: NickTakenComponent },
  { path: 'mainPage', component: MainPageComponent},
  { path: 'addHotel', component: AddHotelComponent, canActivate: [AuthGuardService], data: { role: 'MANAGER' } },
  { path: 'viewHotelsManager', component: ViewHotelsManagerComponent, canActivate: [AuthGuardService], data: { role: 'MANAGER' } },
  { path: '**', redirectTo: 'error' },


  { path: 'editProfile/:userId', component: EditProfileComponent },

  { path: 'editProfile/:userId', component: EditProfileComponent, canActivate: [AuthGuardService], 
  data: { role: 'USER', method: 'canActivateWithUserId' } },

  { path: 'clientReservations/:userId', component: ClientReservationsComponent, canActivate: [AuthGuardService], 
  data: { role: 'CLIENT', method: 'canActivateWithUserId' } },
  

  { path: 'editHotel/:hotelId', component: ViewHotelsManagerComponent, canActivate: [AuthGuardService], 
  data: { role: 'MANAGER', method: 'canActivateWithHotelId' } },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
