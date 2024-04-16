import { Injectable, InjectionToken } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, catchError, from, map, of, switchMap } from 'rxjs';
import { UserService } from './User.service';
import { HotelService } from './Hotel.service';
import { Router } from '@angular/router';
import { User } from '../entities/user.model';
import { Hotel } from '../entities/hotel.model';

export const AUTH_GUARD = new InjectionToken<((route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => Observable<boolean> | Promise<boolean> | boolean)>('AuthGuard');

@Injectable({
    providedIn: 'root',
    useFactory: (userService: UserService, hotelService: HotelService, router: Router) => new AuthGuardService(userService, hotelService, router),
    deps: [UserService, HotelService, Router],
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
        console.log("canActivate");
        const roleFromRoute = next.data['role'];
        return this.userService.getCurrentUser().pipe(
            map(user => {
                if (user.rols.includes(roleFromRoute)) {
                    return true;
                } else {
                    console.log("false");
                    this.router.navigate(['/error']);
                    return false;
                }
            }),
            catchError(err => {
                console.log("error");
                return from(this.router.navigate(['/error'])).pipe(
                    map(() => false)
                );
            })
        );
    }





    //NEEDS TO IMPLEMENT METHODS TO WORK 

    /*canActivateWithReservationId(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
            const idFromRoute = next.params['reservationId'];
            const roleFromRoute = next.data['role'];
    
            return this.getCurrentUser().pipe(
                switchMap((loggedUser: User | null) => {
                    if (!loggedUser) {
                        this.router.navigate(['/error']);
                        return of(false);
                    } else {
                        return this.reservationService.getReservationById(idFromRoute).pipe(
                            map(reservation => {     
                                if (reservation.user.id === loggedUser.id && loggedUser.rols.includes(roleFromRoute)) {
                                    return true;
                                } else {
                                    this.router.navigate(['/error']);
                                    return false;
                                }
                            }),
                            catchError(err => {
                                console.log("error");
                                return from(this.router.navigate(['/error'])).pipe(
                                    map(() => false)
                                );
                            })
                        );
                    }
                })
            );
        }*/




    //THIS MIGHT NOT BE NEEDED

    /*canActivateWithReviewId(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean> {
            const idFromRoute = next.params['reviewId'];
            const roleFromRoute = next.data['role'];
    
            return this.getCurrentUser().pipe(
                switchMap((loggedUser: User | null) => {
                    if (!loggedUser) {
                        this.router.navigate(['/error']);
                        return of(false);
                    } else {
                        return this.reviewService.getReviewById(idFromRoute).pipe(
                            map(review => {     
                                if (hotel.manager.id === loggedUser.id && loggedUser.rols.includes(roleFromRoute)) {
                                    return true;
                                } else {
                                    this.router.navigate(['/error']);
                                    return false;
                                }
                            }),
                            catchError(err => {
                                console.log("error");
                                return from(this.router.navigate(['/error'])).pipe(
                                    map(() => false)
                                );
                            })
                        );
                    }
                })
            );
        }*/

    //THIS MIGHT NOT BE NEEDED
    /*canActivateWithRoomlId(
            next: ActivatedRouteSnapshot,
            state: RouterStateSnapshot): Observable<boolean> {
                const idFromRoute = next.params['roomlId'];
                const roleFromRoute = next.data['role'];
        
                return this.getCurrentUser().pipe(
                    switchMap((loggedUser: User | null) => {
                        if (!loggedUser) {
                            this.router.navigate(['/error']);
                            return of(false);
                        } else {
                            return this.roomService.getRoomById(idFromRoute).pipe(
                                map(hotel => {     
                                    if (hotel.manager.id === loggedUser.id && loggedUser.rols.includes(roleFromRoute)) {
                                        return true;
                                    } else {
                                        this.router.navigate(['/error']);
                                        return false;
                                    }
                                }),
                                catchError(err => {
                                    console.log("error");
                                    return from(this.router.navigate(['/error'])).pipe(
                                        map(() => false)
                                    );
                                })
                            );
                        }
                    })
                );
            }*/
}