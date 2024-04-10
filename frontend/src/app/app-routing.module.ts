import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './profileScreens/login.component';
import { Profile } from './profileScreens/profile.component';


const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'profile', component: Profile },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
