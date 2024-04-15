import { Injectable, InjectionToken } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, catchError, map, of, switchMap } from 'rxjs';
import { UserService } from './User.service';
import { HotelService } from './Hotel.service';
import { Router } from '@angular/router';
import { User } from '../entities/user.model';
import { Hotel } from '../entities/hotel.model';

export const AUTH_GUARD = new InjectionToken<((route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => Observable<boolean> | Promise<boolean> | boolean)>('AuthGuard');

@Injectable({
    providedIn: 'root',
    useFactory: (userService: UserService, hotelService: HotelService, router: Router) => new AuthGuardService(userService, hotelService, router),
    deps: [UserService, Router],
})
export class AuthGuardService {

    constructor(private userService: UserService, private hotelService: HotelService, 
        private router: Router) { }

        getCurrentUser(): Observable<User | null> {
            return this.userService.getCurrentUser().pipe(
                map((user: User) => user),
                catchError(err => {
                    if (err.status === 403) {
                        console.log('Forbidden error');
                    } else {
                        console.log('No user logged in');
                    }
                    this.router.navigate(['/error']);
                    return of(null);
                })
            );
        }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
        const roleFromRoute = next.data['role'];
        return this.userService.getCurrentUser().pipe(
            map(user => {
                if (user.rols.includes(roleFromRoute)) {
                    return true;
                } else {
                    this.router.navigate(['/error']);
                    return false;
                }
            }),
            catchError(err => {
                this.router.navigate(['/error']);
                return of(false);
            })
        );
    }
    
    canActivateWithUserId(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
        const idFromRoute = next.params['userId'];
        const roleFromRoute = next.data['role'];
        console.log("1");
        return this.userService.getUserById(idFromRoute).pipe(
            map(user => {
                console.log("2");
                if (user.rols.includes(roleFromRoute) && user.id === idFromRoute) {
                    console.log("true");
                    return true;
                } else {
                    console.log("false");
                    this.router.navigate(['/error']);
                    return false;
                }
            }),
            catchError(err => {
                console.log("error");
                this.router.navigate(['/error']);
                return of(false);
            })
        );
    }

    /*canActivateWithUserI2d(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
        const idFromRoute = next.params['userId'];
        const roleFromRoute = next.data['role'];
        let loggedUser: User = this.getCurrentUser();

        return this.hotelService.getHotelById(idFromRoute).pipe(
            map(hotel => {
                let hotelManager: User = hotel.manager;
                if (loggedUser.id = hotelManager.id && loggedUser.rols.includes(roleFromRoute) && user.id === idFromRoute) {
                    return true;
                } else {
                    this.router.navigate(['/error']);
                    return false;
                }
            }),
            catchError(err => {
                this.router.navigate(['/error']);
                return of(false);
            })
        );
    }*/
    
    canActivateWithHotelId(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
            const idFromRoute = next.params['hotelId'];
            const roleFromRoute = next.data['role'];
    
            return this.getCurrentUser().pipe(
                switchMap((loggedUser: User | null) => {
                    if (!loggedUser) {
                        this.router.navigate(['/error']);
                        return of(false);
                    } else {
                        return this.hotelService.getHotelById(idFromRoute).pipe(
                            map(hotel => {     
                                if (hotel.manager.id === loggedUser.id && loggedUser.rols.includes(roleFromRoute)) {
                                    return true;
                                } else {
                                    this.router.navigate(['/error']);
                                    return false;
                                }
                            }),
                            catchError(err => {
                                this.router.navigate(['/error']);
                                return of(false);
                            })
                        );
                    }
                })
            );
        }
}