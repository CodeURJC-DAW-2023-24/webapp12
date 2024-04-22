import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../entities/user.model';
import { PageResponse } from '../interfaces/pageResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  login(username: string, password: string) : Observable<string>{
    return this.http.post<string>('/api/login', {username, password});
  }

  getCurrentUser(): Observable<User>{
    console.log('Getting current user');
    return this.http.get<User>('/api/currentUser');

  }

  getHotelClients(id : number) : Observable<User[]>{
    return this.http.get<User[]>(`/api/hotels/${id}/clients`);
  }

  getUserById(id: number): Observable<User>{
    return this.http.get<User>(`/api/users/${id}`);
  }

  createUser(nick: string, name:string, lastname: string,
    email: string, pass: string, userType: number): Observable<User>{
      return this.http.post<User>('/api/users', {nick, name, lastname, pass, email}, {params: {type: userType.toString()}});
  }

  applyManager(id: number): Observable<User>{
    return this.http.put<User>(`/api/managers/${id}/applied`, {params: {state: true}});
  }

  logOut(): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>('/api/logout', {});
  }

  updateUserDetails(id:number, updates: FormData): Observable<User> {
    return this.http.put<User>(`/api/users/${id}`, updates);
  }

  editProfileImage(id: number, file: File): Observable<HttpResponse<any>>{
    const formData = new FormData();
    formData.append('imageFile', file);
    return this.http.post<HttpResponse<any>>(`/api/users/${id}/image`, formData);
  }

  getUnvalidatedManagers(page: number, size: number): Observable<PageResponse<User>>{
    return this.http.get<PageResponse<User>>('/api/managers/validated', {params: {page: page.toString(), size: size.toString(), validated: 'false'}});
  }

  setManagerState(id: number, rejected: boolean): Observable<User> {
    return this.http.put<User>(`/api/managers/${id}/rejected/state`, {rejected: rejected});
  }
}
