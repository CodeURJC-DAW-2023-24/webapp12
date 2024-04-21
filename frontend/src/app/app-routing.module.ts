import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './profileComponents/screens/login.component';
import { ProfileComponent } from './profileComponents/screens/profile.component';
import { RegisterComponent } from './profileComponents/screens/register.component';
import { LoginErrorComponent } from './profileComponents/errors/loginError.component';
import { NickTakenComponent } from './profileComponents/errors/nickTaken.component';

import { ErrorComponent } from './globalComponents/error.component';
import { AuthGuardService } from './service/AuthGuard.service';
import { AuthGuardWithUserIdService } from './service/AuthGuardWithUserId.service';
import { AuthGuardWithHotelIdService } from './service/AuthGuardWithHotelId.service';

import { MainPageComponent } from './mainPageComponents/screens/mainPage.component';
import { EditProfileComponent } from './profileComponents/screens/editProfile.component';
import { ClientReservationsComponent } from './profileComponents/screens/clientReservations.component';
import { AddHotelComponent } from './profileComponents/screens/addHotel.component';
import { ViewHotelsManagerComponent } from './profileComponents/screens/viewHotelsManager.component';
import { EditHotelComponent } from './profileComponents/screens/editHotel.component';
import { HotelReviewsComponent } from './mainPageComponents/screens/hotelReviews.component';
import { ManagerValidationComponent } from './profileComponents/screens/managerValidation.component';




const routes: Routes = [
  //PUBLIC PAGES
  { path: 'login', component: LoginComponent},
  { path: 'error', component: ErrorComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'loginError', component: LoginErrorComponent },
  { path: 'nickTaken', component: NickTakenComponent },
  { path: 'mainPage', component: MainPageComponent},
  { path: 'hotelReviews/:hotelId', component: HotelReviewsComponent},


  //USER PAGES
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], data: { role: 'USER' } },
  { path: 'editProfile/:userId', component: EditProfileComponent, canActivate: [AuthGuardWithUserIdService],
  data: { role: 'USER'} },

  //CLIENT PAGES
  { path: 'clientReservations/:userId', component: ClientReservationsComponent, canActivate: [AuthGuardWithUserIdService],
  data: { role: 'CLIENT'}},

  //MANAGER PAGES
  { path: 'editHotel/:hotelId', component: EditHotelComponent, canActivate: [AuthGuardWithHotelIdService],
  data: { role: 'MANAGER'}},

  { path: 'addHotel', component: AddHotelComponent, canActivate: [AuthGuardService],
  data: { role: 'MANAGER'}},

  { path: 'viewHotelsManager', component: ViewHotelsManagerComponent, canActivate: [AuthGuardService],
  data: { role: 'MANAGER' } },

  //ADMIN PAGES
  { path: 'managerValidation', component: ManagerValidationComponent, canActivate: [AuthGuardService], data: { role: 'ADMIN' } },

  //DEFAULT. PUTH ALL ROUTES AVOBE THIS ONE
  { path: '**', redirectTo: 'mainPage' },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
